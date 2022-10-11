package com.gitegg.service.extension.sms.controller;


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
import com.gitegg.service.extension.sms.entity.*;
import com.gitegg.service.extension.sms.dto.*;

import com.gitegg.service.extension.sms.service.ISmsChannelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* <p>
* 短信渠道表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-24
*/
@RestController
@RequestMapping("/extension/sms/channel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsChannelController|短信渠道表前端控制器", tags = {"短信渠道配置"})
@RefreshScope
public class SmsChannelController {

    private final ISmsChannelService smsChannelService;

    /**
    * 查询短信渠道表列表
    *
    * @param querySmsChannelDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询短信渠道表列表")
    public PageResult<SmsChannelDTO> list(QuerySmsChannelDTO querySmsChannelDTO, Page<SmsChannelDTO> page) {
        Page<SmsChannelDTO> pageSmsChannel = smsChannelService.querySmsChannelList(page, querySmsChannelDTO);
        return PageResult.data(pageSmsChannel.getTotal(), pageSmsChannel.getRecords());
    }
    
    /**
     * 查询短信渠道表列表
     *
     * @param querySmsChannelDTO
     * @return
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询所有短信渠道表列表")
    public Result<?> listAll(QuerySmsChannelDTO querySmsChannelDTO) {
        List<SmsChannelDTO> smsChannelList = smsChannelService.querySmsChannelList(querySmsChannelDTO);
        return Result.data(smsChannelList);
    }

    /**
    * 查询短信渠道表详情
    *
    * @param querySmsChannelDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询短信渠道表详情")
    public Result<?> query(QuerySmsChannelDTO querySmsChannelDTO) {
        SmsChannelDTO smsChannelDTO = smsChannelService.querySmsChannel(querySmsChannelDTO);
        return Result.data(smsChannelDTO);
    }

    /**
    * 添加短信渠道表
    *
    * @param smsChannel
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加短信渠道表")
    public Result<?> create(@RequestBody CreateSmsChannelDTO smsChannel) {
        boolean result = smsChannelService.createSmsChannel(smsChannel);
        return Result.result(result);
    }

    /**
    * 修改短信渠道表
    *
    * @param smsChannel
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新短信渠道表")
    public Result<?> update(@RequestBody UpdateSmsChannelDTO smsChannel) {
        boolean result = smsChannelService.updateSmsChannel(smsChannel);
        return Result.result(result);
    }

    /**
    * 删除短信渠道表
    *
    * @param smsChannelId
    * @return
    */
    @PostMapping("/delete/{smsChannelId}")
    @ApiOperation(value = "删除短信渠道表")
    @ApiImplicitParam(paramType = "path", name = "smsChannelId", value = "短信渠道表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("smsChannelId") Long smsChannelId) {
        if (null == smsChannelId) {
            return Result.error("ID不能为空");
        }
        boolean result = smsChannelService.deleteSmsChannel(smsChannelId);
        return Result.result(result);
    }

    /**
    * 批量删除短信渠道表
    *
    * @param smsChannelIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除短信渠道表")
    @ApiImplicitParam(name = "smsChannelIds", value = "短信渠道表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> smsChannelIds) {
        if (CollectionUtils.isEmpty(smsChannelIds)) {
            return Result.error("短信渠道表ID列表不能为空");
        }
        boolean result = smsChannelService.batchDeleteSmsChannel(smsChannelIds);
        return Result.result(result);
    }
     /**
     * 修改短信渠道表状态
     *
     * @param smsChannelId
     * @param channelStatus
     * @return
     */
     @PostMapping("/status/{smsChannelId}/{channelStatus}")
     @ApiOperation(value = "修改短信渠道表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "smsChannelId", value = "短信渠道表ID", required = true, dataTypeClass = Long.class, paramType = "path"),
     @ApiImplicitParam(name = "channelStatus", value = "短信渠道表状态", required = true, dataTypeClass = Integer.class, paramType = "path") })
     public Result<?> updateStatus(@PathVariable("smsChannelId") Long smsChannelId,
         @PathVariable("channelStatus") Integer channelStatus) {

         if (null == smsChannelId || StringUtils.isEmpty(channelStatus)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateSmsChannelDTO smsChannel = new UpdateSmsChannelDTO();
         smsChannel.setId(smsChannelId);
         smsChannel.setChannelStatus(channelStatus);
         boolean result = smsChannelService.updateSmsChannel(smsChannel);
         return Result.result(result);
     }


    /**
    * 批量导出短信渠道表数据
    * @param response
    * @param querySmsChannelDTO
    * @throws IOException
    */
    @GetMapping("/download")
    @ApiOperation("批量导出短信渠道表数据")
    public void download(HttpServletResponse response, QuerySmsChannelDTO querySmsChannelDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("短信渠道表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<SmsChannelDTO> smsChannelList = smsChannelService.querySmsChannelList(querySmsChannelDTO);
        List<SmsChannelExport> smsChannelExportList = new ArrayList<>();
        for (SmsChannelDTO smsChannelDTO : smsChannelList) {
           SmsChannelExport smsChannelExport = BeanCopierUtils.copyByClass(smsChannelDTO, SmsChannelExport.class);
           smsChannelExportList.add(smsChannelExport);
        }
        String sheetName = "短信渠道表数据列表";
        EasyExcel.write(response.getOutputStream(), SmsChannelExport.class).sheet(sheetName).doWrite(smsChannelExportList);
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
