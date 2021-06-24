package com.gitegg.service.extension.dfs.factory;

import com.gitegg.platform.dfs.minio.props.MinioDfsProperties;
import com.gitegg.platform.dfs.minio.service.impl.MinioDfsServiceImpl;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import io.minio.MinioClient;

/**
 * MINIO上传服务接口工厂类
 */
public class DfsMinioFactory {

    public static IDfsBaseService getDfsBaseService(DfsDTO dfsDTO) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(dfsDTO.getUploadUrl())
                        .credentials(dfsDTO.getAccessKey(), dfsDTO.getSecretKey()).build();;
        MinioDfsProperties minioDfsProperties = new MinioDfsProperties();
        minioDfsProperties.setAccessKey(dfsDTO.getAccessKey());
        minioDfsProperties.setSecretKey(dfsDTO.getSecretKey());
        minioDfsProperties.setRegion(dfsDTO.getRegion());
        minioDfsProperties.setBucket(dfsDTO.getBucket());
        minioDfsProperties.setUploadUrl(dfsDTO.getUploadUrl());
        minioDfsProperties.setAccessUrlPrefix(dfsDTO.getAccessUrlPrefix());
        minioDfsProperties.setAccessControl(dfsDTO.getAccessControl());

        return new MinioDfsServiceImpl(minioClient, minioDfsProperties);
    }
}
