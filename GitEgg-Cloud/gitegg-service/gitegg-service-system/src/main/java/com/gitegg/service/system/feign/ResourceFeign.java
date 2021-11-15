package com.gitegg.service.system.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ResourceFeign
 * @Description: ResourceFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "ResourceFeign|提供微服务调用接口")
@RefreshScope
public class ResourceFeign {

    private final IResourceService resourceService;

    @GetMapping(value = "/query/max/id")
    @ApiOperation(value = "查询资源权限表最大的id值", notes = "查询资源权限表最大的id值")
    public Result<Long> queryResourceMaxId() {
        Long idMax = 0L;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        List<Resource> resourceList = resourceService.list(queryWrapper);
        if(!CollectionUtils.isEmpty(resourceList))
        {
            idMax = resourceList.get(0).getId();
        }
        return Result.data(idMax);
    }
}
