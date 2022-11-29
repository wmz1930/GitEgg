package com.gitegg.code.generator.datasource.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.code.generator.datasource.dto.*;
import com.gitegg.code.generator.datasource.entity.Datasource;
import com.gitegg.code.generator.datasource.entity.DatasourceExport;
import com.gitegg.code.generator.datasource.entity.DatasourceImport;
import com.gitegg.code.generator.datasource.service.IDatasourceService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
@RequestMapping("/code/generator/datasource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "DatasourceController|数据源配置表前端控制器", tags = {"数据源配置"})
@RefreshScope
public class DatasourceController {

    private final IDatasourceService datasourceService;

    /**
    * 查询数据源配置表列表
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询数据源配置表列表")
    public Result<Page<DatasourceDTO>> list(QueryDatasourceDTO queryDatasourceDTO, Page<DatasourceDTO> page) {
        Page<DatasourceDTO> pageDatasource = datasourceService.queryDatasourceList(page, queryDatasourceDTO);
        return Result.data(pageDatasource);
    }

    /**
    * 查询数据源配置表详情
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询数据源配置表详情")
    public Result<?> query(QueryDatasourceDTO queryDatasourceDTO) {
        DatasourceDTO datasourceDTO = datasourceService.queryDatasource(queryDatasourceDTO);
        return Result.data(datasourceDTO);
    }

    /**
    * 添加数据源配置表
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加数据源配置表")
    public Result<?> create(@RequestBody CreateDatasourceDTO datasource) {
        boolean result = datasourceService.createDatasource(datasource);
        return Result.result(result);
    }

    /**
    * 修改数据源配置表
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新数据源配置表")
    public Result<?> update(@RequestBody UpdateDatasourceDTO datasource) {
        boolean result = datasourceService.updateDatasource(datasource);
        return Result.result(result);
    }

    /**
    * 删除数据源配置表
    */
    @PostMapping("/delete/{datasourceId}")
    @ApiOperation(value = "删除数据源配置表")
    @ApiImplicitParam(paramType = "path", name = "datasourceId", value = "数据源配置表ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("datasourceId") Long datasourceId) {
        if (null == datasourceId) {
            return Result.error("ID不能为空");
        }
        boolean result = datasourceService.deleteDatasource(datasourceId);
        return Result.result(result);
    }

    /**
    * 批量删除数据源配置表
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除数据源配置表")
    @ApiImplicitParam(name = "datasourceIds", value = "数据源配置表ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> datasourceIds) {
        if (CollectionUtils.isEmpty(datasourceIds)) {
            return Result.error("数据源配置表ID列表不能为空");
        }
        boolean result = datasourceService.batchDeleteDatasource(datasourceIds);
        return Result.result(result);
    }

    /**
    * 校验数据源配置表是否存在
    *
    * @param datasource
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验数据源配置是否存在", notes = "校验数据源配置是否存在")
    public Result<Boolean> checkDatasourceExist(CheckExistDTO datasource) {
        String field = datasource.getCheckField();
        String value = datasource.getCheckValue();
        QueryWrapper<Datasource> datasourceQueryWrapper = new QueryWrapper<>();
        datasourceQueryWrapper.eq(field, value);
        if(null != datasource.getId()) {
            datasourceQueryWrapper.ne("id", datasource.getId());
        }
        int count = datasourceService.count(datasourceQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 批量导出数据
     * @param response
     * @param queryDatasourceDTO
     * @throws IOException
     */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryDatasourceDTO queryDatasourceDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据源列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<DatasourceDTO> dataSourceList = datasourceService.queryDatasourceList(queryDatasourceDTO);
        List<DatasourceExport> dataSourceExportList = new ArrayList<>();
        for (DatasourceDTO datasourceDTO : dataSourceList) {
            DatasourceExport dataSourceExport = BeanCopierUtils.copyByClass(datasourceDTO, DatasourceExport.class);
            dataSourceExportList.add(dataSourceExport);
        }
        String sheetName = "数据源列表";
        EasyExcel.write(response.getOutputStream(), DatasourceExport.class).sheet(sheetName).doWrite(dataSourceExportList);
    }

    /**
     * 下载导入模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据源导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "数据源列表";
        EasyExcel.write(response.getOutputStream(), DatasourceImport.class).sheet(sheetName).doWrite(new ArrayList<>());
    }

    /**
     * 上传数据
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
        List<DatasourceImport> datasourceImportList =  EasyExcel.read(file.getInputStream(), DatasourceImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(datasourceImportList))
        {
            List<Datasource> datasourceList = new ArrayList<>();
            datasourceImportList.stream().forEach(datasourceImport-> {
                datasourceList.add(BeanCopierUtils.copyByClass(datasourceImport, Datasource.class));
            });
            datasourceService.saveBatch(datasourceList);
        }
        return Result.success();
    }

 }
