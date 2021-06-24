package com.gitegg.platform.sms.service;

import cn.hutool.core.util.StrUtil;
import com.gitegg.platform.sms.domain.SmsData;
import com.gitegg.platform.sms.domain.SmsResponse;

import java.util.Collection;
import java.util.Collections;

/**
 * 短信发送接口
 */
public interface ISmsSendService {

    /**
     * 发送单个短信
     * @param smsData
     * @param phoneNumber
     * @return
     */
    default SmsResponse sendSms(SmsData smsData, String phoneNumber){
        if (StrUtil.isEmpty(phoneNumber)) {
            return new SmsResponse();
        }
        return this.sendSms(smsData, Collections.singletonList(phoneNumber));
    }

    /**
     * 群发发送短信
     * @param smsData
     * @param phoneNumbers
     * @return
     */
    SmsResponse sendSms(SmsData smsData, Collection<String> phoneNumbers);

}
