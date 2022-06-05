package com.gitegg.oauth.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.gitegg.oauth.dto.SocialBindAccountDTO;
import com.gitegg.oauth.dto.SocialBindMobileDTO;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.justauth.factory.GitEggAuthRequestFactory;
import com.gitegg.service.extension.client.dto.JustAuthSocialInfoDTO;
import com.gitegg.service.extension.client.feign.IJustAuthFeign;
import com.gitegg.service.extension.client.feign.ISmsFeign;
import com.gitegg.service.system.client.dto.UserAddDTO;
import com.gitegg.service.system.client.feign.IUserFeign;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 第三方登录
 * @author GitEgg
 */
@Slf4j
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SocialController {
    
    private final GitEggAuthRequestFactory factory;
    
    private final IJustAuthFeign justAuthFeign;
    
    private final IUserFeign userFeign;
    
    private final ISmsFeign smsFeign;
    
    @Value("${system.secret-key}")
    private String secretKey;
    
    @Value("${system.secret-key-salt}")
    private String secretKeySalt;
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 密码最大尝试次数
     */
    @Value("${system.maxTryTimes}")
    private int maxTryTimes;
    
    /**
     * 锁定时间，单位 秒
     */
    @Value("${system.maxTryTimes}")
    private long maxLockTime;
    
    /**
     * 第三方登录缓存时间，单位 秒
     */
    @Value("${system.socialLoginExpiration}")
    private long socialLoginExpiration;

    @GetMapping
    public List<String> list() {
        return factory.oauthList();
    }
    
    /**
     * 获取到对应类型的登录url
     * @param type
     * @return
     */
    @GetMapping("/login/{type}")
    public Result login(@PathVariable String type) {
        AuthRequest authRequest = factory.get(type);
        return Result.data(authRequest.authorize(AuthStateUtils.createState()));
    }
    
    /**
     * 保存或更新用户数据，并进行判断是否进行注册或绑定
     * @param type
     * @param callback
     * @return
     */
    @RequestMapping("/{type}/callback")
    public Result login(@PathVariable String type, AuthCallback callback) {
        AuthRequest authRequest = factory.get(type);
        AuthResponse response = authRequest.login(callback);
        if (response.ok())
        {
            AuthUser authUser = (AuthUser) response.getData();
            JustAuthSocialInfoDTO justAuthSocialInfoDTO = BeanCopierUtils.copyByClass(authUser, JustAuthSocialInfoDTO.class);
            BeanCopierUtils.copyByObject(authUser.getToken(), justAuthSocialInfoDTO);
            // 获取到第三方用户信息后，先进行保存或更新
            Result<Object> createResult = justAuthFeign.userCreateOrUpdate(justAuthSocialInfoDTO);
            if(createResult.isSuccess() && null != createResult.getData())
            {
                Long socialId = Long.parseLong((String)createResult.getData());
                // 判断此第三方用户是否被绑定到系统用户
                Result<Object> bindResult = justAuthFeign.userBindQuery(socialId);
                // 这里需要处理返回消息，前端需要根据返回是否已经绑定好的消息来判断
                // 将socialId进行加密返回
                DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
                // 这里将source+uuid通过des加密作为key返回到前台
                String socialKey = authUser.getSource() + StrPool.UNDERLINE + authUser.getUuid();
                // 将socialKey放入缓存，默认有效期2个小时，如果2个小时未完成验证，那么操作失效，重新获取，在system:socialLoginExpiration配置
                redisTemplate.opsForValue().set(AuthConstant.SOCIAL_VALIDATION_PREFIX + socialKey, createResult.getData(), socialLoginExpiration,
                        TimeUnit.SECONDS);
                String desSocialKey = des.encryptHex(socialKey);
                bindResult.setData(desSocialKey);
                // 这里返回的成功是请求成功，里面放置的result是是否有绑定用户的成功
                return Result.data(bindResult);
            }
            return Result.error("获取第三方用户绑定信息失败");
        }
        else
        {
            throw new BusinessException(response.getMsg());
        }
    }
    
    /**
     * 绑定用户手机号
     * 这里不走手机号登录的流程，因为如果手机号不存在那么可以直接创建一个用户并进行绑定
     */
    @PostMapping("/bind/mobile")
    @ApiOperation(value = "绑定用户手机号")
    public Result<?> bindMobile(@Valid @RequestBody SocialBindMobileDTO socialBind) {
        Result<?> smsResult = smsFeign.checkSmsVerificationCode(socialBind.getSmsCode(), socialBind.getPhoneNumber(), socialBind.getCode());
        // 判断短信验证是否成功
        if (smsResult.isSuccess() && null != smsResult.getData() && (Boolean)smsResult.getData()) {
            // 解密前端传来的socialId
            DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
            String desSocialKey = des.decryptStr(socialBind.getSocialKey());
    
            // 将socialKey放入缓存，默认有效期2个小时，如果2个小时未完成验证，那么操作失效，重新获取，在system:socialLoginExpiration配置
            String desSocialId = (String)redisTemplate.opsForValue().get(AuthConstant.SOCIAL_VALIDATION_PREFIX + desSocialKey);
            
            // 查询第三方用户信息
            Result<Object> justAuthInfoResult = justAuthFeign.querySocialInfo(Long.valueOf(desSocialId));
    
            if (null == justAuthInfoResult || !justAuthInfoResult.isSuccess() || null == justAuthInfoResult.getData())
            {
                throw new BusinessException("未查询到第三方用户信息，请返回到登录页重试");
            }
    
            JustAuthSocialInfoDTO justAuthSocialInfoDTO = BeanUtil.copyProperties(justAuthInfoResult.getData(), JustAuthSocialInfoDTO.class);
            
           // 查询用户是否存在，如果存在，那么直接调用绑定接口
           Result<Object> result = userFeign.queryUserByPhone(socialBind.getPhoneNumber());
           Long userId;
           // 判断返回信息
           if (null != result && result.isSuccess() && null != result.getData()) {
               GitEggUser gitEggUser = BeanUtil.copyProperties(result.getData(), GitEggUser.class);
               userId = gitEggUser.getId();
           }
           else
           {
               // 如果用户不存在，那么调用新建用户接口，并绑定
               UserAddDTO userAdd = new UserAddDTO();
               userAdd.setAccount(socialBind.getPhoneNumber());
               userAdd.setMobile(socialBind.getPhoneNumber());
               userAdd.setNickname(justAuthSocialInfoDTO.getNickname());
               userAdd.setPassword(StringUtils.isEmpty(justAuthSocialInfoDTO.getUnionId()) ? justAuthSocialInfoDTO.getUuid() : justAuthSocialInfoDTO.getUnionId());
               userAdd.setStatus(GitEggConstant.UserStatus.ENABLE);
               userAdd.setAvatar(justAuthSocialInfoDTO.getAvatar());
               userAdd.setEmail(justAuthSocialInfoDTO.getEmail());
               userAdd.setStreet(justAuthSocialInfoDTO.getLocation());
               userAdd.setComments(justAuthSocialInfoDTO.getRemark());
               Result<?> resultUserAdd = userFeign.userAdd(userAdd);
               if (null != resultUserAdd && resultUserAdd.isSuccess() && null != resultUserAdd.getData())
               {
                   userId = Long.parseLong((String) resultUserAdd.getData());
               }
               else
               {
                   // 如果添加失败，则返回失败信息
                   return resultUserAdd;
               }
           }
            // 执行绑定操作
            return justAuthFeign.userBind(Long.valueOf(desSocialId), userId);
        }
        return smsResult;
    }
    
    /**
     * 绑定账号
     * 这里只有绑定操作，没有创建用户操作
     */
    @PostMapping("/bind/account")
    @ApiOperation(value = "绑定用户账号")
    public Result<?> bindAccount(@Valid @RequestBody SocialBindAccountDTO socialBind) {
        // 查询用户是否存在，如果存在，那么直接调用绑定接口
        Result<?> result = userFeign.queryUserByAccount(socialBind.getUsername());
        // 判断返回信息
        if (null != result && result.isSuccess() && null != result.getData()) {
            
            GitEggUser gitEggUser = BeanUtil.copyProperties(result.getData(), GitEggUser.class);
            // 必须添加次数验证，和登录一样，超过最大验证次数那么直接锁定账户
            // 从Redis获取账号密码错误次数
            Object lockTimes = redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + gitEggUser.getId()).get();
            // 判断账号密码输入错误几次，如果输入错误多次，则锁定账号
            if(null != lockTimes && (int)lockTimes >= maxTryTimes){
                throw new BusinessException("密码尝试次数过多，请使用其他方式绑定");
            }
    
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String password = AuthConstant.BCRYPT + gitEggUser.getAccount() +  DigestUtils.md5DigestAsHex(socialBind.getPassword().getBytes());
            // 验证账号密码是否正确
            if ( passwordEncoder.matches(password, gitEggUser.getPassword()))
            {
                // 解密前端传来的socialId
                DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
                String desSocialKey = des.decryptStr(socialBind.getSocialKey());
                // 将socialKey放入缓存，默认有效期2个小时，如果2个小时未完成验证，那么操作失效，重新获取，在system:socialLoginExpiration配置
                String desSocialId = (String)redisTemplate.opsForValue().get(AuthConstant.SOCIAL_VALIDATION_PREFIX + desSocialKey);
          
                // 执行绑定操作
                return justAuthFeign.userBind(Long.valueOf(desSocialId), gitEggUser.getId());
            }
            else
            {
                // 增加锁定次数
                redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + gitEggUser.getId()).increment(GitEggConstant.Number.ONE);
                redisTemplate.expire(AuthConstant.LOCK_ACCOUNT_PREFIX +gitEggUser.getId(), maxLockTime , TimeUnit.SECONDS);
                throw new BusinessException("账号或密码错误");
            }
        }
        else
        {
            throw new BusinessException("账号不存在");
        }
    }

}
