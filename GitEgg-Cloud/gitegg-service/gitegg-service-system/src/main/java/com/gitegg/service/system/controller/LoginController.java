package com.gitegg.service.system.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gitegg.platform.base.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IUserRoleService;
import com.gitegg.service.system.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName: LoginController
 * @Description: 登录相关前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:08:26
 */
@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "LoginController|登录鉴权相关的前端控制器")
@RefreshScope
public class LoginController {

    private final IUserService userService;

    private final IUserRoleService userRoleService;

    private final IResourceService resourceService;

//    @Value("${system.smsTimes}")
    private int smsTimes;

    /**
     * 获取登录后的用户信息
     */
    @GetMapping("/user/info")
    @ApiOperation(value = "登录后获取用户个人信息")
    public Result<UserInfo> userInfo( @ApiIgnore @CurrentUser GitEggUser currentUser) {
        GitEggUser tmp = GitEggAuthUtils.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/reg")
    @ApiOperation(value = "注册用户时，发送短信验证码")
    public Result<?> sendSmsReg(@Valid @RequestBody SmsDTO smsDTO) {
        String phoneNumber = smsDTO.getMobile();
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("user_account", phoneNumber).or().eq("user_nick_name", phoneNumber).or().eq("user_email", phoneNumber)
                .or().eq("user_mobile", phoneNumber);
        List<User> userList = userService.list(ew);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new BusinessException("账号已经存在");
        }
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("验证码发送超过最大次数");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_times", 1);
//        }
        String smsCode = String.valueOf(new Random().nextInt(899999) + 100000);
//        System.out.println("注册短信：" + smsCode);
//        iSmsService.sendVcodeSms(phoneNumber, smsCode);
//        cacheChannel.set("smsCode", phoneNumber + "_sms_reg", smsCode);
        return Result.success("验证码发送成功");
    }

    /**
     * 注册用户
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    public Result<?> create(@Valid @RequestBody RegisterUserDTO user) {
        String phoneNumber = user.getMobile();
        String userSmsCode = user.getSmsCode();
//        CacheObject smsCodeCache = cacheChannel.get("smsCode", phoneNumber + "_sms_reg");
//        String smsCode = (String) smsCodeCache.getValue();
//        if (StringUtils.isEmpty(smsCode)) {
//            return Result.error("短信验证码已失效，请重新获取");
//        }
//        if (!smsCode.equals(userSmsCode)) {
//            return Result.error("短信验证码错误，请重新输入");
//        }
CreateUserDTO createUser = BeanCopierUtils.copyByClass(user, CreateUserDTO.class);
        createUser.setRoleId(8L);
        createUser.setStatus(1);
        createUser.setAccount(phoneNumber);
        boolean result = userService.createUser(createUser);
        if (result) {
            return Result.success("注册成功");
        } else {
            return Result.error("添加失败，请重试");
        }
    }

    /**
     * 发送修改密码的短信验证码
     */
    @PostMapping("/sms/pwd")
    @ApiOperation(value = "未登录用户找回密码，发送修改密码的短信验证码")
    public Result<?> sendSmsPwd(@Valid @RequestBody SmsDTO smsDTO) {
        String phoneNumber = smsDTO.getMobile();
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("USER_MOBILE", smsDTO.getMobile());
        User user = userService.getOne(wrapper);
        if (null == user) {
            return Result.error("该手机尚未注册");
        }
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("验证码发送超过最大次数");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_times", 1);
//        }
//        String smsCode = String.valueOf(new Random().nextInt(899999) + 100000);
//        System.out.println("注册短信：" + smsCode);
//        iSmsService.sendVcodeSms(phoneNumber, smsCode);
//        cacheChannel.set("smsCode", phoneNumber + "_sms_pwd", smsCode);
        return Result.success("验证码发送成功");
    }

    /**
     * 验证找回密码
     */
    @PostMapping("/pwd/code/check")
    @ApiOperation(value = "未登录用户找回密码，判断验证码是否正确")
    public Result<?> pwdCodeCheck(@Valid @RequestBody RegisterUserDTO user) {
        String phoneNumber = user.getMobile();
        String userSmsCode = user.getSmsCode();
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("超过最大验证次数，请明天再试");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", 1);
//        }
//        CacheObject smsCodeCache = cacheChannel.get("smsCode", phoneNumber + "_sms_pwd");
//        String smsCode = (String) smsCodeCache.getValue();
//        if (StringUtils.isEmpty(smsCode)) {
//            return Result.error("短信验证码已失效，请重新获取");
//        }
//        if (!smsCode.equals(userSmsCode)) {
//            return Result.error("短信验证码错误，请重新输入");
//        }
        return Result.success();
    }

    /**
     * 更新密码
     */
    @PostMapping("/pwd/update")
    @ApiOperation(value = "未登录用户找回密码，更新密码")
    public Result<?> changePwd(@Valid @RequestBody PwdUserDTO user) {
        String phoneNumber = user.getMobile();
        String userSmsCode = user.getSmsCode();
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_pwd_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("超过最大验证次数，请明天再试");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", 1);
//        }
//        CacheObject smsCodeCache = cacheChannel.get("smsCode", phoneNumber + "_sms_pwd");
//        String smsCode = (String) smsCodeCache.getValue();
//        if (StringUtils.isEmpty(smsCode)) {
//            return Result.error("短信验证码已失效，请重新获取");
//        }
//        if (!smsCode.equals(userSmsCode)) {
//            return Result.error("短信验证码错误，请重新输入");
//        }
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        String pwd = userEntity.getPassword();
        if (!StringUtils.isEmpty(pwd)) {
            User oldUser = userService.queryUserByAccount(phoneNumber);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldUser.getAccount() + DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userEntity.setPassword(cryptPwd);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_mobile", userEntity.getMobile());
        boolean result = userService.update(userEntity, wrapper);
        if (result) {
            return Result.success("密码修改成功,请登录");
        } else {
            return Result.error("密码修改失败，请重试");
        }
    }

    /**
     * 发送修改密码的短信验证码
     */
    @PostMapping("/sms/personal/pwd")
    @ApiOperation(value = "登录用户，发送修改密码的短信验证码")
    public Result<?> sendPersonalSmsPwd(@ApiIgnore User userc) {
        if (null == userc) {
            return Result.error("当前用户未登陆");
        }
        String phoneNumber = userc.getMobile();
        if (StringUtils.isEmpty(phoneNumber)) {
            return Result.error("未获取到当前登录用户手机号");
        }
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_pw_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("验证码发送超过最大次数");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pw_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pw_times", 1);
//        }
//        String smsCode = String.valueOf(new Random().nextInt(899999) + 100000);
//        System.out.println("注册短信：" + smsCode);
//        iSmsService.sendVcodeSms(phoneNumber, smsCode);
//        cacheChannel.set("smsCode", phoneNumber + "_sms_pwd_pw", smsCode);
        return Result.success();
    }

    /**
     * 验证找回密码
     */
    @GetMapping("/pwd/personal/code/check/{scode}")
    @ApiOperation(value = "登录用户，判断验证码是否正确")
    public Result<?> pwdPersonalCodeCheck(@PathVariable("scode") String scode, @ApiIgnore User userc) {
        if (null == userc) {
            return Result.error("当前用户未登陆");
        }
        String phoneNumber = userc.getMobile();
        if (StringUtils.isEmpty(phoneNumber)) {
            return Result.error("未获取到当前登录用户手机号");
        }
//        String userSmsCode = scode;
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_pw_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("超过最大验证次数，请明天再试");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pw_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pw_times", 1);
//        }
//        CacheObject smsCodeCache = cacheChannel.get("smsCode", phoneNumber + "_sms_pwd_pw");
//        String smsCode = (String) smsCodeCache.getValue();
//        if (StringUtils.isEmpty(smsCode)) {
//            return Result.error("短信验证码已失效，请重新获取");
//        }
//        if (!smsCode.equals(userSmsCode)) {
//            return Result.error("短信验证码错误，请重新输入");
//        }
        return Result.success();
    }

    /**
     * 更新密码
     */
    @PostMapping("/pwd/personal/update")
    @ApiOperation(value = "登录用户，更新密码")
    public Result<?> changePwdPersonal(@Valid @RequestBody UpdateUserDTO user, @ApiIgnore User userc) {
         String phoneNumber = userc.getMobile();
//        String userSmsCode = user.getSmsCode();
//        CacheObject smsTimesCache = cacheChannel.get("smsTimes", phoneNumber + "_sms_pwd_times");
//        Integer vcodeNumbers = (Integer) smsTimesCache.getValue();
//        if (null != vcodeNumbers) {
//            int num = vcodeNumbers.intValue();
//            if (num >= smsTimes) {
//                return Result.error("超过最大验证次数，请明天再试");
//            }
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", num + 1);
//        } else {
//            cacheChannel.set("smsTimes", phoneNumber + "_sms_pwd_times", 1);
//        }
//        CacheObject smsCodeCache = cacheChannel.get("smsCode", phoneNumber + "_sms_pwd_pw");
//        String smsCode = (String) smsCodeCache.getValue();
//        if (StringUtils.isEmpty(smsCode)) {
//            return Result.error("短信验证码已失效，请重新获取");
//        }
//        if (!smsCode.equals(userSmsCode)) {
//            return Result.error("短信验证码错误，请重新输入");
//        }
        User userEntity = new User();
        userEntity.setMobile(phoneNumber);
        String pwd = user.getPassword();
        if (!StringUtils.isEmpty(pwd)) {
            User oldUser = userService.queryUserByAccount(phoneNumber);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldUser.getAccount() + DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userEntity.setPassword(cryptPwd);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_mobile", userEntity.getMobile());
        boolean result = userService.update(userEntity, wrapper);
        if (result) {
            return Result.success("密码修改成功,请登录");
        } else {
            return Result.error("密码修改失败，请重试");
        }
    }
}
