package com.gitegg.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 *
 * Gateway Plugin Properties
 * @author chenggang
 * @date 2019/04/12
 */
@Slf4j
@Getter
@Setter
@ToString
public class GatewayPluginProperties implements InitializingBean {

    public static final String GATEWAY_PLUGIN_PROPERTIES_PREFIX = "spring.cloud.gateway.plugin.config";

    /**
     * Enable Or Disable Log Request Detail
     */
    private Boolean enable = false;

    /**
     * Enable Or Disable Read Request Data
     */
    private Boolean requestLog = false;

    /**
     * Enable Or Disable Read Response Data
     */
    private Boolean responseLog = false;

    /**
     * logType
     * all： 所有日志
     * configure:serviceId和pathList交集
     * serviceId： 只记录serviceId配置列表
     * pathList：只记录pathList配置列表
     */
    private String logType = "all";

    /**
     * Enable Read Request Data When use discover route by serviceId
     */
    private List<String> serviceIdList = Collections.emptyList();

    /**
     * Enable Read Request Data by specific path
     */
    private List<String> pathList = Collections.emptyList();

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!CollectionUtils.isEmpty(serviceIdList)){
            serviceIdList = serviceIdList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
        if(!CollectionUtils.isEmpty(pathList)){
            pathList = pathList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }
}
