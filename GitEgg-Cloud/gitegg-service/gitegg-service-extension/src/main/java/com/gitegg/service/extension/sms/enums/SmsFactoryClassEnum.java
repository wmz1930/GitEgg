package com.gitegg.service.extension.sms.enums;


/**
 * @ClassName: SmsFactoryClassEnum
 * @Description: 短信工厂类
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum SmsFactoryClassEnum {

    /**
     * 阿里云短信
     */
    ALI_YUN("aliyun", "com.gitegg.service.extension.sms.factory.SmsAliyunFactory"),

    /**
     * 腾讯云短信
     */
    TENCENT("tencent", "com.gitegg.service.extension.sms.factory.SmsTencentFactory"),

    /**
     * 七牛云短信
     */
    QI_NIU("qiniu", "com.gitegg.service.extension.sms.factory.SmsQiniuFactory");

    public String code;

    public String value;

    SmsFactoryClassEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        SmsFactoryClassEnum[] smsFactoryClassEnums = values();
        for (SmsFactoryClassEnum smsFactoryClassEnum : smsFactoryClassEnums) {
            if (smsFactoryClassEnum.getCode().equals(code)) {
                return smsFactoryClassEnum.getValue();
            }
        }
        return null;
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
