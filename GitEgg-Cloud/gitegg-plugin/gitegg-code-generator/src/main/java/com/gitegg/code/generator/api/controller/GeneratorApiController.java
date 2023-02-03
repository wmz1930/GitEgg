package com.gitegg.code.generator.api.controller;

import java.util.ArrayList;
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
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import com.gitegg.code.generator.api.entity.*;
import com.gitegg.code.generator.api.dto.*;

import com.gitegg.code.generator.api.service.IGeneratorApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import com.gitegg.code.generator.api.bo.*;
import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* <p>
* 接口配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-12-12
*/
@Slf4j
@RestController
@RequestMapping("/code/generator/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "GeneratorApiController|接口配置表前端控制器", tags = {"接口配置表"})
@RefreshScope
public class GeneratorApiController {

    private final IGeneratorApiService generatorApiService;

    /**
    * 查询接口配置表列表
    *
    * @param queryGeneratorApiDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询接口配置表列表")
    public Result<Page<GeneratorApiDTO>> list(QueryGeneratorApiDTO queryGeneratorApiDTO, Page<GeneratorApiDTO> page) {
        Page<GeneratorApiDTO> pageGeneratorApi = generatorApiService.queryGeneratorApiList(page, queryGeneratorApiDTO);
        return Result.data(pageGeneratorApi);
    }
    
    /**
     * 查询所有接口配置列表
     * @param queryGeneratorApiDTO
     * @return
     */
    @GetMapping(value = "/all")
    @ApiOperation(value = "查询所有接口配置列表")
    public Result<List<GeneratorApiDTO>> listAll(QueryGeneratorApiDTO queryGeneratorApiDTO) {
        List<GeneratorApiDTO> pageGeneratorApiList = generatorApiService.queryGeneratorApiList(queryGeneratorApiDTO);
        return Result.data(pageGeneratorApiList);
    }

    /**
    * 查询接口配置表详情
    *
    * @param queryGeneratorApiDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询接口配置表详情")
    public Result<?> query(QueryGeneratorApiDTO queryGeneratorApiDTO) {
        GeneratorApiDTO generatorApiDTO = generatorApiService.queryGeneratorApi(queryGeneratorApiDTO);
        return Result.data(generatorApiDTO);
    }

    /**
    * 添加接口配置表
    *
    * @param generatorApi
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加接口配置表")
    public Result<?> create(@RequestBody CreateGeneratorApiDTO generatorApi) {
        boolean result = generatorApiService.createGeneratorApi(generatorApi);
        return Result.result(result);
    }

    /**
    * 修改接口配置表
    *
    * @param generatorApi
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新接口配置表")
    public Result<?> update(@RequestBody UpdateGeneratorApiDTO generatorApi) {
        boolean result = generatorApiService.updateGeneratorApi(generatorApi);
        return Result.result(result);
    }

    /**
    * 删除接口配置表
    *
    * @param generatorApiId
    * @return
    */
    @PostMapping("/delete/{generatorApiId}")
    @ApiOperation(value = "删除接口配置表")
    @ApiImplicitParam(paramType = "path", name = "generatorApiId", value = "接口配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("generatorApiId") Long generatorApiId) {
        if (null == generatorApiId) {
            return Result.error("ID不能为空");
        }
        boolean result = generatorApiService.deleteGeneratorApi(generatorApiId);
        return Result.result(result);
    }

    /**
    * 批量删除接口配置表
    *
    * @param generatorApiIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除接口配置表")
    @ApiImplicitParam(name = "generatorApiIds", value = "接口配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> generatorApiIds) {
        if (CollectionUtils.isEmpty(generatorApiIds)) {
            return Result.error("接口配置表ID列表不能为空");
        }
        boolean result = generatorApiService.batchDeleteGeneratorApi(generatorApiIds);
        return Result.result(result);
    }
     /**
     * 修改接口配置表状态
     *
     * @param generatorApiId
     * @param apiStatus
     * @return
     */
     @PostMapping("/status/{generatorApiId}/{apiStatus}")
     @ApiOperation(value = "修改接口配置表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "generatorApiId", value = "接口配置表ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "apiStatus", value = "接口配置表状态", required = true, dataType = "Integer", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("generatorApiId") Long generatorApiId,
         @PathVariable("apiStatus") Integer apiStatus) {

         if (null == generatorApiId || StringUtils.isEmpty(apiStatus)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateGeneratorApiDTO generatorApi = new UpdateGeneratorApiDTO();
         generatorApi.setId(generatorApiId);
         generatorApi.setApiStatus(apiStatus);
         boolean result = generatorApiService.updateGeneratorApi(generatorApi);
         return Result.result(result);
     }

    /**
    * 校验接口配置表是否存在
    *
    * @param generatorApi
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验接口配置表是否存在", notes = "校验接口配置表是否存在")
    public Result<Boolean> checkGeneratorApiExist(@RequestBody CheckExistDTO generatorApi) {
        String field = generatorApi.getCheckField();
        String value = generatorApi.getCheckValue();
        QueryWrapper<GeneratorApi> generatorApiQueryWrapper = new QueryWrapper<>();
        generatorApiQueryWrapper.eq(field, value);
        if(null != generatorApi.getId()) {
            generatorApiQueryWrapper.ne("id", generatorApi.getId());
        }
        int count = generatorApiService.count(generatorApiQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 批量导出接口配置表数据
     * @param response
     * @param queryGeneratorApiDTO
     */
    @PostMapping("/export")
    @ApiOperation("导出数据")
    public void exportGeneratorApiList(HttpServletResponse response, @RequestBody QueryGeneratorApiDTO queryGeneratorApiDTO) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户数据列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<GeneratorApiExportBO> generatorApiExportList = generatorApiService.exportGeneratorApiList(queryGeneratorApiDTO);
            String sheetName = "用户数据列表";
            EasyExcel.write(response.getOutputStream(), GeneratorApiExportBO.class).sheet(sheetName).doWrite(generatorApiExportList);
        } catch (Exception e) {
            log.error("批量导出用户数据时发生错误:{}", e);
            throw new BusinessException("批量导出失败:" + e);
        }

    }

    /**
     * 批量上传接口配置表数据
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation("批量上传接口配置表数据")
    public Result<?> importGeneratorApiList(@RequestParam("uploadFile") MultipartFile file) {
        boolean importSuccess = generatorApiService.importGeneratorApiList(file);
        return Result.result(importSuccess);
    }

    /**
    * 下载接口配置表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("接口配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "接口配置表数据列表";
            EasyExcel.write(response.getOutputStream(), GeneratorApiImportBO.class).sheet(sheetName).doWrite(new ArrayList<>());
        } catch (Exception e) {
            log.error("下载接口配置表批量模板时发生错误:{}", e);
            throw new BusinessException("下载批量模板失败:" + e);
        }
    }
 }
