package com.gitegg.service.extension.wx.miniapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 微信小程序配置
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MiniappDTO对象", description="微信小程序配置")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"createTime", "updateTime"}, ignoreUnknown = true)
public class MiniappDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "小程序名称")
    private String miniappName;

    @ApiModelProperty(value = "appid")
    private String appid;

    @ApiModelProperty(value = "secret")
    private String secret;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "aesKey")
    private String aesKey;

    @ApiModelProperty(value = "消息格式")
    private String msgDataFormat;

    @ApiModelProperty(value = "缓存类型")
    private String storageType;

    @ApiModelProperty(value = "缓存前缀")
    private String keyPrefix;

    @ApiModelProperty(value = "Redis地址")
    private String redisHost;

    @ApiModelProperty(value = "Redis端口")
    private Integer redisPort;

    @ApiModelProperty(value = "http类型")
    private String httpClientType;

    @ApiModelProperty(value = "代理地址")
    private String httpProxyHost;

    @ApiModelProperty(value = "代理端口")
    private String httpProxyPort;

    @ApiModelProperty(value = "代理账号")
    private String httpProxyUsername;

    @ApiModelProperty(value = "代理密码")
    private String httpProxyPassword;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "MD5")
    private String md5;

    @ApiModelProperty(value = "描述")
    private String comments;
}