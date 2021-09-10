package com.gitegg.code.generator.field.dto;

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
@ApiModel(value="FieldValidateDTO对象", description="字段校验规则配置表")
public class FieldValidateDTO extends BaseEntity {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "字段主键")
    private Long fieldId;

    @ApiModelProperty(value = "字段类型")
    private String validateType;

    @ApiModelProperty(value = "正则表达式校验规则")
    private String validateRegular;


}
