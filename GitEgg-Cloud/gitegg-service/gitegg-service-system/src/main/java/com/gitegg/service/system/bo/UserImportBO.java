
package com.gitegg.service.system.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <p>
 * 用户数据批量导入模板
 * </p>
 *
 * @author GitEgg
 * @since 2019-05-26
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value = "UserImportBO对象", description = "用户批量导入模板")
public class UserImportBO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "组织机构")
    @ExcelProperty(value = "组织机构" ,index = 0)
    @ColumnWidth(20)
    private Long organizationId;

    @ApiModelProperty(value = "账号")
    @NotBlank(message="账号不能为空")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$", message="账号格式不正确")
    @ExcelProperty(value = "账号" ,index = 1)
    @ColumnWidth(20)
    private String account;

    @ApiModelProperty(value = "昵称")
    @Size(min=2,max=16,message="昵称长度不正确")
    @ExcelProperty(value = "昵称" ,index = 2)
    @ColumnWidth(20)
    private String nickname;

    @ApiModelProperty(value = "姓名")
    @Size(min=2,max=16,message="姓名长度不正确")
    @ExcelProperty(value = "姓名" ,index = 3)
    @ColumnWidth(20)
    private String realName;
    
    @ApiModelProperty(value = "角色")
    @ExcelProperty(value = "角色" ,index = 4)
    @ColumnWidth(20)
    private Long roleId;

    @ApiModelProperty(value = "性别  1 : 男，0 : 女， 2: 未知")
    @ExcelProperty(value = "性别" ,index = 5)
    @ColumnWidth(20)
    private String gender;

    @ApiModelProperty(value = "邮箱")
    @NotBlank
    @Email
    @ExcelProperty(value = "邮箱" ,index = 6)
    @ColumnWidth(20)
    private String email;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message="手机号码不能为空")
    @Size(min=11,max=11,message="手机号码长度不正确")
    @ExcelProperty(value = "手机号码" ,index = 7)
    @ColumnWidth(20)
    private String mobile;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message="密码格式不正确")
    @ExcelProperty(value = "密码" ,index = 8)
    @ColumnWidth(20)
    private String password;

    @ApiModelProperty(value = "用户状态： '0'禁用, '1' 启用, '2' 密码过期或初次未修改")
    @ExcelProperty(value = "用户状态" ,index = 9)
    @ColumnWidth(20)
    private Integer status;

    @ApiModelProperty(value = "头像图片地址")
    @Pattern(regexp = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$", message="头像图片地址格式不正确")
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
    @Size(min=0,max=255,message="备注信息长度不正确")
    @ExcelProperty(value = "备注" ,index = 15)
    @ColumnWidth(20)
    private String comments;
    
}
