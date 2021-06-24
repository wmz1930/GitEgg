package com.gitegg.service.system.mapper;

import com.gitegg.service.system.entity.RoleDataPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.system.dto.RoleDataPermissionDTO;
import com.gitegg.service.system.dto.QueryRoleDataPermissionDTO;

/**
 * <p>
 * 角色和数据权限关联表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-14
 */
public interface RoleDataPermissionMapper extends BaseMapper<RoleDataPermission> {

    /**
    * 查询角色和数据权限关联表列表
    * @param page
    * @param roleDataPermissionDTO
    * @return
    */
    Page<RoleDataPermissionDTO> queryRoleDataPermissionList(Page<RoleDataPermissionDTO> page, @Param("roleDataPermission") QueryRoleDataPermissionDTO roleDataPermissionDTO);

    /**
    * 查询角色和数据权限关联表信息
    * @param roleDataPermissionDTO
    * @return
    */
    RoleDataPermissionDTO queryRoleDataPermission(@Param("roleDataPermission") QueryRoleDataPermissionDTO roleDataPermissionDTO);
}
