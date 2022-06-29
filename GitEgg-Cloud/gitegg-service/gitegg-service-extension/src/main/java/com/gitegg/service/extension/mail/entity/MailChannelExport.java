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
 * 邮件渠道表
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="MailChannel对象", description="邮件渠道表数据导出")
public class MailChannelExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "渠道编码")
    @ExcelProperty(value = "渠道编码" ,index = 0)
    @ColumnWidth(20)
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    @ExcelProperty(value = "渠道名称" ,index = 1)
    @ColumnWidth(20)
    private String channelName;

    @ApiModelProperty(value = "SMTP地址")
    @ExcelProperty(value = "SMTP地址" ,index = 2)
    @ColumnWidth(20)
    private String host;

    @ApiModelProperty(value = "SMTP端口")
    @ExcelProperty(value = "SMTP端口" ,index = 3)
    @ColumnWidth(20)
    private Integer port;

    @ApiModelProperty(value = "账户名")
    @ExcelProperty(value = "账户名" ,index = 4)
    @ColumnWidth(20)
    private String username;

    @ApiModelProperty(value = "密码")
    @ExcelProperty(value = "密码" ,index = 5)
    @ColumnWidth(20)
    private String password;

    @ApiModelProperty(value = "协议")
    @ExcelProperty(value = "协议" ,index = 6)
    @ColumnWidth(20)
    private String protocol;

    @ApiModelProperty(value = "默认编码")
    @ExcelProperty(value = "默认编码" ,index = 7)
    @ColumnWidth(20)
    private String defaultEncoding;

    @ApiModelProperty(value = "会话JNDI")
    @ExcelProperty(value = "会话JNDI" ,index = 8)
    @ColumnWidth(20)
    private String jndiName;

    @ApiModelProperty(value = "JavaMail配置")
    @ExcelProperty(value = "JavaMail配置" ,index = 9)
    @ColumnWidth(20)
    private String properties;

    @ApiModelProperty(value = "渠道状态")
    @ExcelProperty(value = "渠道状态" ,index = 10)
    @ColumnWidth(20)
    private Integer channelStatus;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 11)
    @ColumnWidth(20)
    private String comments;
}
