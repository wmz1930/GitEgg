package com.gitegg.code.generator.join.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.join.dto.CreateTableJoinDTO;
import com.gitegg.code.generator.join.dto.QueryTableJoinDTO;
import com.gitegg.code.generator.join.dto.TableJoinDTO;
import com.gitegg.code.generator.join.dto.UpdateTableJoinDTO;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
* <p>
* 多表查询时的联合表配置 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-03 11:38:07
*/
@RestController
@RequestMapping("/code/generator/table/join")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "TableJoinController|多表查询时的联合表配置前端控制器")
@RefreshScope
public class TableJoinController {

    private final ITableJoinService tableJoinService;

    /**
    * 查询多表查询时的联合表配置列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询多表查询时的联合表配置列表")
    public PageResult<TableJoinDTO> list(QueryTableJoinDTO queryTableJoinDTO, Page<TableJoinDTO> page) {
        Page<TableJoinDTO> pageTableJoin = tableJoinService.queryTableJoinList(page, queryTableJoinDTO);
        return PageResult.data(pageTableJoin.getTotal(), pageTableJoin.getRecords());
    }

    /**
    * 查询多表查询时的联合表配置详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询多表查询时的联合表配置详情")
    public Result<?> query(QueryTableJoinDTO queryTableJoinDTO) {
        TableJoinDTO tableJoinDTO = tableJoinService.queryTableJoin(queryTableJoinDTO);
        return Result.data(tableJoinDTO);
    }

    /**
    * 添加多表查询时的联合表配置
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加多表查询时的联合表配置")
    public Result<?> create(@RequestBody CreateTableJoinDTO tableJoin) {
        boolean result = tableJoinService.createTableJoin(tableJoin);
        return Result.result(result);
    }

    /**
    * 修改多表查询时的联合表配置
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新多表查询时的联合表配置")
    public Result<?> update(@RequestBody UpdateTableJoinDTO tableJoin) {
        boolean result = tableJoinService.updateTableJoin(tableJoin);
        return Result.result(result);
    }

    /**
    * 删除多表查询时的联合表配置
    */
    @PostMapping("/delete/{tableJoinId}")
    @ApiOperation(value = "删除多表查询时的联合表配置")
    @ApiImplicitParam(paramType = "path", name = "tableJoinId", value = "多表查询时的联合表配置ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("tableJoinId") Long tableJoinId) {
        if (null == tableJoinId) {
            return Result.error("ID不能为空");
        }
        boolean result = tableJoinService.deleteTableJoin(tableJoinId);
        return Result.result(result);
    }

    /**
    * 批量删除多表查询时的联合表配置
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除多表查询时的联合表配置")
    @ApiImplicitParam(name = "tableJoinIds", value = "多表查询时的联合表配置ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> tableJoinIds) {
        if (CollectionUtils.isEmpty(tableJoinIds)) {
            return Result.error("多表查询时的联合表配置ID列表不能为空");
        }
        boolean result = tableJoinService.batchDeleteTableJoin(tableJoinIds);
        return Result.result(result);
    }

    /**
    * 校验多表查询时的联合表配置是否存在
    *
    * @param tableJoin
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验多表查询时的联合表配置是否存在", notes = "校验多表查询时的联合表配置是否存在")
    public Result<Boolean> checkTableJoinExist(CheckExistDTO tableJoin) {
        String field = tableJoin.getCheckField();
        String value = tableJoin.getCheckValue();
        QueryWrapper<TableJoin> tableJoinQueryWrapper = new QueryWrapper<>();
        tableJoinQueryWrapper.eq(field, value);
        if(null != tableJoin.getId()) {
            tableJoinQueryWrapper.ne("id", tableJoin.getId());
        }
        int count = tableJoinService.count(tableJoinQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
