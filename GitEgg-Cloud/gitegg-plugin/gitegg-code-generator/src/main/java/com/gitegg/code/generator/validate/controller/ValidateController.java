package com.gitegg.code.generator.validate.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.code.generator.validate.entity.*;
import com.gitegg.code.generator.validate.dto.*;

import com.gitegg.code.generator.validate.service.IValidateService;

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
* 字段校验规则配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-10-15
*/
@RestController
@RequestMapping("/code/generator/validate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "ValidateController|字段校验规则配置表前端控制器", tags = {"字段校验规则配置"})
@RefreshScope
public class ValidateController {

    private final IValidateService validateService;

    /**
    * 查询字段校验规则配置表列表
    *
    * @param queryValidateDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询字段校验规则配置表列表")
    public PageResult<ValidateDTO> list(QueryValidateDTO queryValidateDTO, Page<ValidateDTO> page) {
        Page<ValidateDTO> pageValidate = validateService.queryValidateList(page, queryValidateDTO);
        return PageResult.data(pageValidate.getTotal(), pageValidate.getRecords());
    }

    /**
     * 查询所有字段校验规则配置
     *
     * @param queryValidateDTO
     * @return
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询字段校验规则配置表列表")
    public Result<?> listAll(QueryValidateDTO queryValidateDTO) {
        List<ValidateDTO> validateList = validateService.queryValidateList(queryValidateDTO);
        return Result.data(validateList);
    }

    /**
    * 查询字段校验规则配置表详情
    *
    * @param queryValidateDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询字段校验规则配置表详情")
    public Result<?> query(QueryValidateDTO queryValidateDTO) {
        ValidateDTO validateDTO = validateService.queryValidate(queryValidateDTO);
        return Result.data(validateDTO);
    }

    /**
    * 添加字段校验规则配置表
    *
    * @param validate
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加字段校验规则配置表")
    public Result<?> create(@RequestBody CreateValidateDTO validate) {
        boolean result = validateService.createValidate(validate);
        return Result.result(result);
    }

    /**
    * 修改字段校验规则配置表
    *
    * @param validate
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新字段校验规则配置表")
    public Result<?> update(@RequestBody UpdateValidateDTO validate) {
        boolean result = validateService.updateValidate(validate);
        return Result.result(result);
    }

    /**
    * 删除字段校验规则配置表
    *
    * @param validateId
    * @return
    */
    @PostMapping("/delete/{validateId}")
    @ApiOperation(value = "删除字段校验规则配置表")
    @ApiImplicitParam(paramType = "path", name = "validateId", value = "字段校验规则配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("validateId") Long validateId) {
        if (null == validateId) {
            return Result.error("ID不能为空");
        }
        boolean result = validateService.deleteValidate(validateId);
        return Result.result(result);
    }

    /**
    * 批量删除字段校验规则配置表
    *
    * @param validateIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除字段校验规则配置表")
    @ApiImplicitParam(name = "validateIds", value = "字段校验规则配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> validateIds) {
        if (CollectionUtils.isEmpty(validateIds)) {
            return Result.error("字段校验规则配置表ID列表不能为空");
        }
        boolean result = validateService.batchDeleteValidate(validateIds);
        return Result.result(result);
    }
     /**
     * 修改字段校验规则配置表状态
     *
     * @param validateId
     * @param status
     * @return
     */
     @PostMapping("/status/{validateId}/{status}")
     @ApiOperation(value = "修改字段校验规则配置表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "validateId", value = "字段校验规则配置表ID", required = true, dataTypeClass = Long.class, paramType = "path"),
     @ApiImplicitParam(name = "status", value = "字段校验规则配置表状态", required = true, dataTypeClass = Integer.class, paramType = "path") })
     public Result<?> updateStatus(@PathVariable("validateId") Long validateId,
         @PathVariable("status") Integer status) {

         if (null == validateId || StringUtils.isEmpty(status)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateValidateDTO validate = new UpdateValidateDTO();
         validate.setId(validateId);
         validate.setStatus(status);
         boolean result = validateService.updateValidate(validate);
         return Result.result(result);
     }

    /**
    * 校验字段校验规则配置表是否存在
    *
    * @param validate
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验字段校验规则配置表是否存在", notes = "校验字段校验规则配置表是否存在")
    public Result<Boolean> checkValidateExist(CheckExistDTO validate) {
        String field = validate.getCheckField();
        String value = validate.getCheckValue();
        QueryWrapper<Validate> validateQueryWrapper = new QueryWrapper<>();
        validateQueryWrapper.eq(field, value);
        if(null != validate.getId()) {
            validateQueryWrapper.ne("id", validate.getId());
        }
        int count = validateService.count(validateQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
    * 批量导出字段校验规则配置表数据
    * @param response
    * @param queryValidateDTO
    * @throws IOException
    */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryValidateDTO queryValidateDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("字段校验规则配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<ValidateDTO> validateList = validateService.queryValidateList(queryValidateDTO);
        List<ValidateExport> validateExportList = new ArrayList<>();
        for (ValidateDTO validateDTO : validateList) {
           ValidateExport validateExport = BeanCopierUtils.copyByClass(validateDTO, ValidateExport.class);
           validateExportList.add(validateExport);
        }
        String sheetName = "字段校验规则配置表数据列表";
        EasyExcel.write(response.getOutputStream(), ValidateExport.class).sheet(sheetName).doWrite(validateExportList);
    }

    /**
    * 批量上传字段校验规则配置表数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<ValidateImport> validateImportList =  EasyExcel.read(file.getInputStream(), ValidateImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(validateImportList))
        {
            List<Validate> validateList = new ArrayList<>();
            validateImportList.stream().forEach(validateImport-> {
               validateList.add(BeanCopierUtils.copyByClass(validateImport, Validate.class));
            });
            validateService.saveBatch(validateList);
        }
        return Result.success();
    }

    /**
    * 下载字段校验规则配置表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("字段校验规则配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "字段校验规则配置表数据列表";
        EasyExcel.write(response.getOutputStream(), ValidateImport.class).sheet(sheetName).doWrite(new ArrayList<>());
    }
 }
