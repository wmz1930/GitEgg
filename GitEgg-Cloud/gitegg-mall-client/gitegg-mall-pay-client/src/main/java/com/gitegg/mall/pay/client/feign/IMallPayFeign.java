package com.gitegg.mall.pay.client.feign;

import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

//添加contextId用于区分相同name的client，否则会报错
@FeignClient(name = "gitegg-mall-pay", contextId = "MallPayClient")
public interface IMallPayFeign {


    @ApiOperation(value = "支付")
    @PostMapping(value = "/feign/mall/pay/pay")
    Result<Object> pay(@RequestParam("userId") Long userId, @RequestParam("payMoney") BigDecimal payMoney);

}
