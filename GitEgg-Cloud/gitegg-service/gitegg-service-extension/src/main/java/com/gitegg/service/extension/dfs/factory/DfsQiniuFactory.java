package com.gitegg.service.extension.dfs.factory;

import com.gitegg.platform.dfs.qiniu.props.QiNiuDfsProperties;
import com.gitegg.platform.dfs.qiniu.service.impl.QiNiuDfsServiceImpl;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * 七牛云上传服务接口工厂类
 */
public class DfsQiniuFactory {

    public static IDfsBaseService getDfsBaseService(DfsDTO dfsDTO) {
        Auth auth = Auth.create(dfsDTO.getAccessKey(), dfsDTO.getSecretKey());
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        QiNiuDfsProperties qiNiuDfsProperties = new QiNiuDfsProperties();
        qiNiuDfsProperties.setAccessKey(dfsDTO.getAccessKey());
        qiNiuDfsProperties.setSecretKey(dfsDTO.getSecretKey());
        qiNiuDfsProperties.setRegion(dfsDTO.getRegion());
        qiNiuDfsProperties.setBucket(dfsDTO.getBucket());
        qiNiuDfsProperties.setUploadUrl(dfsDTO.getUploadUrl());
        qiNiuDfsProperties.setAccessUrlPrefix(dfsDTO.getAccessUrlPrefix());
        qiNiuDfsProperties.setAccessControl(dfsDTO.getAccessControl());
        return new QiNiuDfsServiceImpl(auth, uploadManager, bucketManager, qiNiuDfsProperties);
    }
}
