
package com.gitegg.service.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gitegg
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo extends User {

    @ApiModelProperty(value = "机构id")
    private Long organizationId;

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色标识")
    private String roleKey;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色id集合")
    private String roleIds;

    @ApiModelProperty(value = "角色标识集合")
    private String roleKeys;

    @ApiModelProperty(value = "角色名称集合")
    private String roleNames;

    @ApiModelProperty(value = "角色数据权限类型列表字符串")
    private String dataPermissionTypes;

    @ApiModelProperty(value = "角色数据权限类型列表")
    private List<String> dataPermissionTypeList;

    @ApiModelProperty(value = "数据权限机构id集合")
    private String organizationIds;

    @ApiModelProperty(value = "数据权限机构名称集合")
    private String organizationNames;

    @ApiModelProperty(value = "角色id列表")
    private List<String> roleIdList;

    @ApiModelProperty(value = "角色key列表")
    private List<String> roleKeyList;

    @ApiModelProperty(value = "机构id列表")
    private List<String> organizationIdList;

    @ApiModelProperty(value = "机构名称列表")
    private List<String> organizationNameList;

    @ApiModelProperty(value = "资源列表字符串")
    private List<String> resourceKeyList;

    @ApiModelProperty(value = "资源请求列表")
    private List<String> resourceUrlList;

    @ApiModelProperty(value = "前端展示的用户菜单树")
    private List<Resource> menuTree;

}
