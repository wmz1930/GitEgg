package com.gitegg.mall.goods.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GitEgg
 * @date 2021-03-18 17:58:54
 **/
@ApiModel(value = "扣减库存入参")
@Data
public class ReduceStockDTO {

    @NotNull
    @ApiModelProperty(value = "skuid")
    private Long skuId;

    @NotNull
    @ApiModelProperty(value = "减少的库存数量")
    private Integer number;

    public ReduceStockDTO(Long goodsSkuId, Integer goodsSkuNumber) {
        this.skuId = goodsSkuId;
        this.number = goodsSkuNumber;
    }
}
