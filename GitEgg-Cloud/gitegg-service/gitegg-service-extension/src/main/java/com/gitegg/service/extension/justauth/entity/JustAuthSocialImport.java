package com.gitegg.service.extension.justauth.entity;

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
* 租户第三方登录功能配置表
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="JustAuthSocial对象", description="租户第三方登录功能配置表数据导入模板")
public class JustAuthSocialImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第三方ID	")
    @ExcelProperty(value = "第三方ID	" ,index = 0)
    @ColumnWidth(20)
    private String uuid;

    @ApiModelProperty(value = "用户来源")
    @ExcelProperty(value = "用户来源" ,index = 1)
    @ColumnWidth(20)
    private String source;

    @ApiModelProperty(value = "授权令牌")
    @ExcelProperty(value = "授权令牌" ,index = 2)
    @ColumnWidth(20)
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    @ExcelProperty(value = "令牌有效期" ,index = 3)
    @ColumnWidth(20)
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @ExcelProperty(value = "刷新令牌" ,index = 4)
    @ColumnWidth(20)
    private String refreshToken;

    @ApiModelProperty(value = "OpenId")
    @ExcelProperty(value = "OpenId" ,index = 5)
    @ColumnWidth(20)
    private String openId;

    @ApiModelProperty(value = "用户ID")
    @ExcelProperty(value = "用户ID" ,index = 6)
    @ColumnWidth(20)
    private String uid;

    @ApiModelProperty(value = "授权信息")
    @ExcelProperty(value = "授权信息" ,index = 7)
    @ColumnWidth(20)
    private String accessCode;

    @ApiModelProperty(value = "UnionId")
    @ExcelProperty(value = "UnionId" ,index = 8)
    @ColumnWidth(20)
    private String unionId;

    @ApiModelProperty(value = "用户权限")
    @ExcelProperty(value = "用户权限" ,index = 9)
    @ColumnWidth(20)
    private String scope;

    @ApiModelProperty(value = "授权类型")
    @ExcelProperty(value = "授权类型" ,index = 10)
    @ColumnWidth(20)
    private String tokenType;

    @ApiModelProperty(value = "IdToken")
    @ExcelProperty(value = "IdToken" ,index = 11)
    @ColumnWidth(20)
    private String idToken;

    @ApiModelProperty(value = "mac_algorithm")
    @ExcelProperty(value = "mac_algorithm" ,index = 12)
    @ColumnWidth(20)
    private String macAlgorithm;

    @ApiModelProperty(value = "mac_key")
    @ExcelProperty(value = "mac_key" ,index = 13)
    @ColumnWidth(20)
    private String macKey;

    @ApiModelProperty(value = "授权code")
    @ExcelProperty(value = "授权code" ,index = 14)
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty(value = "oauth_token")
    @ExcelProperty(value = "oauth_token" ,index = 15)
    @ColumnWidth(20)
    private String oauthToken;

    @ApiModelProperty(value = "oauth_token_secret")
    @ExcelProperty(value = "oauth_token_secret" ,index = 16)
    @ColumnWidth(20)
    private String oauthTokenSecret;
}
