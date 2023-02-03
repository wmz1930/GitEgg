package com.gitegg.code.generator.validate.dto;

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
@ApiModel(value="Validate对象", description="字段校验规则配置表")
public class UpdateValidateDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "校验名称")
    private String validateName;

    @ApiModelProperty(value = "校验规则")
    private String validateRegular;

    @ApiModelProperty(value = "状态")
    private Integer status;
    
    @ApiModelProperty(value = "备注")
    private String comments;
}
