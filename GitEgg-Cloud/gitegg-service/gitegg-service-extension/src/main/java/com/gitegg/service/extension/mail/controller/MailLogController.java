package com.gitegg.service.extension.mail.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.mail.dto.CreateMailLogDTO;
import com.gitegg.service.extension.mail.dto.MailLogDTO;
import com.gitegg.service.extension.mail.dto.QueryMailLogDTO;
import com.gitegg.service.extension.mail.dto.UpdateMailLogDTO;
import com.gitegg.service.extension.mail.entity.MailLog;
import com.gitegg.service.extension.mail.entity.MailLogExport;
import com.gitegg.service.extension.mail.entity.MailLogImport;
import com.gitegg.service.extension.mail.service.IMailLogService;
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
* 邮件记录 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-06-24
*/
@RestController
@RequestMapping("/extension/mail/log")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "MailLogController|邮件记录前端控制器", tags = {"邮件记录配置"})
@RefreshScope
public class MailLogController {

    private final IMailLogService mailLogService;

    /**
    * 查询邮件记录列表
    *
    * @param queryMailLogDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询邮件记录列表")
    public Result<Page<MailLogDTO>> list(QueryMailLogDTO queryMailLogDTO, Page<MailLogDTO> page) {
        Page<MailLogDTO> pageMailLog = mailLogService.queryMailLogList(page, queryMailLogDTO);
        return Result.data(pageMailLog);
    }

    /**
    * 查询邮件记录详情
    *
    * @param queryMailLogDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询邮件记录详情")
    public Result<?> query(QueryMailLogDTO queryMailLogDTO) {
        MailLogDTO mailLogDTO = mailLogService.queryMailLog(queryMailLogDTO);
        return Result.data(mailLogDTO);
    }

    /**
    * 添加邮件记录
    *
    * @param mailLog
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加邮件记录")
    public Result<?> create(@RequestBody CreateMailLogDTO mailLog) {
        boolean result = mailLogService.createMailLog(mailLog);
        return Result.result(result);
    }

    /**
    * 修改邮件记录
    *
    * @param mailLog
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新邮件记录")
    public Result<?> update(@RequestBody UpdateMailLogDTO mailLog) {
        boolean result = mailLogService.updateMailLog(mailLog);
        return Result.result(result);
    }

    /**
    * 删除邮件记录
    *
    * @param mailLogId
    * @return
    */
    @PostMapping("/delete/{mailLogId}")
    @ApiOperation(value = "删除邮件记录")
    @ApiImplicitParam(paramType = "path", name = "mailLogId", value = "邮件记录ID", required = true, dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("mailLogId") Long mailLogId) {
        if (null == mailLogId) {
            return Result.error("ID不能为空");
        }
        boolean result = mailLogService.deleteMailLog(mailLogId);
        return Result.result(result);
    }

    /**
    * 批量删除邮件记录
    *
    * @param mailLogIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除邮件记录")
    @ApiImplicitParam(name = "mailLogIds", value = "邮件记录ID列表", required = true, dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> mailLogIds) {
        if (CollectionUtils.isEmpty(mailLogIds)) {
            return Result.error("邮件记录ID列表不能为空");
        }
        boolean result = mailLogService.batchDeleteMailLog(mailLogIds);
        return Result.result(result);
    }


    /**
    * 批量导出邮件记录数据
    * @param response
    * @param queryMailLogDTO
    * @throws IOException
    */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryMailLogDTO queryMailLogDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件记录数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<MailLogDTO> mailLogList = mailLogService.queryMailLogList(queryMailLogDTO);
        List<MailLogExport> mailLogExportList = new ArrayList<>();
        for (MailLogDTO mailLogDTO : mailLogList) {
           MailLogExport mailLogExport = BeanCopierUtils.copyByClass(mailLogDTO, MailLogExport.class);
           mailLogExportList.add(mailLogExport);
        }
        String sheetName = "邮件记录数据列表";
        EasyExcel.write(response.getOutputStream(), MailLogExport.class).sheet(sheetName).doWrite(mailLogExportList);
    }

    /**
    * 批量上传邮件记录数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<MailLogImport> mailLogImportList =  EasyExcel.read(file.getInputStream(), MailLogImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(mailLogImportList))
        {
            List<MailLog> mailLogList = new ArrayList<>();
            mailLogImportList.stream().forEach(mailLogImport-> {
               mailLogList.add(BeanCopierUtils.copyByClass(mailLogImport, MailLog.class));
            });
            mailLogService.saveBatch(mailLogList);
        }
        return Result.success();
    }

    /**
    * 下载邮件记录数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("邮件记录数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "邮件记录数据列表";
        EasyExcel.write(response.getOutputStream(), MailLogImport.class).sheet(sheetName).doWrite(new ArrayList<>());
    }
 }
