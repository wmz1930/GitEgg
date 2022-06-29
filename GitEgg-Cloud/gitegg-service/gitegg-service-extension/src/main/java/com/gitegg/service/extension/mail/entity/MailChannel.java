package com.gitegg.service.extension.mail.entity;

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
* 邮件渠道表
* </p>
*
* @author GitEgg
* @since 2022-06-24
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_mail_channel")
@ApiModel(value = "MailChannel对象", description = "邮件渠道表")
public class MailChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "渠道编码")
    @TableField("channel_code")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    @TableField("channel_name")
    private String channelName;

    @ApiModelProperty(value = "SMTP服务器地址")
    @TableField("host")
    private String host;

    @ApiModelProperty(value = "SMTP服务器端口")
    @TableField("port")
    private Integer port;

    @ApiModelProperty(value = "账户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "协议")
    @TableField("protocol")
    private String protocol;

    @ApiModelProperty(value = "默认编码")
    @TableField("default_encoding")
    private String defaultEncoding;

    @ApiModelProperty(value = "会话JNDI名称")
    @TableField("jndi_name")
    private String jndiName;

    @ApiModelProperty(value = "JavaMail 配置")
    @TableField("properties")
    private String properties;

    @ApiModelProperty(value = "渠道状态 1有效 0禁用")
    @TableField("channel_status")
    private Integer channelStatus;

    @ApiModelProperty(value = "MD5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty(value = "描述")
    @TableField("comments")
    private String comments;


}
