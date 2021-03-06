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
* 第三方用户绑定
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="JustAuthSocialUser对象", description="第三方用户绑定数据导入模板")
public class JustAuthSocialUserImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @ExcelProperty(value = "用户id" ,index = 0)
    @ColumnWidth(20)
    private Long userId;

    @ApiModelProperty(value = "第三方用户id")
    @ExcelProperty(value = "第三方用户id" ,index = 1)
    @ColumnWidth(20)
    private Long socialId;
}
