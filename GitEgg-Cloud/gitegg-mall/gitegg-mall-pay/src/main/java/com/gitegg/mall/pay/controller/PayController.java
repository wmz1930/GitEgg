package com.gitegg.mall.pay.controller;

import com.gitegg.mall.pay.service.IPayService;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

/**
 * @author GitEgg
 * @date 2021-03-19 14:59:33
 **/
@RestController
@RequestMapping("/mall/pay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "UserBalanceLogController|前端控制器")
@RefreshScope
public class PayController {

    private final IPayService payService;

    @ApiOperation(value = "支付")
    @PostMapping(value = "/pay")
    public Result<?> pay(@ApiParam(value = "支付金额")@RequestParam BigDecimal payAmount
            , @ApiIgnore @CurrentUser GitEggUser user) {
        Long balanceLogId = payService.pay(user.getId(), payAmount);
        return Result.data(balanceLogId);
    }
}
