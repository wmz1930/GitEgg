package com.gitegg.platform.sms.tencent.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sms.tencent")
public class TencentSmsProperties {

    /* 填充请求参数，这里 request 对象的成员变量即对应接口的入参
     * 您可以通过官网接口文档或跳转到 request 对象的定义处查看请求参数的定义
     * 基本类型的设置:
     * 帮助链接：
     * 短信控制台：https://console.cloud.tencent.com/smsv2
     * sms helper：https://cloud.tencent.com/document/product/382/3773 */
    /* 短信应用 ID: 在 [短信控制台] 添加应用后生成的实际 SDKAppID，例如1400006666 */
    private String SmsSdkAppId;

    /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
    private String senderId;

    /* 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper] */
    private String extendCode;

    /**
     * 短信签名
     */
    private String signName;
}
