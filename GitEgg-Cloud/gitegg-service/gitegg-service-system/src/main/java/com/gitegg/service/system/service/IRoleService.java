package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.CreateRoleDTO;
import com.gitegg.service.system.dto.UpdateRoleDTO;
import com.gitegg.service.system.entity.Role;

import java.util.List;

/**
 * @ClassName: IRoleService
 * @Description: 角色相关操作接口
 * @author gitegg
 * @date
 */
public interface IRoleService extends IService<Role> {

    /**
     * 分页查询角色列表
     * @param page
     * @param role
     * @return
     */
    Page<Role> selectRoleList(Page<Role> page, Role role);

    /**
     * 创建角色
     * @param role
     * @return
     */
    boolean createRole(CreateRoleDTO role);

    /**
     * 更新角色
     * @param role
     * @return
     */
    boolean updateRole(UpdateRoleDTO role);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    boolean deleteRole(Long roleId);

    /**
     * 批量删除角色
     * @param roleIds
     * @return
     */
    boolean batchDeleteRole(List<Long> roleIds);
}
