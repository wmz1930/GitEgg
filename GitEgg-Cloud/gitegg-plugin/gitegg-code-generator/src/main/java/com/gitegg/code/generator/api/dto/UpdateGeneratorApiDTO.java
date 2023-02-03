package com.gitegg.code.generator.api.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 接口配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="GeneratorApi对象", description="接口配置表")
public class UpdateGeneratorApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    @NotBlank(message="接口名称不能为空")
    @Length(min=1,max=40)
    private String apiName;

    @ApiModelProperty(value = "接口地址")
    @NotBlank(message="接口地址不能为空")
    @Length(min=1,max=255)
    private String apiPath;

    @ApiModelProperty(value = "接口方法")
    @NotBlank(message="接口方法不能为空")
    @Length(min=1,max=100)
    private String apiMethod;

    @ApiModelProperty(value = "接口参数")
    @Length(min=1,max=100)
    private String apiParams;

    @ApiModelProperty(value = "状态")
    @NotBlank(message="状态不能为空")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer apiStatus;

    @ApiModelProperty(value = "接口对象")
    @Length(min=1,max=32)
    private String apiObject;

    @ApiModelProperty(value = "label字段")
    @NotBlank(message="label字段不能为空")
    @Length(min=1,max=32)
    private String labelField;

    @ApiModelProperty(value = "value字段")
    @NotBlank(message="value字段不能为空")
    @Length(min=1,max=32)
    private String valueField;

    @ApiModelProperty(value = "备注")
    @Length(min=1,max=255)
    private String comments;
}
