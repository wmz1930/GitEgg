package com.gitegg.service.extension.sms.factory;

import com.gitegg.platform.sms.service.ISmsSendService;
import com.gitegg.service.extension.sms.constant.SmsConstant;
import com.gitegg.service.extension.sms.dto.SmsTemplateDTO;
import com.gitegg.service.extension.sms.enums.SmsFactoryClassEnum;
import com.gitegg.service.extension.sms.service.ISmsTemplateService;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GitEgg
 */
@Component
public class SmsFactory {

    private final ISmsTemplateService smsTemplateService;

    /**
     * SmsSendService 缓存
     */
    private final static Map<Long, ISmsSendService> smsSendServiceMap = new ConcurrentHashMap<>();

    /**
     * 获取 SmsSendService
     *
     * @param smsTemplateDTO 短信模板
     * @return SmsSendService
     */
    public ISmsSendService getSmsSendService(SmsTemplateDTO smsTemplateDTO) {

        //根据channelId获取对应的发送短信服务接口，channelId是唯一的，每个租户有其自有的channelId
        Long channelId = smsTemplateDTO.getChannelId();
        ISmsSendService smsSendService = smsSendServiceMap.get(channelId);
        if (null == smsSendService) {
            Class cls = null;
            try {
                cls = Class.forName(SmsFactoryClassEnum.getValue(smsTemplateDTO.getChannelCode()));
                Method staticMethod = cls.getDeclaredMethod(SmsConstant.SMS_SERVICE_FUNCTION, SmsTemplateDTO.class);
                smsSendService = (ISmsSendService) staticMethod.invoke(cls,smsTemplateDTO);
                smsSendServiceMap.put(channelId, smsSendService);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return smsSendService;
    }

    public SmsFactory(ISmsTemplateService smsTemplateService) {
        this.smsTemplateService = smsTemplateService;
    }
}
