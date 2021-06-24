package com.gitegg.service.extension.sms.service;

import com.gitegg.platform.sms.domain.SmsResponse;

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
    SmsResponse sendSmsNormal(String smsCode, String smsData, String phoneNumbers);

    /**
     * 发送短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    SmsResponse sendSmsVerificationCode( String smsCode, String phoneNumber);

    /**
     * 校验短信验证码
     *
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    boolean checkSmsVerificationCode(String smsCode, String phoneNumber, String verificationCode);

}
