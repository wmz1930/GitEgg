package com.gitegg.service.extension.sms.factory;

import com.gitegg.platform.sms.service.SmsSendService;
import com.gitegg.platform.sms.tencent.props.TencentSmsProperties;
import com.gitegg.platform.sms.tencent.service.impl.TencentSmsSendServiceImpl;
import com.gitegg.service.extension.dto.SmsTemplateDTO;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import org.springframework.util.StringUtils;

/**
 * 腾讯云短信服务接口工厂类
 */
public class SmsTencentFactory {

    public static SmsSendService getSmsSendService(SmsTemplateDTO sms) {

        TencentSmsProperties tencentSmsProperties = new TencentSmsProperties();
        tencentSmsProperties.setSmsSdkAppId(sms.getSecretId());
        tencentSmsProperties.setExtendCode(sms.getSecretKey());
        tencentSmsProperties.setSenderId(sms.getRegionId());
        tencentSmsProperties.setSignName(sms.getSignName());

        /* 必要步骤：
         * 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
         * 本示例采用从环境变量读取的方式，需要预先在环境变量中设置这两个值
         * 您也可以直接在代码中写入密钥对，但需谨防泄露，不要将代码复制、上传或者分享给他人
         * CAM 密钥查询：https://console.cloud.tencent.com/cam/capi
         */
        Credential cred = new Credential(sms.getSecretId(), sms.getSecretKey());
        // 实例化一个 http 选项，可选，无特殊需求时可以跳过
        HttpProfile httpProfile = new HttpProfile();
        // 设置代理
//        httpProfile.setProxyHost("host");
//        httpProfile.setProxyPort(port);
        /* SDK 默认使用 POST 方法。
         * 如需使用 GET 方法，可以在此处设置，但 GET 方法无法处理较大的请求 */
        httpProfile.setReqMethod("POST");
        /* SDK 有默认的超时时间，非必要请不要进行调整
         * 如有需要请在代码中查阅以获取最新的默认值 */
        httpProfile.setConnTimeout(60);
        /* SDK 会自动指定域名，通常无需指定域名，但访问金融区的服务时必须手动指定域名
         * 例如 SMS 的上海金融区域名为 sms.ap-shanghai-fsi.tencentcloudapi.com */
        if (!StringUtils.isEmpty(sms.getRegionId()))
        {
            httpProfile.setEndpoint(sms.getRegionId());
        }

        /* 非必要步骤:
         * 实例化一个客户端配置对象，可以指定超时时间等配置 */
        ClientProfile clientProfile = new ClientProfile();
        /* SDK 默认用 TC3-HMAC-SHA256 进行签名
         * 非必要请不要修改该字段 */
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        /* 实例化 SMS 的 client 对象
         * 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量 */
        SmsClient client = new SmsClient(cred, "",clientProfile);

        return new TencentSmsSendServiceImpl(tencentSmsProperties, client);
    }
}
