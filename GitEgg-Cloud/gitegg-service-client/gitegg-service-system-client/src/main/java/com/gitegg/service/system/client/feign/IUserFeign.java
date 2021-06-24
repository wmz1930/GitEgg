package com.gitegg.service.system.client.feign;

import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//添加contextId用于区分相同name的client，否则会报错
@FeignClient(name = "gitegg-service-system", contextId = "UserClient")
public interface IUserFeign {

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber
     * @return
     */
    @GetMapping("/feign/user/query/by/phone")
    Result<Object> queryUserByPhone(@RequestParam("phoneNumber") String phoneNumber);

    /**
     * 通过账号查询用户
     *
     * @param account
     * @return
     */
    @GetMapping("/feign/user/query/by/account")
    Result<Object> queryUserByAccount(@RequestParam("account") String account);

    /**
     * 通过微信openId查询用户
     *
     * @param openid
     * @return
     */
    @GetMapping("/feign/user/query/by/openid")
    Result<Object> queryUserByOpenId(@RequestParam("openid") String openid);

}
