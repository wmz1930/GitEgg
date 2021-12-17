package com.gitegg.code.generator.join.entity;

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
 * 多表查询时的联合表配置
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_table_join")
@ApiModel(value="TableJoin对象", description="多表查询时的联合表配置")
public class TableJoin extends BaseEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "代码生成主键")
    @TableField("generation_id")
    private Long generationId;

    @ApiModelProperty(value = "数据源和主表一致")
    @TableField("datasource_id")
    private Long datasourceId;

    @ApiModelProperty(value = "模块名称")
    @TableField("module_name")
    private String moduleName;

    @ApiModelProperty(value = "模块代码")
    @TableField("module_code")
    private String moduleCode;

    @ApiModelProperty(value = "controller请求路径")
    @TableField("controller_path")
    private String controllerPath;

    @ApiModelProperty(value = "表名")
    @TableField("join_table_name")
    private String joinTableName;

    @ApiModelProperty(value = "表别名")
    @TableField("join_table_alias")
    private String joinTableAlias;

    @ApiModelProperty(value = "表前缀")
    @TableField("join_table_prefix")
    private String joinTablePrefix;

    @ApiModelProperty(value = "left左连接 right右连接 inner等值连接 union联合查询")
    @TableField("join_table_type")
    private String joinTableType;

    @ApiModelProperty(value = "自定义查询字段")
    @TableField("join_table_select")
    private String joinTableSelect;

    @ApiModelProperty(value = "自定义on条件")
    @TableField("join_table_on")
    private String joinTableOn;

    @ApiModelProperty(value = "和主表关联的id")
    @TableField("association_id")
    private String associationId;

    @ApiModelProperty(value = "显示排序")
    @TableField("table_sort")
    private Integer tableSort;


}
