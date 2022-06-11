package com.gitegg.service.extension.sms.dto;

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
 * 短信配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SmsTemplate对象", description="短信配置表")
public class UpdateSmsTemplateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "短信渠道")
    @NotBlank(message="短信渠道不能为空")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long channelId;

    @ApiModelProperty(value = "短信编码")
    @NotBlank(message="短信编码不能为空")
    @Length(min=1,max=32)
    private String smsCode;

    @ApiModelProperty(value = "短信名称")
    @NotBlank(message="短信名称不能为空")
    @Length(min=1,max=32)
    private String smsName;

    @ApiModelProperty(value = "模板ID")
    @NotBlank(message="模板ID不能为空")
    @Length(min=1,max=64)
    private String templateId;

    @ApiModelProperty(value = "短信签名")
    @NotBlank(message="短信签名不能为空")
    @Length(min=1,max=64)
    private String signName;

    @ApiModelProperty(value = "短信状态")
    @NotBlank(message="短信状态不能为空")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer templateStatus;
    
    @ApiModelProperty(value = "短信类型")
    @NotBlank(message="短信类型不能为空")
    @Min(0)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer templateType;

    @ApiModelProperty(value = "缓存key")
    @NotBlank(message="缓存key不能为空")
    @Length(min=1,max=255)
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long cacheTimeOut;

    @ApiModelProperty(value = "有效期单位")
    @Length(min=1,max=32)
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    @Min(-9223372036854775808L)
    @Max(9223372036854775807L)
    @Length(min=1,max=19)
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "时间间隔单位")
    @Length(min=1,max=32)
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    @Length(min=1,max=255)
    private String comments;
}
