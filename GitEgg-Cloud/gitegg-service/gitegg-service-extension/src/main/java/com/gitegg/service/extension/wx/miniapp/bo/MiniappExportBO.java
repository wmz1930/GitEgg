package com.gitegg.service.extension.wx.miniapp.bo;

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
 * 微信小程序配置
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="Miniapp对象", description="微信小程序配置数据导出")
public class MiniappExportBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小程序名称")
    @ExcelProperty(value = "小程序名称" ,index = 0)
    @ColumnWidth(20)
    private String miniappName;

    @ApiModelProperty(value = "appid")
    @ExcelProperty(value = "appid" ,index = 1)
    @ColumnWidth(20)
    private String appid;

    @ApiModelProperty(value = "secret")
    @ExcelProperty(value = "secret" ,index = 2)
    @ColumnWidth(20)
    private String secret;

    @ApiModelProperty(value = "token")
    @ExcelProperty(value = "token" ,index = 3)
    @ColumnWidth(20)
    private String token;

    @ApiModelProperty(value = "aesKey")
    @ExcelProperty(value = "aesKey" ,index = 4)
    @ColumnWidth(20)
    private String aesKey;

    @ApiModelProperty(value = "消息格式")
    @ExcelProperty(value = "消息格式" ,index = 5)
    @ColumnWidth(20)
    private String msgDataFormat;

    @ApiModelProperty(value = "缓存类型")
    @ExcelProperty(value = "缓存类型" ,index = 6)
    @ColumnWidth(20)
    private String storageType;

    @ApiModelProperty(value = "缓存前缀")
    @ExcelProperty(value = "缓存前缀" ,index = 7)
    @ColumnWidth(20)
    private String keyPrefix;

    @ApiModelProperty(value = "Redis地址")
    @ExcelProperty(value = "Redis地址" ,index = 8)
    @ColumnWidth(20)
    private String redisHost;

    @ApiModelProperty(value = "Redis端口")
    @ExcelProperty(value = "Redis端口" ,index = 9)
    @ColumnWidth(20)
    private Integer redisPort;

    @ApiModelProperty(value = "http类型")
    @ExcelProperty(value = "http类型" ,index = 10)
    @ColumnWidth(20)
    private String httpClientType;

    @ApiModelProperty(value = "代理地址")
    @ExcelProperty(value = "代理地址" ,index = 11)
    @ColumnWidth(20)
    private String httpProxyHost;

    @ApiModelProperty(value = "代理端口")
    @ExcelProperty(value = "代理端口" ,index = 12)
    @ColumnWidth(20)
    private String httpProxyPort;

    @ApiModelProperty(value = "代理账号")
    @ExcelProperty(value = "代理账号" ,index = 13)
    @ColumnWidth(20)
    private String httpProxyUsername;

    @ApiModelProperty(value = "代理密码")
    @ExcelProperty(value = "代理密码" ,index = 14)
    @ColumnWidth(20)
    private String httpProxyPassword;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 15)
    @ColumnWidth(20)
    private String status;

    @ApiModelProperty(value = "MD5")
    @ExcelProperty(value = "MD5" ,index = 16)
    @ColumnWidth(20)
    private String md5;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述" ,index = 17)
    @ColumnWidth(20)
    private String comments;
}
