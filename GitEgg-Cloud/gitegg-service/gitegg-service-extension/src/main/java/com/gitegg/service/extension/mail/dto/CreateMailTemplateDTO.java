package com.gitegg.service.extension.mail.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

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
@ApiModel(value="MailTemplate对象", description="邮件模板")
public class CreateMailTemplateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板编码")
    @NotBlank(message="模板编码不能为空")
    @Length(min=1,max=32)
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    @NotBlank(message="模板名称不能为空")
    @Length(min=1,max=32)
    private String templateName;

    @ApiModelProperty(value = "模板签名")
    @Length(min=1,max=64)
    private String signName;

    @ApiModelProperty(value = "模板状态")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer templateStatus;

    @ApiModelProperty(value = "模板类型")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer templateType;

    @ApiModelProperty(value = "模板内容")
    @Length(min=1,max=65535)
    private String templateContent;

    @ApiModelProperty(value = "缓存key")
    @Length(min=1,max=255)
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期")
    @Min(0L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long cacheTimeOut;

    @ApiModelProperty(value = "缓存有效期单位")
    @Length(min=1,max=32)
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    @Min(0L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    @Min(0L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "限制时间间隔单位")
    @Length(min=1,max=32)
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    @Length(min=1,max=255)
    private String comments;
}
