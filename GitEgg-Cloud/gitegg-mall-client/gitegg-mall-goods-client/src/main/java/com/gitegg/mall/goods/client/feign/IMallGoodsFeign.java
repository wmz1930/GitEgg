package com.gitegg.mall.goods.client.feign;

import com.gitegg.mall.goods.client.dto.ReduceStockDTO;
import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//添加contextId用于区分相同name的client，否则会报错
@FeignClient(name = "gitegg-mall-goods", contextId = "MallGoodsClient")
public interface IMallGoodsFeign {

    /**
     * 根据商品id列表查询商品信息列表
     *
     * @param ids
     * @return
     */
    @GetMapping("/feign/mall/goods/query/by/ids")
    Result<Object> queryByIds(@RequestParam("ids") List<Long> ids);


    /**
     * 扣减库存接口
     * @param reduceStockReqList
     * @return
     */
    @PostMapping("/feign/mall/goods/stock/reduce")
    Result<Object> reduceStock(@RequestBody List<ReduceStockDTO> reduceStockReqList);

}
