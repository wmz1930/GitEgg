package com.gitegg.platform.dfs.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author GitEgg
 * @date 2021-05-08 21:45:04
 **/
public class DfsConstants {

    /**
     * 获取上传文件的方法
     */
    public static final String DFS_SERVICE_FUNCTION = "getDfsBaseService";


    /**
     * 获取文件下载的有效时间默认值
     */
    public static final int DFS_FILE_DURATION = 2;


    /**
     * 获取文件下载的有效时间单位默认值
     */
    public static final TimeUnit DFS_FILE_DURATION_UNIT = TimeUnit.HOURS;


    /**
     * 远程下载文件超时时间 600秒
     */
    public static final int DOWNLOAD_TIMEOUT = 60 * 10 * 1000;

    /**
     * 文件存储，私有
     */
    public static final int DFS_FILE_PRIVATE = 0;

    /**
     * 文件存储，公开
     */
    public static final int DFS_FILE_OPEN = 1;
}
