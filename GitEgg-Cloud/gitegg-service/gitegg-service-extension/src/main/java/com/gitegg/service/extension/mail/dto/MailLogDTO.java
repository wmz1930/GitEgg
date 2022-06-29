package com.gitegg.service.extension.mail.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.time.LocalDateTime;
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
@ApiModel(value="MailLogDTO对象", description="邮件记录")
public class MailLogDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "邮件渠道")
    private Long channelId;

    @ApiModelProperty(value = "邮件模板")
    private Long templateId;

    @ApiModelProperty(value = "邮件主题")
    private String mailSubject;

    @ApiModelProperty(value = "发送人")
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    private String mailTo;

    @ApiModelProperty(value = "抄送")
    private String mailCc;

    @ApiModelProperty(value = "密抄送")
    private String mailBcc;

    @ApiModelProperty(value = "邮件内容")
    private String mailContent;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "附件大小")
    private String attachmentSize;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送结果码")
    private String sendResultCode;

    @ApiModelProperty(value = "发送结果消息")
    private String sendResultMsg;
}
