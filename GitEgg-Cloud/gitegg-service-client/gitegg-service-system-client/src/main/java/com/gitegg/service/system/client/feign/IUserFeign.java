package com.gitegg.service.system.client.feign;

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.system.client.dto.UserAddDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 添加contextId用于区分相同name的client，否则会报错
 * @author GitEgg
 */
@FeignClient(name = "gitegg-service-system", contextId = "UserClient")
public interface IUserFeign {
    
    /**
     * 通过用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/feign/user/query/by/id")
    Result<Object> queryById(@RequestParam("id") Long id);

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
    
    /**
     * 新增用户信息
     *
     * @param userAddDTO
     * @return
     */
    @PostMapping("/feign/user/add")
    Result<Object> userAdd(@RequestBody UserAddDTO userAddDTO);

}
