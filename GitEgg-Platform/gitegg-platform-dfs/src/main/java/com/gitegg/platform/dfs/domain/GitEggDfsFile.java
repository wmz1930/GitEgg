package com.gitegg.platform.dfs.domain;

import lombok.Data;

/**
 * @author GitEgg
 * @date 2021-04-29 10:47:53
 **/
@Data
public class GitEggDfsFile {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * UTF-8编码文件名
     */
    private String encodedFileName;

    /**
     * 存储的域名
     */
    private String bucketDomain;

    /**
     * 文件访问地址
     */
    private String fileUrl;

    /**
     * 文件key值
     */
    public String key;

    /**
     * 文件hash值
     */
    public String hash;

    /**
     * 分布式存储bucket
     */
    public String bucket;

    /**
     * 文件大小
     */
    private long fileSize;

}
