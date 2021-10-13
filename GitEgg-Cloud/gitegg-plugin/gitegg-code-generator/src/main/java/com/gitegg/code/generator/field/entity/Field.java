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
 * 字段属性配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_field")
@ApiModel(value="Field对象", description="字段属性配置表")
public class Field extends BaseEntity {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "代码生成主键")
    @TableField("generation_id")
    private Long generationId;

    @ApiModelProperty(value = "关联表主键")
    @TableField("join_id")
    private Long joinId;

    @ApiModelProperty(value = "表名")
    @TableField("join_table_name")
    private String joinTableName;

    @ApiModelProperty(value = "字段名称")
    @TableField("field_name")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    @TableField("field_type")
    private String fieldType;

    @ApiModelProperty(value = "字段描述")
    private String comment;

    @ApiModelProperty(value = "实体类型")
    @TableField("entity_type")
    private String entityType;

    @ApiModelProperty(value = "实体名称")
    @TableField("entity_name")
    private String entityName;

    @ApiModelProperty(value = "表单新增")
    @TableField("form_add")
    private Boolean formAdd;

    @ApiModelProperty(value = "表单编辑")
    @TableField("form_edit")
    private Boolean formEdit;

    @ApiModelProperty(value = "查询条件")
    @TableField("query_term")
    private Boolean queryTerm;

    @ApiModelProperty(value = "列表展示")
    @TableField("list_show")
    private Boolean listShow;

    @ApiModelProperty(value = "是否支持导入 1支持 0不支持")
    @TableField("import_flag")
    private Boolean importFlag;

    @ApiModelProperty(value = "是否支持导出 1支持 0不支持")
    @TableField("export_flag")
    private Boolean exportFlag;

    @ApiModelProperty(value = "是否必填")
    private Boolean required;

    @ApiModelProperty(value = "是否唯一")
    @TableField("field_unique")
    private Boolean fieldUnique;

    @ApiModelProperty(value = "查询类型")
    @TableField("query_type")
    private String queryType;

    @ApiModelProperty(value = "组件类型")
    @TableField("control_type")
    private String controlType;

    @ApiModelProperty(value = "字典编码")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty(value = "最小值")
    private Long min;

    @ApiModelProperty(value = "最大值")
    private Long max;

    @ApiModelProperty(value = "最小长度")
    @TableField("min_length")
    private Integer minLength;

    @ApiModelProperty(value = "字段最大长度")
    @TableField("max_length")
    private Integer maxLength;

    @ApiModelProperty(value = "正则表达式")
    @TableField("validate_regular")
    private String validateRegular;

    @ApiModelProperty(value = "显示排序")
    @TableField("field_sort")
    private Integer fieldSort;


}
