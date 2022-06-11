package com.gitegg.service.system.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
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
    
    @ApiModelProperty(value = "账号")
    @Length(max = 32, min = 2, message = "账号长度范围应该在2-32位之间。")
    private String account;
    
    @ApiModelProperty(value = "昵称")
    @Length(max = 32, min = 2, message = "昵称长度范围应该在2-32位之间。")
    private String nickname;
    
    @Length(max = 8, min = 2, message = "姓名长度范围应该在2-8位之间。")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String", notes = "姓名长度范围应该在2-8位之间。")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][1-9][0-9]{9}$", message = "请输入正确手机号。")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String")
    private String mobile;

    @NotBlank(message = "密码不能为空")
//    @Pattern(regexp = "^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z.!@#$%^&*].*)(?=.*[0-9.!@#$%^&*].*).{6,64}$", message = "密码至少包含数字，字母和符号的两种")
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String", notes = "密码至少包含数字，字母和符号的两种")
    private String password;

    @NotBlank(message = "短信模板编码")
    @ApiModelProperty(value = "短信模板编码", required = true, dataType = "String", notes = "输入短信模板编码。")
    private String smsCode;
    
    @NotBlank(message = "短信验证码不能为空")
    @Length(max = 6, min = 6, message = "请输入6位数短信验证码。")
    @ApiModelProperty(value = "短信验证码", required = true, dataType = "String", notes = "输入6位数短信验证码。")
    private String code;
    
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;
    
    @ApiModelProperty(value = "街道详细地址")
    private String street;
    
    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "用户角色")
    private Long roleId;
    
    @ApiModelProperty(value = "头像")
    private String avatar;
    
    @ApiModelProperty(value = "1 : 男，0 : 女， -1: 未知")
    private String gender;
    
    @ApiModelProperty(value = "第三方用户id")
    private Long socialId;
}
