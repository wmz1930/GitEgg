package com.gitegg.service.extension.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短信渠道表
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SmsChannel对象", description="短信渠道表")
public class QuerySmsChannelDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "短信发送的key id")
    private String secretId;

    @ApiModelProperty(value = "短信发送的秘钥")
    private String secretKey;

    @ApiModelProperty(value = "regionId仅阿里云需要")
    private String regionId;

    @ApiModelProperty(value = "渠道状态 1有效 0禁用")
    private Integer channelStatus;

    @ApiModelProperty(value = "描述")
    private String comments;


    @ApiModelProperty(value = "开始时间")
    private String startDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
