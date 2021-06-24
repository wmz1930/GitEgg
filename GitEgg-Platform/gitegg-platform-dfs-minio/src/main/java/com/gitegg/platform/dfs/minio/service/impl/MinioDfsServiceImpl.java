package com.gitegg.platform.dfs.minio.service.impl;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.dfs.constant.DfsConstants;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import com.gitegg.platform.dfs.minio.props.MinioDfsProperties;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.CollectionUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author GitEgg
 * @date 2021-04-29 19:05:54
 **/
@Slf4j
@AllArgsConstructor
public class MinioDfsServiceImpl implements IDfsBaseService {

    private final MinioClient minioClient;

    private final MinioDfsProperties minioDfsProperties;

    @Override
    public String uploadToken(String bucket) {
        return null;
    }

    @Override
    public String uploadToken(String bucket, String key) {
        return null;
    }

    @Override
    public void createBucket(String bucket) {
        BucketExistsArgs bea = BucketExistsArgs.builder().bucket(bucket).build();
        try {
            if (!minioClient.bucketExists(bea)) {
                MakeBucketArgs mba = MakeBucketArgs.builder().bucket(bucket).build();
                minioClient.makeBucket(mba);
            }
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GitEggDfsFile uploadFile(InputStream inputStream, String fileName) {
        return this.uploadFile(inputStream, minioDfsProperties.getBucket(), fileName);
    }

    @Override
    public GitEggDfsFile uploadFile(InputStream inputStream, String bucket, String fileName) {
        GitEggDfsFile dfsFile = new GitEggDfsFile();
        try {

            dfsFile.setBucket(bucket);
            dfsFile.setBucketDomain(minioDfsProperties.getUploadUrl());
            dfsFile.setFileUrl(minioDfsProperties.getAccessUrlPrefix());
            dfsFile.setEncodedFileName(fileName);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .stream(inputStream, -1, 5*1024*1024)
                    .object(fileName)
                    .build());

        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return dfsFile;
    }

    @Override
    public String getFileUrl(String fileName) {
        return this.getFileUrl(minioDfsProperties.getBucket(), fileName);
    }

    @Override
    public String getFileUrl(String bucket, String fileName) {
        return this.getFileUrl(bucket, fileName, DfsConstants.DFS_FILE_DURATION, DfsConstants.DFS_FILE_DURATION_UNIT);
    }

    @Override
    public String getFileUrl(String bucket, String fileName, int duration, TimeUnit unit) {
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(fileName)
                            .expiry(duration, unit)
                            .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public OutputStream getFileObject(String fileName, OutputStream outputStream) {
        return this.getFileObject(minioDfsProperties.getBucket(), fileName, outputStream);
    }

    @Override
    public OutputStream getFileObject(String bucket, String fileName, OutputStream outputStream) {
        BufferedInputStream bis = null;
        InputStream stream = null;
        try {
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build());
            bis = new BufferedInputStream(stream);
            IOUtils.copy(bis, outputStream);
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputStream;
    }

    @Override
    public String removeFile(String fileName) {
        return this.removeFile(minioDfsProperties.getBucket(), fileName);
    }

    @Override
    public String removeFile(String bucket, String fileName) {
        return this.removeFiles(bucket, Collections.singletonList(fileName));
    }

    @Override
    public String removeFiles(List<String> fileNames) {
        return this.removeFiles(minioDfsProperties.getBucket(), fileNames);
    }

    @Override
    public String removeFiles(String bucket, List<String> fileNames) {
        List<DeleteObject> deleteObject = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileNames))
        {
            fileNames.stream().forEach(item -> {
                deleteObject.add(new DeleteObject(item));
            });
        }
        Iterable<Result<DeleteError>> result = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucket)
                .objects(deleteObject)
                .build());
        try {
            return JsonUtils.objToJsonIgnoreNull(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
