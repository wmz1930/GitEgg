package com.gitegg.service.system.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
 * 这里使用Lombok setter注入方式，使用构造器注入IUserRoleService和IRoleService有循环依赖问题
 * @author gitegg
 * @date 2019年5月18日 下午3:22:21
 */
@Slf4j
@Service
@Setter(onMethod_ = {@Autowired})
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private RoleMapper roleMapper;

    private IUserRoleService userRoleService;

    @Lazy
    private IRoleResourceService roleResourceService;

    @Override
    public Page<Role> selectRoleList(Page<Role> page, Role role) {
        Page<Role> roleInfoList = roleMapper.selectRoleList(page, role);
        return roleInfoList;
    }

    @Override
    public boolean createRole(CreateRoleDTO role) {
        LambdaQueryWrapper<Role> ew = new LambdaQueryWrapper<>();
        ew.eq(Role::getRoleName, role.getRoleName()).or().eq(Role::getRoleKey, role.getRoleKey()).or()
                .eq(Role::getRoleName, role.getRoleKey()).or().eq(Role::getRoleKey, role.getRoleName());
        List<Role> roleList = list(ew);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException("角色名称或角色标识已经存在");
        }
        Role roleEntity = BeanCopierUtils.copyByClass(role, Role.class);
        boolean result = save(roleEntity);
        return result;
    }

    @Override
    public boolean updateRole(UpdateRoleDTO role) {
        LambdaQueryWrapper<Role> ew = new LambdaQueryWrapper<>();
        ew.ne(Role::getId, role.getId()).and(e -> e.eq(Role::getRoleName, role.getRoleName()).or().eq(Role::getRoleKey, role.getRoleKey()).or()
                .eq(Role::getRoleName, role.getRoleKey()).or().eq(Role::getRoleKey, role.getRoleName()));
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
    public boolean deleteRole(Long roleId) {
        Role role = this.getById(roleId);
        boolean result = removeById(roleId);
        if (result) {
            // 删除用户和角色的关联关系
            LambdaQueryWrapper<UserRole> wr = new LambdaQueryWrapper<>();
            wr.eq(UserRole::getRoleId, roleId);
            userRoleService.removeById(wr);
            // 删除角色和权限的关联关系
            LambdaQueryWrapper<RoleResource> rr = new LambdaQueryWrapper<>();
            rr.eq(RoleResource::getRoleId, roleId);
            roleResourceService.remove(rr);
            //重新初始化角色和权限的对应关系
            roleResourceService.removeBatchRoles(Arrays.asList(role));
        }
        return result;
    }

    @Override
    public boolean batchDeleteRole(List<Long> roleIds) {
        List<Role> roles = this.listByIds(roleIds);
        boolean result = removeByIds(roleIds);
        if (result) {
            // 删除用户和角色的关联关系
            LambdaQueryWrapper<UserRole> wrs = new LambdaQueryWrapper<>();
            wrs.in(UserRole::getRoleId, roleIds);
            userRoleService.remove(wrs);
            // 删除角色和权限的关联关系
            LambdaQueryWrapper<RoleResource> rrs = new LambdaQueryWrapper<>();
            rrs.in(RoleResource::getRoleId, roleIds);
            roleResourceService.remove(rrs);
            //重新初始化角色和权限的对应关系
            roleResourceService.initResourceRoles();
            roleResourceService.removeBatchRoles(roles);
        }
        return result;
    }
}
