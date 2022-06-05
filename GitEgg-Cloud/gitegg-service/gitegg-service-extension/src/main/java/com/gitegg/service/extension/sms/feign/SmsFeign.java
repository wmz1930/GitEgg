package com.gitegg.service.extension.sms.feign;

import com.gitegg.platform.base.annotation.resubmit.ResubmitLock;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.sms.constant.SmsConstant;
import com.gitegg.service.extension.sms.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: SmsFeign
 * @Description: SmsFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/sms")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsFeign|提供微服务调用接口")
@RefreshScope
public class SmsFeign {

    private final ISmsService smsService;

    @GetMapping(value = "/send/normal")
    @ApiOperation(value = "发送普通短信", notes = "发送普通短信")
    Result<?> sendSmsNormal(@RequestParam("smsCode") String smsCode, @RequestParam("smsData") String smsData, @RequestParam("phoneNumbers") String phoneNumbers) {
        return smsService.sendSmsNormal(smsCode, smsData, phoneNumbers);
    }

    @GetMapping(value = "/send/verification/code")
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
//    @ResubmitLock(interval = SmsConstant.SMS_INTERVAL, keys = {"[0]['" + TokenConstant.SMS_CODE + "']", "[0]['" + TokenConstant.PHONE_NUMBER + "']"})
    Result<?> sendSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber) {
        return smsService.sendSmsVerificationCode(smsCode, phoneNumber);
    }

    @GetMapping(value = "/check/verification/code")
    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
    Result<?> checkSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode) {
        return smsService.checkSmsVerificationCode(smsCode, phoneNumber, verificationCode);
    }
    
    @GetMapping(value = "/check/verification/code/pre")
    @ApiOperation(value = "前置校验短信验证码", notes = "前置校验短信验证码")
    Result<?> checkSmsVerificationCodePre(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode) {
        return smsService.checkSmsVerificationCodePre(smsCode, phoneNumber, verificationCode);
    }
}
