package com.gitegg.mall.user.controller;


import java.math.BigDecimal;
import java.util.List;

import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.domain.GitEggUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.mall.user.entity.UserAccount;
import com.gitegg.mall.user.dto.UserAccountDTO;
import com.gitegg.mall.user.dto.CreateUserAccountDTO;
import com.gitegg.mall.user.dto.UpdateUserAccountDTO;
import com.gitegg.mall.user.dto.QueryUserAccountDTO;

import com.gitegg.mall.user.service.IUserAccountService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;


/**
* <p>
* 用户账户表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-03-19
*/
@RestController
@RequestMapping("/mall/user/account")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "UserAccountController|用户账户表前端控制器")
@RefreshScope
public class UserAccountController {

    private final IUserAccountService userAccountService;


    @ApiOperation(value = "账户扣款接口")
    @PostMapping(value = "/deduction")
    public Result<?> deduction( @ApiParam(value = "金额") @RequestParam BigDecimal amountOfMoney
            , @ApiIgnore @CurrentUser GitEggUser user) {
        userAccountService.deduction(user.getId(), amountOfMoney);
        return Result.success();
    }




    /**
    * 查询用户账户表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询用户账户表列表")
    public PageResult<UserAccountDTO> list(QueryUserAccountDTO queryUserAccountDTO, Page<UserAccountDTO> page) {
        Page<UserAccountDTO> pageUserAccount = userAccountService.queryUserAccountList(page, queryUserAccountDTO);
        return PageResult.data(pageUserAccount.getTotal(), pageUserAccount.getRecords());
    }

    /**
    * 查询用户账户表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询用户账户表详情")
    public Result<?> query(QueryUserAccountDTO queryUserAccountDTO) {
        UserAccountDTO userAccountDTO = userAccountService.queryUserAccount(queryUserAccountDTO);
        return Result.data(userAccountDTO);
    }

    /**
    * 添加用户账户表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加用户账户表")
    public Result<?> create(@RequestBody CreateUserAccountDTO userAccount) {
        boolean result = userAccountService.createUserAccount(userAccount);
        return Result.result(result);
    }

    /**
    * 修改用户账户表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户账户表")
    public Result<?> update(@RequestBody UpdateUserAccountDTO userAccount) {
        boolean result = userAccountService.updateUserAccount(userAccount);
        return Result.result(result);
    }

    /**
    * 删除用户账户表
    */
    @PostMapping("/delete/{userAccountId}")
    @ApiOperation(value = "删除用户账户表")
    @ApiImplicitParam(paramType = "path", name = "userAccountId", value = "用户账户表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("userAccountId") Long userAccountId) {
        if (null == userAccountId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = userAccountService.deleteUserAccount(userAccountId);
        return Result.result(result);
    }

    /**
    * 批量删除用户账户表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除用户账户表")
    @ApiImplicitParam(name = "userAccountIds", value = "用户账户表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> userAccountIds) {
        if (CollectionUtils.isEmpty(userAccountIds)) {
            return new Result<>().error("用户账户表ID列表不能为空");
        }
        boolean result = userAccountService.batchDeleteUserAccount(userAccountIds);
        return Result.result(result);
    }

    /**
    * 修改用户账户表状态
    */
    @PostMapping("/status/{userAccountId}/{userAccountStatus}")
    @ApiOperation(value = "修改用户账户表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "userAccountId", value = "用户账户表ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "userAccountStatus", value = "用户账户表状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("userAccountId") Long userAccountId,
            @PathVariable("userAccountStatus") Integer userAccountStatus) {
        if (null == userAccountId || StringUtils.isEmpty(userAccountStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateUserAccountDTO userAccount = new UpdateUserAccountDTO();
        userAccount.setId(userAccountId);
        userAccount.setAccountStatus(userAccountStatus);
        boolean result = userAccountService.updateUserAccount(userAccount);
        return Result.result(result);
    }

    /**
    * 校验用户账户表是否存在
    *
    * @param userAccount
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验用户账户表是否存在", notes = "校验用户账户表是否存在")
    public Result<Boolean> checkUserAccountExist(CheckExistDTO userAccount) {
        String field = userAccount.getCheckField();
        String value = userAccount.getCheckValue();
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq(field, value);
        if(null != userAccount.getId()) {
            userAccountQueryWrapper.ne("id", userAccount.getId());
        }
        int count = userAccountService.count(userAccountQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
