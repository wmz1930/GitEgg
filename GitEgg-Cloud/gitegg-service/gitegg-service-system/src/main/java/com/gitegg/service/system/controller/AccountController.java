package com.gitegg.service.system.controller;

import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.captcha.util.CaptchaUtils;
import com.gitegg.service.extension.client.feign.ISmsFeign;
import com.gitegg.service.system.dto.CreateUserDTO;
import com.gitegg.service.system.dto.PwdUserDTO;
import com.gitegg.service.system.dto.RegisterUserDTO;
import com.gitegg.service.system.dto.SmsDTO;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;
import com.gitegg.service.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

/**
 * @ClassName: AccountController
 * @Description: 账号相关的前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:08:26
 */
@RestController
@RequestMapping(value = "account")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "AccountController|账号相关的前端控制器", tags = {"账号操作"})
@Slf4j
@RefreshScope
public class AccountController {

    private final IUserService userService;
    
    private final ISmsFeign smsFeign;
    
    private final CaptchaService captchaService;
    
    private final RedisTemplate redisTemplate;

    /**
     * 获取登录后的用户信息
     */
    @GetMapping("/user/info")
    @ApiOperation(value = "登录后获取用户个人信息")
    public Result<UserInfo> userInfo( @ApiIgnore @CurrentUser GitEggUser currentUser) {
        User user = new User();
        user.setId(currentUser.getId());
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }
    
    /**
     * 注册用户时，校验用户是否存在
     */
    @PostMapping("/register/check")
    @ApiOperation(value = "注册用户")
    public Result<?> registerUserCheck(@RequestBody RegisterUserDTO user) {
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        boolean exist = userService.checkUserExist(userEntity);
        return Result.data(!exist);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/register/sms/send")
    @ApiOperation(value = "注册用户时，发送短信验证码")
    public Result<?> sendSmsRegister(@ApiIgnore @RequestParam Map<String, String> parameters) {
        //校验验证码
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        if (!checkCaptchaResult)
        {
            throw new BusinessException("请通过正确的安全验证，再发送短信验证码");
        }
        //校验手机号是否已注册
        String mobile = parameters.get(TokenConstant.PHONE_NUMBER);
        User user = new User();
        user.setMobile(mobile);
        boolean exist = userService.checkUserExist(user);
        if (exist)
        {
            throw new BusinessException("该手机号已注册");
        }
        // 发送验证码
        Result<Object> smsResponse = smsFeign.sendSmsVerificationCode(parameters.get(TokenConstant.SMS_CODE), mobile);
        return smsResponse;
    }

    /**
     * 注册用户
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    public Result<?> registerUser(@Valid @RequestBody RegisterUserDTO user) {
        Result<?> smsResult = smsFeign.checkSmsVerificationCode(user.getSmsCode(), user.getMobile(), user.getCode());
        // 判断短信验证是否成功
        if (smsResult.isSuccess() && null != smsResult.getData()) {
            Boolean checkResult = (Boolean)smsResult.getData();
            if(checkResult.booleanValue())
            {
                CreateUserDTO createUser = BeanCopierUtils.copyByClass(user, CreateUserDTO.class);
                createUser.setAccount(user.getMobile());
                CreateUserDTO userDTO = userService.createUser(createUser);
                return Result.data(userDTO.getId());
            }
        }
        return smsResult;
    }

    /**
     * 发送修改密码的短信验证码
     */
    @PostMapping("/sms/pwd")
    @ApiOperation(value = "未登录用户找回密码，发送修改密码的短信验证码")
    public Result<?> sendSmsPwd(@Valid @RequestBody SmsDTO smsDTO) {
        String phoneNumber = smsDTO.getMobile();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile, smsDTO.getMobile());
        User user = userService.getOne(wrapper);
        if (null == user) {
            return Result.error("该手机尚未注册");
        }
        return smsFeign.sendSmsVerificationCode("sms_change_pwd_code", phoneNumber);
    }

    /**
     * 验证找回密码的验证码
     */
    @PostMapping("/pwd/code/check")
    @ApiOperation(value = "未登录用户找回密码，判断验证码是否正确")
    public Result<?> pwdCodeCheck(@Valid @RequestBody RegisterUserDTO user) {
        Result<?> smsResult = smsFeign.checkSmsVerificationCode("sms_register_code", user.getMobile(), user.getSmsCode());
        return smsResult;
    }

    /**
     * 更新密码
     */
    @PostMapping("/pwd/update")
    @ApiOperation(value = "未登录用户找回密码，更新密码")
    public Result<?> changePwd(@Valid @RequestBody PwdUserDTO user) {
        Result<?> smsResult = smsFeign.checkSmsVerificationCode("sms_register_code", user.getMobile(), user.getSmsCode());
        // 判断短信验证是否成功
        if (smsResult.isSuccess() && null != smsResult.getData()) {
            Boolean checkResult = (Boolean)smsResult.getData();
            if(checkResult.booleanValue())
            {
                User userEntity = BeanCopierUtils.copyByClass(user, User.class);
                if (!StringUtils.isEmpty(userEntity.getPassword())) {
                    User oldUser = userService.queryUserByAccount(user.getMobile());
                    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                    String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldUser.getAccount() + DigestUtils.md5DigestAsHex(userEntity.getPassword().getBytes()));
                    userEntity.setPassword(cryptPwd);
                    userEntity.setId(oldUser.getId());
                    userService.updateById(userEntity);
                }
                return Result.success("密码更新成功");
            }
        }
        return smsResult;
    }

    /**
     * 发送修改密码的短信验证码
     * 使用场景为：登录用户需要修改密码时，选择通过验证码来修改密码
     */
    @PostMapping("/sms/personal/pwd")
    @ApiOperation(value = "登录用户，发送修改密码的短信验证码")
    public Result<?> sendPersonalSmsPwd(@ApiIgnore @CurrentUser User user) {
        if (null == user) {
            return Result.error("当前用户未登陆");
        }
        String phoneNumber = user.getMobile();
        if (StringUtils.isEmpty(phoneNumber)) {
            return Result.error("未获取到当前登录用户手机号");
        }
        return smsFeign.sendSmsVerificationCode("sms_change_pwd_code", phoneNumber);
    }

    /**
     * 验证找回密码
     * 使用场景为：登录用户需要修改密码时，选择通过验证码来修改密码
     */
    @GetMapping("/pwd/personal/code/check/{scode}")
    @ApiOperation(value = "登录用户，判断验证码是否正确")
    public Result<?> pwdPersonalCodeCheck(@PathVariable("scode") String scode, @ApiIgnore @CurrentUser User user) {
        if (null == user) {
            return Result.error("当前用户未登陆");
        }
        String phoneNumber = user.getMobile();
        if (StringUtils.isEmpty(phoneNumber)) {
            return Result.error("未获取到当前登录用户手机号");
        }
        Result<?> smsResult = smsFeign.checkSmsVerificationCode("sms_change_pwd_code", user.getMobile(), scode);
        return smsResult;
    }

    /**
     * 更新密码
     */
    @PostMapping("/pwd/personal/update")
    @ApiOperation(value = "登录用户，更新密码")
    public Result<?> changePwdPersonal(@Valid @RequestBody PwdUserDTO user, @ApiIgnore @CurrentUser User userCurrent) {
        Result<?> smsResult = smsFeign.checkSmsVerificationCode("sms_register_code", user.getMobile(), user.getSmsCode());
        // 判断短信验证是否成功
        if (smsResult.isSuccess() && null != smsResult.getData()) {
            Boolean checkResult = (Boolean)smsResult.getData();
            if(checkResult.booleanValue())
            {
                User userEntity = BeanCopierUtils.copyByClass(user, User.class);
                if (!StringUtils.isEmpty(userEntity.getPassword())) {
                    User oldUser = userService.queryUserByAccount(userCurrent.getMobile());
                    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                    String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldUser.getAccount() + DigestUtils.md5DigestAsHex(userEntity.getPassword().getBytes()));
                    userEntity.setPassword(cryptPwd);
                    userEntity.setId(oldUser.getId());
                    userService.updateById(userEntity);
                }
                return Result.success("密码更新成功");
            }
        }
        return smsResult;
    }
}
