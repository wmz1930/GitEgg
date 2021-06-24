package com.gitegg.service.extension.controller;


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
import com.gitegg.service.extension.entity.SmsTemplate;
import com.gitegg.service.extension.dto.SmsTemplateDTO;
import com.gitegg.service.extension.dto.CreateSmsTemplateDTO;
import com.gitegg.service.extension.dto.UpdateSmsTemplateDTO;
import com.gitegg.service.extension.dto.QuerySmsTemplateDTO;

import com.gitegg.service.extension.service.ISmsTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
* 短信配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-01-26
*/
@RestController
@RequestMapping("/extension/sms/template")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsTemplateController|短信配置表前端控制器")
@RefreshScope
public class SmsTemplateController {

    private final ISmsTemplateService smsTemplateService;

    /**
    * 查询短信配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询短信配置表列表")
    public PageResult<SmsTemplateDTO> list(QuerySmsTemplateDTO querySmsTemplateDTO, Page<SmsTemplateDTO> page) {
        Page<SmsTemplateDTO> pageSmsTemplate = smsTemplateService.querySmsTemplateList(page, querySmsTemplateDTO);
        return PageResult.data(pageSmsTemplate.getTotal(), pageSmsTemplate.getRecords());
    }

    /**
    * 查询短信配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询短信配置表详情")
    public Result<?> query(QuerySmsTemplateDTO querySmsTemplateDTO) {
        SmsTemplateDTO smsTemplateDTO = smsTemplateService.querySmsTemplate(querySmsTemplateDTO);
        return Result.data(smsTemplateDTO);
    }

    /**
    * 添加短信配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加短信配置表")
    public Result<?> create(@RequestBody CreateSmsTemplateDTO smsTemplate) {
        boolean result = smsTemplateService.createSmsTemplate(smsTemplate);
        return Result.result(result);
    }

    /**
    * 修改短信配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新短信配置表")
    public Result<?> update(@RequestBody UpdateSmsTemplateDTO smsTemplate) {
        boolean result = smsTemplateService.updateSmsTemplate(smsTemplate);
        return Result.result(result);
    }

    /**
    * 删除短信配置表
    */
    @PostMapping("/delete/{smsTemplateId}")
    @ApiOperation(value = "删除短信配置表")
    @ApiImplicitParam(paramType = "path", name = "smsTemplateId", value = "短信配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("smsTemplateId") Long smsTemplateId) {
        if (null == smsTemplateId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = smsTemplateService.deleteSmsTemplate(smsTemplateId);
        return Result.result(result);
    }

    /**
    * 批量删除短信配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除短信配置表")
    @ApiImplicitParam(name = "smsTemplateIds", value = "短信配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> smsTemplateIds) {
        if (CollectionUtils.isEmpty(smsTemplateIds)) {
            return new Result<>().error("短信配置表ID列表不能为空");
        }
        boolean result = smsTemplateService.batchDeleteSmsTemplate(smsTemplateIds);
        return Result.result(result);
    }

    /**
    * 修改短信配置表状态
    */
    @PostMapping("/status/{smsTemplateId}/{smsTemplateStatus}")
    @ApiOperation(value = "修改短信配置表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "smsTemplateId", value = "短信配置表ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "smsTemplateStatus", value = "短信配置表状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("smsTemplateId") Long smsTemplateId,
            @PathVariable("smsTemplateStatus") Integer smsTemplateStatus) {
        if (null == smsTemplateId || StringUtils.isEmpty(smsTemplateStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateSmsTemplateDTO smsTemplate = new UpdateSmsTemplateDTO();
        smsTemplate.setId(smsTemplateId);
        smsTemplate.setTemplateStatus(smsTemplateStatus);
        boolean result = smsTemplateService.updateSmsTemplate(smsTemplate);
        return Result.result(result);
    }

    /**
    * 校验短信配置表是否存在
    *
    * @param smsTemplate
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验短信配置表是否存在", notes = "校验短信配置表是否存在")
    public Result<Boolean> checkSmsTemplateExist(CheckExistDTO smsTemplate) {
        String field = smsTemplate.getCheckField();
        String value = smsTemplate.getCheckValue();
        QueryWrapper<SmsTemplate> smsTemplateQueryWrapper = new QueryWrapper<>();
        smsTemplateQueryWrapper.eq(field, value);
        if(null != smsTemplate.getId()) {
            smsTemplateQueryWrapper.ne("id", smsTemplate.getId());
        }
        int count = smsTemplateService.count(smsTemplateQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
