package com.gitegg.mall.goods.client.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GitEgg
 * @date 2021-03-18 18:06:13
 **/
@Data
public class GoodsSkuDTO  {

    private Long id;

    private String title;

    private BigDecimal price;

    private Integer stock;
}
