package com.gitegg.service.system.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 权限创建
 * </p>
 *
 * @author gitegg
 * @since 2019-05-19
 */
@Data
@ApiModel(value = "CreateResource对象", description = "创建资源时的对象")
public class CreateResourceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "资源标识")
    private String resourceKey;

    @ApiModelProperty(value = "资源类型：1、模块 2、菜单 3、按钮 4、链接")
    private String resourceType;

    @ApiModelProperty(value = "资源图标")
    private String resourceIcon;

    @ApiModelProperty(value = "资源路径")
    private String resourcePath;

    @ApiModelProperty(value = "资源链接")
    private String resourceUrl;

    @ApiModelProperty(value = "资源级别")
    private Integer resourceLevel;

    @ApiModelProperty(value = "是否显示")
    private Boolean resourceShow;

    @ApiModelProperty(value = "是否缓存")
    private Boolean resourceCache;

    @ApiModelProperty(value = "页面name")
    private String resourcePageName;

    @ApiModelProperty(value = "1有效，0禁用")
    private Integer resourceStatus;

    @ApiModelProperty(value = "备注")
    private String comments;
}
