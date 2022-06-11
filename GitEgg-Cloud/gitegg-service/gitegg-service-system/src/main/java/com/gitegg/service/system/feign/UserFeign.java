package com.gitegg.service.system.feign;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.system.dto.CreateUserDTO;
import com.gitegg.service.system.dto.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;
import com.gitegg.service.system.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

/**
 * @ClassName: UserFeign
 * @Description: UserFeign前端控制器
 * @author GitEgg
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "UserFeign|提供微服务调用接口")
@RefreshScope
public class UserFeign {

    private final IUserService userService;
    
    @GetMapping(value = "/query/by/id")
    @ApiOperation(value = "通过用户id查询用户信息", notes = "通过用户id查询用户信息")
    public Result<UserInfo> queryById(Long id) {
        User user = new User();
        user.setId(id);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    @GetMapping(value = "/query/by/phone")
    @ApiOperation(value = "通过手机号码查询用户信息", notes = "通过手机号码查询用户信息")
    public Result<UserInfo> queryByPhone(String phoneNumber) {
        User user = new User();
        user.setMobile(phoneNumber);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    @GetMapping(value = "/query/by/account")
    @ApiOperation(value = "通过账号查询用户信息", notes = "通过账号查询用户信息")
    public Result<UserInfo> queryByAccount(String account) {
        User user = new User();
        user.setAccount(account);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    @GetMapping(value = "/query/by/openid")
    @ApiOperation(value = "通过微信openid查询用户信息", notes = "通过微信openid查询用户信息")
    public Result<UserInfo> queryByOpenId(String openid) {
        User user = new User();
        //此处待定
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }
    
    /**
     * 新增用户信息
     *
     * @param createUserDTO
     * @return
     */
    @PostMapping("/add")
    Result<Object> userAdd(@RequestBody CreateUserDTO createUserDTO){
        CreateUserDTO userDTO = userService.createUser(createUserDTO);
        return Result.data(userDTO.getId());
    }
}
