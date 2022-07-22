package com.gitegg.service.system.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.system.entity.DataPermissionUser;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IRoleService;
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
 * @ClassName: RoleFeign
 * @Description: RoleFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "RoleFeign|提供微服务调用接口")
@RefreshScope
public class RoleFeign {
    
    private final IRoleService roleService;
    
    @GetMapping(value = "/query/by/ids")
    @ApiOperation(value = "根据id批量查询角色信息", notes = "根据id批量查询角色信息")
    public Result<List<Role>> queryRoleByIds(List<Long> ids) {
        List<Role> roleList = roleService.listByIds(ids);
        return Result.data(roleList);
    }
    
    @GetMapping(value = "/query/by/keys")
    @ApiOperation(value = "根据key批量查询角色信息", notes = "根据key批量查询角色信息")
    public Result<List<Role>> queryRoleByKeys(List<String> keys) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getRoleKey, keys);
        List<Role> roleList = roleService.list(queryWrapper);
        return Result.data(roleList);
    }
}
