package com.gitegg.service.extension.mail.entity;

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
* 邮件模板
* </p>
*
* @author GitEgg
* @since 2022-06-24
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="MailTemplate对象", description="邮件模板数据导入模板")
public class MailTemplateImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板编码")
    @ExcelProperty(value = "模板编码" ,index = 0)
    @ColumnWidth(20)
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    @ExcelProperty(value = "模板名称" ,index = 1)
    @ColumnWidth(20)
    private String templateName;

    @ApiModelProperty(value = "模板签名")
    @ExcelProperty(value = "模板签名" ,index = 2)
    @ColumnWidth(20)
    private String signName;

    @ApiModelProperty(value = "模板状态")
    @ExcelProperty(value = "模板状态" ,index = 3)
    @ColumnWidth(20)
    private Integer templateStatus;

    @ApiModelProperty(value = "模板类型")
    @ExcelProperty(value = "模板类型" ,index = 4)
    @ColumnWidth(20)
    private Integer templateType;

    @ApiModelProperty(value = "模板内容")
    @ExcelProperty(value = "模板内容" ,index = 5)
    @ColumnWidth(20)
    private String templateContent;

    @ApiModelProperty(value = "缓存key")
    @ExcelProperty(value = "缓存key" ,index = 6)
    @ColumnWidth(20)
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期")
    @ExcelProperty(value = "缓存有效期" ,index = 7)
    @ColumnWidth(20)
    private Long cacheTimeOut;

    @ApiModelProperty(value = "缓存有效期单位")
    @ExcelProperty(value = "缓存有效期单位" ,index = 8)
    @ColumnWidth(20)
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    @ExcelProperty(value = "发送次数限制" ,index = 9)
    @ColumnWidth(20)
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    @ExcelProperty(value = "限制时间间隔" ,index = 10)
    @ColumnWidth(20)
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "限制时间间隔单位")
    @ExcelProperty(value = "限制时间间隔单位" ,index = 11)
    @ColumnWidth(20)
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 12)
    @ColumnWidth(20)
    private String comments;
}
