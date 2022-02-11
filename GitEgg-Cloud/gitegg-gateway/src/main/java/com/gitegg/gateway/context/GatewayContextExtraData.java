package com.gitegg.gateway.context;

/**
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 * @author: chenggang
 * @date 2019-11-04.
 */
public interface GatewayContextExtraData<T> {

    /**
     * get context extra data
     * @return
     */
    T getData();
}
