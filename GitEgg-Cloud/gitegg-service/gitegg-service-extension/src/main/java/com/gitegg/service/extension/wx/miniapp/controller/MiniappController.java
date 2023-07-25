package com.gitegg.service.extension.wx.miniapp.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.extension.wx.miniapp.bo.MiniappExportBO;
import com.gitegg.service.extension.wx.miniapp.bo.MiniappImportBO;
import com.gitegg.service.extension.wx.miniapp.dto.CreateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.MiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.QueryMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.UpdateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.entity.Miniapp;
import com.gitegg.service.extension.wx.miniapp.service.IMiniappService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 微信小程序配置 前端控制器
* </p>
*
* @author GitEgg
* @since 2023-07-16
*/
@Slf4j
@RestController
@RequestMapping("/extension/wx/miniapp")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MiniappController|微信小程序配置前端控制器", tags = {"微信小程序配置"})
public class MiniappController {

    private final IMiniappService miniappService;

    /**
    * 查询微信小程序配置列表
    *
    * @param queryMiniappDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询微信小程序配置列表")
    public Result<Page<MiniappDTO>> list(QueryMiniappDTO queryMiniappDTO, Page<MiniappDTO> page) {
        Page<MiniappDTO> pageMiniapp = miniappService.queryMiniappList(page, queryMiniappDTO);
        return Result.data(pageMiniapp);
    }

    /**
    * 查询微信小程序配置详情
    *
    * @param queryMiniappDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询微信小程序配置详情")
    public Result<?> query(QueryMiniappDTO queryMiniappDTO) {
        MiniappDTO miniappDTO = miniappService.queryMiniapp(queryMiniappDTO);
        return Result.data(miniappDTO);
    }

    /**
    * 添加微信小程序配置
    *
    * @param miniapp
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加微信小程序配置")
    public Result<?> create(@RequestBody @Valid CreateMiniappDTO miniapp) {
        boolean result = miniappService.createMiniapp(miniapp);
        return Result.result(result);
    }

    /**
    * 修改微信小程序配置
    *
    * @param miniapp
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新微信小程序配置")
    public Result<?> update(@RequestBody @Valid UpdateMiniappDTO miniapp) {
        boolean result = miniappService.updateMiniapp(miniapp);
        return Result.result(result);
    }

    /**
    * 删除微信小程序配置
    *
    * @param miniappId
    * @return
    */
    @PostMapping("/delete/{miniappId}")
    @ApiOperation(value = "删除微信小程序配置")
    @ApiImplicitParam(paramType = "path", name = "miniappId", value = "微信小程序配置ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("miniappId") Long miniappId) {
        if (null == miniappId) {
            return Result.error("ID不能为空");
        }
        boolean result = miniappService.deleteMiniapp(miniappId);
        return Result.result(result);
    }

    /**
    * 批量删除微信小程序配置
    *
    * @param miniappIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除微信小程序配置")
    @ApiImplicitParam(name = "miniappIds", value = "微信小程序配置ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> miniappIds) {
        if (CollectionUtils.isEmpty(miniappIds)) {
            return Result.error("微信小程序配置ID列表不能为空");
        }
        boolean result = miniappService.batchDeleteMiniapp(miniappIds);
        return Result.result(result);
    }
     /**
     * 修改微信小程序配置状态
     *
     * @param miniappId
     * @param status
     * @return
     */
     @PostMapping("/status/{miniappId}/{status}")
     @ApiOperation(value = "修改微信小程序配置状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "miniappId", value = "微信小程序配置ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "status", value = "微信小程序配置状态", required = true, dataType = "String", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("miniappId") Long miniappId,
         @PathVariable("status") String status) {

         if (null == miniappId || StringUtils.isEmpty(status)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateMiniappDTO miniapp = new UpdateMiniappDTO();
         miniapp.setId(miniappId);
         miniapp.setStatus(status);
         boolean result = miniappService.updateMiniapp(miniapp);
         return Result.result(result);
     }

    /**
    * 校验微信小程序配置是否存在
    *
    * @param miniapp
    * @return
    */
    @PostMapping(value = "/check")
    @ApiOperation(value = "校验微信小程序配置是否存在", notes = "校验微信小程序配置是否存在")
    public Result<Boolean> checkMiniappExist(@RequestBody @Valid CheckExistDTO miniapp) {
        String field = miniapp.getCheckField();
        String value = miniapp.getCheckValue();
        QueryWrapper<Miniapp> miniappQueryWrapper = new QueryWrapper<>();
        miniappQueryWrapper.eq(field, value);
        if(null != miniapp.getId()) {
            miniappQueryWrapper.ne("id", miniapp.getId());
        }
        int count = miniappService.count(miniappQueryWrapper);
        if (GitEggConstant.COUNT_ZERO == count){
            return Result.data(true);
        } else{
            return Result.data(false);
        }
    }

    /**
     * 批量导出微信小程序配置数据
     * @param response
     * @param queryMiniappDTO
     */
    @PostMapping("/export")
    @ApiOperation("导出数据")
    public void exportMiniappList(HttpServletResponse response, @RequestBody QueryMiniappDTO queryMiniappDTO) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户数据列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<MiniappExportBO> miniappExportList = miniappService.exportMiniappList(queryMiniappDTO);
            String sheetName = "用户数据列表";
            EasyExcel.write(response.getOutputStream(), MiniappExportBO.class).sheet(sheetName).doWrite(miniappExportList);
        } catch (Exception e) {
            log.error("批量导出用户数据时发生错误:{}", e);
            throw new BusinessException("批量导出失败:" + e);
        }

    }

    /**
     * 批量上传微信小程序配置数据
     * @param file
     * @return
     */
    @PostMapping("/import")
    @ApiOperation("批量上传微信小程序配置数据")
    public Result<?> importMiniappList(@RequestParam("uploadFile") MultipartFile file) {
        boolean importSuccess = miniappService.importMiniappList(file);
        return Result.result(importSuccess);
    }

    /**
    * 下载微信小程序配置数据导入模板
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
            fileName = URLEncoder.encode("微信小程序配置数据导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            String sheetName = "微信小程序配置数据列表";
            EasyExcel.write(response.getOutputStream(), MiniappImportBO.class).sheet(sheetName).doWrite(new ArrayList<>());
        } catch (Exception e) {
            log.error("下载微信小程序配置批量模板时发生错误:{}", e);
            throw new BusinessException("下载批量模板失败:" + e);
        }
    }
 }
