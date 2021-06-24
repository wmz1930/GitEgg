package com.gitegg.platform.dfs.service;

import com.gitegg.platform.dfs.domain.GitEggDfsFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式文件存储操作接口定义
 * 为了保留系统操作记录，原则上不允许上传文件物理删除，修改等操作。
 * 业务操作的修改删除文件，只是关联关系的修改，重新上传文件后并与业务关联即可。
 */
public interface IDfsBaseService {

    /**
     * 获取简单上传凭证
     * @param bucket
     * @return
     */
    String uploadToken(String bucket);


    /**
     * 获取覆盖上传凭证
     * @param bucket
     * @return
     */
    String uploadToken(String bucket, String key);

    /**
     * 创建 bucket
     * @param bucket
     */
    void createBucket(String bucket);

    /**
     * 通过流上传文件，指定文件名
     * @param inputStream
     * @param fileName
     * @return
     */
    GitEggDfsFile uploadFile(InputStream inputStream, String fileName);

    /**
     * 通过流上传文件，指定文件名和bucket
     * @param inputStream
     * @param bucket
     * @param fileName
     * @return
     */
    GitEggDfsFile uploadFile(InputStream inputStream, String bucket, String fileName);


    /**
     * 通过文件名获取文件访问链接
     * @param fileName
     * @return
     */
    String getFileUrl(String fileName);

    /**
     * 通过文件名和bucket获取文件访问链接
     * @param fileName
     * @param bucket
     * @return
     */
    String getFileUrl(String bucket, String fileName);

    /**
     * 通过文件名和bucket获取文件访问链接,设置有效期
     * @param bucket
     * @param fileName
     * @param duration
     * @param unit
     * @return
     */
    String getFileUrl(String bucket, String fileName, int duration, TimeUnit unit);

    /**
     * 通过文件名以流的形式下载一个对象
     * @param fileName
     * @return
     */
    OutputStream getFileObject(String fileName, OutputStream outputStream);

    /**
     * 通过文件名和bucket以流的形式下载一个对象
     * @param fileName
     * @param bucket
     * @return
     */
    OutputStream getFileObject(String bucket, String fileName, OutputStream outputStream);

    /**
     * 根据文件名删除文件
     * @param fileName
     */
    String removeFile(String fileName);

    /**
     * 根据文件名删除指定bucket下的文件
     * @param bucket
     * @param fileName
     */
    String removeFile(String bucket, String fileName);


    /**
     * 根据文件名列表批量删除文件
     * @param fileNames
     */
    String removeFiles(List<String> fileNames);

    /**
     *
     * @param bucket
     * @param fileNames
     */
    String removeFiles(String bucket, List<String> fileNames);
}
