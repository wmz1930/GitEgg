package com.gitegg.platform.oauth2.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 公共鉴权url配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oauth-list")
public class AuthUrlWhiteListProperties {

    // 白名单配置，不需要鉴权的url
    private List<String> whiteUrls;

    //需要鉴权的公共url配置，不需要单独给所有的角色增加的url
    private List<String> authUrls;

}
