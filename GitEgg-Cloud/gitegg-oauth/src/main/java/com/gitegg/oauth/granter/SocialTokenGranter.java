package com.gitegg.oauth.granter;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.client.feign.IJustAuthFeign;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 第三方登录模式
 * @author GitEgg
 */
public class SocialTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "social";

    private final AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;
    
    private IJustAuthFeign justAuthFeign;

    private RedisTemplate redisTemplate;

    private String captchaType;
    
    private String secretKey;
    
    private String secretKeySalt;

    public SocialTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory, RedisTemplate redisTemplate, IJustAuthFeign justAuthFeign,
                              UserDetailsService userDetailsService, String captchaType, String secretKey, String secretKeySalt) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.redisTemplate = redisTemplate;
        this.captchaType = captchaType;
        this.secretKey = secretKey;
        this.secretKeySalt = secretKeySalt;
        this.justAuthFeign = justAuthFeign;
        this.userDetailsService = userDetailsService;
    }

    protected SocialTokenGranter(AuthenticationManager authenticationManager,
                                 AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                 OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());

        String socialKey = parameters.get(TokenConstant.SOCIAL_KEY);
        // Protect from downstream leaks of password
        parameters.remove(TokenConstant.SOCIAL_KEY);
    
        // 校验socialId
        String socialId;
        try {
            // 将socialId进行加密返回
            DES des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKeySalt.getBytes());
            String desSocialKey = des.decryptStr(socialKey);
            // 获取缓存中的key
            socialId = (String) redisTemplate.opsForValue().get(AuthConstant.SOCIAL_VALIDATION_PREFIX + desSocialKey);
        }
        catch (Exception e)
        {
            throw new InvalidGrantException("第三方登录验证已失效，请返回登录页重新操作");
        }
        
        if (StringUtils.isEmpty(socialId))
        {
            throw new InvalidGrantException("第三方登录验证已失效，请返回登录页重新操作");
        }
    
        // 校验userId
        String userId;
        try {
            Result<Object> socialResult = justAuthFeign.userBindQuery(Long.parseLong(socialId));
            if (null == socialResult || StringUtils.isEmpty(socialResult.getData())) {
                throw new InvalidGrantException("操作失败，请返回登录页重新操作");
            }
            userId = (String) socialResult.getData();
        }
        catch (Exception e)
        {
            throw new InvalidGrantException("操作失败，请返回登录页重新操作");
        }
    
        if (StringUtils.isEmpty(userId))
        {
            throw new InvalidGrantException("操作失败，请返回登录页重新操作");
        }
        
        // 这里是通过用户id查询用户信息
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}

