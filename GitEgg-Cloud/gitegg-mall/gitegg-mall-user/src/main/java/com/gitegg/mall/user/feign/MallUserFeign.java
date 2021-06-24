package com.gitegg.mall.user.feign;

import com.gitegg.mall.user.service.IUserAccountService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @ClassName: UserFeign
 * @Description: UserFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/mall/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MallUserFeign|提供微服务调用接口")
@RefreshScope
public class MallUserFeign {

    private final IUserAccountService userAccountService;

    @GetMapping(value = "/account/deduction")
    @ApiOperation(value = "扣减账户余额", notes = "扣减账户余额")
    public Result<?> accountDeduction(@RequestParam("userId") Long userId
            ,@RequestParam("amountOfMoney") BigDecimal amountOfMoney) {
        userAccountService.deduction(userId, amountOfMoney);
        return Result.success();
    }
}
