package com.gitegg.service.extension.mail.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.time.LocalDateTime;
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
 * 邮件记录
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MailLog对象", description="邮件记录")
public class UpdateMailLogDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "邮件渠道")
    @NotBlank(message="邮件渠道不能为空")
    @Min(0L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long channelId;

    @ApiModelProperty(value = "邮件模板")
    @Min(0L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long templateId;

    @ApiModelProperty(value = "邮件主题")
    @NotBlank(message="邮件主题不能为空")
    @Length(min=1,max=255)
    private String mailSubject;

    @ApiModelProperty(value = "发送人")
    @NotBlank(message="发送人不能为空")
    @Length(min=1,max=100)
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    @NotBlank(message="收件人不能为空")
    @Length(min=1,max=65535)
    private String mailTo;

    @ApiModelProperty(value = "抄送")
    @Length(min=1,max=65535)
    private String mailCc;

    @ApiModelProperty(value = "密抄送")
    @Length(min=1,max=65535)
    private String mailBcc;

    @ApiModelProperty(value = "邮件内容")
    @NotBlank(message="邮件内容不能为空")
    @Length(min=1,max=65535)
    private String mailContent;

    @ApiModelProperty(value = "附件名称")
    @NotBlank(message="附件名称不能为空")
    @Length(min=1,max=32)
    private String attachmentName;

    @ApiModelProperty(value = "附件大小")
    @Length(min=1,max=32)
    private String attachmentSize;

    @ApiModelProperty(value = "发送时间")
    @Length(min=1,max=19)
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送结果码")
    @Length(min=1,max=32)
    private String sendResultCode;

    @ApiModelProperty(value = "发送结果消息")
    @Length(min=1,max=500)
    private String sendResultMsg;
}
