package com.gitegg.code.generator.api.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* <p>
* 接口配置表
* </p>
*
* @author GitEgg
* @since 2022-12-12
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="GeneratorApi对象", description="接口配置表数据导入模板")
public class GeneratorApiImportBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口名称")
    @ExcelProperty(value = "接口名称" ,index = 0)
    @ColumnWidth(20)
    private String apiName;

    @ApiModelProperty(value = "接口地址")
    @ExcelProperty(value = "接口地址" ,index = 1)
    @ColumnWidth(20)
    private String apiPath;

    @ApiModelProperty(value = "接口方法")
    @ExcelProperty(value = "接口方法" ,index = 2)
    @ColumnWidth(20)
    private String apiMethod;

    @ApiModelProperty(value = "接口参数")
    @ExcelProperty(value = "接口参数" ,index = 3)
    @ColumnWidth(20)
    private String apiParams;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 4)
    @ColumnWidth(20)
    private Integer apiStatus;

    @ApiModelProperty(value = "接口对象")
    @ExcelProperty(value = "接口对象" ,index = 5)
    @ColumnWidth(20)
    private String apiObject;

    @ApiModelProperty(value = "label字段")
    @ExcelProperty(value = "label字段" ,index = 6)
    @ColumnWidth(20)
    private String labelField;

    @ApiModelProperty(value = "value字段")
    @ExcelProperty(value = "value字段" ,index = 7)
    @ColumnWidth(20)
    private String valueField;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 8)
    @ColumnWidth(20)
    private String comments;
}
