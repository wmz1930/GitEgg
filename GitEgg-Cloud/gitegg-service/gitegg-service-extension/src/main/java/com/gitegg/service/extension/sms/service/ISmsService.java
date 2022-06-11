package com.gitegg.service.extension.sms.service;

import com.gitegg.platform.base.result.Result;

/**
 * <p>
 * 短信发送接口定义
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-25
 */
public interface ISmsService {


    /**
     * 发送短信
     *
     * @param smsCode
     * @param smsData
     * @param phoneNumbers
     * @return
     */
    Result<?> sendSmsNormal(String smsCode, String smsData, String phoneNumbers);

    /**
     * 发送短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    Result<?> sendSmsVerificationCode( String smsCode, String phoneNumber);

    /**
     * 校验短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    Result<?> checkSmsVerificationCode(String smsCode, String phoneNumber, String verificationCode);
    
    /**
     * 前置校验短信验证码，使用场景为输入框输入之后进行实时校验
     * @param smsCode
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    Result<?> checkSmsVerificationCodePre(String smsCode, String phoneNumber, String verificationCode);
}
