package com.gitegg.service.extension.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

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
@ApiModel(value="JustAuthSocial对象", description="租户第三方登录功能配置表")
public class CreateJustAuthSocialDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第三方ID	")
    @Length(min=1,max=100)
    private String uuid;

    @ApiModelProperty(value = "用户来源")
    @Length(min=1,max=32)
    private String source;

    @ApiModelProperty(value = "授权令牌")
    @Length(min=1,max=500)
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @Length(min=1,max=500)
    private String refreshToken;

    @ApiModelProperty(value = "OpenId")
    @Length(min=1,max=100)
    private String openId;

    @ApiModelProperty(value = "用户ID")
    @Length(min=1,max=100)
    private String uid;

    @ApiModelProperty(value = "授权信息")
    @Length(min=1,max=255)
    private String accessCode;

    @ApiModelProperty(value = "UnionId")
    @Length(min=1,max=255)
    private String unionId;

    @ApiModelProperty(value = "用户权限")
    @Length(min=1,max=255)
    private String scope;

    @ApiModelProperty(value = "授权类型")
    @Length(min=1,max=255)
    private String tokenType;

    @ApiModelProperty(value = "IdToken")
    @Length(min=1,max=255)
    private String idToken;

    @ApiModelProperty(value = "mac_algorithm")
    @Length(min=1,max=255)
    private String macAlgorithm;

    @ApiModelProperty(value = "mac_key")
    @Length(min=1,max=255)
    private String macKey;

    @ApiModelProperty(value = "授权code")
    @Length(min=1,max=255)
    private String code;

    @ApiModelProperty(value = "oauth_token")
    @Length(min=1,max=255)
    private String oauthToken;

    @ApiModelProperty(value = "oauth_token_secret")
    @Length(min=1,max=255)
    private String oauthTokenSecret;
}
