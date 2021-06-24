package com.gitegg.platform.oauth2.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2获取Token返回信息封装
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(description = "Oauth2Token")
public class Oauth2Token {

    @ApiModelProperty(value = "访问令牌")
    private String token;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "访问令牌头前缀")
    private String tokenHead;

    @ApiModelProperty(value = "token有效时间（秒）")
    private int expiresIn;

    @ApiModelProperty(value = "token过期时间")
    private Long exp;

    @ApiModelProperty(value = "refreshToken有效时间（秒）")
    private int refreshExpiresIn;

    @ApiModelProperty(value = "refreshToken过期时间")
    private Long refreshExp;
}
