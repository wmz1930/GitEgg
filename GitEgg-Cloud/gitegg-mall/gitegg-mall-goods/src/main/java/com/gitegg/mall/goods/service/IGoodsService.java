package com.gitegg.mall.goods.service;

import com.gitegg.mall.goods.dto.ReduceStockDTO;
import com.gitegg.mall.goods.entity.GoodsSku;

import java.util.List;

/**
 * @author GitEgg
 * @date 2021-03-18 18:00:32
 **/
public interface IGoodsService {


    /**
     * 查询商品详情接口
     * @param idList
     * @return
     */
    List<GoodsSku> queryGoodsByIds(List<Long> idList);

    /**
     * 扣减库存接口
     * @param reduceStockReqList
     */
    void reduceStock(List<ReduceStockDTO> reduceStockReqList);

}
