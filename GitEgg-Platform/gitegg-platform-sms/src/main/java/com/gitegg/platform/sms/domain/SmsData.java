package com.gitegg.platform.sms.domain;

import lombok.Data;

import java.util.Map;

/**
 * 短信内容
 */
@Data
public class SmsData {

    /**
     * 短信模板
     */
    private String templateId;

    /**
     * 参数列表
     */
    private Map<String, String> params;
}
