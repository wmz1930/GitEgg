package com.gitegg.oauth.granter;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gitegg.oauth.util.CaptchaUtils;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.captcha.constant.CaptchaConstant;

/**
 * 验证码模式
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "captcha";

    private final AuthenticationManager authenticationManager;

    private RedisTemplate redisTemplate;

    private CaptchaService captchaService;

    private String captchaType;

    public CaptchaTokenGranter(AuthenticationManager authenticationManager,
        AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
        OAuth2RequestFactory requestFactory, RedisTemplate redisTemplate, CaptchaService captchaService,
        String captchaType) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.redisTemplate = redisTemplate;
        this.captchaService = captchaService;
        this.captchaType = captchaType;
    }

    protected CaptchaTokenGranter(AuthenticationManager authenticationManager,
        AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
        OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        if (!checkCaptchaResult)
        {
            throw new UserDeniedAuthorizationException(ResultCodeEnum.INVALID_CAPTCHA.getMsg());
        }
        String username = parameters.get(TokenConstant.USER_NAME);
        String password = parameters.get(TokenConstant.PASSWORD);
        // Protect from downstream leaks of password
        parameters.remove(TokenConstant.PASSWORD);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        // If the username/password are wrong the spec says we should send 400/invalid grant

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
