package com.gitegg.service.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@Data
@TableName("t_sys_dict")
@ApiModel(value = "Dict对象", description = "数据字典表")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字典类型")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty(value = "字典值")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty(value = "排序")
    @TableField("dict_order")
    private Integer dictOrder;

    @ApiModelProperty(value = "1有效，0禁用")
    @TableField("dict_status")
    private Integer dictStatus;

    @TableField("comments")
    private String comments;
}
