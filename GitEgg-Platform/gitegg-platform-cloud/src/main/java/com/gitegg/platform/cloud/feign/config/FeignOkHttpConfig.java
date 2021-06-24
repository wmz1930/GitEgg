//package com.gitegg.platform.cloud.feign.config;
//
//import feign.Feign;
//import okhttp3.ConnectionPool;
//import okhttp3.OkHttpClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.openfeign.FeignAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author GitEgg
// * @date 2021-03-30 17:51:11
// **/
//@Configuration
//@ConditionalOnClass(Feign.class)
//@AutoConfigureBefore(FeignAutoConfiguration.class)
//public class FeignOkHttpConfig {
//
//    @Bean
//    @LoadBalanced
//    public OkHttpClient okHttpClient() {
//        return new OkHttpClient().newBuilder().retryOnConnectionFailure(true).connectionPool(pool())
//                .connectTimeout(100000, TimeUnit.SECONDS)
//                .readTimeout(100000, TimeUnit.SECONDS)
//                .writeTimeout(100000, TimeUnit.SECONDS)
//                .build();
//    }
//
//    @Bean
//    public ConnectionPool pool() {
//        return new ConnectionPool(50, 5, TimeUnit.MINUTES);
//    }
//}
