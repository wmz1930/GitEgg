package com.gitegg.service.extension.sms.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Sms 系统默认配置
 * @author GitEgg
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.verification-code")
public class SmsProperties {
    
    /**
     * 短信厂商验证码模板中的key
     */
    private String codeKey;

    /**
     * 验证码的长度
     */
    private Integer codeLength;

    /**
     * 为true则验证一次后立即删除验证码，然后重新获取
     */
    private Boolean deleteByVerifyOnce = false;

    /**
     * 验证码有效期
     */
    private Long expirationTime;
    
    /**
     * 验证码有效期单位
     */
    private String expirationTimeUnit;
    
    /**
     * 是否启用识别码
     */
    private Boolean useIdentificationCode = false;
    
    /**
     * 识别码的长度
     */
    private Integer identificationCodeLength = 6;
    
    /**
     * 验证码业务在保存到redis时的key的前缀
     */
    private String cacheKey = "SMS:CAPTCHA:KEY:";
    
    /**
     * 验证码重复发送的时间间隔
     */
    private Integer retryIntervalTime = 60;
    
    /**
     * 验证码验证的尝试次数
     */
    private Long retryVerificationTimes = 3L;

}
