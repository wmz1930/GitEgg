package com.gitegg.service.system.service;

import java.util.List;

import com.gitegg.service.system.entity.RoleDataPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.system.dto.RoleDataPermissionDTO;
import com.gitegg.service.system.dto.CreateRoleDataPermissionDTO;
import com.gitegg.service.system.dto.UpdateRoleDataPermissionDTO;
import com.gitegg.service.system.dto.QueryRoleDataPermissionDTO;

/**
 * <p>
 * 角色和数据权限关联表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-14
 */
public interface IRoleDataPermissionService extends IService<RoleDataPermission> {

    /**
    * 分页查询角色和数据权限关联表列表
    * @param page
    * @param queryRoleDataPermissionDTO
    * @return
    */
    Page<RoleDataPermissionDTO> queryRoleDataPermissionList(Page<RoleDataPermissionDTO> page, QueryRoleDataPermissionDTO queryRoleDataPermissionDTO);

    /**
    * 查询角色和数据权限关联表详情
    * @param queryRoleDataPermissionDTO
    * @return
    */
    RoleDataPermissionDTO queryRoleDataPermission(QueryRoleDataPermissionDTO queryRoleDataPermissionDTO);

    /**
    * 创建角色和数据权限关联表
    * @param roleDataPermission
    * @return
    */
    boolean createRoleDataPermission(CreateRoleDataPermissionDTO roleDataPermission);

    /**
    * 更新角色和数据权限关联表
    * @param roleDataPermission
    * @return
    */
    boolean updateRoleDataPermission(UpdateRoleDataPermissionDTO roleDataPermission);

    /**
    * 删除角色和数据权限关联表
    * @param roleDataPermissionId
    * @return
    */
    boolean deleteRoleDataPermission(Long roleDataPermissionId);

    /**
    * 批量删除角色和数据权限关联表
    * @param roleDataPermissionIds
    * @return
    */
    boolean batchDeleteRoleDataPermission(List<Long> roleDataPermissionIds);

    /**
     * 批量更新数据权限和角色的关系
     * @param updateRoleDataPermissionDTO
     * @return
     */
    boolean updateDataPermissionRoleList(UpdateRoleDataPermissionDTO updateRoleDataPermissionDTO);
}
