package com.gitegg.service.extension.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_just_auth_social")
@ApiModel(value = "JustAuthSocial对象", description = "租户第三方登录功能配置表")
public class JustAuthSocial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "第三方系统的唯一ID	")
    @TableField("uuid")
    private String uuid;

    @ApiModelProperty(value = "第三方用户来源")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "用户的授权令牌")
    @TableField("access_token")
    private String accessToken;

    @ApiModelProperty(value = "第三方用户的授权令牌的有效期")
    @TableField("expire_in")
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @TableField("refresh_token")
    private String refreshToken;

    @ApiModelProperty(value = "第三方用户的 open id")
    @TableField("open_id")
    private String openId;

    @ApiModelProperty(value = "第三方用户的 ID")
    @TableField("uid")
    private String uid;

    @ApiModelProperty(value = "个别平台的授权信息")
    @TableField("access_code")
    private String accessCode;

    @ApiModelProperty(value = "第三方用户的 union id")
    @TableField("union_id")
    private String unionId;

    @ApiModelProperty(value = "第三方用户授予的权限")
    @TableField("scope")
    private String scope;

    @ApiModelProperty(value = "个别平台的授权信息")
    @TableField("token_type")
    private String tokenType;

    @ApiModelProperty(value = "id token")
    @TableField("id_token")
    private String idToken;

    @ApiModelProperty(value = "小米平台用户的附带属性")
    @TableField("mac_algorithm")
    private String macAlgorithm;

    @ApiModelProperty(value = "小米平台用户的附带属性")
    @TableField("mac_key")
    private String macKey;

    @ApiModelProperty(value = "用户的授权code")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "Twitter平台用户的附带属性")
    @TableField("oauth_token")
    private String oauthToken;

    @ApiModelProperty(value = "Twitter平台用户的附带属性")
    @TableField("oauth_token_secret")
    private String oauthTokenSecret;


}
