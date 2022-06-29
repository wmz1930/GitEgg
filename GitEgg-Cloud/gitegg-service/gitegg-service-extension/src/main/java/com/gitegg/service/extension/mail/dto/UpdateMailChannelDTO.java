package com.gitegg.service.extension.mail.dto;

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
 * 邮件渠道表
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MailChannel对象", description="邮件渠道表")
public class UpdateMailChannelDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道编码")
    @NotBlank(message="渠道编码不能为空")
    @Length(min=1,max=32)
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    @NotBlank(message="渠道名称不能为空")
    @Length(min=1,max=32)
    private String channelName;

    @ApiModelProperty(value = "SMTP地址")
    @NotBlank(message="SMTP地址不能为空")
    @Length(min=1,max=100)
    private String host;

    @ApiModelProperty(value = "SMTP端口")
    @NotBlank(message="SMTP端口不能为空")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer port;

    @ApiModelProperty(value = "账户名")
    @NotBlank(message="账户名不能为空")
    @Length(min=1,max=100)
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    @Length(min=1,max=100)
    private String password;

    @ApiModelProperty(value = "协议")
    @Length(min=1,max=32)
    private String protocol;

    @ApiModelProperty(value = "默认编码")
    @Length(min=1,max=32)
    private String defaultEncoding;

    @ApiModelProperty(value = "会话JNDI")
    @Length(min=1,max=100)
    private String jndiName;

    @ApiModelProperty(value = "JavaMail配置")
    @Length(min=1,max=500)
    private String properties;

    @ApiModelProperty(value = "渠道状态")
    @NotBlank(message="渠道状态不能为空")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer channelStatus;

    @ApiModelProperty(value = "描述")
    @Length(min=1,max=255)
    private String comments;
}
