package com.gitegg.service.extension.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.sms.domain.SmsData;
import com.gitegg.platform.sms.service.ISmsSendService;
import com.gitegg.service.extension.sms.constant.SmsConstant;
import com.gitegg.service.extension.sms.dto.QuerySmsTemplateDTO;
import com.gitegg.service.extension.sms.dto.SmsTemplateDTO;
import com.gitegg.service.extension.sms.factory.SmsFactory;
import com.gitegg.service.extension.sms.props.SmsProperties;
import com.gitegg.service.extension.sms.service.ISmsService;
import com.gitegg.service.extension.sms.service.ISmsTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    
    private final SmsProperties smsProperties;
    
    /**
     * 发送普通短信
     * @param smsCode
     * @param smsData
     * @param phoneNumbers
     * @return
     */
    @Override
    public Result<?> sendSmsNormal(String smsCode, String smsData, String phoneNumbers) {
        try {
            QuerySmsTemplateDTO querySmsTemplateDTO = new QuerySmsTemplateDTO();
            querySmsTemplateDTO.setSmsCode(smsCode);
            querySmsTemplateDTO.setTemplateStatus(GitEggConstant.ENABLE);
            querySmsTemplateDTO.setChannelStatus(GitEggConstant.ENABLE);
            // 根据短信code获取短信模板配置和渠道，如果多个渠道可用，那么随机取一个
            List<SmsTemplateDTO> smsTemplateDTOList = smsTemplateService.querySmsTemplateList(querySmsTemplateDTO);
            
            if (!CollectionUtils.isEmpty(smsTemplateDTOList))
            {
                // 存在多个模板，则随机取一个
                int index = RandomUtil.randomInt(smsTemplateDTOList.size());
                SmsTemplateDTO smsTemplateDTO = smsTemplateDTOList.get(index);
                ObjectMapper mapper = new ObjectMapper();
                Map smsDataMap = mapper.readValue(smsData, Map.class);
                List<String> phoneNumberList =  JsonUtils.jsonToList(phoneNumbers, String.class);
    
                // 单条短信验证码做数量限制，批量发送时，不做限制
                if (phoneNumberList.size() == GitEggConstant.ZERO_LONG && !this.checkSmsLimit(smsTemplateDTO, phoneNumberList.get(GitEggConstant.COUNT_ZERO)))
                {
                    return Result.error(ResultCodeEnum.SMS_LIMIT);
                }
                SmsData smsDataParam = new SmsData();
                smsDataParam.setTemplateId(smsTemplateDTO.getTemplateId());
                smsDataParam.setParams(smsDataMap);
                ISmsSendService smsSendService = smsFactory.getSmsSendService(smsTemplateDTO);
                Result result = smsSendService.sendSms(smsDataParam, phoneNumberList);
                smsTemplateDTO.setSecretId(null);
                smsTemplateDTO.setSecretKey(null);
                smsTemplateDTO.setRegionId(null);
                result.setData(smsTemplateDTO);
                return result;
            }
            else
            {
                return Result.error(ResultCodeEnum.SMS_TEMPLATE_NOT_FOUND);
            }

        } catch (Exception e) {
            log.error("Send Sms Fail: {}", e);
        }
        return Result.error(ResultCodeEnum.SMS_SEND_ERROR);
    }
    
    /**
     * 发送短信验证码
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    @Override
    public Result<?> sendSmsVerificationCode(String smsCode, String phoneNumber) {
        String verificationCode = RandomUtil.randomNumbers(smsProperties.getCodeLength());
        Map<String, String> smsDataMap = new HashMap<>();
        smsDataMap.put(smsProperties.getCodeKey(), verificationCode);
        List<String> phoneNumbers = Arrays.asList(phoneNumber);
        Result result = this.sendSmsNormal(smsCode, JsonUtils.mapToJson(smsDataMap), JsonUtils.listToJson(phoneNumbers));
        if (null != result && result.isSuccess()) {
            SmsTemplateDTO smsTemplateDTO = (SmsTemplateDTO) result.getData();
            String redisKey = this.getRedisKeyPrefix(smsCode, phoneNumber, StrUtil.EMPTY);
            Long cacheTimeOut = null == smsTemplateDTO.getCacheTimeOut() ? smsProperties.getExpirationTime() : smsTemplateDTO.getCacheTimeOut();
            TimeUnit cacheTimeOutUnit = StringUtils.isEmpty(smsTemplateDTO.getCacheTimeOutUnit()) ? TimeUnit.valueOf(smsProperties.getExpirationTimeUnit()) : TimeUnit.valueOf(smsTemplateDTO.getCacheTimeOutUnit());
            // 将短信验证码存入redis并设置过期时间
            redisTemplate.opsForValue().set(redisKey, verificationCode, cacheTimeOut, cacheTimeOutUnit);
            result.setData(true);
            // 发送成功，设置计数一次
            String redisKeyLimit = this.getRedisKeyPrefix(smsTemplateDTO.getSmsCode(), phoneNumber, SmsConstant.SMS_CACHE_LIMIT_PREFIX);
            redisTemplate.opsForValue().increment(redisKeyLimit, GitEggConstant.ONE_LONG);
            // 发送成功，就把校验计数删除
            String redisKeyLimitTimes = this.getRedisKeyPrefix(smsCode, phoneNumber, SmsConstant.CHECK_TIMES_PREFIX);
            redisTemplate.delete(redisKeyLimitTimes);
        }
        return result;
    }
    
    /**
     * 校验短信验证码
     * @param smsCode
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    @Override
    public Result<?> checkSmsVerificationCode(String smsCode, String phoneNumber, String verificationCode) {
    
        // 超过验证次数，直接删除验证码，并重新获取
        if (!this.checkVerificationCodeTimes(smsCode, phoneNumber))
        {
            return Result.data(false, ResultCodeEnum.SMS_CODE_EXPIRE_OR_EXPIRE.msg);
        }
        
        String redisKey = this.getRedisKeyPrefix(smsCode, phoneNumber, StrUtil.EMPTY);
        String verificationCodeRedis = (String) redisTemplate.opsForValue().get(redisKey);
    
        // 验证码不能为空
        if (StringUtils.isEmpty(verificationCode)) {
            return Result.data(false, ResultCodeEnum.SMS_CODE_EXPIRE.msg);
        }
        
        // 验证码已过期失效
        if (StringUtils.isEmpty(verificationCodeRedis)) {
            return Result.data(false, ResultCodeEnum.SMS_CODE_EXPIRE.msg);
        }
    
        // 验证码不正确
        if (!verificationCode.equalsIgnoreCase(verificationCodeRedis)) {
            // 根据配置判断是否，校验一次即删掉，然后重新获取验证码
            if (smsProperties.getDeleteByVerifyOnce())
            {
                redisTemplate.delete(redisKey);
            }
            return Result.data(false, ResultCodeEnum.SMS_CODE_ERROR.msg);
        }
        redisTemplate.delete(redisKey);
        
        return Result.data(true);
    }
    
    /**
     * 前置校验短信验证码，使用场景为输入框输入之后进行实时校验
     * @param smsCode
     * @param phoneNumber
     * @param verificationCode
     * @return
     */
    @Override
    public Result<?> checkSmsVerificationCodePre(String smsCode, String phoneNumber, String verificationCode) {
        
        // 超过验证次数，直接删除验证码，并重新获取
        if (!this.checkVerificationCodeTimes(smsCode, phoneNumber))
        {
            return Result.data(false, ResultCodeEnum.SMS_CODE_EXPIRE_OR_EXPIRE.msg);
        }
    
        String redisKey = this.getRedisKeyPrefix(smsCode, phoneNumber, StrUtil.EMPTY);
        String verificationCodeRedis = (String) redisTemplate.opsForValue().get(redisKey);
        // 验证码错误或已失效
        if (StringUtils.isEmpty(verificationCode) || StringUtils.isEmpty(verificationCodeRedis)
        || !verificationCode.equalsIgnoreCase(verificationCodeRedis)) {
            return Result.data(false, ResultCodeEnum.SMS_CODE_EXPIRE_OR_EXPIRE.msg);
        }
        
        // 如果正确的话，就把计数删除
        String redisKeyLimitTimes = this.getRedisKeyPrefix(smsCode, phoneNumber, SmsConstant.CHECK_TIMES_PREFIX);
        redisTemplate.delete(redisKeyLimitTimes);
        
        return Result.data(true);
    }
    
    /**
     * 校验短信验证码前置验证次数，超过验证次数直接删除验证码
     * @param smsCode
     * @param phoneNumber
     * @return
     */
    private boolean checkVerificationCodeTimes(String smsCode, String phoneNumber){
        if (null != smsProperties.getRetryVerificationTimes() && smsProperties.getRetryVerificationTimes().intValue() > GitEggConstant.ZERO_LONG)
        {
            String redisKey = this.getRedisKeyPrefix(smsCode, phoneNumber, SmsConstant.CHECK_TIMES_PREFIX);
            // 计数
            Long limitCurrent = redisTemplate.opsForValue().increment(redisKey, GitEggConstant.ONE_LONG);
            if (null != limitCurrent && limitCurrent.longValue() > smsProperties.getRetryVerificationTimes().longValue())
            {
                // 如果超过最大次数，那么直接删除验证码，重新获取
                String redisCodeKey = this.getRedisKeyPrefix(smsCode, phoneNumber, StrUtil.EMPTY);
                redisTemplate.delete(redisCodeKey);
                return false;
            }
        }
        return true;
    }
    
    /**
     * 校验某个模板每天发送次数限制
     * @param smsTemplateDTO
     * @param phoneNumber
     * @return
     */
    private boolean checkSmsLimit(SmsTemplateDTO smsTemplateDTO, String phoneNumber){
        if (null != smsTemplateDTO.getSendTimesLimit() && smsTemplateDTO.getSendTimesLimit().longValue() > GitEggConstant.ZERO_LONG)
        {
            String redisKey = this.getRedisKeyPrefix(smsTemplateDTO.getSmsCode(), phoneNumber, SmsConstant.SMS_CACHE_LIMIT_PREFIX);
            Long limitCurrent = (Long) redisTemplate.opsForValue().get(redisKey);
            if (null != limitCurrent && limitCurrent.longValue() >= smsTemplateDTO.getSendTimesLimit().longValue())
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取验证码缓存key
     * @param smsCode
     * @param phoneNumber
     * @param keyType
     * @return
     */
    private String getRedisKeyPrefix(String smsCode, String phoneNumber, String keyType){
        StringBuffer redisKeyBuffer = new StringBuffer(smsProperties.getCacheKey());
        if (!smsProperties.getCacheKey().endsWith(StrUtil.COLON))
        {
            redisKeyBuffer.append(StrUtil.COLON);
        }
        redisKeyBuffer.append(smsCode).append(StrUtil.COLON).append(StringUtils.isEmpty(keyType)? StrUtil.EMPTY : keyType).append(phoneNumber);
        return redisKeyBuffer.toString();
    }
}
