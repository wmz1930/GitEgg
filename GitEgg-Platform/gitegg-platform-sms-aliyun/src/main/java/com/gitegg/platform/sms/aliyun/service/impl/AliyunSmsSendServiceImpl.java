package com.gitegg.platform.sms.aliyun.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.utils.StringUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.sms.domain.SmsData;
import com.gitegg.platform.sms.domain.SmsResponse;
import com.gitegg.platform.sms.service.ISmsSendService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gitegg.platform.sms.aliyun.props.AliyunSmsProperties;

import java.util.Collection;

/**
 * 阿里云短信发送
 */
@Slf4j
@AllArgsConstructor
public class AliyunSmsSendServiceImpl implements ISmsSendService {

    private static final String successCode = "OK";

    private final AliyunSmsProperties properties;

    private final IAcsClient acsClient;

    @Override
    public SmsResponse sendSms(SmsData smsData, Collection<String> phoneNumbers) {
        SmsResponse smsResponse = new SmsResponse();
        SendSmsRequest request = new SendSmsRequest();
        request.setSysMethod(MethodType.POST);
        request.setPhoneNumbers(StrUtil.join(",", phoneNumbers));
        request.setSignName(properties.getSignName());
        request.setTemplateCode(smsData.getTemplateId());
        request.setTemplateParam(JsonUtils.mapToJson(smsData.getParams()));
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (null != sendSmsResponse && !StringUtils.isEmpty(sendSmsResponse.getCode())) {
                if (this.successCode.equals(sendSmsResponse.getCode())) {
                    smsResponse.setSuccess(true);
                } else {
                    log.error("Send Aliyun Sms Fail: [code={}, message={}]", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
                }
                smsResponse.setCode(sendSmsResponse.getCode());
                smsResponse.setMessage(sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Send Aliyun Sms Fail: {}", e);
            smsResponse.setMessage("Send Aliyun Sms Fail!");
        }
        return smsResponse;
    }


}
