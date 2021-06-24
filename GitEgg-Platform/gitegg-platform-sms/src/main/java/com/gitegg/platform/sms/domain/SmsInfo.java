package com.gitegg.platform.sms.domain;

import lombok.Data;

import java.util.Collection;

/**
 * 短信请求内容
 */
@Data
public class SmsInfo {

    /**
     * 短信内容
     */
    private SmsData smaData;

    /**
     * 手机号码列表
     */
    private Collection<String> phoneNumbers;
}
