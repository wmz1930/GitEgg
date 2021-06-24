package com.gitegg.service.extension.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_sys_sms_channel")
@ApiModel(value="SmsChannel对象", description="短信渠道表")
public class SmsChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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
    private Boolean channelStatus;

    @ApiModelProperty(value = "描述")
    private String comments;


}
