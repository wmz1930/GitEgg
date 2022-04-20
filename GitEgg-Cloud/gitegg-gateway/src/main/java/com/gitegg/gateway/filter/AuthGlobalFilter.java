package com.gitegg.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gitegg.platform.base.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * @author GitEgg
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String tenantId = exchange.getRequest().getHeaders().getFirst(AuthConstant.TENANT_ID);

        String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);

        if (StrUtil.isEmpty(tenantId) && StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }

        Map<String, String> addHeaders = new HashMap<>();

        // 如果系统配置已开启租户模式，设置tenantId
        if (enable && StrUtil.isEmpty(tenantId)) {
            addHeaders.put(AuthConstant.TENANT_ID, tenantId);
        }

        if (!StrUtil.isEmpty(token) && token.startsWith(AuthConstant.JWT_TOKEN_PREFIX)) {
            try {
                //从token中解析用户信息并设置到Header中去
                String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
                JWSObject jwsObject = JWSObject.parse(realToken);
                String userStr = jwsObject.getPayload().toString();
                log.info("AuthGlobalFilter.filter() User:{}", userStr);
                addHeaders.put(AuthConstant.HEADER_USER, URLEncoder.encode(userStr, "UTF-8"));

            } catch (ParseException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
         }

         Consumer<HttpHeaders> httpHeaders = httpHeader -> {
             addHeaders.forEach((k, v) -> {
                 httpHeader.set(k, v);
             });
         };

         ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders).build();
         exchange = exchange.mutate().request(request).build();
             return chain.filter(exchange);
         }

         @Override
         public int getOrder() {
        return 0;
    }
}
