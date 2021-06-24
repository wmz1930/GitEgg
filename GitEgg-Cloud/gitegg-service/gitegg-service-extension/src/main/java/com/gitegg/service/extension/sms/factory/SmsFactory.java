package com.gitegg.service.extension.sms.factory;

import com.gitegg.platform.sms.service.SmsSendService;
import com.gitegg.service.extension.dto.SmsTemplateDTO;
import com.gitegg.service.extension.service.ISmsTemplateService;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SmsFactory {

    private final ISmsTemplateService smsTemplateService;

    /**
     * SmsSendService 缓存
     */
    private final Map<Long, SmsSendService> SmsSendServiceMap = new ConcurrentHashMap<>();

    public SmsFactory(ISmsTemplateService smsTemplateService) {
        this.smsTemplateService = smsTemplateService;
    }

    /**
     * 获取 SmsSendService
     *
     * @param smsTemplateDTO 短信模板
     * @return SmsSendService
     */
    public SmsSendService getSmsSendService(SmsTemplateDTO smsTemplateDTO) {

        //根据channelId获取对应的发送短信服务接口，channelId是唯一的，每个租户有其自有的channelId
        Long channelId = smsTemplateDTO.getChannelId();
        SmsSendService smsSendService = SmsSendServiceMap.get(channelId);
        if (null == smsSendService) {
            Class cls = null;
            try {
                cls = Class.forName("com.gitegg.service.extension.sms.factory.SmsAliyunFactory");
                Method staticMethod = cls.getDeclaredMethod("getSmsSendService", SmsTemplateDTO.class);
                smsSendService = (SmsSendService) staticMethod.invoke(cls,smsTemplateDTO);
                SmsSendServiceMap.put(channelId, smsSendService);
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
}
