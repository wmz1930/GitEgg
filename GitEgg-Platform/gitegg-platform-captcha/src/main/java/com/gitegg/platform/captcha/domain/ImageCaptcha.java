package com.gitegg.platform.captcha.domain;

import lombok.Data;

/**
 * 
 * @author GitEgg
 * @date 2020/12/07 22:37
 **/
@Data
public class ImageCaptcha {

    private String captchaKey;

    private String captchaImage;
}
