package com.gitegg.service.extension.mail.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author 发送邮件的DTO
 * @date 2022/6/28
 */
@Data
public class SendSimpleMailDTO {

    @ApiModelProperty(value = "邮件渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "邮件模板编码")
    private String templateCode;

    @ApiModelProperty(value = "模板需替换的值")
    private Map<String,String> templateKeyValue;

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

    @ApiModelProperty(value = "是否是html格式")
    private Boolean htmlFlag = false;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "附件大小")
    private String attachmentSize;

    @ApiModelProperty(value = "附件内容")
    private Object attachment;

}
