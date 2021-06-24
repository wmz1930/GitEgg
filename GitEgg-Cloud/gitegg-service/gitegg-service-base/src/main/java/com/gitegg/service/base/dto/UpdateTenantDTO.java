package com.gitegg.service.base.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 修改租户信息
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
@Data
@ApiModel(value = "UpdateTenantDTO", description = "修改租户信息")
public class UpdateTenantDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "域名")
    private String domainName;

    @ApiModelProperty(value = "背景图片")
    private String backgroundImage;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "账号限额")
    private Long accountLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "授权码")
    private String licenseKey;

    @ApiModelProperty(value = "租户状态 '0'禁用，'1' 启用,")
    private Integer tenantStatus;

    @ApiModelProperty(value = "备注")
    private String comments;

}
