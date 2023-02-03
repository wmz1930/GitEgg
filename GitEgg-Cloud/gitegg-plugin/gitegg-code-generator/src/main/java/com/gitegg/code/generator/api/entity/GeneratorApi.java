package com.gitegg.code.generator.api.entity;

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
* 接口配置表
* </p>
*
* @author GitEgg
* @since 2022-12-12
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_api")
@ApiModel(value = "GeneratorApi对象", description = "接口配置表")
public class GeneratorApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "接口名称")
    @TableField("api_name")
    private String apiName;

    @ApiModelProperty(value = "接口地址")
    @TableField("api_path")
    private String apiPath;

    @ApiModelProperty(value = "请求方法")
    @TableField("api_method")
    private String apiMethod;

    @ApiModelProperty(value = "接口参数")
    @TableField("api_params")
    private String apiParams;

    @ApiModelProperty(value = "状态")
    @TableField("api_status")
    private Integer apiStatus;

    @ApiModelProperty(value = "接口对象")
    @TableField("api_object")
    private String apiObject;

    @ApiModelProperty(value = "label字段")
    @TableField("label_field")
    private String labelField;

    @ApiModelProperty(value = "value字段")
    @TableField("value_field")
    private String valueField;

    @ApiModelProperty(value = "备注")
    @TableField("comments")
    private String comments;

}