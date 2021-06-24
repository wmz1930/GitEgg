package com.gitegg.mall.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@ApiModel(value="OrderDTO对象", description="")
public class OrderDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "主键")
    private Long userId;

    @ApiModelProperty(value = "店铺id")
    private Integer storeId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单总金额(含运费)")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "实际支付总费用(含运费）")
    private BigDecimal totalPayPrice;

    @ApiModelProperty(value = "运费")
    private BigDecimal expressOriginalPrice;

    @ApiModelProperty(value = "修改后运费")
    private BigDecimal expressPrice;

    @ApiModelProperty(value = "订单商品总金额")
    private BigDecimal totalGoodsOriginalPrice;

    @ApiModelProperty(value = "优惠后订单商品总金额")
    private BigDecimal totalGoodsPrice;

    @ApiModelProperty(value = "商家改价优惠")
    private BigDecimal storeDiscountPrice;

    @ApiModelProperty(value = "会员优惠价格")
    private BigDecimal memberDiscountPrice;

    @ApiModelProperty(value = "优惠券id")
    private Integer couponId;

    @ApiModelProperty(value = "优惠券优惠金额")
    private BigDecimal couponDiscountPrice;

    @ApiModelProperty(value = "使用的积分数量")
    private Integer integral;

    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal integralDeductionPrice;

    @ApiModelProperty(value = "收件人姓名")
    private String name;

    @ApiModelProperty(value = "收件人手机号")
    private String mobile;

    @ApiModelProperty(value = "收件人地址")
    private String address;

    @ApiModelProperty(value = "用户订单备注")
    private String comments;

    @ApiModelProperty(value = "自定义表单（JSON）")
    private String orderForm;

    @ApiModelProperty(value = "留言")
    private String leavingMessage;

    @ApiModelProperty(value = "商家订单备注")
    private String storeComments;

    @ApiModelProperty(value = "是否支付：0.未支付 1.已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "支付方式：1.在线支付 2.货到付款 3.余额支付")
    private Integer payType;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "是否发货：0.未发货 1.已发货")
    private Integer deliverStatus;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "物流公司")
    private String express;

    @ApiModelProperty(value = "物流订单号")
    private String expressNo;

    @ApiModelProperty(value = "收货状态：0.未收货 1.已收货")
    private Integer confirmReceipt;

    @ApiModelProperty(value = "确认收货时间")
    private LocalDateTime confirmReceiptTime;

    @ApiModelProperty(value = "订单取消状态：0.未取消 1.已取消 2.申请取消")
    private Integer cancelStatus;

    @ApiModelProperty(value = "订单取消时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "是否加入回收站 0.否 1.是")
    private Integer recycleStatus;

    @ApiModelProperty(value = "是否到店自提：0.否 1.是")
    private Integer offline;

    @ApiModelProperty(value = "核销码")
    private String offlineCode;

    @ApiModelProperty(value = "核销员ID")
    private Integer verifierId;

    @ApiModelProperty(value = "自提门店ID")
    private Integer verifierStoreId;

    @ApiModelProperty(value = "支持的支付方式")
    private String supportPayTypes;

    @ApiModelProperty(value = "是否评价 0.否 1.是")
    private Integer evaluationStatus;

    private LocalDateTime evaluationTime;

    @ApiModelProperty(value = "是否过售后时间 0.否 1.是")
    private Integer afterSalesOut;

    @ApiModelProperty(value = "是否申请售后 0.否 1.是")
    private Integer afterSalesStatus;

    @ApiModelProperty(value = "订单状态 1.已完成 0.进行中")
    private Integer status;

    @ApiModelProperty(value = "自动取消时间")
    private LocalDateTime autoCancelTime;

    @ApiModelProperty(value = "自动确认收货时间")
    private LocalDateTime autoConfirmVerifierTime;

    @ApiModelProperty(value = "自动售后时间")
    private LocalDateTime autoAfterSalesTime;


}
