package com.gitegg.code.generator.datasource.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据源配置上传
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(15)
@ApiModel(value="CodeGenerationDatasourceImport对象", description="数据源配置导入")
public class CodeGenerationDatasourceImport {

    @ApiModelProperty(value = "数据源名称")
    @ExcelProperty(value = "数据源名称" ,index = 0)
    @ColumnWidth(20)
    private String datasourceName;

    @ApiModelProperty(value = "连接地址")
    @ExcelProperty(value = "连接地址" ,index = 1)
    @ColumnWidth(20)
    private String url;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty(value = "用户名" ,index = 2)
    @ColumnWidth(20)
    private String username;

    @ApiModelProperty(value = "密码")
    @ExcelProperty(value = "密码" ,index = 3)
    @ColumnWidth(20)
    private String password;

    @ApiModelProperty(value = "数据库驱动")
    @ExcelProperty(value = "数据库驱动" ,index = 4)
    @ColumnWidth(20)
    private String driver;

    @ApiModelProperty(value = "数据库类型")
    @ExcelProperty(value = "数据库类型" ,index = 5)
    @ColumnWidth(20)
    private String dbType;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 6)
    @ColumnWidth(20)
    private String comments;

}
