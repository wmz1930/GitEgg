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
 * 用户注册
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "RegisterUser对象", description = "用户注册信息")
public class RegisterUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "姓名不能为空")
    @Length(max = 8, min = 2, message = "姓名长度范围应该在2-8位之间。")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String", notes = "姓名长度范围应该在2-8位之间。")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][1-9][0-9]{9}$", message = "请输入正确手机号。")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String")
    private String mobile;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$", message = "请输入6-18位，不含特殊字符密码。")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String", notes = "输入6-18位，不含特殊字符密码。")
    private String password;

    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "图片验证码", required = true, dataType = "String")
    private String verKey;

    @NotBlank(message = "短信验证码不能为空")
    @Length(max = 6, min = 6, message = "请输入6位数短信验证码。")
    @ApiModelProperty(value = "短信验证码", required = true, dataType = "String", notes = "输入6位数短信验证码。")
    private String smsCode;

    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "用户角色")
    private Integer roleId;
}
