package com.gitegg.service.extension.sms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import java.time.LocalDateTime;
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
 * @since 2022-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SmsChannelDTO对象", description="短信渠道表")
public class SmsChannelDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "SecretId")
    private String secretId;

    @ApiModelProperty(value = "SecretKey")
    private String secretKey;

    @ApiModelProperty(value = "RegionId")
    private String regionId;

    @ApiModelProperty(value = "渠道状态")
    private Integer channelStatus;

    @ApiModelProperty(value = "短信数量")
    private Long smsQuantity;

    @ApiModelProperty(value = "有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime smsValidity;

    @ApiModelProperty(value = "描述")
    private String comments;
}
