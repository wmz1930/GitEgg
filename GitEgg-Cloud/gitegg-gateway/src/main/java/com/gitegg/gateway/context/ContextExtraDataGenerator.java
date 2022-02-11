package com.gitegg.gateway.context;

import org.springframework.web.server.ServerWebExchange;

/**
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 * @author: chenggang
 * @date 2019-11-04.
 */
public interface ContextExtraDataGenerator<T> {

    /**
     * generate context extra data
     * @param serverWebExchange
     * @return
     */
    GatewayContextExtraData<T> generateContextExtraData(ServerWebExchange serverWebExchange);

}
