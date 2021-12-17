package com.gitegg.code.generator.field.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 字段属性配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="FieldDTO对象", description="字段属性配置表")
public class FieldDTO extends BaseEntity {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "代码生成主键")
    private Long generationId;

    @ApiModelProperty(value = "关联表主键")
    private Long joinId;

    @ApiModelProperty(value = "表名")
    private String joinTableName;

    @ApiModelProperty(value = "表别名")
    private String joinTableAlias;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段描述")
    private String comment;

    @ApiModelProperty(value = "实体类型")
    private String entityType;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    @ApiModelProperty(value = "表单新增")
    private Boolean formAdd;

    @ApiModelProperty(value = "表单编辑")
    private Boolean formEdit;

    @ApiModelProperty(value = "查询条件")
    private Boolean queryTerm;

    @ApiModelProperty(value = "列表展示")
    private Boolean listShow;

    @ApiModelProperty(value = "是否支持导入 1支持 0不支持")
    private Boolean importFlag;

    @ApiModelProperty(value = "是否支持导出 1支持 0不支持")
    private Boolean exportFlag;

    @ApiModelProperty(value = "是否必填")
    private Boolean required;

    @ApiModelProperty(value = "是否唯一")
    private Boolean fieldUnique;

    @ApiModelProperty(value = "查询类型")
    private String queryType;

    @ApiModelProperty(value = "组件类型")
    private String controlType;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "最小值")
    private String min;

    @ApiModelProperty(value = "最大值")
    private String max;

    @ApiModelProperty(value = "最小长度")
    private Integer minLength;

    @ApiModelProperty(value = "字段最大长度")
    private Integer maxLength;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "校验规则主键")
    private Long validateId;

    @ApiModelProperty(value = "基础校验规则")
    private String validateValue;

    @ApiModelProperty(value = "自定义正则表达式")
    private String validateRegular;

    @ApiModelProperty(value = "显示排序")
    private Integer fieldSort;

    @ApiModelProperty(value = "主表实体名称")
    private String mainEntityName;

}
