package com.gitegg.oauth.enums;

/**
 * @author GitEgg
 */

public enum AuthEnum {

    /**
     * 密码登录
     */
    PASSWORD("password", "密码登录"),

    /**
     * 验证码登录
     */
    CAPTCHA("captcha", "验证码登录"),

    /**
     * 短信验证码登录
     */
    SMS_CAPTCHA("sms_captcha", "短信验证码登录"),
    
    /**
     * 第三方登录
     */
    SOCIAL("social", "第三方登录"),

    /**
     * 二维码登录
     */
    QR("qr", "二维码登录");

    public String code;

    public String value;

    AuthEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
