package com.gitegg.service.base.component;

import com.gitegg.service.base.service.IDictBusinessService;
import com.gitegg.service.base.service.IDictService;
import com.gitegg.service.base.service.IDistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成加载基础数据到缓存
 * @author GitEgg
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class InitBaseCacheRunner implements CommandLineRunner {
    
    /**
     * 系统数据字典表
     */
    private final IDictService dictService;
    
    /**
     * 业务数据字典表
     */
    private final IDictBusinessService dictBusinessService;
    
    /**
     * 系统数据字典表
     */
    private final IDistrictService districtService;

    @Override
    public void run(String... args) {

        log.info("InitBaseCacheRunner running");
        
        // 初始化系统数据字典
        dictService.initDictConfig();
    
        // 初始化业务数据字典
        dictBusinessService.initDictBusinessConfig();

    }
}

