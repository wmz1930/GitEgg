package com.gitegg.service.extension.client.feign;

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.client.dto.JustAuthSocialInfoDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * 添加contextId用于区分相同name的client，否则会报错
 * @author GitEgg
 */
@FeignClient(name = "gitegg-service-extension", contextId = "JustAuthClient")
public interface IJustAuthFeign {

    /**
     * 获取该用户绑定关系
     *
     * @param uuid
     * @param source
     * @return
     */
    @GetMapping("/feign/justauth/user/bind/id")
    Result<Object> userBindId(@RequestParam("uuid") String uuid, @RequestParam("source") String source);

    /**
     * 保存第三方用户信息
     *
     * @param justAuthSocialInfoDTO
     * @return
     */
    @PostMapping("/feign/justauth/user/create/or/update")
    Result<Object> userCreateOrUpdate(@RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO);
    
    /**
     * 查询绑定第三方用户信息
     * @param socialId
     * @return
     */
    @GetMapping(value = "/feign/justauth/user/bind/query")
    Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId);
    
    /**
     * 查询第三方用户信息
     * @param socialId
     * @return
     */
    @GetMapping(value = "/feign/justauth/social/info/query")
    Result<Object> querySocialInfo(@NotNull @RequestParam("socialId") Long socialId);
    
    /**
     * 绑定第三方用户信息
     * @param socialId
     * @param userId
     * @return
     */
    @GetMapping(value = "/feign/justauth/user/bind")
    @ApiOperation(value = "绑定第三方用户信息", notes = "绑定第三方用户信息")
    Result<Object> userBind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);
    
    /**
     * 解绑第三方用户信息
     * @param socialId
     * @param userId
     * @return
     */
    @GetMapping(value = "/feign/justauth/user/unbind")
    Result<Object> userUnbind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);

}
