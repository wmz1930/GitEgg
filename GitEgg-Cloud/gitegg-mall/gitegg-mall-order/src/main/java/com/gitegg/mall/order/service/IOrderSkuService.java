package com.gitegg.mall.order.service;

import java.util.List;

import com.gitegg.mall.order.entity.OrderSku;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.mall.order.dto.OrderSkuDTO;
import com.gitegg.mall.order.dto.CreateOrderSkuDTO;
import com.gitegg.mall.order.dto.UpdateOrderSkuDTO;
import com.gitegg.mall.order.dto.QueryOrderSkuDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
public interface IOrderSkuService extends IService<OrderSku> {

    /**
    * 分页查询列表
    * @param page
    * @param queryOrderSkuDTO
    * @return
    */
    Page<OrderSkuDTO> queryOrderSkuList(Page<OrderSkuDTO> page, QueryOrderSkuDTO queryOrderSkuDTO);

    /**
    * 查询详情
    * @param queryOrderSkuDTO
    * @return
    */
    OrderSkuDTO queryOrderSku(QueryOrderSkuDTO queryOrderSkuDTO);

    /**
    * 创建
    * @param orderSku
    * @return
    */
    boolean createOrderSku(CreateOrderSkuDTO orderSku);

    /**
    * 更新
    * @param orderSku
    * @return
    */
    boolean updateOrderSku(UpdateOrderSkuDTO orderSku);

    /**
    * 删除
    * @param orderSkuId
    * @return
    */
    boolean deleteOrderSku(Long orderSkuId);

    /**
    * 批量删除
    * @param orderSkuIds
    * @return
    */
    boolean batchDeleteOrderSku(List<Long> orderSkuIds);
}
