package com.gitegg.platform.dfs.qiniu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.dfs.constant.DfsConstants;
import com.gitegg.platform.dfs.domain.GitEggDfsFile;
import com.gitegg.platform.dfs.qiniu.props.QiNiuDfsProperties;
import com.gitegg.platform.dfs.service.IDfsBaseService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author GitEgg
 * @date 2021-04-29 19:17:43
 **/
@Slf4j
@AllArgsConstructor
public class QiNiuDfsServiceImpl implements IDfsBaseService {

    private final Auth auth;

    private final UploadManager uploadManager;

    private final BucketManager bucketManager;

    private final QiNiuDfsProperties qiNiuDfsProperties;

    /**
     *
     * @param bucket
     * @return
     */
    @Override
    public String uploadToken(String bucket) {
        Auth auth = Auth.create(qiNiuDfsProperties.getAccessKey(), qiNiuDfsProperties.getSecretKey());
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    /**
     *
     * @param bucket
     * @param key
     * @return
     */
    @Override
    public String uploadToken(String bucket, String key) {
        Auth auth = Auth.create(qiNiuDfsProperties.getAccessKey(), qiNiuDfsProperties.getSecretKey());
        String upToken = auth.uploadToken(bucket, key);
        return upToken;
    }

    @Override
    public void createBucket(String bucket) {
        try {
            String[] buckets = bucketManager.buckets();
            if (!ArrayUtil.contains(buckets, bucket)) {
                bucketManager.createBucket(bucket, qiNiuDfsProperties.getRegion());
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    @Override
    public GitEggDfsFile uploadFile(InputStream inputStream, String fileName) {
        return this.uploadFile(inputStream, qiNiuDfsProperties.getBucket(), fileName);
    }

    /**
     *
     * @param inputStream
     * @param bucket
     * @param fileName
     * @return
     */
    @Override
    public GitEggDfsFile uploadFile(InputStream inputStream, String bucket, String fileName) {
        GitEggDfsFile dfsFile = null;
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        if (!StringUtils.isEmpty(fileName))
        {
            key = fileName;
        }
        try {
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(inputStream, key, upToken,null, null);
            //解析上传成功的结果
            dfsFile = JsonUtils.jsonToPojo(response.bodyString(), GitEggDfsFile.class);
            if (dfsFile != null) {
                dfsFile.setBucket(bucket);
                dfsFile.setBucketDomain(qiNiuDfsProperties.getUploadUrl());
                dfsFile.setFileUrl(qiNiuDfsProperties.getAccessUrlPrefix());
                dfsFile.setEncodedFileName(fileName);
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                log.error(ex2.toString());
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return dfsFile;
    }

    @Override
    public String getFileUrl(String fileName) {
        return this.getFileUrl(qiNiuDfsProperties.getBucket(), fileName);
    }

    @Override
    public String getFileUrl(String bucket, String fileName) {
        return this.getFileUrl(bucket, fileName, DfsConstants.DFS_FILE_DURATION, DfsConstants.DFS_FILE_DURATION_UNIT);
    }

    @Override
    public String getFileUrl(String bucket, String fileName, int duration, TimeUnit unit) {
        String finalUrl = null;
        try {
            Integer accessControl = qiNiuDfsProperties.getAccessControl();
            if (accessControl != null && DfsConstants.DFS_FILE_PRIVATE == accessControl.intValue()) {
                String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
                String publicUrl = String.format("%s/%s", qiNiuDfsProperties.getAccessUrlPrefix(), encodedFileName);
                String accessKey = qiNiuDfsProperties.getAccessKey();
                String secretKey = qiNiuDfsProperties.getSecretKey();
                Auth auth = Auth.create(accessKey, secretKey);
                long expireInSeconds = unit.toSeconds(duration);
                finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            }
            else {
                finalUrl = String.format("%s/%s", qiNiuDfsProperties.getAccessUrlPrefix(), fileName);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    @Override
    public OutputStream getFileObject(String fileName, OutputStream outputStream) {
        return this.getFileObject(qiNiuDfsProperties.getBucket(), fileName, outputStream);
    }

    @Override
    public OutputStream getFileObject(String bucket, String fileName, OutputStream outputStream) {
        URL url = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        try {
            String path =  this.getFileUrl(bucket, fileName, DfsConstants.DFS_FILE_DURATION, DfsConstants.DFS_FILE_DURATION_UNIT);
            url = new URL(path);
            conn = (HttpURLConnection)url.openConnection();
            //设置超时间
            conn.setConnectTimeout(DfsConstants.DOWNLOAD_TIMEOUT);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            //得到输入流
            bis = new BufferedInputStream(conn.getInputStream());
            IOUtils.copy(bis, outputStream);
        } catch (Exception e) {
            log.error("读取网络文件异常:" + fileName);
        }
        finally {
            conn.disconnect();
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

    /**
     *
     * @param fileName
     * @return
     */
    @Override
    public String removeFile(String fileName) {
        return this.removeFile( qiNiuDfsProperties.getBucket(), fileName);
    }

    /**
     *
     * @param bucket
     * @param fileName
     * @return
     */
    @Override
    public String removeFile(String bucket, String fileName) {
        String resultStr = null;
        try {
            Response response = bucketManager.delete(bucket, fileName);
            resultStr = JsonUtils.objToJson(response);
        } catch (QiniuException e) {
            Response r = e.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                log.error(ex2.toString());
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return resultStr;
    }

    /**
     *
     * @param fileNames
     * @return
     */
    @Override
    public String removeFiles(List<String> fileNames) {
        return this.removeFiles(qiNiuDfsProperties.getBucket(), fileNames);
    }

    /**
     *
     * @param bucket
     * @param fileNames
     * @return
     */
    @Override
    public String removeFiles(String bucket, List<String> fileNames) {
        String resultStr = null;
        try {
            if (!CollectionUtils.isEmpty(fileNames) && fileNames.size() > GitEggConstant.Number.THOUSAND)
            {
                throw new BusinessException("单次批量请求的文件数量不得超过1000");
            }
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, (String [])fileNames.toArray());
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            resultStr = JsonUtils.objToJson(batchStatusList);

        } catch (QiniuException ex) {
            log.error(ex.response.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
        return resultStr;
    }
}
