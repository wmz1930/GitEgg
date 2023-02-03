package com.gitegg.code.generator.validate.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;

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
@ApiModel(value="Validate对象", description="字段校验规则配置表")
public class CreateValidateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "校验名称")
    @NotBlank(message="校验名称不能为空")
    private String validateName;

    @ApiModelProperty(value = "校验规则")
    @NotBlank(message="校验规则不能为空")
    private String validateRegular;

    @ApiModelProperty(value = "状态")
    @NotBlank(message="状态不能为空")
    private Integer status;
    
    @ApiModelProperty(value = "备注")
    private String comments;
}
