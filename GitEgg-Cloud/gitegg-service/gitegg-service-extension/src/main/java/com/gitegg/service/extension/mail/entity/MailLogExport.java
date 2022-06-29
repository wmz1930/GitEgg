package com.gitegg.service.extension.mail.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.gitegg.platform.boot.excel.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 邮件记录
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="MailLog对象", description="邮件记录数据导出")
public class MailLogExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邮件渠道")
    @ExcelProperty(value = "邮件渠道" ,index = 0)
    @ColumnWidth(20)
    private Long channelId;

    @ApiModelProperty(value = "邮件模板")
    @ExcelProperty(value = "邮件模板" ,index = 1)
    @ColumnWidth(20)
    private Long templateId;

    @ApiModelProperty(value = "邮件主题")
    @ExcelProperty(value = "邮件主题" ,index = 2)
    @ColumnWidth(20)
    private String mailSubject;

    @ApiModelProperty(value = "发送人")
    @ExcelProperty(value = "发送人" ,index = 3)
    @ColumnWidth(20)
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    @ExcelProperty(value = "收件人" ,index = 4)
    @ColumnWidth(20)
    private String mailTo;

    @ApiModelProperty(value = "抄送")
    @ExcelProperty(value = "抄送" ,index = 5)
    @ColumnWidth(20)
    private String mailCc;

    @ApiModelProperty(value = "密抄送")
    @ExcelProperty(value = "密抄送" ,index = 6)
    @ColumnWidth(20)
    private String mailBcc;

    @ApiModelProperty(value = "邮件内容")
    @ExcelProperty(value = "邮件内容" ,index = 7)
    @ColumnWidth(20)
    private String mailContent;

    @ApiModelProperty(value = "附件名称")
    @ExcelProperty(value = "附件名称" ,index = 8)
    @ColumnWidth(20)
    private String attachmentName;

    @ApiModelProperty(value = "附件大小")
    @ExcelProperty(value = "附件大小" ,index = 9)
    @ColumnWidth(20)
    private String attachmentSize;

    @ApiModelProperty(value = "发送时间")
    @ExcelProperty(value = "发送时间" ,index = 10, converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送结果码")
    @ExcelProperty(value = "发送结果码" ,index = 11)
    @ColumnWidth(20)
    private String sendResultCode;

    @ApiModelProperty(value = "发送结果消息")
    @ExcelProperty(value = "发送结果消息" ,index = 12)
    @ColumnWidth(20)
    private String sendResultMsg;
}
