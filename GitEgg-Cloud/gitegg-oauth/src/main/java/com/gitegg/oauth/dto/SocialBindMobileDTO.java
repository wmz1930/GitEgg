package com.gitegg.oauth.dto;

import lombok.Data;

/**
 * 手机号绑定或注册绑定
 * @author GitEgg
 */
@Data
public class SocialBindMobileDTO {
    
    /**
     * 要绑定的加密后的socialId
     */
    private String socialKey;
    
    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 短信验证码模板编码
     */
    private String smsCode;
    
    /**
     * 短信验证码
     */
    private String code;

}
