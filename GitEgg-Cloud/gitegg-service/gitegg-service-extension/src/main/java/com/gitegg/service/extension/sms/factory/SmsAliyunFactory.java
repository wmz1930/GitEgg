package com.gitegg.service.extension.sms.factory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.gitegg.platform.sms.aliyun.props.AliyunSmsProperties;
import com.gitegg.platform.sms.aliyun.service.impl.AliyunSmsSendServiceImpl;
import com.gitegg.platform.sms.service.SmsSendService;
import com.gitegg.service.extension.dto.SmsTemplateDTO;

/**
 * 阿里云短信服务接口工厂类
 */
public class SmsAliyunFactory {

    public static SmsSendService getSmsSendService(SmsTemplateDTO sms) {
        AliyunSmsProperties aliyunSmsProperties = new AliyunSmsProperties();
        aliyunSmsProperties.setAccessKeyId(sms.getSecretId());
        aliyunSmsProperties.setAccessKeySecret(sms.getSecretKey());
        aliyunSmsProperties.setRegionId(sms.getRegionId());
        aliyunSmsProperties.setSignName(sms.getSignName());
        IClientProfile profile = DefaultProfile.getProfile(aliyunSmsProperties.getRegionId(), aliyunSmsProperties.getAccessKeyId(), aliyunSmsProperties.getAccessKeySecret());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return new AliyunSmsSendServiceImpl(aliyunSmsProperties, acsClient);
    }

}
