package com.gitegg.service.extension.dfs.controller;


import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.gitegg.service.extension.dfs.entity.Dfs;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.CreateDfsDTO;
import com.gitegg.service.extension.dfs.dto.UpdateDfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;

import com.gitegg.service.extension.dfs.service.IDfsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
* 分布式存储配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-05-06
*/
@RestController
@RequestMapping("/extension/dfs")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DfsController|分布式存储配置表前端控制器")
@RefreshScope
public class DfsController {

    private final IDfsService dfsService;

    /**
    * 查询分布式存储配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询分布式存储配置表列表")
    public PageResult<DfsDTO> list(QueryDfsDTO queryDfsDTO, Page<DfsDTO> page) {
        Page<DfsDTO> pageDfs = dfsService.queryDfsList(page, queryDfsDTO);
        return PageResult.data(pageDfs.getTotal(), pageDfs.getRecords());
    }

    /**
    * 查询分布式存储配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询分布式存储配置表详情")
    public Result<?> query(QueryDfsDTO queryDfsDTO) {
        DfsDTO dfsDTO = dfsService.queryDfs(queryDfsDTO);
        return Result.data(dfsDTO);
    }

    /**
    * 添加分布式存储配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加分布式存储配置表")
    public Result<?> create(@RequestBody CreateDfsDTO dfs) {
        boolean result = dfsService.createDfs(dfs);
        return Result.result(result);
    }

    /**
    * 修改分布式存储配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新分布式存储配置表")
    public Result<?> update(@RequestBody UpdateDfsDTO dfs) {
        boolean result = dfsService.updateDfs(dfs);
        return Result.result(result);
    }

    /**
    * 删除分布式存储配置表
    */
    @PostMapping("/delete/{dfsId}")
    @ApiOperation(value = "删除分布式存储配置表")
    @ApiImplicitParam(paramType = "path", name = "dfsId", value = "分布式存储配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("dfsId") Long dfsId) {
        if (null == dfsId) {
            return Result.error("ID不能为空");
        }
        boolean result = dfsService.deleteDfs(dfsId);
        return Result.result(result);
    }

    /**
    * 批量删除分布式存储配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除分布式存储配置表")
    @ApiImplicitParam(name = "dfsIds", value = "分布式存储配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> dfsIds) {
        if (CollectionUtils.isEmpty(dfsIds)) {
            return Result.error("分布式存储配置表ID列表不能为空");
        }
        boolean result = dfsService.batchDeleteDfs(dfsIds);
        return Result.result(result);
    }

    /**
    * 修改分布式存储配置表状态
    */
    @PostMapping("/status/{dfsId}/{dfsStatus}")
    @ApiOperation(value = "修改分布式存储配置表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "dfsId", value = "分布式存储配置表ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "dfsStatus", value = "分布式存储配置表状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("dfsId") Long dfsId,
            @PathVariable("dfsStatus") Integer dfsStatus) {
        if (null == dfsId || StringUtils.isEmpty(dfsStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateDfsDTO dfs = new UpdateDfsDTO();
        dfs.setId(dfsId);
        dfs.setDfsStatus(dfsStatus);
        boolean result = dfsService.updateDfs(dfs);
        return Result.result(result);
    }

    /**
     * 修改分布式存储配置为默认
     */
    @PostMapping("/default/{dfsId}")
    @ApiOperation(value = "修改分布式存储配置为默认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dfsId", value = "分布式存储配置表ID", required = true, dataType = "Long", paramType = "path")})
    public Result<?> updateDefaultResult(@PathVariable("dfsId") Long dfsId) {
        if (null == dfsId) {
            return Result.error("ID不能为空");
        }
        boolean result = dfsService.updateDfsDefault(dfsId);
        return Result.result(result);
    }

    /**
    * 校验分布式存储配置表是否存在
    *
    * @param dfs
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验分布式存储配置表是否存在", notes = "校验分布式存储配置表是否存在")
    public Result<Boolean> checkDfsExist(CheckExistDTO dfs) {
        String field = dfs.getCheckField();
        String value = dfs.getCheckValue();
        QueryWrapper<Dfs> dfsQueryWrapper = new QueryWrapper<>();
        dfsQueryWrapper.eq(field, value);
        if(null != dfs.getId()) {
            dfsQueryWrapper.ne("id", dfs.getId());
        }
        int count = dfsService.count(dfsQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
