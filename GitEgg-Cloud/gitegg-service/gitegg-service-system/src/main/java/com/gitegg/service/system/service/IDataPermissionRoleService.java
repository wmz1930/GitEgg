package com.gitegg.service.system.service;

import java.util.List;

import com.gitegg.service.system.entity.DataPermissionRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.system.dto.DataPermissionRoleDTO;
import com.gitegg.service.system.dto.CreateDataPermissionRoleDTO;
import com.gitegg.service.system.dto.UpdateDataPermissionRoleDTO;
import com.gitegg.service.system.dto.QueryDataPermissionRoleDTO;

/**
 * <p>
 * 数据权限配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
public interface IDataPermissionRoleService extends IService<DataPermissionRole> {

    /**
    * 分页查询数据权限配置表列表
    * @param page
    * @param queryDataPermissionRoleDTO
    * @return
    */
    Page<DataPermissionRoleDTO> queryDataPermissionRoleList(Page<DataPermissionRoleDTO> page, QueryDataPermissionRoleDTO queryDataPermissionRoleDTO);

    /**
    * 查询数据权限配置表详情
    * @param queryDataPermissionRoleDTO
    * @return
    */
    DataPermissionRoleDTO queryDataPermissionRole(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO);

    /**
    * 创建数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    boolean createDataPermissionRole(CreateDataPermissionRoleDTO dataPermissionRole);

    /**
    * 更新数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    boolean updateDataPermissionRole(UpdateDataPermissionRoleDTO dataPermissionRole);

    /**
    * 删除数据权限配置表
    * @param dataPermissionRoleId
    * @return
    */
    boolean deleteDataPermissionRole(Long dataPermissionRoleId);

    /**
    * 批量删除数据权限配置表
    * @param dataPermissionRoleIds
    * @return
    */
    boolean batchDeleteDataPermissionRole(List<Long> dataPermissionRoleIds);

    /**
     * 初始化系统数据权限权限
     */
    void initDataRolePermissions();
}
