package com.gitegg.service.base.service;

import java.util.List;

import com.gitegg.service.base.entity.Tenant;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.base.dto.TenantDTO;
import com.gitegg.service.base.dto.CreateTenantDTO;
import com.gitegg.service.base.dto.UpdateTenantDTO;
import com.gitegg.service.base.dto.QueryTenantDTO;

/**
 * <p>
 * 租户信息表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
public interface ITenantService extends IService<Tenant> {

    /**
    * 分页查询租户信息表列表
    * @param page
    * @param queryTenantDTO
    * @return
    */
    Page<TenantDTO> queryTenantList(Page<TenantDTO> page, QueryTenantDTO queryTenantDTO);

    /**
    * 查询租户信息表详情
    * @param queryTenantDTO
    * @return
    */
    TenantDTO queryTenant(QueryTenantDTO queryTenantDTO);

    /**
    * 创建租户信息表
    * @param tenant
    * @return
    */
    boolean createTenant(CreateTenantDTO tenant);

    /**
    * 更新租户信息表
    * @param tenant
    * @return
    */
    boolean updateTenant(UpdateTenantDTO tenant);

    /**
    * 删除租户信息表
    * @param tenantId
    * @return
    */
    boolean deleteTenant(Long tenantId);

    /**
    * 批量删除租户信息表
    * @param tenantIds
    * @return
    */
    boolean batchDeleteTenant(List<Long> tenantIds);
}
