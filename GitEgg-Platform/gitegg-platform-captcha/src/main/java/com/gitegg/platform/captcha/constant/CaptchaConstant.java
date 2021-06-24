package com.gitegg.platform.captcha.constant;

public class CaptchaConstant {

    /**
     * 图片验证码redis缓存的key
     */
    public static final String IMAGE_CAPTCHA_KEY = "CAPTCHA:KEY:";

    /**
     * 验证码类型：图片验证码 滑动验证码
     */
    public final static String CAPTCHA_TYPE = "captcha_type";

    /**
     * 滑动验证码类型：clickWord  blockPuzzle
     */
    public final static String SLIDING_CAPTCHA_TYPE = "sliding_type";

    /**
     * 图片验证码
     */
    public final static String IMAGE_CAPTCHA = "image";

    /**
     * 验证码的key
     */
    public final static String CAPTCHA_KEY = "captcha_key";

    /**
     * 验证码的值
     */
    public final static String CAPTCHA_CODE = "captcha_code";

    /**
     * 滑动验证码的验证校验
     */
    public final static String CAPTCHA_VERIFICATION = "captcha_verification";

}
