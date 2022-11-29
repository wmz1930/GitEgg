package com.gitegg.service.system.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import com.gitegg.service.system.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author gitegg
 * @since 2019-10-24
 */
@Data
@ApiModel(value = "Resource对象", description = "权限表")
public class QueryResourceDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "所有上级组织id的集合")
    private String ancestors;

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
    
    @ApiModelProperty(value = "当类型为菜单且不展示时，左侧菜单的选中项")
    private String currentActivePath;

    @ApiModelProperty(value = "是否缓存")
    private Boolean resourceCache;

    @ApiModelProperty(value = "页面name")
    private String resourcePageName;
    
    @ApiModelProperty(value = "是否在tab中显示")
    private Boolean tabShow;

    @ApiModelProperty(value = "状态")
    private Integer resourceStatus;
}
