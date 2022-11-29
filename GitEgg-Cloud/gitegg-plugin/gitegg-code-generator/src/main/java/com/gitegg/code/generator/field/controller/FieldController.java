package com.gitegg.code.generator.field.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.field.dto.*;
import com.gitegg.code.generator.field.entity.Field;
import com.gitegg.code.generator.field.service.IFieldService;
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
* 字段属性配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-09-03 11:55:34
*/
@RestController
@RequestMapping("/code/generator/field")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "FieldController|字段属性配置表前端控制器", tags = {"字段属性配置"})
@RefreshScope
public class FieldController {

    private final IFieldService fieldService;

    /**
    * 查询字段属性配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询字段属性配置表列表")
    public Result<Page<FieldDTO>> list(QueryFieldDTO queryFieldDTO, Page<FieldDTO> page) {
        Page<FieldDTO> pageField = fieldService.queryFieldList(page, queryFieldDTO);
        return Result.data(pageField);
    }

    /**
     * 查询所有字段属性配置表列表
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询字段属性配置表列表")
    public Result<?> listAll(QueryFieldDTO queryFieldDTO) {
        List<TableFieldDTO> fieldList = fieldService.queryTableFieldList(queryFieldDTO);
        return Result.data(fieldList);
    }

    /**
     * 字段配置的新增和更新，统一入口
     */
    @PostMapping("/edit")
    @ApiOperation(value = "字段配置的新增和更新")
    public Result<?> edit(@RequestBody List<Field> fieldList) {
        boolean result = fieldService.editField(fieldList);
        return Result.result(result);
    }

    /**
    * 查询字段属性配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询字段属性配置表详情")
    public Result<?> query(QueryFieldDTO queryFieldDTO) {
        FieldDTO fieldDTO = fieldService.queryField(queryFieldDTO);
        return Result.data(fieldDTO);
    }

    /**
    * 添加字段属性配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加字段属性配置表")
    public Result<?> create(@RequestBody CreateFieldDTO field) {
        boolean result = fieldService.createField(field);
        return Result.result(result);
    }

    /**
    * 修改字段属性配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新字段属性配置表")
    public Result<?> update(@RequestBody UpdateFieldDTO field) {
        boolean result = fieldService.updateField(field);
        return Result.result(result);
    }

    /**
    * 删除字段属性配置表
    */
    @PostMapping("/delete/{fieldId}")
    @ApiOperation(value = "删除字段属性配置表")
    @ApiImplicitParam(paramType = "path", name = "fieldId", value = "字段属性配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("fieldId") Long fieldId) {
        if (null == fieldId) {
            return Result.error("ID不能为空");
        }
        boolean result = fieldService.deleteField(fieldId);
        return Result.result(result);
    }

    /**
    * 批量删除字段属性配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除字段属性配置表")
    @ApiImplicitParam(name = "fieldIds", value = "字段属性配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> fieldIds) {
        if (CollectionUtils.isEmpty(fieldIds)) {
            return Result.error("字段属性配置表ID列表不能为空");
        }
        boolean result = fieldService.batchDeleteField(fieldIds);
        return Result.result(result);
    }

    /**
    * 校验字段属性配置表是否存在
    *
    * @param tableField
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字段属性配置表是否存在", notes = "校验字段属性配置表是否存在")
    public Result<Boolean> checkFieldExist(CheckExistDTO tableField) {
        String field = tableField.getCheckField();
        String value = tableField.getCheckValue();
        QueryWrapper<Field> fieldQueryWrapper = new QueryWrapper<>();
        fieldQueryWrapper.eq(field, value);
        if(null != tableField.getId()) {
            fieldQueryWrapper.ne("id", tableField.getId());
        }
        int count = fieldService.count(fieldQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }
 }
