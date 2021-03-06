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
 * ????????????
 * ??????????????????@EnableWebFluxSecurity??????@EnableWebSecurity,??????SpringCloud Gateway??????WebFlux
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
     * ??????????????????????????????
     * @param http
     * @return
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    @ConditionalOnProperty( value = {"management.security.enabled", "management.endpoints.enabled-by-default"}, havingValue = "true")
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        if (StringUtils.isEmpty(actuatorPath))
        {
            throw new BusinessException("???????????????????????????????????????????????????????????????");
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
     * ??????Basic??????????????????
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
     * ??????????????????
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
     * ????????????????????????
     * @param http
     * @return
     */
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    
        // ???????????????JWT???????????????????????????????????????
        http.oauth2ResourceServer().authenticationEntryPoint(authServerAuthenticationEntryPoint);
    
        // ?????????????????????????????????JWT?????????????????????????????????????????????jwt
        http.addFilterBefore(whiteListRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
    
        // Basic??????????????????
        if (!CollectionUtils.isEmpty(authUrlWhiteListProperties.getTokenUrls()))
        {
            http.authorizeExchange().pathMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getTokenUrls(), String.class)).permitAll();
        }
    
        // ???????????????????????????
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
                 * ???????????????
                 */
                .accessDeniedHandler(authServerAccessDeniedHandler)
                /**
                 * ???????????????
                 */
                .authenticationEntryPoint(authServerAuthenticationEntryPoint)
                .and()
                .cors()
                .and().csrf().disable();
    
        return http.build();
    }
    
    /**
     * ServerHttpSecurity?????????jwt???authorities?????????????????????Authentication????????????jwt???Claim??????authorities??????
     * ???????????????????????????ReactiveAuthenticationManager?????????????????????????????????JwtGrantedAuthoritiesConverter
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
