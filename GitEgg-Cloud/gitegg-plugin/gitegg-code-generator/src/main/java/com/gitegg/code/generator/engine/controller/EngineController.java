package com.gitegg.code.generator.engine.controller;


import cn.hutool.core.util.ArrayUtil;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.engine.dto.QueryTableFieldDTO;
import com.gitegg.code.generator.engine.dto.TableDTO;
import com.gitegg.code.generator.engine.service.IEngineService;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 字段属性表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-03 11:55:34
*/
@RestController
@RequestMapping("/code/generator/engine")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "EngineController|字段属性前端控制器")
@RefreshScope
public class EngineController {

    private final IEngineService engineService;

    /**
    * 查询数据源下所有的表
    */
    @GetMapping("/table/list")
    @ApiOperation(value = "查询数据源下所有的表")
    public Result<?> listTable(QueryConfigDTO queryConfigDTO) {
        List<TableDTO> tableDTOS = engineService.queryTableList(queryConfigDTO);
        return Result.data(tableDTOS);
    }

    /**
     * 查询数据源下所有的表字段
     */
    @GetMapping("/table/field/list")
    @ApiOperation(value = "查询表字段信息")
    public Result<?> listField(String datasourceId, String tableName) {
        List<String> tableNames = new ArrayList<>();
        tableNames.add(tableName);
        List<TableInfo> tableInfo = engineService.queryTableFields(datasourceId, tableNames);
        return Result.data(tableInfo);
    }

    /**
     * 执行代码生成操作
     */
    @GetMapping("/process/generate/code")
    @ApiOperation(value = "查询表字段信息")
    public Result<?> processGenerateCode(QueryConfigDTO queryConfigDTO) {
        engineService.processGenerateCode(queryConfigDTO);
        return Result.success();
    }

 }
