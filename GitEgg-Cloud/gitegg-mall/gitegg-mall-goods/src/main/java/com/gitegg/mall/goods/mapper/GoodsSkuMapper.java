package com.gitegg.mall.goods.mapper;

import com.gitegg.mall.goods.entity.GoodsSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsSkuMapper {

    List<GoodsSku> queryGoodsByIds(@Param("idList") List<Long> idList);

    Integer reduceStock(@Param("number") Integer number, @Param("skuId")Long skuId);
}
