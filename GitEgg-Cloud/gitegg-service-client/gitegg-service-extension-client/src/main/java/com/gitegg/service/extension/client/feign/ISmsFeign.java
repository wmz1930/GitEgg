package com.gitegg.service.extension.client.feign;

import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 添加contextId用于区分相同name的client，否则会报错
 * @author GitEgg
 */
@FeignClient(name = "gitegg-service-extension", contextId = "SmsClient")
public interface ISmsFeign {

    /**
     * 发送短信
     *
     * @param smsCode
     * @param smsData
     * @param phoneNumbers
     * @return
     */
    @GetMapping("/feign/sms/send/normal")
    Result<Object> sendSmsNormal(@RequestParam("smsCode") String smsCode, @RequestParam("smsData") String smsData, @RequestParam("phoneNumbers") String phoneNumbers);

    /**
     * 发送短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    @GetMapping("/feign/sms/send/verification/code")
    Result<Object> sendSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber);

    /**
     * 校验短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    @GetMapping("/feign/sms/check/verification/code")
    Result<Boolean> checkSmsVerificationCode(@RequestParam("smsCode") String smsCode, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("verificationCode") String verificationCode);

}
