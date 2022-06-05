package com.gitegg.service.system.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 新增用户
 * </p>
 *
 * @author GitEgg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "UserAddDTO对象", description = "新增用户")
public class UserAddDTO implements Serializable {

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
    
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String")
    private String mobile;
    
    @ApiModelProperty(value = "用户姓名", required = true, dataType = "String", notes = "密码至少包含数字，字母和符号的两种")
    private String password;
    
    @ApiModelProperty(value = "短信模板编码", required = true, dataType = "String", notes = "输入短信模板编码。")
    private String smsCode;
    
    @ApiModelProperty(value = "短信验证码", required = true, dataType = "String", notes = "输入6位数短信验证码。")
    private String code;
    
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "用户状态")
    private Integer status;
    
    @ApiModelProperty(value = "头像")
    private String avatar;
    
    @ApiModelProperty(value = "1 : 男，0 : 女， -1: 未知")
    private String gender;
    
    @ApiModelProperty(value = "街道详细地址")
    private String street;
    
    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "用户角色")
    private Long roleId;
    
    @ApiModelProperty(value = "第三方用户id")
    private Long socialId;
}
