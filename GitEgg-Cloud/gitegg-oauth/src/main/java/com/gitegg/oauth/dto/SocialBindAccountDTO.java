package com.gitegg.oauth.dto;

import lombok.Data;

/**
 * 账号绑定
 * @author GitEgg
 */
@Data
public class SocialBindAccountDTO {
    
    /**
     * 要绑定的加密后的socialId
     */
    private String socialKey;
    
    /**
     * 账号
     */
    private String username;
    
    /**
     * 登录密码
     */
    private String password;

}
