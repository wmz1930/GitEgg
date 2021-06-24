package com.gitegg.platform.dfs.qiniu.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author GitEgg
 * @date 2021-04-29 19:17:05
 **/

@Data
@Component
@ConfigurationProperties(prefix = "dfs.qiniu")
public class QiNiuDfsProperties {


    /**
     * AccessKey
     */
    private String accessKey;

    /**
     * SecretKey
     */
    private String secretKey;

    /**
     * 七牛云机房
     */
    private String region;

    /**
     * Bucket 存储块
     */
    private String bucket;

    /**
     * 公开还是私有
     */
    private Integer accessControl;

    /**
     * 上传服务器域名地址
     */
    private String uploadUrl;

    /**
     * 文件请求地址前缀
     */
    private String accessUrlPrefix;

    /**
     * 上传文件夹前缀
     */
    private String uploadDirPrefix;
}
