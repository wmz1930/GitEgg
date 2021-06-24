package com.gitegg.service.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.system.entity.DataPermissionRole;
import com.gitegg.service.system.mapper.DataPermissionRoleMapper;
import com.gitegg.service.system.service.IDataPermissionRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.system.dto.DataPermissionRoleDTO;
import com.gitegg.service.system.dto.CreateDataPermissionRoleDTO;
import com.gitegg.service.system.dto.UpdateDataPermissionRoleDTO;
import com.gitegg.service.system.dto.QueryDataPermissionRoleDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据权限配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataPermissionRoleServiceImpl extends ServiceImpl<DataPermissionRoleMapper, DataPermissionRole> implements IDataPermissionRoleService {

    private final DataPermissionRoleMapper dataPermissionRoleMapper;

    /**
    * 分页查询数据权限配置表列表
    * @param page
    * @param queryDataPermissionRoleDTO
    * @return
    */
    @Override
    public Page<DataPermissionRoleDTO> queryDataPermissionRoleList(Page<DataPermissionRoleDTO> page, QueryDataPermissionRoleDTO queryDataPermissionRoleDTO) {
        Page<DataPermissionRoleDTO> dataPermissionRoleInfoList = dataPermissionRoleMapper.queryDataPermissionRoleList(page, queryDataPermissionRoleDTO);
        return dataPermissionRoleInfoList;
    }

    /**
    * 查询数据权限配置表详情
    * @param queryDataPermissionRoleDTO
    * @return
    */
    @Override
    public DataPermissionRoleDTO queryDataPermissionRole(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO) {
        DataPermissionRoleDTO dataPermissionRoleDTO = dataPermissionRoleMapper.queryDataPermissionRole(queryDataPermissionRoleDTO);
        return dataPermissionRoleDTO;
    }

    /**
    * 创建数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    @Override
    public boolean createDataPermissionRole(CreateDataPermissionRoleDTO dataPermissionRole) {
        DataPermissionRole dataPermissionRoleEntity = BeanCopierUtils.copyByClass(dataPermissionRole, DataPermissionRole.class);
        boolean result = this.save(dataPermissionRoleEntity);
        return result;
    }

    /**
    * 更新数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    @Override
    public boolean updateDataPermissionRole(UpdateDataPermissionRoleDTO dataPermissionRole) {
        DataPermissionRole dataPermissionRoleEntity = BeanCopierUtils.copyByClass(dataPermissionRole, DataPermissionRole.class);
        QueryWrapper<DataPermissionRole> wrapper = new QueryWrapper<>();
        wrapper.eq("id", dataPermissionRoleEntity.getId());
        boolean result = this.update(dataPermissionRoleEntity, wrapper);
        return result;
    }

    /**
    * 删除数据权限配置表
    * @param dataPermissionRoleId
    * @return
    */
    @Override
    public boolean deleteDataPermissionRole(Long dataPermissionRoleId) {
        boolean result = this.removeById(dataPermissionRoleId);
        return result;
    }

    /**
    * 批量删除数据权限配置表
    * @param dataPermissionRoleIds
    * @return
    */
    @Override
    public boolean batchDeleteDataPermissionRole(List<Long> dataPermissionRoleIds) {
        boolean result = this.removeByIds(dataPermissionRoleIds);
        return result;
    }
}
