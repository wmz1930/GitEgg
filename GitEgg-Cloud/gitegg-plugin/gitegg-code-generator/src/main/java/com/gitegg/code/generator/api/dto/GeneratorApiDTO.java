package com.gitegg.code.generator.api.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
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
@ApiModel(value="GeneratorApiDTO对象", description="接口配置表")
public class GeneratorApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口地址")
    private String apiPath;

    @ApiModelProperty(value = "接口方法")
    private String apiMethod;

    @ApiModelProperty(value = "接口参数")
    private String apiParams;

    @ApiModelProperty(value = "状态")
    private Integer apiStatus;

    @ApiModelProperty(value = "接口对象")
    private String apiObject;

    @ApiModelProperty(value = "label字段")
    private String labelField;

    @ApiModelProperty(value = "value字段")
    private String valueField;

    @ApiModelProperty(value = "备注")
    private String comments;
}