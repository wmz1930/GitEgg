package com.gitegg.service.extension.sms.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SmsTemplate对象", description="短信配置表")
public class QuerySmsTemplateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短信渠道")
    private Long channelId;

    @ApiModelProperty(value = "短信编码")
    private String smsCode;

    @ApiModelProperty(value = "短信名称")
    private String smsName;

    @ApiModelProperty(value = "模板ID")
    private String templateId;
    
    @ApiModelProperty(value = "短信状态")
    private Integer templateStatus;
    
    @ApiModelProperty(value = "渠道状态")
    private Integer channelStatus;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
