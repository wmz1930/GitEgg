package com.gitegg.gateway.filter;

import com.gitegg.gateway.context.GatewayContext;
import com.gitegg.gateway.option.FilterOrderEnum;
import com.gitegg.gateway.properties.GatewayPluginProperties;
import com.gitegg.gateway.util.SqlInjectionRuleUtils;
import com.gitegg.gateway.util.WebfluxResponseUtils;
import com.gitegg.gateway.util.XssInjectionRuleUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 防xss注入
 * @author GitEgg
 */
@Log4j2
@AllArgsConstructor
public class XssInjectionFilter implements GlobalFilter, Ordered {

    private static final String HTTP_SCHEME = "http";

    private static final String HTTPS_SCHEME = "https";

    private GatewayPluginProperties gatewayPluginProperties;
    
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        
        ServerHttpRequest request = exchange.getRequest();
        
        URI requestURI = request.getURI();
        
        String scheme = requestURI.getScheme();
        
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        
        /*
         * not http or https scheme
         */
        if ((!HTTP_SCHEME.equalsIgnoreCase(scheme) && !HTTPS_SCHEME.equals(scheme)) || !gatewayContext.getReadRequestData()){
            return chain.filter(exchange);
        }

        // 当返回参数为true时，记录请求参数和返回参数
        if (shouldXssInjection(exchange))
        {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            boolean chkRetGetParams = XssInjectionRuleUtils.mapRequestSqlKeyWordsCheck(queryParams);
    
            boolean chkRetJson = false;
            boolean chkRetFormData = false;
            
            HttpHeaders headers = request.getHeaders();
            MediaType contentType = headers.getContentType();
            long length = headers.getContentLength();

            if(length > 0 && null != contentType && (contentType.includes(MediaType.APPLICATION_JSON)
                    ||contentType.includes(MediaType.APPLICATION_JSON_UTF8))){
                chkRetJson = XssInjectionRuleUtils.jsonRequestSqlKeyWordsCheck(gatewayContext.getRequestBody());
            }
            
            if(length > 0 && null != contentType  && contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)){
                log.debug("[RequestLogFilter](Request)FormData:{}",gatewayContext.getFormData());
                chkRetFormData = XssInjectionRuleUtils.mapRequestSqlKeyWordsCheck(gatewayContext.getFormData());
            }
            
            if (chkRetGetParams || chkRetJson || chkRetFormData)
            {
                return WebfluxResponseUtils.responseWrite(exchange, "参数中不允许存在XSS注入关键字");
            }
            return chain.filter(exchange);
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
     * 因为加入了XSS注入拦截，这里单独判断
     * @return boolean
     */
    private boolean shouldXssInjection(ServerWebExchange exchange){
        
        if((gatewayPluginProperties.getXssInjection().getEnable()
                && CollectionUtils.isEmpty(gatewayPluginProperties.getXssInjection().getServiceIdList())
                && CollectionUtils.isEmpty(gatewayPluginProperties.getXssInjection().getPathList()))
                ){
            log.debug("[GatewayContext]Properties Set Read All Request Data");
            return true;
        }
        
        boolean serviceFlag = false;
        boolean pathFlag = false;
        
        List<String> readRequestDataServiceIdList = gatewayPluginProperties.getXssInjection().getServiceIdList();
        
        List<String> readRequestDataPathList = gatewayPluginProperties.getXssInjection().getPathList();
        
        // 因为请求的路径太多，防注入采取白名单模式，如果配置了地址，那么就放过，所以不需要进行参数解析
        if(!CollectionUtils.isEmpty(readRequestDataPathList)){
            String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
            for(String path : readRequestDataPathList){
                if(ANT_PATH_MATCHER.match(path,requestPath)){
                    log.debug("[GatewayContext]Properties Set Not Read Specific Request Data With Request Path:{},Math Pattern:{}", requestPath, path);
                    pathFlag =  true;
                    break;
                }
            }
        }
        
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI routeUri = route.getUri();
        
        String routeServiceId = routeUri.getHost().toLowerCase();
        if(!CollectionUtils.isEmpty(readRequestDataServiceIdList)){
            if(readRequestDataServiceIdList.contains(routeServiceId)){
                log.debug("[GatewayContext]Properties Set Not Read Specific Request Data With ServiceId:{}",routeServiceId);
                serviceFlag =  true;
            }
        }
        
        if (serviceFlag && pathFlag)
        {
            return false;
        }
        
        return true;
    }

}
