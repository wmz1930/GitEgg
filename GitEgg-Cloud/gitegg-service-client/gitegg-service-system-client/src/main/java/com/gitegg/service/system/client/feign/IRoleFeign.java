package com.gitegg.service.system.client.feign;

import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 添加contextId用于区分相同name的client，否则会报错
 * @author GitEgg
 */
@FeignClient(name = "gitegg-service-system", contextId = "RoleClient")
public interface IRoleFeign {

    /**
     * 根据id批量查询角色信息
     * @param ids
     * @return
     */
    @GetMapping("/feign/role/query/by/ids")
    Result<List<Object>> queryRoleByIds(@RequestParam("ids") List<Long> ids);
    
    /**
     * 根据key批量查询角色信息
     * @param keys
     * @return
     */
    @GetMapping("/feign/role/query/by/keys")
    Result<List<Object>> queryRoleByKeys(@RequestParam("keys") List<String> keys);

}
