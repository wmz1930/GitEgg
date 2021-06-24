package com.gitegg.service.extension.sms.contorller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.sms.dto.CreateSmsChannelDTO;
import com.gitegg.service.extension.sms.dto.QuerySmsChannelDTO;
import com.gitegg.service.extension.sms.dto.SmsChannelDTO;
import com.gitegg.service.extension.sms.dto.UpdateSmsChannelDTO;
import com.gitegg.service.extension.sms.entity.SmsChannel;
import com.gitegg.service.extension.sms.service.ISmsChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
* <p>
* 短信渠道表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-01-26
*/
@RestController
@RequestMapping("/extension/sms/channel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsChannelController|短信渠道表前端控制器")
@RefreshScope
public class SmsChannelController {

    private final ISmsChannelService smsChannelService;

    /**
    * 查询短信渠道表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询短信渠道表列表")
    public PageResult<SmsChannelDTO> list(QuerySmsChannelDTO querySmsChannelDTO, Page<SmsChannelDTO> page) {
        Page<SmsChannelDTO> pageSmsChannel = smsChannelService.querySmsChannelList(page, querySmsChannelDTO);
        return PageResult.data(pageSmsChannel.getTotal(), pageSmsChannel.getRecords());
    }

    /**
    * 查询短信渠道表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询短信渠道表详情")
    public Result<?> query(QuerySmsChannelDTO querySmsChannelDTO) {
        SmsChannelDTO smsChannelDTO = smsChannelService.querySmsChannel(querySmsChannelDTO);
        return Result.data(smsChannelDTO);
    }

    /**
    * 添加短信渠道表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加短信渠道表")
    public Result<?> create(@RequestBody CreateSmsChannelDTO smsChannel) {
        boolean result = smsChannelService.createSmsChannel(smsChannel);
        return Result.result(result);
    }

    /**
    * 修改短信渠道表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新短信渠道表")
    public Result<?> update(@RequestBody UpdateSmsChannelDTO smsChannel) {
        boolean result = smsChannelService.updateSmsChannel(smsChannel);
        return Result.result(result);
    }

    /**
    * 删除短信渠道表
    */
    @PostMapping("/delete/{smsChannelId}")
    @ApiOperation(value = "删除短信渠道表")
    @ApiImplicitParam(paramType = "path", name = "smsChannelId", value = "短信渠道表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("smsChannelId") Long smsChannelId) {
        if (null == smsChannelId) {
            return new Result<>().error("ID不能为空");
        }
        boolean result = smsChannelService.deleteSmsChannel(smsChannelId);
        return Result.result(result);
    }

    /**
    * 批量删除短信渠道表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除短信渠道表")
    @ApiImplicitParam(name = "smsChannelIds", value = "短信渠道表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> smsChannelIds) {
        if (CollectionUtils.isEmpty(smsChannelIds)) {
            return new Result<>().error("短信渠道表ID列表不能为空");
        }
        boolean result = smsChannelService.batchDeleteSmsChannel(smsChannelIds);
        return Result.result(result);
    }

    /**
    * 修改短信渠道表状态
    */
    @PostMapping("/status/{smsChannelId}/{smsChannelStatus}")
    @ApiOperation(value = "修改短信渠道表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "smsChannelId", value = "短信渠道表ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "smsChannelStatus", value = "短信渠道表状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("smsChannelId") Long smsChannelId,
            @PathVariable("smsChannelStatus") Integer smsChannelStatus) {
        if (null == smsChannelId || StringUtils.isEmpty(smsChannelStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateSmsChannelDTO smsChannel = new UpdateSmsChannelDTO();
        smsChannel.setId(smsChannelId);
        smsChannel.setChannelStatus(smsChannelStatus);
        boolean result = smsChannelService.updateSmsChannel(smsChannel);
        return Result.result(result);
    }

    /**
    * 校验短信渠道表是否存在
    *
    * @param smsChannel
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验短信渠道表是否存在", notes = "校验短信渠道表是否存在")
    public Result<Boolean> checkSmsChannelExist(CheckExistDTO smsChannel) {
        String field = smsChannel.getCheckField();
        String value = smsChannel.getCheckValue();
        QueryWrapper<SmsChannel> smsChannelQueryWrapper = new QueryWrapper<>();
        smsChannelQueryWrapper.eq(field, value);
        if(null != smsChannel.getId()) {
            smsChannelQueryWrapper.ne("id", smsChannel.getId());
        }
        int count = smsChannelService.count(smsChannelQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
