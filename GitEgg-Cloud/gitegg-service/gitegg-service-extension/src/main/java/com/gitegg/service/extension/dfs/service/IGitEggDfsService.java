package com.gitegg.service.extension.dfs.service;

import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * 业务文件上传下载接口实现
 *
 */
public interface IGitEggDfsService {


    /**
     * 获取文件上传的 token
     * @param dfsCode
     * @return
     */
    String uploadToken(String dfsCode);


    /**
     * 上传文件
     *
     * @param dfsCode
     * @param file
     * @return
     */
    GitEggDfsFile uploadFile(String dfsCode, MultipartFile file);

    /**
     * 获取文件访问链接
     * @param dfsCode
     * @param fileName
     * @return
     */
    String getFileUrl(String dfsCode, String fileName);


    /**
     * 下载文件
     * @param dfsCode
     * @param fileName
     * @return
     */
    OutputStream downloadFile(String dfsCode, String fileName, OutputStream outputStream);
}
