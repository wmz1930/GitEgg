
package com.gitegg.service.system.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户更新
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "UpdateUser对象", description = "更新用户时的对象")
public class UpdateUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull(message="不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "账号")
    @NotBlank(message="账号不能为空")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$", message="账号格式不正确")
    private String account;

    @ApiModelProperty(value = "昵称")
    @Size(min=2,max=16,message="昵称长度不正确")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    @Size(min=2,max=16,message="姓名长度不正确")
    private String realName;

    @ApiModelProperty(value = "1 : 男，0 : 女， 2: 未知")
    private String gender;

    @ApiModelProperty(value = "邮箱")
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message="手机号码不能为空")
    @Size(min=11,max=11,message="手机号码长度不正确")
    private String mobile;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message="密码格式不正确")
    private String password;

    @ApiModelProperty(value = "用户状态 '0'禁用,'1' 启用, '2' 密码过期或初次未修改")
    private Integer status;

    @ApiModelProperty(value = "头像图片地址")
    @Pattern(regexp = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$", message="头像图片地址格式不正确")
    private String avatar;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "用户地区ID数组")
    private List<String> areas;

    @ApiModelProperty(value = "街道详细地址")
    private String street;

    @ApiModelProperty(value = "备注")
    @Size(min=0,max=255,message="备注信息长度不正确")
    private String comments;

    @ApiModelProperty(value = "角色id（单角色时）")
    private Long roleId;

    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;

    @ApiModelProperty(value = "用户角色id数组（多角色时）")
    private List<Long> roleIds;

    @ApiModelProperty(value = "用户新密码")
    private String newPwd;

    @ApiModelProperty(value = "用户旧密码")
    private String oldPwd;

}
