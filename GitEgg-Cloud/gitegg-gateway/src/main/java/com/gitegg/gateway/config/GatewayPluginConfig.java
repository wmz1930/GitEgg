package com.gitegg.gateway.config;

import com.gitegg.gateway.context.ContextExtraDataGenerator;
import com.gitegg.gateway.filter.*;
import com.gitegg.gateway.properties.GatewayPluginProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quoted from @see https://github.com/chenggangpro/spring-cloud-gateway-plugin
 *
 * Gateway Plugin Config
 * @author chenggang
 * @date 2019/01/29
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "enable"}, havingValue = "true")
public class GatewayPluginConfig {
    
    /**
     * Gateway插件是否生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GatewayPluginProperties.class)
    @ConfigurationProperties(GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX)
    public GatewayPluginProperties gatewayPluginProperties(){
        return new GatewayPluginProperties();
    }
    
    /**
     * 缓存请求消息插件是否生效
     * logRequest sqlInjection xssInjection 其中任何一个插件生效即可
     * @return
     */
    @Bean
    @ConditionalOnBean(GatewayPluginProperties.class)
    @ConditionalOnMissingBean(GatewayRequestContextFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "enable" },havingValue = "true")
    public GatewayRequestContextFilter gatewayContextFilter(@Autowired GatewayPluginProperties gatewayPluginProperties , @Autowired(required = false) ContextExtraDataGenerator contextExtraDataGenerator){
        GatewayRequestContextFilter gatewayContextFilter = new GatewayRequestContextFilter(gatewayPluginProperties, contextExtraDataGenerator);
        log.debug("Load GatewayContextFilter Config Bean");
        return gatewayContextFilter;
    }
    
    /**
     * 缓存返回消息插件是否生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GatewayResponseContextFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "logRequest.enable", "logRequest.responseLog" }, havingValue = "true")
    public GatewayResponseContextFilter responseLogFilter(){
        GatewayResponseContextFilter responseLogFilter = new GatewayResponseContextFilter();
        log.debug("Load Response Log Filter Config Bean");
        return responseLogFilter;
    }
    
    /**
     * 清除缓存消息插件是否生效
     * @return
     */
    @Bean
    @ConditionalOnBean(GatewayPluginProperties.class)
    @ConditionalOnMissingBean(RemoveGatewayContextFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "enable" }, havingValue = "true")
    public RemoveGatewayContextFilter removeGatewayContextFilter(){
        RemoveGatewayContextFilter gatewayContextFilter = new RemoveGatewayContextFilter();
        log.debug("Load RemoveGatewayContextFilter Config Bean");
        return gatewayContextFilter;
    }
    
    /**
     * 记录日志插件是否生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RequestLogFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "logRequest.enable" },havingValue = "true")
    public RequestLogFilter requestLogFilter(@Autowired GatewayPluginProperties gatewayPluginProperties){
        RequestLogFilter requestLogFilter = new RequestLogFilter(gatewayPluginProperties);
        log.debug("Load Request Log Filter Config Bean");
        return requestLogFilter;
    }
    
    /**
     * sql注入拦截插件
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SqlInjectionFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "sqlInjection.enable" },havingValue = "true")
    public SqlInjectionFilter sqlInjectionFilter(@Autowired GatewayPluginProperties gatewayPluginProperties){
        SqlInjectionFilter sqlInjectionFilter = new SqlInjectionFilter(gatewayPluginProperties);
        log.debug("Load SQL Injection Filter Config Bean");
        return sqlInjectionFilter;
    }
    
    /**
     * xss注入拦截插件
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(XssInjectionFilter.class)
    @ConditionalOnProperty(prefix = GatewayPluginProperties.GATEWAY_PLUGIN_PROPERTIES_PREFIX, value = { "xssInjection.enable" },havingValue = "true")
    public XssInjectionFilter xssInjectionFilter(@Autowired GatewayPluginProperties gatewayPluginProperties){
        XssInjectionFilter xssInjectionFilter = new XssInjectionFilter(gatewayPluginProperties);
        log.debug("Load XSS Injection Filter Config Bean");
        return xssInjectionFilter;
    }
    
}
