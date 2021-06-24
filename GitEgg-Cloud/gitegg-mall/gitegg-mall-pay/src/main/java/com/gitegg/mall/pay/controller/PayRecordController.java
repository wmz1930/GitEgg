package com.gitegg.mall.pay.controller;


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
import com.gitegg.mall.pay.entity.PayRecord;
import com.gitegg.mall.pay.dto.PayRecordDTO;
import com.gitegg.mall.pay.dto.CreatePayRecordDTO;
import com.gitegg.mall.pay.dto.UpdatePayRecordDTO;
import com.gitegg.mall.pay.dto.QueryPayRecordDTO;

import com.gitegg.mall.pay.service.IPayRecordService;

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
@RequestMapping("/mall/pay/record")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "PayRecordController|前端控制器")
@RefreshScope
public class PayRecordController {

    private final IPayRecordService payRecordService;

    /**
    * 查询列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表")
    public PageResult<PayRecordDTO> list(QueryPayRecordDTO queryPayRecordDTO, Page<PayRecordDTO> page) {
        Page<PayRecordDTO> pagePayRecord = payRecordService.queryPayRecordList(page, queryPayRecordDTO);
        return PageResult.data(pagePayRecord.getTotal(), pagePayRecord.getRecords());
    }

    /**
    * 查询详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询详情")
    public Result<?> query(QueryPayRecordDTO queryPayRecordDTO) {
        PayRecordDTO payRecordDTO = payRecordService.queryPayRecord(queryPayRecordDTO);
        return Result.data(payRecordDTO);
    }

    /**
    * 添加
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加")
    public Result<?> create(@RequestBody CreatePayRecordDTO payRecord) {
        boolean result = payRecordService.createPayRecord(payRecord);
        return Result.result(result);
    }

    /**
    * 修改
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public Result<?> update(@RequestBody UpdatePayRecordDTO payRecord) {
        boolean result = payRecordService.updatePayRecord(payRecord);
        return Result.result(result);
    }

    /**
    * 删除
    */
    @PostMapping("/delete/{payRecordId}")
    @ApiOperation(value = "删除")
    @ApiImplicitParam(paramType = "path", name = "payRecordId", value = "ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("payRecordId") Long payRecordId) {
        if (null == payRecordId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = payRecordService.deletePayRecord(payRecordId);
        return Result.result(result);
    }

    /**
    * 批量删除
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "payRecordIds", value = "ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> payRecordIds) {
        if (CollectionUtils.isEmpty(payRecordIds)) {
            return new Result<>().error("ID列表不能为空");
        }
        boolean result = payRecordService.batchDeletePayRecord(payRecordIds);
        return Result.result(result);
    }

    /**
    * 修改状态
    */
    @PostMapping("/status/{payRecordId}/{payRecordStatus}")
    @ApiOperation(value = "修改状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "payRecordId", value = "ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "payRecordStatus", value = "状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("payRecordId") Long payRecordId,
            @PathVariable("payRecordStatus") Integer payRecordStatus) {
        if (null == payRecordId || StringUtils.isEmpty(payRecordStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdatePayRecordDTO payRecord = new UpdatePayRecordDTO();
        payRecord.setId(payRecordId);
        payRecord.setPayStatus(payRecordStatus);
        boolean result = payRecordService.updatePayRecord(payRecord);
        return Result.result(result);
    }

    /**
    * 校验是否存在
    *
    * @param payRecord
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验是否存在", notes = "校验是否存在")
    public Result<Boolean> checkPayRecordExist(CheckExistDTO payRecord) {
        String field = payRecord.getCheckField();
        String value = payRecord.getCheckValue();
        QueryWrapper<PayRecord> payRecordQueryWrapper = new QueryWrapper<>();
        payRecordQueryWrapper.eq(field, value);
        if(null != payRecord.getId()) {
            payRecordQueryWrapper.ne("id", payRecord.getId());
        }
        int count = payRecordService.count(payRecordQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
