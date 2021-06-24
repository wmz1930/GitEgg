package com.gitegg.service.extension.dfs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.DfsFileDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsFileDTO;
import com.gitegg.service.extension.dfs.entity.DfsFile;
import com.gitegg.service.extension.dfs.service.IDfsFileService;
import com.gitegg.service.extension.dfs.service.IGitEggDfsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * <p>
 * 分布式存储上传 前端控制器
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
@RestController
@RequestMapping("/extension")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "GitEggDfsController|文件上传前端控制器")
@RefreshScope
public class GitEggDfsController {

    private final IGitEggDfsService gitEggDfsService;

    /**
     * 上传文件
     * @param uploadFile
     * @param dfsCode
     * @return
     */
    @PostMapping("/upload/file")
    public Result<?> uploadFile(@RequestParam("uploadFile") MultipartFile[] uploadFile, String dfsCode) {
        GitEggDfsFile gitEggDfsFile = null;
        if (ArrayUtils.isNotEmpty(uploadFile))
        {
            for (MultipartFile file : uploadFile) {
                gitEggDfsFile = gitEggDfsService.uploadFile(dfsCode, file);
            }
        }
        return Result.data(gitEggDfsFile);
    }


    /**
     * 通过文件名获取文件访问链接
     */
    @GetMapping("/get/file/url")
    @ApiOperation(value = "查询分布式存储配置表详情")
    public Result<?> query(String dfsCode, String fileName) {
        String fileUrl = gitEggDfsService.getFileUrl(dfsCode, fileName);
        return Result.data(fileUrl);
    }

    /**
     * 通过文件名以文件流的方式下载文件
     */
    @GetMapping("/get/file/download")
    public void downloadFile(HttpServletResponse response,HttpServletRequest request,String dfsCode, String fileName) {
        if (fileName != null) {
            response.setCharacterEncoding(request.getCharacterEncoding());
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os = gitEggDfsService.downloadFile(dfsCode, fileName, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
