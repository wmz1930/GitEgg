package com.gitegg.code.generator.validate.entity;

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
* 字段校验规则配置表
* </p>
*
* @author GitEgg
* @since 2021-10-15
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_validate")
@ApiModel(value = "Validate对象", description = "字段校验规则配置表")
public class Validate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "校验名称")
    @TableField("validate_name")
    private String validateName;

    @ApiModelProperty(value = "正则表达式校验规则")
    @TableField("validate_regular")
    private String validateRegular;

    @ApiModelProperty(value = "'0'禁用，'1' 启用")
    @TableField("status")
    private Integer status;
    
    @ApiModelProperty(value = "备注")
    @TableField("comments")
    private String comments;

}
