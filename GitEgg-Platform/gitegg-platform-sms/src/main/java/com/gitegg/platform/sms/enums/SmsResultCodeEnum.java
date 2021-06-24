package com.gitegg.platform.sms.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: ResultCodeEnum
 * @Description: 自定义返回码枚举
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
@Getter
@AllArgsConstructor
public enum SmsResultCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 系统繁忙，请稍后重试
     */
    ERROR(429, "短信发送失败，请稍后重试"),

    /**
     * 系统错误
     */
    PHONE_NUMBER_ERROR(500, "手机号错误");

    /**
     * 短信发送返回结果码
     */
    public int code;

    /**
     * 短信发送返回结果信息
     */
    public String msg;
}
