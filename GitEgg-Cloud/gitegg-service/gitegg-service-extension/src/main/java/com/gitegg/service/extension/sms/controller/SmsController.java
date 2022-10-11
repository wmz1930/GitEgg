package com.gitegg.service.extension.sms.controller;


import com.anji.captcha.service.CaptchaService;
import com.gitegg.platform.base.annotation.resubmit.ResubmitLock;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.captcha.util.CaptchaUtils;
import com.gitegg.service.extension.sms.constant.SmsConstant;
import com.gitegg.service.extension.sms.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
* <p>
* 短信发送服务
* </p>
*
* @author GitEgg
* @since 2021-01-25
*/
@RestController
@RequestMapping("/extension/sms")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsController|短信配置表前端控制器", tags = {"短信发送服务"})
@RefreshScope
public class SmsController {
    
    private final RedisTemplate redisTemplate;
    
    private final CaptchaService captchaService;
    
    private final ISmsService smsService;
    
    /**
     * 发送短信验证码通用方法，使用防止重复提交锁来控制重复发送间隔,60s内只允许发送一次验证码
     * TODO 反射获取Map字段值
     * @param parameters
     * @return
     */
    @ApiOperation("发送短信验证码")
    @PostMapping("/code/send")
//    @ResubmitLock(interval = SmsConstant.SMS_INTERVAL, keys = {"[0]['" + TokenConstant.SMS_CODE + "']", "[0]['" + TokenConstant.PHONE_NUMBER + "']"})
    public Result<?> sendSmsCaptcha(@ApiIgnore @RequestParam Map<String, String> parameters) {
       boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
       Result<?> sendResult;
       if (checkCaptchaResult)
       {
          sendResult = smsService.sendSmsVerificationCode(parameters.get(TokenConstant.SMS_CODE), parameters.get(TokenConstant.PHONE_NUMBER));
       }
       else {
          throw new BusinessException("请通过正确的安全验证，再发送短信验证码");
       }
       return sendResult;
    }
    
    @GetMapping(value = "/check/code")
    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
    Result<?> checkSmsSmsCaptcha(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode) {
        return smsService.checkSmsVerificationCode(smsCode, phoneNumber, verificationCode);
    }
    
    @GetMapping(value = "/check/code/pre")
    @ApiOperation(value = "前置校验短信验证码", notes = "前置校验短信验证码")
    Result<?> checkSmsSmsCaptchaPre(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode) {
        return smsService.checkSmsVerificationCodePre(smsCode, phoneNumber, verificationCode);
    }

 }
