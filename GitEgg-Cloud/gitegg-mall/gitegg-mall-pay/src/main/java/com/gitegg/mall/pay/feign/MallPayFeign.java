package com.gitegg.mall.pay.feign;

import com.gitegg.mall.pay.service.IPayService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @ClassName: MallPayFeign
 * @Description: MallPayFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/mall/pay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MallPayFeign|提供微服务调用接口")
@RefreshScope
public class MallPayFeign {

    private final IPayService payService;

    @PostMapping(value = "/pay")
    @ApiOperation(value = "通过商品id列表查询商品信息", notes = "通过商品id列表查询商品信息")
    public Result<?> pay(@RequestParam("userId") Long userId,@RequestParam("payMoney") BigDecimal payAmount) {
        Long balanceLogId = payService.pay(userId, payAmount);
        return Result.data(balanceLogId);
    }
}
