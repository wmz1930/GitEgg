package com.gitegg.service.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.system.dto.CreateResourceDTO;
import com.gitegg.service.system.dto.UpdateResourceDTO;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.service.IResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author gitegg
 * @since 2019s-05-19
 */
@RestController
@RequestMapping(value = "resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "ResourceController|权限资源相关的前端控制器")
@RefreshScope
public class ResourceController {

    private final IResourceService resourceService;

    /**
     * 查询权限资源树
     * 
     * @param parentId
     * @return
     */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询权限资源树", notes = "树状展示权限资源信息")
    @ApiImplicitParam(paramType = "query", name = "parentId", value = "父级ID", required = false, dataType = "Long")
    public Result<List<Resource>> queryResourceTree(Long parentId) {
        List<Resource> treeList = resourceService.queryResourceByParentId(parentId);
        return Result.data(treeList);
    }

    /**
     * 添加资源
     * 
     * @param resource
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加权限资源")
    public Result<Resource> createResource(@RequestBody CreateResourceDTO resource) {
        Resource resEntity = BeanCopierUtils.copyByClass(resource, Resource.class);
        boolean result = resourceService.createResource(resEntity);
        if (result) {
            return Result.data(resEntity);
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改资源
     * 
     * @param resource
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新权限资源")
    public Result<Resource> updateResource(@RequestBody UpdateResourceDTO resource) {
        Resource resEntity = BeanCopierUtils.copyByClass(resource, Resource.class);
        boolean result = resourceService.updateResource(resEntity);
        if (result) {
            return Result.data(resEntity);
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改资源
     * 
     * @param resourceId
     * @return
     */
    @PostMapping("/delete/{resourceId}")
    @ApiOperation(value = "删除权限资源")
    @ApiImplicitParam(paramType = "path", name = "resourceId", value = "权限资源ID", required = true, dataType = "Long")
    public Result<?> deleteResource(@PathVariable("resourceId") Long resourceId) {
        boolean result = resourceService.deleteResource(resourceId);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 修改资源权限状态
     */
    @PostMapping("/status/{resourceId}/{resourceStatus}")
    @ApiOperation(value = "修改角色状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "resourceId", value = "角色ID", required = true, dataType = "Long", paramType = "path"),
        @ApiImplicitParam(name = "resourceStatus", value = "角色状态", required = true, dataType = "Integer",
            paramType = "path")})
    public Result<?> updateStatus(@PathVariable("resourceId") Long resourceId,
        @PathVariable("resourceStatus") Integer resourceStatus) {
        if (null == resourceId || StringUtils.isEmpty(resourceStatus)) {
            return Result.error("ID和状态不能为空");
        }
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setResourceStatus(resourceStatus);
        boolean result = resourceService.updateResource(resource);
        if (result) {
            return Result.success();
        } else {
            return Result.error(ResultCodeEnum.FAILED);
        }
    }

    /**
     * 检查资源的key是否存在
     * 
     * @param resource
     * @return
     */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验Resource是否存在", notes = "校验Resource是否存在")
    public Result<Boolean> checkResource(CheckExistDTO resource) {
        String field = resource.getCheckField();
        String value = resource.getCheckValue();
        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq(field, value);
        if(null != resource && null != resource.getId()) {
            resourceQueryWrapper.ne("id", resource.getId());
        }
        int count = resourceService.count(resourceQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 获取登录后的菜单权限
     * 
     * @param currentUser
     * @return
     */
    @GetMapping("/menu")
    @ApiOperation(value = "登陆后获取个人权限资源")
    public Result<List<Resource>> navMenu(@ApiIgnore User currentUser) {
        Long userId = currentUser.getId();
        List<Resource> resourceList = resourceService.queryMenuTreeByUserId(userId);
        return Result.data(resourceList);
    }
}
