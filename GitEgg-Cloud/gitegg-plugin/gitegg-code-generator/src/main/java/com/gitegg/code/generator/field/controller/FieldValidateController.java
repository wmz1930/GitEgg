package com.gitegg.code.generator.field.controller;


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
import com.gitegg.code.generator.field.entity.FieldValidate;
import com.gitegg.code.generator.field.dto.FieldValidateDTO;
import com.gitegg.code.generator.field.dto.CreateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.UpdateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.QueryFieldValidateDTO;

import com.gitegg.code.generator.field.service.IFieldValidateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
* <p>
* 字段校验规则配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-03 12:01:04
*/
@RestController
@RequestMapping("/code/generator/field/validate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "FieldValidateController|字段校验规则配置表前端控制器")
@RefreshScope
public class FieldValidateController {

    private final IFieldValidateService fieldValidateService;

    /**
    * 查询字段校验规则配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询字段校验规则配置表列表")
    public PageResult<FieldValidateDTO> list(QueryFieldValidateDTO queryFieldValidateDTO, Page<FieldValidateDTO> page) {
        Page<FieldValidateDTO> pageFieldValidate = fieldValidateService.queryFieldValidateList(page, queryFieldValidateDTO);
        return PageResult.data(pageFieldValidate.getTotal(), pageFieldValidate.getRecords());
    }

    /**
    * 查询字段校验规则配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询字段校验规则配置表详情")
    public Result<?> query(QueryFieldValidateDTO queryFieldValidateDTO) {
        FieldValidateDTO fieldValidateDTO = fieldValidateService.queryFieldValidate(queryFieldValidateDTO);
        return Result.data(fieldValidateDTO);
    }

    /**
    * 添加字段校验规则配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加字段校验规则配置表")
    public Result<?> create(@RequestBody CreateFieldValidateDTO fieldValidate) {
        boolean result = fieldValidateService.createFieldValidate(fieldValidate);
        return Result.result(result);
    }

    /**
    * 修改字段校验规则配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新字段校验规则配置表")
    public Result<?> update(@RequestBody UpdateFieldValidateDTO fieldValidate) {
        boolean result = fieldValidateService.updateFieldValidate(fieldValidate);
        return Result.result(result);
    }

    /**
    * 删除字段校验规则配置表
    */
    @PostMapping("/delete/{fieldValidateId}")
    @ApiOperation(value = "删除字段校验规则配置表")
    @ApiImplicitParam(paramType = "path", name = "fieldValidateId", value = "字段校验规则配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("fieldValidateId") Long fieldValidateId) {
        if (null == fieldValidateId) {
            return Result.error("ID不能为空");
        }
        boolean result = fieldValidateService.deleteFieldValidate(fieldValidateId);
        return Result.result(result);
    }

    /**
    * 批量删除字段校验规则配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除字段校验规则配置表")
    @ApiImplicitParam(name = "fieldValidateIds", value = "字段校验规则配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> fieldValidateIds) {
        if (CollectionUtils.isEmpty(fieldValidateIds)) {
            return Result.error("字段校验规则配置表ID列表不能为空");
        }
        boolean result = fieldValidateService.batchDeleteFieldValidate(fieldValidateIds);
        return Result.result(result);
    }

    /**
    * 校验字段校验规则配置表是否存在
    *
    * @param fieldValidate
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字段校验规则配置表是否存在", notes = "校验字段校验规则配置表是否存在")
    public Result<Boolean> checkFieldValidateExist(CheckExistDTO fieldValidate) {
        String field = fieldValidate.getCheckField();
        String value = fieldValidate.getCheckValue();
        QueryWrapper<FieldValidate> fieldValidateQueryWrapper = new QueryWrapper<>();
        fieldValidateQueryWrapper.eq(field, value);
        if(null != fieldValidate.getId()) {
            fieldValidateQueryWrapper.ne("id", fieldValidate.getId());
        }
        int count = fieldValidateService.count(fieldValidateQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
