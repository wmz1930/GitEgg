package com.gitegg.service.extension.sms.feign;

import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.sms.domain.SmsResponse;
import com.gitegg.service.extension.service.ISmsService;
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
    Result<Object> sendSmsNormal(@RequestParam("smsCode") String smsCode, @RequestParam("smsData") String smsData, @RequestParam("phoneNumbers") String phoneNumbers) {
        SmsResponse smsResponse = smsService.sendSmsNormal(smsCode, smsData, phoneNumbers);
        return Result.data(smsResponse);
    }

    @GetMapping(value = "/send/verification/code")
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    Result<Object> sendSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber) {
        SmsResponse smsResponse = smsService.sendSmsVerificationCode(smsCode, phoneNumber);
        return Result.data(smsResponse);
    }

    @GetMapping(value = "/check/verification/code")
    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
    Result<Boolean> checkSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode) {
        boolean checkResult = smsService.checkSmsVerificationCode(smsCode, phoneNumber, verificationCode);
        return Result.data(checkResult);
    }
}
