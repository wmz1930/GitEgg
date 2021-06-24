package com.gitegg.service.extension.dfs.service.impl;

import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;
import com.gitegg.service.extension.dfs.entity.DfsFile;
import com.gitegg.service.extension.dfs.factory.DfsFactory;
import com.gitegg.service.extension.dfs.service.IDfsFileService;
import com.gitegg.service.extension.dfs.service.IDfsService;
import com.gitegg.service.extension.dfs.service.IGitEggDfsService;
import com.qiniu.util.Etag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * 短信发送接口实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggDfsServiceImpl implements IGitEggDfsService {

    private final DfsFactory dfsFactory;

    private final IDfsService dfsService;

    private final IDfsFileService dfsFileService;

    @Override
    public String uploadToken(String dfsCode) {
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        queryDfsDTO.setDfsCode(dfsCode);
        DfsDTO dfsDTO = dfsService.queryDfs(queryDfsDTO);
        IDfsBaseService dfsBaseService = dfsFactory.getDfsBaseService(dfsDTO);
        String token = dfsBaseService.uploadToken(dfsDTO.getBucket());
        return token;
    }

    @Override
    public GitEggDfsFile uploadFile(String dfsCode, MultipartFile file) {
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;

        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }

        GitEggDfsFile gitEggDfsFile = null;
        DfsFile dfsFile = new DfsFile();
        try {

            dfsDTO = dfsService.queryDfs(queryDfsDTO);

            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);

            //获取文件名
            String originalName = file.getOriginalFilename();
            //获取文件后缀
            String extension = FilenameUtils.getExtension(originalName);
            String hash = Etag.stream(file.getInputStream(), file.getSize());
            String fileName = hash + "." + extension;

            // 保存文件上传记录
            dfsFile.setDfsId(dfsDTO.getId());
            dfsFile.setOriginalName(originalName);
            dfsFile.setFileName(fileName);
            dfsFile.setFileExtension(extension);
            dfsFile.setFileSize(file.getSize());
            dfsFile.setFileStatus(GitEggConstant.ENABLE);

            //执行文件上传操作
            gitEggDfsFile = dfsFileService.uploadFile(file.getInputStream(), fileName);

            if (gitEggDfsFile != null)
            {
                gitEggDfsFile.setFileName(originalName);
                gitEggDfsFile.setKey(hash);
                gitEggDfsFile.setHash(hash);
                gitEggDfsFile.setFileSize(file.getSize());
            }

            dfsFile.setAccessUrl(gitEggDfsFile.getFileUrl());
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            dfsFile.setFileStatus(GitEggConstant.DISABLE);
            dfsFile.setComments(String.valueOf(e));
        } finally {
            dfsFileService.save(dfsFile);
        }

        return gitEggDfsFile;
    }

    @Override
    public String getFileUrl(String dfsCode, String fileName) {
        String fileUrl = null;

        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;
        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }

        try {
            dfsDTO = dfsService.queryDfs(queryDfsDTO);
            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);
            fileUrl = dfsFileService.getFileUrl(fileName);
        }
        catch (Exception e)
        {
            log.error("文件上传失败：{}", e);
        }
        return fileUrl;
    }

    @Override
    public OutputStream downloadFile(String dfsCode, String fileName, OutputStream outputStream) {
        QueryDfsDTO queryDfsDTO = new QueryDfsDTO();
        DfsDTO dfsDTO = null;
        // 如果上传时没有选择存储方式，那么取默认存储方式
        if(StringUtils.isEmpty(dfsCode)) {
            queryDfsDTO.setDfsDefault(GitEggConstant.ENABLE);
        }
        else {
            queryDfsDTO.setDfsCode(dfsCode);
        }

        try {
            dfsDTO = dfsService.queryDfs(queryDfsDTO);
            IDfsBaseService dfsFileService = dfsFactory.getDfsBaseService(dfsDTO);
            outputStream = dfsFileService.getFileObject(fileName, outputStream);
        }
        catch (Exception e)
        {
            log.error("文件上传失败：{}", e);
        }
        return outputStream;
    }
}
