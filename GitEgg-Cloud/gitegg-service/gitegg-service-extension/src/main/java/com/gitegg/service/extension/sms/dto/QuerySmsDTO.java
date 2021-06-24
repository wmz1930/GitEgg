package com.gitegg.service.extension.sms.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短信配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Sms对象", description="短信配置表")
public class QuerySmsDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "短信渠道")
    private String smsChannel;

    @ApiModelProperty(value = "短信名称")
    private String smsName;

    @ApiModelProperty(value = "短信模板ID")
    private String templateId;

    @ApiModelProperty(value = "短信发送的key id")
    private String secretId;

    @ApiModelProperty(value = "短信发送的秘钥")
    private String secretKey;

    @ApiModelProperty(value = "regionId仅阿里云需要")
    private String regionId;

    @ApiModelProperty(value = "短信签名")
    private String signName;

    @ApiModelProperty(value = "短信状态 1有效 0禁用")
    private Integer smsStatus;

    @ApiModelProperty(value = "描述")
    private String comments;


    @ApiModelProperty(value = "开始时间")
    private String startDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
