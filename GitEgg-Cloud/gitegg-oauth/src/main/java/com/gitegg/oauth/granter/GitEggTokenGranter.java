package com.gitegg.oauth.granter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gitegg.service.extension.client.feign.IJustAuthFeign;
import com.gitegg.service.extension.client.feign.ISmsFeign;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import com.anji.captcha.service.CaptchaService;
import com.gitegg.service.system.client.feign.IUserFeign;

/**
 * 自定义token
 * @author GitEgg
 */
public class GitEggTokenGranter {

    /**
     * 自定义tokenGranter
     */
    public static TokenGranter getTokenGranter(final AuthenticationManager authenticationManager,
                                               final AuthorizationServerEndpointsConfigurer endpoints, RedisTemplate redisTemplate, IUserFeign userFeign,
                                               ISmsFeign smsFeign, IJustAuthFeign justAuthFeign, CaptchaService captchaService, UserDetailsService userDetailsService,
                                               String captchaType, String secretKey, String secretKeySalt) {
        // 默认tokenGranter集合
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 增加验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
            endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, captchaService,
            captchaType));
        // 增加短信验证码模式
        granters.add(new SmsCaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, userFeign, smsFeign, captchaService,
                userDetailsService, captchaType));
        // 增加第三方登录模式
        granters.add(new SocialTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, justAuthFeign,
                userDetailsService, captchaType, secretKey, secretKeySalt));
        // 组合tokenGranter集合
        return new CompositeTokenGranter(granters);
    }

}
