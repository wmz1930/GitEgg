package com.gitegg.code.generator.datasource.controller;


import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.datasource.dto.*;
import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasource;
import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasourceExport;
import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasourceImport;
import com.gitegg.code.generator.datasource.service.ICodeGenerationDatasourceService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 数据源配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-08-18 16:39:49
*/
@RestController
@RequestMapping("/code/generation/datasource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "CodeGenerationDatasourceController|数据源配置表前端控制器")
@RefreshScope
public class CodeGenerationDatasourceController {

    private final ICodeGenerationDatasourceService codeGenerationDatasourceService;

    /**
    * 查询数据源配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询数据源配置表列表")
    public PageResult<CodeGenerationDatasourceDTO> list(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO, Page<CodeGenerationDatasourceDTO> page) {
        Page<CodeGenerationDatasourceDTO> pageCodeGenerationDatasource = codeGenerationDatasourceService.queryCodeGenerationDatasourceList(page, queryCodeGenerationDatasourceDTO);
        return PageResult.data(pageCodeGenerationDatasource.getTotal(), pageCodeGenerationDatasource.getRecords());
    }

    /**
    * 查询数据源配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询数据源配置表详情")
    public Result<?> query(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO) {
        CodeGenerationDatasourceDTO codeGenerationDatasourceDTO = codeGenerationDatasourceService.queryCodeGenerationDatasource(queryCodeGenerationDatasourceDTO);
        return Result.data(codeGenerationDatasourceDTO);
    }

    /**
    * 添加数据源配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加数据源配置表")
    public Result<?> create(@RequestBody CreateCodeGenerationDatasourceDTO codeGenerationDatasource) {
        boolean result = codeGenerationDatasourceService.createCodeGenerationDatasource(codeGenerationDatasource);
        return Result.result(result);
    }

    /**
    * 修改数据源配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新数据源配置表")
    public Result<?> update(@RequestBody UpdateCodeGenerationDatasourceDTO codeGenerationDatasource) {
        boolean result = codeGenerationDatasourceService.updateCodeGenerationDatasource(codeGenerationDatasource);
        return Result.result(result);
    }

    /**
    * 删除数据源配置表
    */
    @PostMapping("/delete/{codeGenerationDatasourceId}")
    @ApiOperation(value = "删除数据源配置表")
    @ApiImplicitParam(paramType = "path", name = "codeGenerationDatasourceId", value = "数据源配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("codeGenerationDatasourceId") Long codeGenerationDatasourceId) {
        if (null == codeGenerationDatasourceId) {
            return Result.error("ID不能为空");
        }
        boolean result = codeGenerationDatasourceService.deleteCodeGenerationDatasource(codeGenerationDatasourceId);
        return Result.result(result);
    }

    /**
    * 批量删除数据源配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除数据源配置表")
    @ApiImplicitParam(name = "codeGenerationDatasourceIds", value = "数据源配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> codeGenerationDatasourceIds) {
        if (CollectionUtils.isEmpty(codeGenerationDatasourceIds)) {
            return Result.error("数据源配置表ID列表不能为空");
        }
        boolean result = codeGenerationDatasourceService.batchDeleteCodeGenerationDatasource(codeGenerationDatasourceIds);
        return Result.result(result);
    }

    /**
    * 校验数据源配置表是否存在
    *
    * @param codeGenerationDatasource
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验数据源配置是否存在", notes = "校验数据源配置是否存在")
    public Result<Boolean> checkCodeGenerationDatasourceExist(CheckExistDTO codeGenerationDatasource) {
        String field = codeGenerationDatasource.getCheckField();
        String value = codeGenerationDatasource.getCheckValue();
        QueryWrapper<CodeGenerationDatasource> codeGenerationDatasourceQueryWrapper = new QueryWrapper<>();
        codeGenerationDatasourceQueryWrapper.eq(field, value);
        if(null != codeGenerationDatasource.getId()) {
            codeGenerationDatasourceQueryWrapper.ne("id", codeGenerationDatasource.getId());
        }
        int count = codeGenerationDatasourceService.count(codeGenerationDatasourceQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 批量导出数据
     * @param response
     * @param queryCodeGenerationDatasourceDTO
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据源列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<CodeGenerationDatasourceDTO> dataSourceList = codeGenerationDatasourceService.queryCodeGenerationDatasourceList(queryCodeGenerationDatasourceDTO);
        List<CodeGenerationDatasourceExport> companyExportList = new ArrayList<>();
        for (CodeGenerationDatasourceDTO codeGenerationDatasourceDTO : dataSourceList) {
            CodeGenerationDatasourceExport companyExport = BeanCopierUtils.copyByClass(codeGenerationDatasourceDTO, CodeGenerationDatasourceExport.class);
            companyExportList.add(companyExport);
        }
        String sheetName = "数据源列表";
        EasyExcel.write(response.getOutputStream(), CodeGenerationDatasourceExport.class).sheet(sheetName).doWrite(companyExportList);
    }

    /**
     * 下载导入模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据源导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "数据源列表";
        EasyExcel.write(response.getOutputStream(), CodeGenerationDatasourceImport.class).sheet(sheetName).doWrite(null);
    }

    /**
     * 上传数据
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
        List<CodeGenerationDatasourceImport> datasourceImportList =  EasyExcel.read(file.getInputStream(), CodeGenerationDatasourceImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(datasourceImportList))
        {
            List<CodeGenerationDatasource> codeGenerationDatasourceList = new ArrayList<>();
            datasourceImportList.stream().forEach(datasourceImport-> {
                codeGenerationDatasourceList.add(BeanCopierUtils.copyByClass(datasourceImport, CodeGenerationDatasource.class));
            });
            codeGenerationDatasourceService.saveBatch(codeGenerationDatasourceList);
        }
        return Result.success();
    }

 }
