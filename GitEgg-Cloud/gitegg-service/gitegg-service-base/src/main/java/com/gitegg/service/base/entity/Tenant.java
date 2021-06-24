package com.gitegg.service.base.entity;

import java.time.LocalDateTime;

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
 * 租户信息表
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_tenant")
@ApiModel(value = "Tenant对象", description = "租户信息表")
public class Tenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户名称")
    @TableField("TENANT_NAME")
    private String tenantName;

    @ApiModelProperty(value = "域名")
    @TableField("DOMAIN_NAME")
    private String domainName;

    @ApiModelProperty(value = "背景图片")
    @TableField("BACKGROUND_IMAGE")
    private String backgroundImage;

    @ApiModelProperty(value = "联系人")
    @TableField("CONTACTS")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @TableField("CONTACT_NUMBER")
    private String contactNumber;

    @ApiModelProperty(value = "联系地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "账号限额")
    @TableField("ACCOUNT_LIMIT")
    private Long accountLimit;

    @ApiModelProperty(value = "过期时间")
    @TableField("EXPIRE_TIME")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "授权码")
    @TableField("LICENSE_KEY")
    private String licenseKey;

    @ApiModelProperty(value = "租户状态 '0'禁用，'1' 启用,")
    @TableField("TENANT_STATUS")
    private Integer tenantStatus;

    @ApiModelProperty(value = "备注")
    @TableField("COMMENTS")
    private String comments;

}
