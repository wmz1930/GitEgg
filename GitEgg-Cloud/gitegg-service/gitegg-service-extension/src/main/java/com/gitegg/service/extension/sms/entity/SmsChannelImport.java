package com.gitegg.service.extension.sms.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.gitegg.platform.boot.excel.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
* <p>
* 短信渠道表
* </p>
*
* @author GitEgg
* @since 2022-05-24
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="SmsChannel对象", description="短信渠道表数据导入模板")
public class SmsChannelImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "渠道编码")
    @ExcelProperty(value = "渠道编码" ,index = 0)
    @ColumnWidth(20)
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    @ExcelProperty(value = "渠道名称" ,index = 1)
    @ColumnWidth(20)
    private String channelName;

    @ApiModelProperty(value = "SecretId")
    @ExcelProperty(value = "SecretId" ,index = 2)
    @ColumnWidth(20)
    private String secretId;

    @ApiModelProperty(value = "SecretKey")
    @ExcelProperty(value = "SecretKey" ,index = 3)
    @ColumnWidth(20)
    private String secretKey;

    @ApiModelProperty(value = "RegionId")
    @ExcelProperty(value = "RegionId" ,index = 4)
    @ColumnWidth(20)
    private String regionId;

    @ApiModelProperty(value = "渠道状态")
    @ExcelProperty(value = "渠道状态" ,index = 5)
    @ColumnWidth(20)
    private Integer channelStatus;

    @ApiModelProperty(value = "短信数量")
    @ExcelProperty(value = "短信数量" ,index = 6)
    @ColumnWidth(20)
    private Long smsQuantity;

    @ApiModelProperty(value = "有效期")
    @ExcelProperty(value = "有效期" ,index = 7, converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime smsValidity;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 8)
    @ColumnWidth(20)
    private String comments;
}
