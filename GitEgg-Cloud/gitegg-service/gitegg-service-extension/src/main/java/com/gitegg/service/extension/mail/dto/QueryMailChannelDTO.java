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
@ApiModel(value="MailChannel对象", description="邮件渠道表")
public class QueryMailChannelDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "账户名")
    private String username;

    @ApiModelProperty(value = "渠道状态")
    private Integer channelStatus;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
