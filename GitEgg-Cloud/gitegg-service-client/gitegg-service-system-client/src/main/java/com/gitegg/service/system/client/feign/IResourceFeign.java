package com.gitegg.service.system.client.feign;

import com.gitegg.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 添加contextId用于区分相同name的client，否则会报错
 * @author GitEgg
 */
@FeignClient(name = "gitegg-service-system", contextId = "ResourceClient")
public interface IResourceFeign {

    /**
     * 查询Resource最大id
     *
     * @return
     */
    @GetMapping("/feign/resource/query/max/id")
    Result<Object> queryResourceMaxId();

}
