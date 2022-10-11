package com.gitegg.service.extension.mail.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.mail.dto.CreateMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.MailTemplateDTO;
import com.gitegg.service.extension.mail.dto.QueryMailTemplateDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailTemplateDTO;
import com.gitegg.service.extension.mail.entity.MailTemplate;
import com.gitegg.service.extension.mail.entity.MailTemplateExport;
import com.gitegg.service.extension.mail.entity.MailTemplateImport;
import com.gitegg.service.extension.mail.service.IMailTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
* <p>
* 邮件模板 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-06-24
*/
@RestController
@RequestMapping("/extension/mail/template")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MailTemplateController|邮件模板前端控制器", tags = {"邮件模板配置"})
@RefreshScope
public class MailTemplateController {

    private final IMailTemplateService mailTemplateService;

    /**
    * 查询邮件模板列表
    *
    * @param queryMailTemplateDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询邮件模板列表")
    public PageResult<MailTemplateDTO> list(QueryMailTemplateDTO queryMailTemplateDTO, Page<MailTemplateDTO> page) {
        Page<MailTemplateDTO> pageMailTemplate = mailTemplateService.queryMailTemplateList(page, queryMailTemplateDTO);
        return PageResult.data(pageMailTemplate.getTotal(), pageMailTemplate.getRecords());
    }

    /**
     * 查询所有邮件模板列表
     *
     * @param queryMailTemplateDTO
     * @return
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询邮件模板列表")
    public Result<List<MailTemplateDTO>> listAll(QueryMailTemplateDTO queryMailTemplateDTO) {
        List<MailTemplateDTO> mailTemplateList = mailTemplateService.queryMailTemplateList(queryMailTemplateDTO);
        return Result.data(mailTemplateList);
    }

    /**
    * 查询邮件模板详情
    *
    * @param queryMailTemplateDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询邮件模板详情")
    public Result<?> query(QueryMailTemplateDTO queryMailTemplateDTO) {
        MailTemplateDTO mailTemplateDTO = mailTemplateService.queryMailTemplate(queryMailTemplateDTO);
        return Result.data(mailTemplateDTO);
    }

    /**
    * 添加邮件模板
    *
    * @param mailTemplate
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加邮件模板")
    public Result<?> create(@RequestBody CreateMailTemplateDTO mailTemplate) {
        boolean result = mailTemplateService.createMailTemplate(mailTemplate);
        return Result.result(result);
    }

    /**
    * 修改邮件模板
    *
    * @param mailTemplate
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新邮件模板")
    public Result<?> update(@RequestBody UpdateMailTemplateDTO mailTemplate) {
        boolean result = mailTemplateService.updateMailTemplate(mailTemplate);
        return Result.result(result);
    }

    /**
    * 删除邮件模板
    *
    * @param mailTemplateId
    * @return
    */
    @PostMapping("/delete/{mailTemplateId}")
    @ApiOperation(value = "删除邮件模板")
    @ApiImplicitParam(paramType = "path", name = "mailTemplateId", value = "邮件模板ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("mailTemplateId") Long mailTemplateId) {
        if (null == mailTemplateId) {
            return Result.error("ID不能为空");
        }
        boolean result = mailTemplateService.deleteMailTemplate(mailTemplateId);
        return Result.result(result);
    }

    /**
    * 批量删除邮件模板
    *
    * @param mailTemplateIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除邮件模板")
    @ApiImplicitParam(name = "mailTemplateIds", value = "邮件模板ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> mailTemplateIds) {
        if (CollectionUtils.isEmpty(mailTemplateIds)) {
            return Result.error("邮件模板ID列表不能为空");
        }
        boolean result = mailTemplateService.batchDeleteMailTemplate(mailTemplateIds);
        return Result.result(result);
    }
     /**
     * 修改邮件模板状态
     *
     * @param mailTemplateId
     * @param templateStatus
     * @return
     */
     @PostMapping("/status/{mailTemplateId}/{templateStatus}")
     @ApiOperation(value = "修改邮件模板状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "mailTemplateId", value = "邮件模板ID", required = true, dataTypeClass = Long.class, paramType = "path"),
     @ApiImplicitParam(name = "templateStatus", value = "邮件模板状态", required = true, dataTypeClass = Integer.class, paramType = "path") })
     public Result<?> updateStatus(@PathVariable("mailTemplateId") Long mailTemplateId,
         @PathVariable("templateStatus") Integer templateStatus) {

         if (null == mailTemplateId || StringUtils.isEmpty(templateStatus)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateMailTemplateDTO mailTemplate = new UpdateMailTemplateDTO();
         mailTemplate.setId(mailTemplateId);
         mailTemplate.setTemplateStatus(templateStatus);
         boolean result = mailTemplateService.updateMailTemplate(mailTemplate);
         return Result.result(result);
     }


    /**
    * 批量导出邮件模板数据
    * @param response
    * @param queryMailTemplateDTO
    * @throws IOException
    */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryMailTemplateDTO queryMailTemplateDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件模板数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<MailTemplateDTO> mailTemplateList = mailTemplateService.queryMailTemplateList(queryMailTemplateDTO);
        List<MailTemplateExport> mailTemplateExportList = new ArrayList<>();
        for (MailTemplateDTO mailTemplateDTO : mailTemplateList) {
           MailTemplateExport mailTemplateExport = BeanCopierUtils.copyByClass(mailTemplateDTO, MailTemplateExport.class);
           mailTemplateExportList.add(mailTemplateExport);
        }
        String sheetName = "邮件模板数据列表";
        EasyExcel.write(response.getOutputStream(), MailTemplateExport.class).sheet(sheetName).doWrite(mailTemplateExportList);
    }

    /**
    * 批量上传邮件模板数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<MailTemplateImport> mailTemplateImportList =  EasyExcel.read(file.getInputStream(), MailTemplateImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(mailTemplateImportList))
        {
            List<MailTemplate> mailTemplateList = new ArrayList<>();
            mailTemplateImportList.stream().forEach(mailTemplateImport-> {
               mailTemplateList.add(BeanCopierUtils.copyByClass(mailTemplateImport, MailTemplate.class));
            });
            mailTemplateService.saveBatch(mailTemplateList);
        }
        return Result.success();
    }

    /**
    * 下载邮件模板数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件模板数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "邮件模板数据列表";
        EasyExcel.write(response.getOutputStream(), MailTemplateImport.class).sheet(sheetName).doWrite(new ArrayList<>());
    }
 }
