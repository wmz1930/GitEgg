package com.gitegg.service.extension.wx.miniapp.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

import com.gitegg.service.extension.wx.miniapp.entity.Miniapp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.wx.miniapp.dto.MiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.QueryMiniappDTO;

import java.util.List;

/**
 * <p>
 * 微信小程序配置 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
public interface MiniappMapper extends BaseMapper<Miniapp> {

    /**
    * 分页查询微信小程序配置列表
    * @param page
    * @param miniappDTO
    * @return
    */
    Page<MiniappDTO> queryMiniappList(Page<MiniappDTO> page, @Param("miniapp") QueryMiniappDTO miniappDTO);

    /**
    * 查询微信小程序配置列表
    * @param miniappDTO
    * @return
    */
    List<MiniappDTO> queryMiniappList(@Param("miniapp") QueryMiniappDTO miniappDTO);

    /**
    * 查询微信小程序配置信息
    * @param miniappDTO
    * @return
    */
    MiniappDTO queryMiniapp(@Param("miniapp") QueryMiniappDTO miniappDTO);

    /**
     * 排除多租户插件查询微信配置列表
     * @param miniappDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<MiniappDTO> initMiniappList(@Param("miniapp") QueryMiniappDTO miniappDTO);

    /**
     * 排除多租户插件查询微信小程序配置信息
     * @param miniappDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    MiniappDTO getMiniapp(@Param("miniapp") QueryMiniappDTO miniappDTO);
}
