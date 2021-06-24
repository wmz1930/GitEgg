package com.gitegg.service.system.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import com.gitegg.service.system.entity.RoleDataPermission;
import com.gitegg.service.system.entity.RoleResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 角色和数据权限关联表
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RoleDataPermission对象", description="角色和数据权限关联表")
public class UpdateRoleDataPermissionDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "资源id")
    private Long dataPermissionId;

    @ApiModelProperty(value = "添加的角色列表")
    private List<RoleDataPermission> addRoles;

    @ApiModelProperty(value = "删除的角色列表")
    private List<RoleDataPermission> delRoles;


}
