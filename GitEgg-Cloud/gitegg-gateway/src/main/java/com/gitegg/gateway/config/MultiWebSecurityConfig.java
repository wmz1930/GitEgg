package com.gitegg.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.gitegg.gateway.auth.AuthorizationManager;
import com.gitegg.gateway.filter.WhiteListRemoveJwtFilter;
import com.gitegg.gateway.handler.AuthServerAccessDeniedHandler;
import com.gitegg.gateway.handler.AuthServerAuthenticationEntryPoint;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.oauth2.props.AuthUrlWhiteListProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * 权限配置
 * 注解需要使用@EnableWebFluxSecurity而非@EnableWebSecurity,因为SpringCloud Gateway基于WebFlux
 *
 * @author GitEgg
 *
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Configuration
@EnableWebFluxSecurity
public class MultiWebSecurityConfig {
    
    private final AuthorizationManager authorizationManager;
    
    private final AuthServerAccessDeniedHandler authServerAccessDeniedHandler;
    
    private final AuthServerAuthenticationEntryPoint authServerAuthenticationEntryPoint;
    
    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;
    
    private final WhiteListRemoveJwtFilter whiteListRemoveJwtFilter;
    
    private final SecurityProperties securityProperties;
    
    @Value("${management.endpoints.web.base-path:}")
    private String actuatorPath;
    
    /**
     * 健康检查接口权限配置
     * @param http
     * @return
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    @ConditionalOnProperty( value = {"management.security.enabled", "management.endpoints.enabled-by-default"}, havingValue = "true")
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        if (StringUtils.isEmpty(actuatorPath))
        {
            throw new BusinessException("当启用健康检查时，不允许健康检查的路径为空");
        }
        http
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .securityMatcher(new OrServerWebExchangeMatcher(
                        new PathPatternParserServerWebExchangeMatcher(actuatorPath + "/**"),
                        new PathPatternParserServerWebExchangeMatcher("/**" + actuatorPath + "/**")
                ))
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().hasAnyRole(securityProperties.getUser().getRoles().toArray(new String[0]))
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    
    /**
     * 设置Basic认证用户信息
     * @return
     */
    @Bean
    @ConditionalOnProperty( value = {"management.security.enabled", "management.endpoints.enabled-by-default"}, havingValue = "true")
    ReactiveUserDetailsService userDetailsService() {
        return new MapReactiveUserDetailsService(User
                .withUsername(securityProperties.getUser().getName())
                .password(passwordEncoder().encode(securityProperties.getUser().getPassword()))
                .roles(securityProperties.getUser().getRoles().toArray(new String[0]))
                .build());
    }
    
    /**
     * 设置密码编码
     * @return
     */
    @Bean
    @ConditionalOnProperty( value = {"management.security.enabled", "management.endpoints.enabled-by-default"}, havingValue = "true")
    public static PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return  delegatingPasswordEncoder;
    }
    
    /**
     * 路由转发权限配置
     * @param http
     * @return
     */
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    
        // 自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(authServerAuthenticationEntryPoint);
    
        // 对白名单路径，直接移除JWT请求头，不移除的话，后台会校验jwt
        http.addFilterBefore(whiteListRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
    
        // Basic认证直接放行
        if (!CollectionUtils.isEmpty(authUrlWhiteListProperties.getTokenUrls()))
        {
            http.authorizeExchange().pathMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getTokenUrls(), String.class)).permitAll();
        }
    
        // 判断是否有静态文件
        if (!CollectionUtils.isEmpty(authUrlWhiteListProperties.getStaticFiles()))
        {
            http.authorizeExchange().pathMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getStaticFiles(), String.class)).permitAll();
        }
    
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getWhiteUrls(), String.class)).permitAll()
                .anyExchange().access(authorizationManager)
                .and()
                .exceptionHandling()
                /**
                 * 处理未授权
                 */
                .accessDeniedHandler(authServerAccessDeniedHandler)
                /**
                 * 处理未认证
                 */
                .authenticationEntryPoint(authServerAuthenticationEntryPoint)
                .and()
                .cors()
                .and().csrf().disable();
    
        return http.build();
    }
    
    /**
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication，需要把jwt的Claim中的authorities加入
     * 解决方案：重新定义ReactiveAuthenticationManager权限管理器，默认转换器JwtGrantedAuthoritiesConverter
     */
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
