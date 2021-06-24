package com.gitegg.oauth.util;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.gitegg.platform.captcha.constant.CaptchaConstant;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author GitEgg
 * @date 2021-03-10 18:11:25
 **/
public class CaptchaUtils {

    public static boolean checkCaptcha(Map<String, String> parameters, RedisTemplate redisTemplate, CaptchaService captchaService)
    {
        // 获取验证码类型
        String captchaType = parameters.get(CaptchaConstant.CAPTCHA_TYPE);
        if (CaptchaConstant.IMAGE_CAPTCHA.equalsIgnoreCase(captchaType)) {
            // 图片验证码验证
            String captchaKey = parameters.get(CaptchaConstant.CAPTCHA_KEY);
            String captchaCode = parameters.get(CaptchaConstant.CAPTCHA_CODE);
            // 获取验证码
            String redisCode = (String)redisTemplate.opsForValue().get(CaptchaConstant.IMAGE_CAPTCHA_KEY + captchaKey);
            // 判断验证码
            if (captchaCode == null || !captchaCode.equalsIgnoreCase(redisCode)) {
                return false;
            }
        } else {
            // 滑动验证码验证
            String captchaVerification = parameters.get(CaptchaConstant.CAPTCHA_VERIFICATION);
            String slidingCaptchaType = parameters.get(CaptchaConstant.SLIDING_CAPTCHA_TYPE);
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerification);
            captchaVO.setCaptchaType(slidingCaptchaType);
            ResponseModel responseModel = captchaService.verification(captchaVO);
            if (null == responseModel || !RepCodeEnum.SUCCESS.getCode().equals(responseModel.getRepCode())) {
                return false;
            }
        }

        return true;

    }
}
