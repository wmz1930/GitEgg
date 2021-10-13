package com.gitegg.code.generator.config.entity;

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
 * 代码生成配置表
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_code_generator_config")
@ApiModel(value="Config对象", description="代码生成配置表")
public class Config extends BaseEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "数据源")
    @TableField("datasource_id")
    private Long datasourceId;

    @ApiModelProperty(value = "模块名称")
    @TableField("module_name")
    private String moduleName;

    @ApiModelProperty(value = "模块代码")
    @TableField("module_code")
    private String moduleCode;

    @ApiModelProperty(value = "服务名称")
    @TableField("service_name")
    private String serviceName;

    @ApiModelProperty(value = "表名")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty(value = "表别名")
    @TableField("table_alias")
    private String tableAlias;

    @ApiModelProperty(value = "表前缀")
    @TableField("table_prefix")
    private String tablePrefix;

    @ApiModelProperty(value = "父级包名")
    @TableField("parent_package")
    private String parentPackage;

    @ApiModelProperty(value = "controller请求路径")
    @TableField("controller_path")
    private String controllerPath;

    @ApiModelProperty(value = "表单类型 model弹出框  drawer抽屉  tab新页面")
    @TableField("form_type")
    private String formType;

    @ApiModelProperty(value = "表类型 single单表  multi多表")
    @TableField("table_type")
    private String tableType;

    @ApiModelProperty(value = "展示类型 table数据表格 tree_table 树表格 3 left_tree_table左树右表  tree数据树")
    @TableField("table_show_type")
    private String tableShowType;

    @ApiModelProperty(value = "表单字段排列 1一列一行  2 两列一行")
    @TableField("form_item_col")
    private String formItemCol;

    @ApiModelProperty(value = "左树类型 organization机构树 resource资源权限树 ")
    @TableField("left_tree_type")
    private String leftTreeType;

    @ApiModelProperty(value = "前端代码路径")
    @TableField("front_code_path")
    private String frontCodePath;

    @ApiModelProperty(value = "后端代码路径")
    @TableField("service_code_path")
    private String serviceCodePath;

    @ApiModelProperty(value = "是否支持导入 1支持 0不支持")
    @TableField("import_flag")
    private Boolean importFlag;

    @ApiModelProperty(value = "是否支持导出 1支持 0不支持")
    @TableField("export_flag")
    private Boolean exportFlag;


}
