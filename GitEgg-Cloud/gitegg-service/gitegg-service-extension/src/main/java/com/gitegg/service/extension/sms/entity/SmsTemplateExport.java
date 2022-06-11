package com.gitegg.service.extension.sms.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 短信配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-24
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="SmsTemplate对象", description="短信配置表数据导出")
public class SmsTemplateExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "短信渠道")
    @ExcelProperty(value = "短信渠道" ,index = 0)
    @ColumnWidth(20)
    private Long channelId;

    @ApiModelProperty(value = "短信编码")
    @ExcelProperty(value = "短信编码" ,index = 1)
    @ColumnWidth(20)
    private String smsCode;

    @ApiModelProperty(value = "短信名称")
    @ExcelProperty(value = "短信名称" ,index = 2)
    @ColumnWidth(20)
    private String smsName;

    @ApiModelProperty(value = "模板ID")
    @ExcelProperty(value = "模板ID" ,index = 3)
    @ColumnWidth(20)
    private String templateId;

    @ApiModelProperty(value = "短信签名")
    @ExcelProperty(value = "短信签名" ,index = 4)
    @ColumnWidth(20)
    private String signName;

    @ApiModelProperty(value = "短信状态")
    @ExcelProperty(value = "短信状态" ,index = 5)
    @ColumnWidth(20)
    private Integer templateStatus;
    
    @ApiModelProperty(value = "短信类型")
    @ExcelProperty(value = "短信类型" ,index = 6)
    @ColumnWidth(20)
    private Integer templateType;

    @ApiModelProperty(value = "缓存key")
    @ExcelProperty(value = "缓存key" ,index = 7)
    @ColumnWidth(20)
    private String cacheCodeKey;

    @ApiModelProperty(value = "缓存有效期")
    @ExcelProperty(value = "缓存有效期" ,index = 8)
    @ColumnWidth(20)
    private Long cacheTimeOut;

    @ApiModelProperty(value = "有效期单位")
    @ExcelProperty(value = "有效期单位" ,index = 9)
    @ColumnWidth(20)
    private String cacheTimeOutUnit;

    @ApiModelProperty(value = "发送次数限制")
    @ExcelProperty(value = "发送次数限制" ,index = 10)
    @ColumnWidth(20)
    private Long sendTimesLimit;

    @ApiModelProperty(value = "限制时间间隔")
    @ExcelProperty(value = "限制时间间隔" ,index = 11)
    @ColumnWidth(20)
    private Long sendTimesLimitPeriod;

    @ApiModelProperty(value = "时间间隔单位")
    @ExcelProperty(value = "时间间隔单位" ,index = 12)
    @ColumnWidth(20)
    private String sendTimesLimitPeriodUnit;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 13)
    @ColumnWidth(20)
    private String comments;
}
