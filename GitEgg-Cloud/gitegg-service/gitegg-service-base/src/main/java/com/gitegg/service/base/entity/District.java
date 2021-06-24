package com.gitegg.service.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-24
 */
@Data
@TableName("t_sys_district")
@ApiModel(value = "District对象", description = "")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("initial")
    private String initial;

    @TableField("initials")
    private String initials;

    @TableField("pinyin")
    private String pinyin;

    @TableField("suffix")
    private String suffix;

    @TableField("code")
    private String code;

    @TableField("order")
    private Integer order;

    @TableField(exist = false)
    private List<District> children = new ArrayList<>();
}
