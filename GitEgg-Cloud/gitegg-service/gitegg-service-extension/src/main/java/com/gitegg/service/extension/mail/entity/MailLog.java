package com.gitegg.service.extension.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_sys_mail_log")
@ApiModel(value = "MailLog对象", description = "邮件记录")
public class MailLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "mail渠道id")
    @TableField("channel_id")
    private Long channelId;

    @ApiModelProperty(value = "mail模板id")
    @TableField("template_id")
    private Long templateId;

    @ApiModelProperty(value = "邮件主题")
    @TableField("mail_subject")
    private String mailSubject;

    @ApiModelProperty(value = "发送人")
    @TableField("mail_from")
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    @TableField("mail_to")
    private String mailTo;

    @ApiModelProperty(value = "抄送")
    @TableField("mail_cc")
    private String mailCc;

    @ApiModelProperty(value = "密抄送")
    @TableField("mail_bcc")
    private String mailBcc;

    @ApiModelProperty(value = "邮件内容")
    @TableField("mail_content")
    private String mailContent;

    @ApiModelProperty(value = "附件名称")
    @TableField("attachment_name")
    private String attachmentName;

    @ApiModelProperty(value = "附件大小")
    @TableField("attachment_size")
    private String attachmentSize;

    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送结果码")
    @TableField("send_result_code")
    private String sendResultCode;

    @ApiModelProperty(value = "发送结果消息")
    @TableField("send_result_msg")
    private String sendResultMsg;


}
