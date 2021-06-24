package com.gitegg.mall.user.controller;


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
import com.gitegg.mall.user.entity.UserBalanceRecord;
import com.gitegg.mall.user.dto.UserBalanceRecordDTO;
import com.gitegg.mall.user.dto.CreateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.UpdateUserBalanceRecordDTO;
import com.gitegg.mall.user.dto.QueryUserBalanceRecordDTO;

import com.gitegg.mall.user.service.IUserBalanceRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
*  前端控制器
* </p>
*
* @author GitEgg
* @since 2021-03-19
*/
@RestController
@RequestMapping("/mall/user/balance/record")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "UserBalanceRecordController|前端控制器")
@RefreshScope
public class UserBalanceRecordController {

    private final IUserBalanceRecordService userBalanceRecordService;

    /**
    * 查询列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表")
    public PageResult<UserBalanceRecordDTO> list(QueryUserBalanceRecordDTO queryUserBalanceRecordDTO, Page<UserBalanceRecordDTO> page) {
        Page<UserBalanceRecordDTO> pageUserBalanceRecord = userBalanceRecordService.queryUserBalanceRecordList(page, queryUserBalanceRecordDTO);
        return PageResult.data(pageUserBalanceRecord.getTotal(), pageUserBalanceRecord.getRecords());
    }

    /**
    * 查询详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询详情")
    public Result<?> query(QueryUserBalanceRecordDTO queryUserBalanceRecordDTO) {
        UserBalanceRecordDTO userBalanceRecordDTO = userBalanceRecordService.queryUserBalanceRecord(queryUserBalanceRecordDTO);
        return Result.data(userBalanceRecordDTO);
    }

    /**
    * 添加
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加")
    public Result<?> create(@RequestBody CreateUserBalanceRecordDTO userBalanceRecord) {
        boolean result = userBalanceRecordService.createUserBalanceRecord(userBalanceRecord);
        return Result.result(result);
    }

    /**
    * 修改
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public Result<?> update(@RequestBody UpdateUserBalanceRecordDTO userBalanceRecord) {
        boolean result = userBalanceRecordService.updateUserBalanceRecord(userBalanceRecord);
        return Result.result(result);
    }

    /**
    * 删除
    */
    @PostMapping("/delete/{userBalanceRecordId}")
    @ApiOperation(value = "删除")
    @ApiImplicitParam(paramType = "path", name = "userBalanceRecordId", value = "ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("userBalanceRecordId") Long userBalanceRecordId) {
        if (null == userBalanceRecordId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = userBalanceRecordService.deleteUserBalanceRecord(userBalanceRecordId);
        return Result.result(result);
    }

    /**
    * 批量删除
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "userBalanceRecordIds", value = "ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> userBalanceRecordIds) {
        if (CollectionUtils.isEmpty(userBalanceRecordIds)) {
            return new Result<>().error("ID列表不能为空");
        }
        boolean result = userBalanceRecordService.batchDeleteUserBalanceRecord(userBalanceRecordIds);
        return Result.result(result);
    }

    /**
    * 校验是否存在
    *
    * @param userBalanceRecord
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验是否存在", notes = "校验是否存在")
    public Result<Boolean> checkUserBalanceRecordExist(CheckExistDTO userBalanceRecord) {
        String field = userBalanceRecord.getCheckField();
        String value = userBalanceRecord.getCheckValue();
        QueryWrapper<UserBalanceRecord> userBalanceRecordQueryWrapper = new QueryWrapper<>();
        userBalanceRecordQueryWrapper.eq(field, value);
        if(null != userBalanceRecord.getId()) {
            userBalanceRecordQueryWrapper.ne("id", userBalanceRecord.getId());
        }
        int count = userBalanceRecordService.count(userBalanceRecordQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
