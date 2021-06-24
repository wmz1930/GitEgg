package com.gitegg.mall.goods.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitegg.mall.goods.dto.ReduceStockDTO;
import com.gitegg.mall.goods.entity.GoodsSku;
import com.gitegg.mall.goods.mapper.GoodsSkuMapper;
import com.gitegg.mall.goods.service.IGoodsService;
import com.gitegg.platform.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GitEgg
 * @date 2021-03-18 18:02:05
 **/
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GoodsServiceImpl implements IGoodsService {

    private final GoodsSkuMapper goodsSkuMapper;


    @Override
    public List<GoodsSku> queryGoodsByIds(List<Long> idList) {
        return goodsSkuMapper.queryGoodsByIds(idList);
    }

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务  重要！！！！一定要使用REQUIRES_NEW
     */
    @DS("mall_goods")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void reduceStock(List<ReduceStockDTO> reduceStockReqList) {
        reduceStockReqList.forEach(sku -> {
            Integer line = goodsSkuMapper.reduceStock(sku.getNumber(), sku.getSkuId());
            if(line == null || line == 0) {
                throw new BusinessException("商品不存在或库存不足");
            }
        });
    }
}
