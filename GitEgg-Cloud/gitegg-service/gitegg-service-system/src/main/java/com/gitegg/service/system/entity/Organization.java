package com.gitegg.service.system.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gitegg.platform.mybatis.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 组织表
 * </p>
 *
 * @author gitegg
 * @since 2019-10-24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("t_sys_organization")
@ApiModel(value = "Organization对象", description = "组织表")
public class Organization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "组织类型：1总公司，2分公司，3事业部")
    @TableField("organization_type")
    private String organizationType;

    @ApiModelProperty(value = "组织名称")
    @TableField("organization_name")
    private String organizationName;

    @ApiModelProperty(value = "组织编码")
    @TableField("organization_key")
    private String organizationKey;

    @ApiModelProperty(value = "组织图标")
    @TableField("organization_icon")
    private String organizationIcon;

    @ApiModelProperty(value = "组织级别（排序）")
    @TableField("organization_level")
    private Integer organizationLevel;

    @ApiModelProperty(value = "1有效，0禁用")
    private Integer organizationStatus;

    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "区")
    @TableField("area")
    private String area;

    @ApiModelProperty(value = "街道详细地址")
    @TableField("street")
    private String street;

    @ApiModelProperty(value = "描述")
    @TableField("comments")
    private String comments;

    /**
     * 是否是叶子节点
     */
    @TableField(exist = false)
    private Integer isLeaf;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Organization> children;
}
