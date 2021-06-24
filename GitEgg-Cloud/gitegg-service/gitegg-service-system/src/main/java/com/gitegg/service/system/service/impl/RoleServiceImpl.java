package com.gitegg.service.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.system.dto.CreateRoleDTO;
import com.gitegg.service.system.dto.UpdateRoleDTO;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.entity.RoleResource;
import com.gitegg.service.system.entity.UserRole;
import com.gitegg.service.system.mapper.RoleMapper;
import com.gitegg.service.system.service.IRoleResourceService;
import com.gitegg.service.system.service.IRoleService;
import com.gitegg.service.system.service.IUserRoleService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色相关操作接口实现类
 * @author gitegg
 * @date 2019年5月18日 下午3:22:21
 */
@Slf4j
@Service
//这里使用Lombok setter注入方式，使用构造器注入IUserRoleService和IRoleService有循环依赖问题
@Setter(onMethod_ = {@Autowired})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private RoleMapper roleMapper;

    private IUserRoleService userRoleService;

    private IRoleResourceService roleResourceService;

    @Override
    public Page<Role> selectRoleList(Page<Role> page, Role role) {
        Page<Role> roleInfoList = roleMapper.selectRoleList(page, role);
        return roleInfoList;
    }

    @Override
    public boolean createRole(CreateRoleDTO role) {
        QueryWrapper<Role> ew = new QueryWrapper<>();
        ew.eq("role_name", role.getRoleName()).or().eq("role_key", role.getRoleKey()).or()
                .eq("role_name", role.getRoleKey()).or().eq("role_key", role.getRoleName());
        List<Role> roleList = list(ew);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException("角色名称或角色标识已经存在");
        }
        Role roleEntity = BeanCopierUtils.copyByClass(role, Role.class);
        boolean result = save(roleEntity);
        return result;
    }

    @Override
//    @CacheEvict(value = "roles", allEntries = true)
    public boolean updateRole(UpdateRoleDTO role) {
        QueryWrapper<Role> ew = new QueryWrapper<>();
        ew.ne("id", role.getId()).and(e -> e.eq("role_name", role.getRoleName()).or().eq("role_key", role.getRoleKey())
                .or().eq("role_name", role.getRoleKey()).or().eq("role_key", role.getRoleName()));
        List<Role> roleList = list(ew);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException("角色名称或角色标识已经存在");
        }

        Role roleEntity = BeanCopierUtils.copyByClass(role, Role.class);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("id", roleEntity.getId());
        boolean result = update(roleEntity, wrapper);
        return result;
    }

    @Override
//    @CacheEvict(value = "roles", allEntries = true)
    public boolean deleteRole(Long roleId) {
        boolean result = removeById(roleId);
        if (result) {
            // 删除用户和角色的关联关系
            QueryWrapper<UserRole> wpd = new QueryWrapper<>();
            wpd.eq("role_id", roleId);
            userRoleService.remove(wpd);
            // 删除角色和权限的关联关系
            QueryWrapper<RoleResource> wpdr = new QueryWrapper<>();
            wpdr.eq("role_id", roleId);
            roleResourceService.remove(wpdr);
            //重新初始化角色和权限的对应关系
            roleResourceService.initResourceRoles();
        }
        return result;
    }

    @Override
//    @CacheEvict(value = "roles", allEntries = true)
    public boolean batchDeleteRole(List<Long> roleIds) {
        boolean result = removeByIds(roleIds);
        if (result) {
            // 删除用户和角色的关联关系
            QueryWrapper<UserRole> wpd = new QueryWrapper<>();
            wpd.in("role_id", roleIds);
            userRoleService.remove(wpd);
            // 删除角色和权限的关联关系
            QueryWrapper<RoleResource> wpdr = new QueryWrapper<>();
            wpdr.in("role_id", roleIds);
            roleResourceService.remove(wpdr);
            //重新初始化角色和权限的对应关系
            roleResourceService.initResourceRoles();
        }
        return result;
    }
}
