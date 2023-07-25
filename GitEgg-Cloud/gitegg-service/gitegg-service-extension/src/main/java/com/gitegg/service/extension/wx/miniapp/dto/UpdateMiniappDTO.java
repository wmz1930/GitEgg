package com.gitegg.service.extension.wx.miniapp.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
 * 微信小程序配置
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Miniapp对象", description="微信小程序配置")
public class UpdateMiniappDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "小程序名称")
    @NotBlank(message="小程序名称不能为空")
    @Length(min=0,max=100)
    private String miniappName;

    @ApiModelProperty(value = "appid")
    @NotBlank(message="appid不能为空")
    @Length(min=0,max=100)
    private String appid;

    @ApiModelProperty(value = "secret")
    @NotBlank(message="secret不能为空")
    @Length(min=0,max=100)
    private String secret;

    @ApiModelProperty(value = "token")
    @Length(min=0,max=100)
    private String token;

    @ApiModelProperty(value = "aesKey")
    @Length(min=0,max=100)
    private String aesKey;

    @ApiModelProperty(value = "消息格式")
    @Length(min=0,max=10)
    private String msgDataFormat;

    @ApiModelProperty(value = "缓存类型")
    @Length(min=0,max=32)
    private String storageType;

    @ApiModelProperty(value = "缓存前缀")
    @Length(min=0,max=32)
    private String keyPrefix;

    @ApiModelProperty(value = "Redis地址")
    @Length(min=0,max=100)
    private String redisHost;

    @ApiModelProperty(value = "Redis端口")
    @Min(0L)
    @Max(2147483647L)
    private Integer redisPort;

    @ApiModelProperty(value = "http类型")
    @Length(min=0,max=32)
    private String httpClientType;

    @ApiModelProperty(value = "代理地址")
    @Length(min=0,max=32)
    private String httpProxyHost;

    @ApiModelProperty(value = "代理端口")
    @Length(min=0,max=100)
    private String httpProxyPort;

    @ApiModelProperty(value = "代理账号")
    @Length(min=0,max=100)
    private String httpProxyUsername;

    @ApiModelProperty(value = "代理密码")
    @Length(min=0,max=100)
    private String httpProxyPassword;

    @ApiModelProperty(value = "状态")
    @NotBlank(message="状态不能为空")
    @Length(min=0,max=32)
    private String status;

    @ApiModelProperty(value = "MD5")
    @Length(min=0,max=32)
    private String md5;

    @ApiModelProperty(value = "描述")
    @Length(min=0,max=255)
    private String comments;
}
