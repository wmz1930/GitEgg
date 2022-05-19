package com.gitegg.service.extension.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录功能配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSocialDTO对象", description="租户第三方登录功能配置表")
public class JustAuthSocialDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "第三方ID	")
    private String uuid;

    @ApiModelProperty(value = "用户来源")
    private String source;

    @ApiModelProperty(value = "授权令牌")
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "OpenId")
    private String openId;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "授权信息")
    private String accessCode;

    @ApiModelProperty(value = "UnionId")
    private String unionId;

    @ApiModelProperty(value = "用户权限")
    private String scope;

    @ApiModelProperty(value = "授权类型")
    private String tokenType;

    @ApiModelProperty(value = "IdToken")
    private String idToken;

    @ApiModelProperty(value = "mac_algorithm")
    private String macAlgorithm;

    @ApiModelProperty(value = "mac_key")
    private String macKey;

    @ApiModelProperty(value = "授权code")
    private String code;

    @ApiModelProperty(value = "oauth_token")
    private String oauthToken;

    @ApiModelProperty(value = "oauth_token_secret")
    private String oauthTokenSecret;
}
