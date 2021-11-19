package com.gitegg.gateway.config;

import com.gitegg.platform.oauth2.props.AuthUrlWhiteListProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.gitegg.gateway.auth.AuthorizationManager;
import com.gitegg.gateway.filter.WhiteListRemoveJwtFilter;
import com.gitegg.gateway.handler.AuthServerAccessDeniedHandler;
import com.gitegg.gateway.handler.AuthServerAuthenticationEntryPoint;
import com.gitegg.platform.base.constant.AuthConstant;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 * 注解需要使用@EnableWebFluxSecurity而非@EnableWebSecurity,因为SpringCloud Gateway基于WebFlux
 *
 * @author Administrator
 * @author GitEgg
 *
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class AuthResourceServerConfig {

    private final AuthorizationManager authorizationManager;

    private final AuthServerAccessDeniedHandler authServerAccessDeniedHandler;

    private final AuthServerAuthenticationEntryPoint authServerAuthenticationEntryPoint;

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    private final WhiteListRemoveJwtFilter whiteListRemoveJwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());

        // 自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(authServerAuthenticationEntryPoint);

        // 对白名单路径，直接移除JWT请求头，不移除的话，后台会校验jwt
        http.addFilterBefore(whiteListRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

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

