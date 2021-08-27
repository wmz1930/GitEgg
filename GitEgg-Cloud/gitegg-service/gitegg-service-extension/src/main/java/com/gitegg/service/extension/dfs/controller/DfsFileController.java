package com.gitegg.service.extension.dfs.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.dfs.dto.CreateDfsFileDTO;
import com.gitegg.service.extension.dfs.dto.DfsFileDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsFileDTO;
import com.gitegg.service.extension.dfs.dto.UpdateDfsFileDTO;
import com.gitegg.service.extension.dfs.service.IDfsFileService;
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
* 分布式存储文件记录表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-05-08
*/
@RestController
@RequestMapping("/extension/dfs/file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DfsFileController|分布式存储文件记录表前端控制器")
@RefreshScope
public class DfsFileController {

    private final IDfsFileService dfsFileService;

    /**
    * 查询分布式存储文件记录表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询分布式存储文件记录表列表")
    public PageResult<DfsFileDTO> list(QueryDfsFileDTO queryDfsFileDTO, Page<DfsFileDTO> page) {
        Page<DfsFileDTO> pageDfsFile = dfsFileService.queryDfsFileList(page, queryDfsFileDTO);
        return PageResult.data(pageDfsFile.getTotal(), pageDfsFile.getRecords());
    }

    /**
    * 查询分布式存储文件记录表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询分布式存储文件记录表详情")
    public Result<?> query(QueryDfsFileDTO queryDfsFileDTO) {
        DfsFileDTO dfsFileDTO = dfsFileService.queryDfsFile(queryDfsFileDTO);
        return Result.data(dfsFileDTO);
    }

    /**
    * 添加分布式存储文件记录表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加分布式存储文件记录表")
    public Result<?> create(@RequestBody CreateDfsFileDTO dfsFile) {
        boolean result = dfsFileService.createDfsFile(dfsFile);
        return Result.result(result);
    }

    /**
    * 修改分布式存储文件记录表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新分布式存储文件记录表")
    public Result<?> update(@RequestBody UpdateDfsFileDTO dfsFile) {
        boolean result = dfsFileService.updateDfsFile(dfsFile);
        return Result.result(result);
    }

    /**
    * 删除分布式存储文件记录表
    */
    @PostMapping("/delete/{dfsFileId}")
    @ApiOperation(value = "删除分布式存储文件记录表")
    @ApiImplicitParam(paramType = "path", name = "dfsFileId", value = "分布式存储文件记录表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("dfsFileId") Long dfsFileId) {
        if (null == dfsFileId) {
            return Result.error("ID不能为空");
        }
        boolean result = dfsFileService.deleteDfsFile(dfsFileId);
        return Result.result(result);
    }

    /**
    * 批量删除分布式存储文件记录表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除分布式存储文件记录表")
    @ApiImplicitParam(name = "dfsFileIds", value = "分布式存储文件记录表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> dfsFileIds) {
        if (CollectionUtils.isEmpty(dfsFileIds)) {
            return Result.error("分布式存储文件记录表ID列表不能为空");
        }
        boolean result = dfsFileService.batchDeleteDfsFile(dfsFileIds);
        return Result.result(result);
    }

    /**
    * 修改分布式存储文件记录表状态
    */
    @PostMapping("/status/{dfsFileId}/{dfsFileStatus}")
    @ApiOperation(value = "修改分布式存储文件记录表状态")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "dfsFileId", value = "分布式存储文件记录表ID", required = true, dataType = "Long", paramType = "path"),
    @ApiImplicitParam(name = "dfsFileStatus", value = "分布式存储文件记录表状态", required = true, dataType = "Integer", paramType = "path") })
    public Result<?> updateStatus(@PathVariable("dfsFileId") Long dfsFileId,
            @PathVariable("dfsFileStatus") Integer dfsFileStatus) {
        if (null == dfsFileId || StringUtils.isEmpty(dfsFileStatus)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateDfsFileDTO dfsFile = new UpdateDfsFileDTO();
        dfsFile.setId(dfsFileId);
        dfsFile.setFileStatus(dfsFileStatus);
        boolean result = dfsFileService.updateDfsFile(dfsFile);
        return Result.result(result);
    }
 }
