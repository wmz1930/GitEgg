package com.gitegg.mall.order.mapper;

import com.gitegg.mall.order.entity.OrderSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.mall.order.dto.OrderSkuDTO;
import com.gitegg.mall.order.dto.QueryOrderSkuDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface OrderSkuMapper extends BaseMapper<OrderSku> {

    /**
    * 查询列表
    * @param page
    * @param orderSkuDTO
    * @return
    */
    Page<OrderSkuDTO> queryOrderSkuList(Page<OrderSkuDTO> page, @Param("orderSku") QueryOrderSkuDTO orderSkuDTO);

    /**
    * 查询信息
    * @param orderSkuDTO
    * @return
    */
    OrderSkuDTO queryOrderSku(@Param("orderSku") QueryOrderSkuDTO orderSkuDTO);
}
