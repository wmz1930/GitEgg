package com.gitegg.mall.user.client.feign;

import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

//添加contextId用于区分相同name的client，否则会报错
@FeignClient(name = "gitegg-mall-user", contextId = "MallUserClient")
public interface IMallUserFeign {

    /**
     * 扣除账户余额接口
     *
     * @param userId
     * @param amountOfMoney
     * @return
     */
    @GetMapping("/feign/mall/user/account/deduction")
    Result<Object> accountDeduction(@RequestParam("userId") Long userId
            ,@RequestParam("amountOfMoney") BigDecimal amountOfMoney);

}
