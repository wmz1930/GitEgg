
package com.gitegg.service.system.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息导出
 * </p>
 *
 * @author GitEgg
 * @since 2022-06-24
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="UserExportBO对象", description="用户信息数据导出")
public class UserExportBO implements Serializable {

    @ApiModelProperty(value = "机构名称")
    @ExcelProperty(value = "机构名称" ,index = 0)
    @ColumnWidth(20)
    private String organizationName;
    
    @ApiModelProperty(value = "账号")
    @ExcelProperty(value = "账号" ,index = 1)
    @ColumnWidth(20)
    private String account;
    
    @ApiModelProperty(value = "昵称")
    @ExcelProperty(value = "昵称" ,index = 2)
    @ColumnWidth(20)
    private String nickname;
    
    @ApiModelProperty(value = "姓名")
    @ExcelProperty(value = "姓名" ,index = 3)
    @ColumnWidth(20)
    private String realName;
    
    @ApiModelProperty(value = "角色名称")
    @ExcelProperty(value = "角色名称" ,index = 4)
    @ColumnWidth(20)
    private String roleNames;
    
    @ApiModelProperty(value = "性别  1 : 男，0 : 女， 2: 未知")
    @ExcelProperty(value = "性别" ,index = 5)
    @ColumnWidth(20)
    private String gender;
    
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty(value = "邮箱" ,index = 6)
    @ColumnWidth(20)
    private String email;
    
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message="手机号码不能为空")
    @ExcelProperty(value = "手机号码" ,index = 7)
    @ColumnWidth(20)
    private String mobile;
    
    @ApiModelProperty(value = "数据权限")
    @ExcelProperty(value = "数据权限" ,index = 8)
    @ColumnWidth(20)
    private String organizationNames;
    
    @ApiModelProperty(value = "用户状态： '0'禁用, '1' 启用, '2' 密码过期或初次未修改")
    @ExcelProperty(value = "用户状态" ,index = 9)
    @ColumnWidth(20)
    private Integer status;
    
    @ApiModelProperty(value = "头像图片地址")
    @ExcelProperty(value = "用户头像" ,index = 10)
    @ColumnWidth(20)
    private String avatar;
    
    @ApiModelProperty(value = "省")
    @ExcelProperty(value = "省" ,index = 11)
    @ColumnWidth(20)
    private String province;
    
    @ApiModelProperty(value = "市")
    @ExcelProperty(value = "市" ,index = 12)
    @ColumnWidth(20)
    private String city;
    
    @ApiModelProperty(value = "区")
    @ExcelProperty(value = "区" ,index = 13)
    @ColumnWidth(20)
    private String area;
    
    @ApiModelProperty(value = "街道详细地址")
    @ExcelProperty(value = "街道详细地址" ,index = 14)
    @ColumnWidth(20)
    private String street;
    
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 15)
    @ColumnWidth(20)
    private String comments;
    
    @ApiModelProperty(value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "注册时间" ,index = 16)
    @ColumnWidth(20)
    private LocalDateTime createTime;

}
