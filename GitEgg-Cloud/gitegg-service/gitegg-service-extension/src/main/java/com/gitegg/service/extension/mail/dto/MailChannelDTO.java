package com.gitegg.service.extension.mail.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 邮件渠道表
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MailChannelDTO对象", description="邮件渠道表")
public class MailChannelDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "SMTP地址")
    private String host;

    @ApiModelProperty(value = "SMTP端口")
    private Integer port;

    @ApiModelProperty(value = "账户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "协议")
    private String protocol;

    @ApiModelProperty(value = "默认编码")
    private String defaultEncoding;

    @ApiModelProperty(value = "会话JNDI")
    private String jndiName;

    @ApiModelProperty(value = "JavaMail配置")
    private String properties;

    @ApiModelProperty(value = "渠道状态")
    private Integer channelStatus;

    @ApiModelProperty(value = "MD5")
    private String md5;

    @ApiModelProperty(value = "描述")
    private String comments;
}
