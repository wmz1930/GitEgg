package com.gitegg.code.generator.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.config.dto.ConfigDTO;
import com.gitegg.code.generator.config.dto.CreateConfigDTO;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.config.dto.UpdateConfigDTO;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.service.IConfigService;
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
* 代码生成配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-02 18:09:28
*/
@RestController
@RequestMapping("/code/generator/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "ConfigController|代码生成配置表前端控制器")
@RefreshScope
public class ConfigController {

    private final IConfigService configService;

    /**
    * 查询代码生成配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询代码生成配置表列表")
    public PageResult<ConfigDTO> list(QueryConfigDTO queryConfigDTO, Page<ConfigDTO> page) {
        Page<ConfigDTO> pageConfig = configService.queryConfigList(page, queryConfigDTO);
        return PageResult.data(pageConfig.getTotal(), pageConfig.getRecords());
    }

    /**
    * 查询代码生成配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询代码生成配置表详情")
    public Result<?> query(QueryConfigDTO queryConfigDTO) {
        ConfigDTO configDTO = configService.queryConfig(queryConfigDTO);
        return Result.data(configDTO);
    }

    /**
    * 添加代码生成配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加代码生成配置表")
    public Result<?> create(@RequestBody CreateConfigDTO config) {
        boolean result = configService.createConfig(config);
        return Result.result(result);
    }

    /**
    * 修改代码生成配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新代码生成配置表")
    public Result<?> update(@RequestBody UpdateConfigDTO config) {
        boolean result = configService.updateConfig(config);
        return Result.result(result);
    }

    /**
    * 删除代码生成配置表
    */
    @PostMapping("/delete/{configId}")
    @ApiOperation(value = "删除代码生成配置表")
    @ApiImplicitParam(paramType = "path", name = "configId", value = "代码生成配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("configId") Long configId) {
        if (null == configId) {
            return Result.error("ID不能为空");
        }
        boolean result = configService.deleteConfig(configId);
        return Result.result(result);
    }

    /**
    * 批量删除代码生成配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除代码生成配置表")
    @ApiImplicitParam(name = "configIds", value = "代码生成配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> configIds) {
        if (CollectionUtils.isEmpty(configIds)) {
            return Result.error("代码生成配置表ID列表不能为空");
        }
        boolean result = configService.batchDeleteConfig(configIds);
        return Result.result(result);
    }

    /**
    * 校验代码生成配置表是否存在
    *
    * @param config
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验代码生成配置表是否存在", notes = "校验代码生成配置表是否存在")
    public Result<Boolean> checkConfigExist(CheckExistDTO config) {
        String field = config.getCheckField();
        String value = config.getCheckValue();
        QueryWrapper<Config> configQueryWrapper = new QueryWrapper<>();
        configQueryWrapper.eq(field, value);
        if(null != config.getId()) {
            configQueryWrapper.ne("id", config.getId());
        }
        int count = configService.count(configQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 复制代码生成配置
     */
    @GetMapping("/copy")
    @ApiOperation(value = "复制代码生成配置")
    public Result<?> copy(QueryConfigDTO queryConfigDTO) {
        boolean result = configService.copyConfig(queryConfigDTO);
        if (result){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
