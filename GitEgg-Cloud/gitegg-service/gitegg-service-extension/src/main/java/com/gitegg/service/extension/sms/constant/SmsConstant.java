package com.gitegg.service.extension.sms.constant;

/**
 * @author GitEgg
 */
public class SmsConstant {
    
    /**
     * 可尝试的验证次数
     */
    public static final String CHECK_TIMES_PREFIX = "CHECK_TIMES:";
    
    /**
     * 短信锁定的前缀
     */
    public static final String SMS_CACHE_LIMIT_PREFIX = "LIMIT:";

    /**
     * 获取短信发送的方法
     */
    public static final String SMS_SERVICE_FUNCTION = "getSmsSendService";
    
    /**
     * 短信默认获取间隔
     */
    public static final long SMS_INTERVAL = 60;
}
