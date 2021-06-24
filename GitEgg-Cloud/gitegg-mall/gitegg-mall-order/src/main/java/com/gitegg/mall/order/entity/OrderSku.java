package com.gitegg.mall.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitEgg
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_mall_order_sku")
@ApiModel(value="OrderSku对象", description="")
public class OrderSku extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "购买商品id")
    private Long goodsSkuId;

    @ApiModelProperty(value = "购买商品数量")
    private Integer goodsSkuNumber;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsSkuPrice;

    @ApiModelProperty(value = "商品总价")
    private BigDecimal totalOriginalPrice;

    @ApiModelProperty(value = "优惠后商品总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "会员优惠金额")
    private BigDecimal memberDiscountPrice;

    @ApiModelProperty(value = "商家改价优惠")
    private BigDecimal storeDiscountPrice;

    @ApiModelProperty(value = "购买商品信息")
    private String goodsSkuInfo;

    @ApiModelProperty(value = "是否退款")
    private Boolean refundStatus;

    @ApiModelProperty(value = "售后状态 0--未售后 1--售后中 2--售后结束")
    private Boolean afterSalesStatus;


}
