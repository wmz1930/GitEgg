package com.gitegg.service.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.base.dto.QueryTenantDTO;
import com.gitegg.service.base.dto.TenantDTO;
import com.gitegg.service.base.entity.Tenant;

/**
 * <p>
 * 租户信息表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2020-12-18
 */
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 查询租户信息表列表
     * 
     * @param page
     * @param tenantDTO
     * @return
     */
    Page<TenantDTO> queryTenantList(Page<TenantDTO> page, @Param("tenant") QueryTenantDTO tenantDTO);

    /**
     * 查询租户信息表信息
     * 
     * @param tenantDTO
     * @return
     */
    @InterceptorIgnore
    TenantDTO queryTenant(@Param("tenant") QueryTenantDTO tenantDTO);
}
