package com.gitegg.service.extension.mail.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 邮件模板
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MailTemplateDTO对象", description="邮件模板")
public class MailTemplateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "模板签名")
    private String signName;

    @ApiModelProperty(value = "模板状态")
    private Integer templateStatus;

    @ApiModelProperty(value = "模板类型")
    private Integer templateType;

    @ApiModelProperty(value = "模板内容")
    private String templateContent;

    @ApiModelProperty(value = "缓存key")
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期")
    private Long cacheTimeOut;

    @ApiModelProperty(value = "缓存有效期单位")
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "限制时间间隔单位")
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    private String comments;
}
