package com.gitegg.service.extension.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.sms.domain.SmsData;
import com.gitegg.platform.sms.domain.SmsResponse;
import com.gitegg.platform.sms.service.ISmsSendService;
import com.gitegg.service.extension.sms.constant.SmsConstant;
import com.gitegg.service.extension.sms.dto.QuerySmsTemplateDTO;
import com.gitegg.service.extension.sms.dto.SmsTemplateDTO;
import com.gitegg.service.extension.sms.factory.SmsFactory;
import com.gitegg.service.extension.sms.service.ISmsService;
import com.gitegg.service.extension.sms.service.ISmsTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 短信发送接口实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-01-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsServiceImpl implements ISmsService {

    private final SmsFactory smsFactory;

    private final ISmsTemplateService smsTemplateService;

    private final RedisTemplate redisTemplate;

    @Override
    public SmsResponse sendSmsNormal(String smsCode, String smsData, String phoneNumbers) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            QuerySmsTemplateDTO querySmsTemplateDTO = new QuerySmsTemplateDTO();
            querySmsTemplateDTO.setSmsCode(smsCode);
            //获取短信code的相关信息，租户信息会根据mybatis plus插件获取
            SmsTemplateDTO smsTemplateDTO = smsTemplateService.querySmsTemplate(querySmsTemplateDTO);
            ObjectMapper mapper = new ObjectMapper();
            Map smsDataMap = mapper.readValue(smsData, Map.class);

            List<String> phoneNumberList =  JsonUtils.jsonToList(phoneNumbers, String.class);
            SmsData smsDataParam = new SmsData();
            smsDataParam.setTemplateId(smsTemplateDTO.getTemplateId());
            smsDataParam.setParams(smsDataMap);
            ISmsSendService smsSendService = smsFactory.getSmsSendService(smsTemplateDTO);
            smsResponse = smsSendService.sendSms(smsDataParam, phoneNumberList);
        } catch (Exception e) {
            smsResponse.setMessage("短信发送失败");
            e.printStackTrace();
        }
        return smsResponse;
    }

    @Override
    public SmsResponse sendSmsVerificationCode(String smsCode, String phoneNumber) {
        String verificationCode = RandomUtil.randomNumbers(6);
        Map<String, String> smsDataMap = new HashMap<>();
        smsDataMap.put(SmsConstant.SMS_CAPTCHA_TEMPLATE_CODE, verificationCode);
        List<String> phoneNumbers = Arrays.asList(phoneNumber);
        SmsResponse smsResponse = this.sendSmsNormal(smsCode, JsonUtils.mapToJson(smsDataMap), JsonUtils.listToJson(phoneNumbers));
        if (null != smsResponse && smsResponse.isSuccess()) {
            // 将短信验证码存入redis并设置过期时间为5分钟
            redisTemplate.opsForValue().set(SmsConstant.SMS_CAPTCHA_KEY + smsCode + phoneNumber, verificationCode, 30,
                    TimeUnit.MINUTES);
        }
        return smsResponse;
    }

    @Override
    public boolean checkSmsVerificationCode(String smsCode, String phoneNumber, String verificationCode) {
        String verificationCodeRedis = (String) redisTemplate.opsForValue().get(SmsConstant.SMS_CAPTCHA_KEY + smsCode + phoneNumber);
        if (!StrUtil.isAllEmpty(verificationCodeRedis, verificationCode) && verificationCode.equalsIgnoreCase(verificationCodeRedis)) {
            return true;
        }
        return false;
    }
}
