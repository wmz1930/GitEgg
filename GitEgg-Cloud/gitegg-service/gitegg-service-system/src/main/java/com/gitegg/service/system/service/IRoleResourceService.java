package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.UpdateRoleResourceDTO;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.entity.RoleResource;

import java.util.List;

/**
 * @ClassName: IRoleService
 * @Description: 角色相关操作接口
 * @author gitegg
 * @date
 */
public interface IRoleResourceService extends IService<RoleResource> {

    /**
     * 根据角色查询菜单
     * 
     * @param roleId 角色主键
     *
     * @return
     */
    List<Resource> queryResourceByRoleId(Long roleId);

    /**
     * 更新角色的权限
     * @param updateRoleResource
     * @return
     */
    boolean updateList(UpdateRoleResourceDTO updateRoleResource);

    /**
     * 初始化系统角色权限
     */
    void initResourceRoles();

    /**
     * 更新资源权限的缓存
     * @param oldResource
     * @param newResource
     */
    void updateResourceRoles(Resource oldResource, Resource newResource);

    /**
     * 删除资源权限的缓存
     * @param resource
     */
    void removeResourceRoles(Resource resource);

    /**
     * 批量删除资源权限的缓存
     * @param resources
     */
    void removeBatchResourceRoles(List<Resource> resources);

    /**
     * 批量删除资源权限的角色缓存
     * @param roles
     */
    void removeBatchRoles(List<Role> roles);

    /**
     * 新增或删除角色权限
     * @param roleResourceList
     * @param addFlag
     */
    void genRoleResources(List<RoleResource> roleResourceList, boolean addFlag);
}
