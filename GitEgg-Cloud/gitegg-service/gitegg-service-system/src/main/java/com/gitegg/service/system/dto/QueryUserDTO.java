
package com.gitegg.service.system.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户查询
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "QueryUser对象", description = "用户查询")
public class QueryUserDTO implements Serializable
{
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "1 : 男，0 : 女， 2: 未知")
    private String gender;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户状态 '0'禁用,'1' 启用, '2' 密码过期或初次未修改")
    private Integer status;

    @ApiModelProperty(value = "头像图片地址")
    private String avatar;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;
    
    /**
     * vue级联选择
     */
    @ApiModelProperty(value = "地址数组")
    private List<String> areas;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;
}
