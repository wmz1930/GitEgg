package com.gitegg.service.extension.justauth.controller;


import java.util.List;

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
import com.gitegg.service.extension.justauth.entity.*;
import com.gitegg.service.extension.justauth.dto.*;

import com.gitegg.service.extension.justauth.service.IJustAuthSocialUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
* 租户第三方登录功能配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@RestController
@RequestMapping("/extension/justauth/social/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthSocialUserController|租户第三方登录功能配置表前端控制器")
@RefreshScope
public class JustAuthSocialUserController {

    private final IJustAuthSocialUserService justAuthSocialUserService;

    /**
    * 查询租户第三方登录功能配置表列表
    *
    * @param queryJustAuthSocialUserDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录功能配置表列表")
    public PageResult<JustAuthSocialUserDTO> list(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO, Page<JustAuthSocialUserDTO> page) {
        Page<JustAuthSocialUserDTO> pageJustAuthSocialUser = justAuthSocialUserService.queryJustAuthSocialUserList(page, queryJustAuthSocialUserDTO);
        return PageResult.data(pageJustAuthSocialUser.getTotal(), pageJustAuthSocialUser.getRecords());
    }

    /**
    * 查询租户第三方登录功能配置表详情
    *
    * @param queryJustAuthSocialUserDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录功能配置表详情")
    public Result<?> query(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
        JustAuthSocialUserDTO justAuthSocialUserDTO = justAuthSocialUserService.queryJustAuthSocialUser(queryJustAuthSocialUserDTO);
        return Result.data(justAuthSocialUserDTO);
    }

    /**
    * 添加租户第三方登录功能配置表
    *
    * @param justAuthSocialUser
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录功能配置表")
    public Result<?> create(@RequestBody CreateJustAuthSocialUserDTO justAuthSocialUser) {
        boolean result = justAuthSocialUserService.createJustAuthSocialUser(justAuthSocialUser);
        return Result.result(result);
    }

    /**
    * 修改租户第三方登录功能配置表
    *
    * @param justAuthSocialUser
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录功能配置表")
    public Result<?> update(@RequestBody UpdateJustAuthSocialUserDTO justAuthSocialUser) {
        boolean result = justAuthSocialUserService.updateJustAuthSocialUser(justAuthSocialUser);
        return Result.result(result);
    }

    /**
    * 删除租户第三方登录功能配置表
    *
    * @param justAuthSocialUserId
    * @return
    */
    @PostMapping("/delete/{justAuthSocialUserId}")
    @ApiOperation(value = "删除租户第三方登录功能配置表")
    @ApiImplicitParam(paramType = "path", name = "justAuthSocialUserId", value = "租户第三方登录功能配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("justAuthSocialUserId") Long justAuthSocialUserId) {
        if (null == justAuthSocialUserId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSocialUserService.deleteJustAuthSocialUser(justAuthSocialUserId);
        return Result.result(result);
    }

    /**
    * 批量删除租户第三方登录功能配置表
    *
    * @param justAuthSocialUserIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录功能配置表")
    @ApiImplicitParam(name = "justAuthSocialUserIds", value = "租户第三方登录功能配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSocialUserIds) {
        if (CollectionUtils.isEmpty(justAuthSocialUserIds)) {
            return Result.error("租户第三方登录功能配置表ID列表不能为空");
        }
        boolean result = justAuthSocialUserService.batchDeleteJustAuthSocialUser(justAuthSocialUserIds);
        return Result.result(result);
    }


 }
