package com.gitegg.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.RoleResource;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.system.entity.RoleDataPermission;
import com.gitegg.service.system.mapper.RoleDataPermissionMapper;
import com.gitegg.service.system.service.IRoleDataPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 角色和数据权限关联表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-14
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleDataPermissionServiceImpl extends ServiceImpl<RoleDataPermissionMapper, RoleDataPermission> implements IRoleDataPermissionService {

    private final RoleDataPermissionMapper roleDataPermissionMapper;

    /**
    * 分页查询角色和数据权限关联表列表
    * @param page
    * @param queryRoleDataPermissionDTO
    * @return
    */
    @Override
    public Page<RoleDataPermissionDTO> queryRoleDataPermissionList(Page<RoleDataPermissionDTO> page, QueryRoleDataPermissionDTO queryRoleDataPermissionDTO) {
        Page<RoleDataPermissionDTO> roleDataPermissionInfoList = roleDataPermissionMapper.queryRoleDataPermissionList(page, queryRoleDataPermissionDTO);
        return roleDataPermissionInfoList;
    }

    /**
    * 查询角色和数据权限关联表详情
    * @param queryRoleDataPermissionDTO
    * @return
    */
    @Override
    public RoleDataPermissionDTO queryRoleDataPermission(QueryRoleDataPermissionDTO queryRoleDataPermissionDTO) {
        RoleDataPermissionDTO roleDataPermissionDTO = roleDataPermissionMapper.queryRoleDataPermission(queryRoleDataPermissionDTO);
        return roleDataPermissionDTO;
    }

    /**
    * 创建角色和数据权限关联表
    * @param roleDataPermission
    * @return
    */
    @Override
    public boolean createRoleDataPermission(CreateRoleDataPermissionDTO roleDataPermission) {
        RoleDataPermission roleDataPermissionEntity = BeanCopierUtils.copyByClass(roleDataPermission, RoleDataPermission.class);
        boolean result = this.save(roleDataPermissionEntity);
        return result;
    }

    /**
    * 更新角色和数据权限关联表
    * @param roleDataPermission
    * @return
    */
    @Override
    public boolean updateRoleDataPermission(UpdateRoleDataPermissionDTO roleDataPermission) {
        RoleDataPermission roleDataPermissionEntity = BeanCopierUtils.copyByClass(roleDataPermission, RoleDataPermission.class);
        QueryWrapper<RoleDataPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("id", roleDataPermissionEntity.getId());
        boolean result = this.update(roleDataPermissionEntity, wrapper);
        return result;
    }

    /**
    * 删除角色和数据权限关联表
    * @param roleDataPermissionId
    * @return
    */
    @Override
    public boolean deleteRoleDataPermission(Long roleDataPermissionId) {
        boolean result = this.removeById(roleDataPermissionId);
        return result;
    }

    /**
    * 批量删除角色和数据权限关联表
    * @param roleDataPermissionIds
    * @return
    */
    @Override
    public boolean batchDeleteRoleDataPermission(List<Long> roleDataPermissionIds) {
        boolean result = this.removeByIds(roleDataPermissionIds);
        return result;
    }


    @Override
    public boolean updateDataPermissionRoleList(UpdateRoleDataPermissionDTO updateRoleDataPermissionDTO) {
        List<RoleDataPermission> addList = updateRoleDataPermissionDTO.getAddRoles();
        if (!CollectionUtils.isEmpty(addList)) {
            this.saveBatch(addList);
        }
        List<RoleDataPermission> delList = updateRoleDataPermissionDTO.getDelRoles();
        if (!CollectionUtils.isEmpty(delList)) {
            List<Long> roleIdList = new ArrayList<>();
            for (RoleDataPermission roleDataPermission : delList) {
                roleIdList.add(roleDataPermission.getRoleId());
            }
            Long dataPermissionId = updateRoleDataPermissionDTO.getDataPermissionId();
            QueryWrapper<RoleDataPermission> ewResource = new QueryWrapper<>();
            ewResource.eq("data_permission_id", dataPermissionId).in("role_id", roleIdList);
            this.remove(ewResource);
        }
        //重新初始化数据权限和角色的缓存
//        this.initRoleDataPermission();
        return true;
    }
}
