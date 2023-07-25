package com.gitegg.service.extension.wx.miniapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import com.gitegg.service.extension.justauth.service.IJustAuthService;
import com.gitegg.service.extension.justauth.service.IJustAuthSocialService;
import com.gitegg.service.extension.justauth.service.IJustAuthSocialUserService;
import com.gitegg.service.extension.wx.miniapp.dto.WeChatMiniAppLoginDTO;
import com.gitegg.service.extension.wx.miniapp.service.IMiniappService;
import com.gitegg.service.system.client.dto.UserAddDTO;
import com.gitegg.service.system.client.feign.IUserFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@Slf4j
@Api(value = "WxMaUserController | 微信小程序账户接口", tags = {"微信小程序账户接口"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/extension/wx/user/{appid}")
public class WxMaUserController {
    
    private final WxMaService wxMaService;
    
    private final IJustAuthService justAuthService;
    
    private final IJustAuthSocialService justAuthSocialService;
    
    private final IJustAuthSocialUserService justAuthSocialUserService;
    
    @Value("${system.secret-key}")
    private String secretKey;
    
    @Value("${system.secret-key-salt}")
    private String secretKeySalt;
    
    private final RedisTemplate redisTemplate;
    
    private final IUserFeign userFeign;
    
    private final IMiniappService miniappService;
    
    /**
     * 第三方登录缓存时间，单位 秒
     */
    @Value("${system.socialLoginExpiration}")
    private long socialLoginExpiration;

    /**
     * 登陆接口
     * 注意： 此方法如果debugger添加端点，会导致微信接口调用时进行重试，进而返回 {"errcode":40163,"errmsg":"code been used, rid: ...}
     */
    @ApiOperation(value = "小程序登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "小程序code", dataType="String", paramType = "query"),
    })
    @GetMapping("/login")
    public Result<?> login(@PathVariable String appid, String code) {
        
        if (StringUtils.isBlank(code)) {
            return Result.error("code 不能为空");
        }

        if (!miniappService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
    
        WeChatMiniAppLoginDTO weChatMiniAppLoginDTO = new WeChatMiniAppLoginDTO();
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            weChatMiniAppLoginDTO.setOpenid(session.getOpenid());
            weChatMiniAppLoginDTO.setUnionid(session.getUnionid());
            // 通过openId获取在系统中是否是已经绑定过的用户，如果没有绑定，那么返回到前台，提示需要绑定或者注册用户
            LambdaQueryWrapper<JustAuthSocial> socialLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 如果微信开通了开放平台，那么各个渠道（小程序、公众号等）都会有统一的unionid，如果没开通，就仅仅使用openId
            if (StringUtils.isBlank(session.getUnionid()))
            {
                socialLambdaQueryWrapper.eq(JustAuthSocial::getOpenId, session.getOpenid())
                        .eq(JustAuthSocial::getSource, "WECHAT_MINI_APP");
            }
            else
            {
                socialLambdaQueryWrapper.eq(JustAuthSocial::getUnionId, session.getUnionid())
                        .and(e -> e.eq(JustAuthSocial::getSource, "WECHAT_MINI_APP")
                                .or().eq(JustAuthSocial::getSource, "WECHAT_OPEN")
                                .or().eq(JustAuthSocial::getSource, "WECHAT_MP")
                                .or().eq(JustAuthSocial::getSource, "WECHAT_ENTERPRISE")
                                .or().eq(JustAuthSocial::getSource, "WECHAT_APP"));
            }
            JustAuthSocial justAuthSocial = justAuthSocialService.getOne(socialLambdaQueryWrapper, false);
            if (null == justAuthSocial)
            {
                weChatMiniAppLoginDTO.setUserInfoAlready(false);
                weChatMiniAppLoginDTO.setUserBindAlready(false);
                justAuthSocial = new JustAuthSocial();
                justAuthSocial.setAccessCode(session.getSessionKey());
                justAuthSocial.setOpenId(session.getOpenid());
                justAuthSocial.setUnionId(session.getUnionid());
                justAuthSocial.setSource("WECHAT_MINI_APP");
                justAuthSocialService.save(justAuthSocial);
            } else {
                justAuthSocial.setAccessCode(session.getSessionKey());
                justAuthSocialService.updateById(justAuthSocial);
            }
    
            // 将socialId进行加密返回，用于前端进行第三方登录，获取token
            DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
            // 这里将source+uuid通过des加密作为key返回到前台
            String socialKey = "WECHAT_MINI_APP" + StringPool.UNDERSCORE + (StringUtils.isBlank(session.getUnionid()) ? session.getOpenid() : session.getUnionid());
            // 将socialKey放入缓存，默认有效期2个小时，如果2个小时未完成验证，那么操作失效，重新获取，在system:socialLoginExpiration配置
            redisTemplate.opsForValue().set(AuthConstant.SOCIAL_VALIDATION_PREFIX + socialKey, String.valueOf(justAuthSocial.getId()), socialLoginExpiration,
                    TimeUnit.SECONDS);


            String desSocialKey = des.encryptHex(socialKey);
            weChatMiniAppLoginDTO.setBindKey(desSocialKey);
            
            // 查询是否绑定用户
            // 判断此第三方用户是否被绑定到系统用户
            Result<Object> bindResult = justAuthService.userBindQuery(justAuthSocial.getId());
            if(null != bindResult && null != bindResult.getData() && bindResult.isSuccess())
            {
                weChatMiniAppLoginDTO.setUserInfoAlready(true);
                weChatMiniAppLoginDTO.setUserBindAlready(true);
            } else {
                // 这里需要处理返回消息，前端需要根据返回是否已经绑定好的消息来判断
                weChatMiniAppLoginDTO.setUserInfoAlready(false);
                weChatMiniAppLoginDTO.setUserBindAlready(false);
            }
            return Result.data(weChatMiniAppLoginDTO);
        } catch (WxErrorException e) {
            log.error("微信小程序登录失败：{}", e);
            return Result.error("小程序登录失败:" + e);
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @ApiOperation(value = "小程序获取用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "socialKey", value = "加密的登录key，用于绑定用户", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "rawData", value = "不包括敏感信息的原始数据字符串，用于计算签名", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量", required = true, dataType="String", paramType = "query")
    })
    @GetMapping("/info")
    public Result<?> info(@PathVariable String appid, String socialKey,
                       String signature, String rawData, String encryptedData, String iv) {
        if (!miniappService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
    
        // 查询第三方用户信息
        JustAuthSocial justAuthSocial = this.getJustAuthSocial(socialKey);
        if (StringUtils.isBlank(justAuthSocial.getAccessCode()))
        {
            throw new BusinessException("登录状态失效，请尝试重新进入小程序");
        }
        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(justAuthSocial.getAccessCode(), rawData, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return Result.error("user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(justAuthSocial.getAccessCode(), encryptedData, iv);
        WxMaConfigHolder.remove();//清理ThreadLocal
        justAuthSocial.setAvatar(userInfo.getAvatarUrl());
        justAuthSocial.setUnionId(userInfo.getUnionId());
        justAuthSocial.setNickname(userInfo.getNickName());
//        justAuthSocial.setGender(userInfo.getGender());
        justAuthSocialService.updateById(justAuthSocial);
        return Result.data(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @ApiOperation(value = "小程序获取获取用户绑定手机号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "socialKey", value = "加密的登录key，用于绑定用户", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", required = true, dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量", required = true, dataType="String", paramType = "query")
    })
    @GetMapping("/phone")
    public Result<?> phone(@PathVariable String appid, String socialKey, String encryptedData, String iv) {
        if (!miniappService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        // 查询第三方用户信息
        JustAuthSocial justAuthSocial = this.getJustAuthSocial(socialKey);
        if (StringUtils.isBlank(justAuthSocial.getAccessCode()))
        {
            throw new BusinessException("登录状态失效，请尝试重新进入小程序");
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(justAuthSocial.getAccessCode(), encryptedData, iv);
        WxMaConfigHolder.remove();//清理ThreadLocal
        
        // 不带区号的手机，国外的手机会带区号
        String phoneNumber = phoneNoInfo.getPurePhoneNumber();
        // 查询用户是否存在，如果存在，那么直接调用绑定接口
        Result<Object> result = userFeign.queryUserByPhone(phoneNumber);
        Long userId;
        // 判断返回信息
        if (null != result && result.isSuccess() && null != result.getData()) {
            GitEggUser gitEggUser = BeanUtil.copyProperties(result.getData(), GitEggUser.class);
            userId = gitEggUser.getId();
        }
        else {
            // 如果用户不存在，那么调用新建用户接口，并绑定
            UserAddDTO userAdd = new UserAddDTO();
            userAdd.setAccount(phoneNumber);
            userAdd.setMobile(phoneNumber);
            userAdd.setNickname(StringUtils.isBlank(justAuthSocial.getNickname()) ? phoneNumber : justAuthSocial.getNickname());
            userAdd.setPassword(StringUtils.isBlank(justAuthSocial.getUnionId()) ? justAuthSocial.getOpenId() : justAuthSocial.getUnionId());
            userAdd.setStatus(GitEggConstant.UserStatus.ENABLE);
            userAdd.setAvatar(justAuthSocial.getAvatar());
            userAdd.setEmail(justAuthSocial.getEmail());
            userAdd.setStreet(justAuthSocial.getLocation());
            userAdd.setComments(justAuthSocial.getRemark());
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
        justAuthService.userBind(justAuthSocial.getId(), userId);
        return Result.success("账号绑定成功");
    }
    
    /**
     * <pre>
     * 绑定当前登录账号
     * </pre>
     */
    @ApiOperation(value = "绑定当前登录账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "socialKey", value = "加密的登录key，用于绑定用户", required = true, dataType="String", paramType = "query")
    })
    @GetMapping("/bind")
    public Result<?> bind(@PathVariable String appid, @NotBlank String socialKey, @CurrentUser GitEggUser user) {
        if (!miniappService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        if (null == user || (null != user && null == user.getId())) {
            throw new BusinessException("用户未登录");
        }
        // 查询第三方用户信息
        JustAuthSocial justAuthSocial = this.getJustAuthSocial(socialKey);
        if (StringUtils.isBlank(justAuthSocial.getAccessCode()))
        {
            throw new BusinessException("账号绑定失败，请尝试重新进入小程序");
        }
        // 执行绑定操作
        justAuthService.userBind(justAuthSocial.getId(), user.getId());
        return Result.success("账号绑定成功");
    }
    
    /**
     * <pre>
     * 解绑当前登录账号
     * </pre>
     */
    @ApiOperation(value = "解绑当前登录账号")
    @GetMapping("/unbind")
    public Result<?> unbind(@PathVariable String appid, @CurrentUser GitEggUser user) {
        if (!miniappService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        if (null == user || (null != user && null == user.getId())) {
            throw new BusinessException("用户未登录");
        }
        LambdaQueryWrapper<JustAuthSocialUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JustAuthSocialUser::getUserId, user.getId());
        justAuthSocialUserService.remove(queryWrapper);
        return Result.success("账号解绑成功");
    }
    
    private JustAuthSocial getJustAuthSocial(String socialKey){
        JustAuthSocial justAuthSocial = null;
        try {
            // 解密前端传来的socialId
            DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
            String desSocialKey = des.decryptStr(socialKey);

            log.info("微信小程序授权登录desSocialKey={}", desSocialKey);
            // 将socialKey放入缓存，默认有效期2个小时，如果2个小时未完成验证，那么操作失效，重新获取，在system:socialLoginExpiration配置
            String desSocialId = (String)redisTemplate.opsForValue().get(AuthConstant.SOCIAL_VALIDATION_PREFIX + desSocialKey);
            log.info("微信小程序授权登录desSocialId={}", desSocialId);

            // 查询第三方用户信息
            justAuthSocial = justAuthService.querySocialInfo(Long.valueOf(desSocialId));
            if (null == justAuthSocial)
            {
                log.error("微信小程序授权登录失败: 没有查询到第三方用户信息");
                throw new BusinessException("未查询到第三方用户信息，请尝试重新进入小程序");
            }
            return justAuthSocial;
        } catch (Exception e) {
            log.error("微信小程序授权登录失败: {}", e);
            throw new BusinessException("微信小程序授权登录失败，请尝试重新进入小程序");
        }
    }

}
