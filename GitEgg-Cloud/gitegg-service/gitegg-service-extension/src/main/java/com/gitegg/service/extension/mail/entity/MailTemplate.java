package com.gitegg.service.extension.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_sys_mail_template")
@ApiModel(value = "MailTemplate对象", description = "邮件模板")
public class MailTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模板编码")
    @TableField("template_code")
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    @TableField("template_name")
    private String templateName;

    @ApiModelProperty(value = "模板签名")
    @TableField("sign_name")
    private String signName;

    @ApiModelProperty(value = "模板状态")
    @TableField("template_status")
    private Integer templateStatus;

    @ApiModelProperty(value = "模板类型")
    @TableField("template_type")
    private Integer templateType;

    @ApiModelProperty(value = "模板内容")
    @TableField("template_content")
    private String templateContent;

    @ApiModelProperty(value = "缓存key")
    @TableField("cache_code_key")
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期 值")
    @TableField("cache_time_out")
    private Long cacheTimeOut;

    @ApiModelProperty(value = "缓存有效期 单位")
    @TableField("cache_time_out_unit")
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    @TableField("send_times_limit")
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    @TableField("send_times_limit_period")
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "限制时间间隔 单位")
    @TableField("send_times_limit_period_unit")
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    @TableField("comments")
    private String comments;


}
