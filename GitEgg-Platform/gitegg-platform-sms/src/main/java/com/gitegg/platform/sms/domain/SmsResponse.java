package com.gitegg.platform.sms.domain;

import lombok.Data;

/**
 * 短信发送返回消息
 */
@Data
public class SmsResponse {

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

}
