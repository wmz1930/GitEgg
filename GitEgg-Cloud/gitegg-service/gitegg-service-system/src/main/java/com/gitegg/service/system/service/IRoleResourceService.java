package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.UpdateRoleResourceDTO;
import com.gitegg.service.system.entity.Resource;
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
     * @param roleId
     *            角色主键
     * @return
     */
    List<Resource> queryResourceByRoleId(Long roleId);

    /**
     * 更新角色的权限
     * @param updateRoleResource
     * @return
     */
    boolean updateList(UpdateRoleResourceDTO updateRoleResource);
}
