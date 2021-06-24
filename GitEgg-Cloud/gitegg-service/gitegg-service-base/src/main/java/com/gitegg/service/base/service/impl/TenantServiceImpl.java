package com.gitegg.service.base.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.base.entity.Tenant;
import com.gitegg.service.base.mapper.TenantMapper;
import com.gitegg.service.base.service.ITenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.base.dto.TenantDTO;
import com.gitegg.service.base.dto.CreateTenantDTO;
import com.gitegg.service.base.dto.UpdateTenantDTO;
import com.gitegg.service.base.dto.QueryTenantDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 租户信息表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    private final TenantMapper tenantMapper;

    /**
    * 分页查询租户信息表列表
    * @param page
    * @param queryTenantDTO
    * @return
    */
    @Override
    public Page<TenantDTO> queryTenantList(Page<TenantDTO> page, QueryTenantDTO queryTenantDTO) {
        Page<TenantDTO> tenantInfoList = tenantMapper.queryTenantList(page, queryTenantDTO);
        return tenantInfoList;
    }

    /**
    * 查询租户信息表详情
    * @param queryTenantDTO
    * @return
    */
    @Override
    public TenantDTO queryTenant(QueryTenantDTO queryTenantDTO) {
        TenantDTO tenantDTO = tenantMapper.queryTenant(queryTenantDTO);
        return tenantDTO;
    }

    /**
    * 创建租户信息表
    * @param tenant
    * @return
    */
    @Override
    public boolean createTenant(CreateTenantDTO tenant) {
        Tenant tenantEntity = BeanCopierUtils.copyByClass(tenant, Tenant.class);
        boolean result = this.save(tenantEntity);
        return result;
    }

    /**
    * 更新租户信息表
    * @param tenant
    * @return
    */
    @Override
    public boolean updateTenant(UpdateTenantDTO tenant) {
        Tenant tenantEntity = BeanCopierUtils.copyByClass(tenant, Tenant.class);
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        wrapper.eq("id", tenantEntity.getId());
        boolean result = this.update(tenantEntity, wrapper);
        return result;
    }

    /**
    * 删除租户信息表
    * @param tenantId
    * @return
    */
    @Override
    public boolean deleteTenant(Long tenantId) {
        boolean result = this.removeById(tenantId);
        return result;
    }

    /**
    * 批量删除租户信息表
    * @param tenantIds
    * @return
    */
    @Override
    public boolean batchDeleteTenant(List<Long> tenantIds) {
        boolean result = this.removeByIds(tenantIds);
        return result;
    }
}
