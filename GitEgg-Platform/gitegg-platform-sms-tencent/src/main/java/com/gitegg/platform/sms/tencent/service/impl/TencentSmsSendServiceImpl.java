package com.gitegg.platform.sms.tencent.service.impl;

import com.gitegg.platform.sms.domain.SmsData;
import com.gitegg.platform.sms.domain.SmsResponse;
import com.gitegg.platform.sms.service.ISmsSendService;
import com.gitegg.platform.sms.tencent.props.TencentSmsProperties;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 腾讯云短信发送
 */
@Slf4j
@AllArgsConstructor
public class TencentSmsSendServiceImpl implements ISmsSendService {

    private static final String successCode = "Ok";

    private final TencentSmsProperties properties;

    private final SmsClient client;

    @Override
    public SmsResponse sendSms(SmsData smsData, Collection<String> phoneNumbers) {
        SmsResponse smsResponse = new SmsResponse();

        SendSmsRequest request = new SendSmsRequest();
        request.setSmsSdkAppid(properties.getSmsSdkAppId());
        /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，可登录 [短信控制台] 查看签名信息 */
        request.setSign(properties.getSignName());
        /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
        if (!StringUtils.isEmpty(properties.getSenderId()))
        {
            request.setSenderId(properties.getSenderId());
        }
        request.setTemplateID(smsData.getTemplateId());
        /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
         * 例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
        String[] phoneNumbersArray = (String[]) phoneNumbers.toArray();
        request.setPhoneNumberSet(phoneNumbersArray);
        /* 模板参数: 若无模板参数，则设置为空*/
        String[] templateParams = new String[]{};
        if (!CollectionUtils.isEmpty(smsData.getParams())) {
            templateParams = (String[]) smsData.getParams().values().toArray();
        }
        request.setTemplateParamSet(templateParams);
        try {
            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse sendSmsResponse = client.SendSms(request);
            //如果是批量发送，那么腾讯云短信会返回每条短信的发送状态，这里默认返回第一条短信的状态
            if (null != sendSmsResponse && null != sendSmsResponse.getSendStatusSet()) {
                SendStatus sendStatus = sendSmsResponse.getSendStatusSet()[0];
                if (this.successCode.equals(sendStatus.getCode()))
                {
                    smsResponse.setSuccess(true);
                }
                else
                {
                    smsResponse.setCode(sendStatus.getCode());
                    smsResponse.setMessage(sendStatus.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Send Aliyun Sms Fail: {}", e);
            smsResponse.setMessage("Send Aliyun Sms Fail!");
        }
        return smsResponse;
    }
}
