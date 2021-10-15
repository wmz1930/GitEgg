package com.gitegg.code.generator.validate.entity;

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
* 字段校验规则配置表
* </p>
*
* @author GitEgg
* @since 2021-10-15
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="Validate对象", description="字段校验规则配置表数据导入模板")
public class ValidateImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "校验名称")
    @ExcelProperty(value = "校验名称" ,index = 0)
    @ColumnWidth(20)
    private String validateName;

    @ApiModelProperty(value = "校验规则")
    @ExcelProperty(value = "校验规则" ,index = 1)
    @ColumnWidth(20)
    private String validateRegular;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 2)
    @ColumnWidth(20)
    private Integer status;
}
