package com.gitegg.code.generator.field.entity;

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
 * @since 2021-09-03 12:01:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_field_validate")
@ApiModel(value="FieldValidate对象", description="字段校验规则配置表")
public class FieldValidate extends BaseEntity {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字段主键")
    @TableField("field_id")
    private Long fieldId;

    @ApiModelProperty(value = "字段类型")
    @TableField("validate_type")
    private String validateType;

    @ApiModelProperty(value = "正则表达式校验规则")
    @TableField("validate_regular")
    private String validateRegular;


}
