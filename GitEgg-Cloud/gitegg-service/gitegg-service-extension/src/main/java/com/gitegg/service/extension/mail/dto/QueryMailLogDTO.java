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
@ApiModel(value="MailLog对象", description="邮件记录")
public class QueryMailLogDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邮件主题")
    private String mailSubject;

    @ApiModelProperty(value = "发送人")
    private String mailFrom;

    @ApiModelProperty(value = "收件人")
    private String mailTo;

    @ApiModelProperty(value = "邮件内容")
    private String mailContent;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
