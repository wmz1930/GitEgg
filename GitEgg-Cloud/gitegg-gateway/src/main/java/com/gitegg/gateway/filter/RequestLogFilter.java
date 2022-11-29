package com.gitegg.gateway.filter;

import com.gitegg.gateway.context.GatewayContext;
import com.gitegg.gateway.domain.GatewayApiLog;
import com.gitegg.gateway.option.FilterOrderEnum;
import com.gitegg.gateway.properties.GatewayPluginProperties;
import com.gitegg.gateway.util.IpUtils;
import com.gitegg.platform.base.constant.LogLevelConstant;
import com.gitegg.platform.base.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 *
 * Filter To Log Request And Response(exclude response body)
 * @author chenggang
 * @date 2019/01/29
 */
@Log4j2
@AllArgsConstructor
public class RequestLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    private static final String HTTP_SCHEME = "http";

    private static final String HTTPS_SCHEME = "https";

    private GatewayPluginProperties gatewayPluginProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        String scheme = requestURI.getScheme();
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        /*
         * not http or https scheme
         */
        if ((!HTTP_SCHEME.equalsIgnoreCase(scheme)
                && !HTTPS_SCHEME.equals(scheme))
                || !gatewayContext.getReadRequestData()){
            return chain.filter(exchange);
        }

        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put(START_TIME, startTime);

        // 当返回参数为true时，记录请求参数和返回参数
        if (gatewayPluginProperties.getLogRequest().getEnable())
        {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> logApiRequest(exchange)));
        }
        else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.REQUEST_LOG_FILTER.getOrder();
    }

    /**
     * log api request
     * @param exchange
     */
    private Mono<Void> logApiRequest(ServerWebExchange exchange){

        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        String scheme = requestURI.getScheme();

        Long startTime = exchange.getAttribute(START_TIME);
        Long endTime = System.currentTimeMillis();
        Long duration = ( endTime - startTime);
        ServerHttpResponse response = exchange.getResponse();

        GatewayApiLog gatewayApiLog = new GatewayApiLog();
        gatewayApiLog.setClientHost(requestURI.getHost());
        gatewayApiLog.setClientIp(IpUtils.getIP(request));
        gatewayApiLog.setStartTime(startTime);
        gatewayApiLog.setEndTime(startTime);
        gatewayApiLog.setDuration(duration);
        gatewayApiLog.setMethod(request.getMethodValue());
        gatewayApiLog.setScheme(scheme);
        gatewayApiLog.setRequestUri(requestURI.getPath());
        gatewayApiLog.setResponseCode(String.valueOf(response.getRawStatusCode()));

        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        // 记录参数请求日志
        if (gatewayPluginProperties.getLogRequest().getRequestLog())
        {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            if(!queryParams.isEmpty()){
                queryParams.forEach((key,value)-> log.debug("[RequestLogFilter](Request)Query Param :Key->({}),Value->({})",key,value));
                gatewayApiLog.setQueryParams(JsonUtils.mapToJson(queryParams));
            }
            HttpHeaders headers = request.getHeaders();
            MediaType contentType = headers.getContentType();
            long length = headers.getContentLength();
            log.debug("[RequestLogFilter](Request)ContentType:{},Content Length:{}",contentType,length);
            if(length>0 && null != contentType && (contentType.includes(MediaType.APPLICATION_JSON)
                    ||contentType.includes(MediaType.APPLICATION_JSON_UTF8))){
                log.debug("[RequestLogFilter](Request)JsonBody:{}",gatewayContext.getRequestBody());
                gatewayApiLog.setRequestBody(gatewayContext.getRequestBody());
            }
            if(length>0 && null != contentType  && contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)){
                log.debug("[RequestLogFilter](Request)FormData:{}",gatewayContext.getFormData());
                gatewayApiLog.setRequestBody(JsonUtils.mapToJson(gatewayContext.getFormData()));
            }
        }

        // 记录参数返回日志
        if (gatewayPluginProperties.getLogRequest().getResponseLog())
        {
            log.debug("[RequestLogFilter](Response)HttpStatus:{}",response.getStatusCode());
            HttpHeaders headers = response.getHeaders();
            headers.forEach((key,value)-> log.debug("[RequestLogFilter]Headers:Key->{},Value->{}",key,value));
            MediaType contentType = headers.getContentType();
            long length = headers.getContentLength();
            log.info("[RequestLogFilter](Response)ContentType:{},Content Length:{}", contentType, length);


            log.debug("[RequestLogFilter](Response)Response Body:{}", gatewayContext.getResponseBody());
            try {
                gatewayApiLog.setResponseBody(JsonUtils.objToJson(gatewayContext.getResponseBody()));
            } catch (Exception e) {
                log.error("记录API日志返回数据转换JSON发生错误:{}", e);
            }

            log.debug("[RequestLogFilter](Response)Original Path:{},Cost:{} ms", exchange.getRequest().getURI().getPath(), duration);
        }

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI routeUri = route.getUri();
        String routeServiceId = routeUri.getHost().toLowerCase();
        // API日志记录级别
        try {
            log.log(LogLevelConstant.API_LEVEL,"{\"serviceId\":{}, \"data\":{}}", routeServiceId, JsonUtils.objToJson(gatewayApiLog));
        } catch (Exception e) {
            log.error("记录API日志数据发生错误:{}", e);
        }

        return Mono.empty();
    }

}
