package com.gitegg.mall.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.mall.goods.client.dto.GoodsSkuDTO;
import com.gitegg.mall.goods.client.dto.ReduceStockDTO;
import com.gitegg.mall.goods.client.feign.IMallGoodsFeign;
import com.gitegg.mall.order.dto.*;
import com.gitegg.mall.order.entity.Order;
import com.gitegg.mall.order.entity.OrderSku;
import com.gitegg.mall.order.mapper.OrderMapper;
import com.gitegg.mall.order.service.IOrderService;
import com.gitegg.mall.order.service.IOrderSkuService;
import com.gitegg.mall.pay.client.feign.IMallPayFeign;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final OrderMapper orderMapper;

    private final IOrderSkuService orderSkuService;

    private final IMallGoodsFeign mallGoodsFeign;

    private final IMallPayFeign mallPayFeign;

    @DS("mall_order")//每一层都需要使用多数据源注解切换所选择的数据库
    @Transactional
    @GlobalTransactional //重点 第一个开启事务的需要添加seata全局事务注解
    @Override
    public void order(List<OrderSkuDTO> orderSkuList, Long userId) {

        //获取商品的详细信息
        Result<Object> goodsSkuResult = mallGoodsFeign.queryByIds(orderSkuList.stream()
                .map(OrderSkuDTO::getGoodsSkuId)
                .collect(Collectors.toList()));
        List<Object> resultSkuList = (List<Object>) goodsSkuResult.getData();
        List<GoodsSkuDTO> goodsSkuList = new ArrayList<>();
        if(CollectionUtils.isEmpty(resultSkuList) || resultSkuList.size() != orderSkuList.size()) {
            throw new BusinessException("商品不存在");
        }
        else {
            resultSkuList.stream().forEach(goodsSku -> {
                GoodsSkuDTO goodsSkuDTO = BeanUtil.fillBeanWithMap((Map<?, ?>) goodsSku, new GoodsSkuDTO(), false);
                goodsSkuList.add(goodsSkuDTO);
            });
        }

        //扣商品库存
        List<ReduceStockDTO> reduceStockDtoList = orderSkuList.stream()
                .map(t -> new ReduceStockDTO(t.getGoodsSkuId(),t.getGoodsSkuNumber()))
                .collect(Collectors.toList());
        mallGoodsFeign.reduceStock(reduceStockDtoList);

//        //支付
        BigDecimal totalMoney = new BigDecimal(0.0d);
        for(OrderSkuDTO orderSkuDTO: orderSkuList) {
            for(GoodsSkuDTO goodsSkuDTO: goodsSkuList) {
                if(orderSkuDTO.getGoodsSkuId().equals(goodsSkuDTO.getId())) {
                    BigDecimal skuNumber = new BigDecimal(orderSkuDTO.getGoodsSkuNumber());
                    totalMoney = totalMoney.add(goodsSkuDTO.getPrice().multiply(skuNumber));
                    break;
                }
            }
        }

        mallPayFeign.pay(userId, totalMoney);

        //主订单表插入数据
        Order order = new Order();
        order.setTotalPrice(totalMoney);
        order.setTotalPayPrice(totalMoney);
        order.setExpressOriginalPrice(totalMoney);
        order.setStatus(1);
        order.setUserId(userId);
        this.save(order);

        //子订单表插入数据
        ArrayList<OrderSku> orderSkus = new ArrayList<>();
        orderSkuList.forEach(payOrderReq -> {
            OrderSku orderSku = new OrderSku();
            orderSku.setOrderId(order.getId());
            orderSku.setGoodsSkuNumber(payOrderReq.getGoodsSkuNumber());
            orderSku.setGoodsSkuId(payOrderReq.getGoodsSkuId());
            for(GoodsSkuDTO goodsSkuDTO : goodsSkuList) {
                if(payOrderReq.getGoodsSkuId().equals(goodsSkuDTO.getId())) {
                    orderSku.setGoodsSkuPrice(goodsSkuDTO.getPrice());
                    break;
                }
            }
            orderSkus.add(orderSku);
        });
        orderSkuService.saveBatch(orderSkus);
    }

    /**
    * 分页查询列表
    * @param page
    * @param queryOrderDTO
    * @return
    */
    @Override
    public Page<OrderDTO> queryOrderList(Page<OrderDTO> page, QueryOrderDTO queryOrderDTO) {
        Page<OrderDTO> orderInfoList = orderMapper.queryOrderList(page, queryOrderDTO);
        return orderInfoList;
    }

    /**
    * 查询详情
    * @param queryOrderDTO
    * @return
    */
    @Override
    public OrderDTO queryOrder(QueryOrderDTO queryOrderDTO) {
        OrderDTO orderDTO = orderMapper.queryOrder(queryOrderDTO);
        return orderDTO;
    }

    /**
    * 创建
    * @param order
    * @return
    */
    @Override
    public boolean createOrder(CreateOrderDTO order) {
        Order orderEntity = BeanCopierUtils.copyByClass(order, Order.class);
        boolean result = this.save(orderEntity);
        return result;
    }

    /**
    * 更新
    * @param order
    * @return
    */
    @Override
    public boolean updateOrder(UpdateOrderDTO order) {
        Order orderEntity = BeanCopierUtils.copyByClass(order, Order.class);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderEntity.getId());
        boolean result = this.update(orderEntity, wrapper);
        return result;
    }

    /**
    * 删除
    * @param orderId
    * @return
    */
    @Override
    public boolean deleteOrder(Long orderId) {
        boolean result = this.removeById(orderId);
        return result;
    }

    /**
    * 批量删除
    * @param orderIds
    * @return
    */
    @Override
    public boolean batchDeleteOrder(List<Long> orderIds) {
        boolean result = this.removeByIds(orderIds);
        return result;
    }
}
