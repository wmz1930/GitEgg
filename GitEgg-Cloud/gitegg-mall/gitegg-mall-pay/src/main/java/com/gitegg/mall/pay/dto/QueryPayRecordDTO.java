package com.gitegg.mall.pay.dto;

import java.math.BigDecimal;
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
@ApiModel(value="PayRecord对象", description="")
public class QueryPayRecordDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String orderNo;

    private BigDecimal amount;

    @ApiModelProperty(value = "支付状态：0=未支付，1=已支付, 2=已退款")
    private Integer payStatus;

    @ApiModelProperty(value = "支付方式：1=微信支付，2=货到付款，3=余额支付，4=支付宝支付")
    private Integer payType;

    private String title;

    @ApiModelProperty(value = "已退款金额")
    private BigDecimal refund;

    @ApiModelProperty(value = "备注")
    private String comments;


    @ApiModelProperty(value = "开始时间")
    private String startDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
