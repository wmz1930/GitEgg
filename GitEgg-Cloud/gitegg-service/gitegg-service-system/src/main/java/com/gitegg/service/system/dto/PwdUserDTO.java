
package com.gitegg.service.system.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户修改密码
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "PwdUser对象", description = "修改用户密码时用到的对象")
public class PwdUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户手机号")
    @NotBlank(message="手机号不能为空")
    @Pattern(regexp="^[1][1-9][0-9]{9}$", message="请输入正确手机号。")
    private String mobile;

    @ApiModelProperty(value = "用户密码")
    @NotBlank(message="密码不能为空")
    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$", message="请输入6-18位，不含特殊字符密码。")
    private String password;

    @ApiModelProperty(value = "用户验证码key")
    @NotBlank(message="不能为空")
    private String verKey;

    @ApiModelProperty(value = "短信验证码")
    @NotBlank(message="短信验证码不能为空")
    @Length(max=6, min=6, message="请输入6位数短信验证码。")
    private String smsCode;

    @ApiModelProperty(value = "用户状态")
    @Length(max = 1, min = 1, message = "请选择用户状态。")
    private Integer status;

    @ApiModelProperty(value = "角色id")
    private Long roleId;
}
