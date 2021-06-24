package com.gitegg.mall.order.mapper;

import com.gitegg.mall.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.mall.order.dto.OrderDTO;
import com.gitegg.mall.order.dto.QueryOrderDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
    * 查询列表
    * @param page
    * @param orderDTO
    * @return
    */
    Page<OrderDTO> queryOrderList(Page<OrderDTO> page, @Param("order") QueryOrderDTO orderDTO);

    /**
    * 查询信息
    * @param orderDTO
    * @return
    */
    OrderDTO queryOrder(@Param("order") QueryOrderDTO orderDTO);
}
