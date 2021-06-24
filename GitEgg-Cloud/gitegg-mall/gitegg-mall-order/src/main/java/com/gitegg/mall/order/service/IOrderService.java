package com.gitegg.mall.order.service;

import java.util.List;

import com.gitegg.mall.order.dto.*;
import com.gitegg.mall.order.entity.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface IOrderService extends IService<Order> {

    void order(List<OrderSkuDTO> orderSkuList, Long userId);

    /**
    * 分页查询列表
    * @param page
    * @param queryOrderDTO
    * @return
    */
    Page<OrderDTO> queryOrderList(Page<OrderDTO> page, QueryOrderDTO queryOrderDTO);

    /**
    * 查询详情
    * @param queryOrderDTO
    * @return
    */
    OrderDTO queryOrder(QueryOrderDTO queryOrderDTO);

    /**
    * 创建
    * @param order
    * @return
    */
    boolean createOrder(CreateOrderDTO order);

    /**
    * 更新
    * @param order
    * @return
    */
    boolean updateOrder(UpdateOrderDTO order);

    /**
    * 删除
    * @param orderId
    * @return
    */
    boolean deleteOrder(Long orderId);

    /**
    * 批量删除
    * @param orderIds
    * @return
    */
    boolean batchDeleteOrder(List<Long> orderIds);
}
