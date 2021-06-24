package com.gitegg.mall.goods.controller;

import com.gitegg.mall.goods.dto.ReduceStockDTO;
import com.gitegg.mall.goods.entity.GoodsSku;
import com.gitegg.mall.goods.service.IGoodsService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author GitEgg
 * @date 2021-03-18 17:54:32
 **/
@Api(tags = "gitegg-mall-goods")
@RestController
@RequestMapping(value = "/mall/goods")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GoodsController {

    private final IGoodsService goodsService;

    @ApiOperation(value = "查询商品详情")
    @GetMapping(value = "list/by/id")
    public Result<List<GoodsSku>> selectByIdList(@RequestParam List<Long> idList) {
        List<GoodsSku> goodsSkuList = goodsService.queryGoodsByIds(idList);
        return Result.data(goodsSkuList);
    }


    @ApiOperation(value = "扣减库存接口")
    @PostMapping(value = "stock/reduce")
    public Result<Object> reduceStock(@Valid @Size(min = 1) @RequestBody List<ReduceStockDTO> reduceStockReqList) {
        goodsService.reduceStock(reduceStockReqList);
        return Result.success();
    }
}
