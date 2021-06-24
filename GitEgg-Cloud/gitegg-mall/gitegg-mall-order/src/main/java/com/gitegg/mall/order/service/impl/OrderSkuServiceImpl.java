package com.gitegg.mall.order.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.mall.order.entity.OrderSku;
import com.gitegg.mall.order.mapper.OrderSkuMapper;
import com.gitegg.mall.order.service.IOrderSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.mall.order.dto.OrderSkuDTO;
import com.gitegg.mall.order.dto.CreateOrderSkuDTO;
import com.gitegg.mall.order.dto.UpdateOrderSkuDTO;
import com.gitegg.mall.order.dto.QueryOrderSkuDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku> implements IOrderSkuService {

    private final OrderSkuMapper orderSkuMapper;

    /**
    * 分页查询列表
    * @param page
    * @param queryOrderSkuDTO
    * @return
    */
    @Override
    public Page<OrderSkuDTO> queryOrderSkuList(Page<OrderSkuDTO> page, QueryOrderSkuDTO queryOrderSkuDTO) {
        Page<OrderSkuDTO> orderSkuInfoList = orderSkuMapper.queryOrderSkuList(page, queryOrderSkuDTO);
        return orderSkuInfoList;
    }

    /**
    * 查询详情
    * @param queryOrderSkuDTO
    * @return
    */
    @Override
    public OrderSkuDTO queryOrderSku(QueryOrderSkuDTO queryOrderSkuDTO) {
        OrderSkuDTO orderSkuDTO = orderSkuMapper.queryOrderSku(queryOrderSkuDTO);
        return orderSkuDTO;
    }

    /**
    * 创建
    * @param orderSku
    * @return
    */
    @Override
    public boolean createOrderSku(CreateOrderSkuDTO orderSku) {
        OrderSku orderSkuEntity = BeanCopierUtils.copyByClass(orderSku, OrderSku.class);
        boolean result = this.save(orderSkuEntity);
        return result;
    }

    /**
    * 更新
    * @param orderSku
    * @return
    */
    @Override
    public boolean updateOrderSku(UpdateOrderSkuDTO orderSku) {
        OrderSku orderSkuEntity = BeanCopierUtils.copyByClass(orderSku, OrderSku.class);
        QueryWrapper<OrderSku> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderSkuEntity.getId());
        boolean result = this.update(orderSkuEntity, wrapper);
        return result;
    }

    /**
    * 删除
    * @param orderSkuId
    * @return
    */
    @Override
    public boolean deleteOrderSku(Long orderSkuId) {
        boolean result = this.removeById(orderSkuId);
        return result;
    }

    /**
    * 批量删除
    * @param orderSkuIds
    * @return
    */
    @Override
    public boolean batchDeleteOrderSku(List<Long> orderSkuIds) {
        boolean result = this.removeByIds(orderSkuIds);
        return result;
    }
}
