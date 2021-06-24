package com.gitegg.mall.goods.feign;

import com.gitegg.mall.goods.dto.ReduceStockDTO;
import com.gitegg.mall.goods.entity.GoodsSku;
import com.gitegg.mall.goods.service.IGoodsService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName: GoodsFeign
 * @Description: GoodsFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/mall/goods")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MallGoodsFeign|提供微服务调用接口")
@RefreshScope
public class MallGoodsFeign {

    private final IGoodsService goodsService;

    @GetMapping(value = "/query/by/ids")
    @ApiOperation(value = "通过商品id列表查询商品信息", notes = "通过商品id列表查询商品信息")
    public Result<?> queryGoodsByIds(@RequestParam("ids") List<Long> ids) {
        List<GoodsSku> goodsSkuList = goodsService.queryGoodsByIds(ids);
        return Result.data(goodsSkuList);
    }

    @PostMapping(value = "/stock/reduce")
    @ApiOperation(value = "扣减库存接口")
    public Result<?> reduceStock(@RequestBody List<ReduceStockDTO> reduceStockReqList) {
        goodsService.reduceStock(reduceStockReqList);
        return Result.success();
    }
}
