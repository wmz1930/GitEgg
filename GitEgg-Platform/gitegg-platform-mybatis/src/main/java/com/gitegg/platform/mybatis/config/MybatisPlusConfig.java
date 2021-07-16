package com.gitegg.platform.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.gitegg.platform.mybatis.handler.GitEggDataPermissionHandler;
import com.gitegg.platform.mybatis.interceptor.GitEggDataPermissionInterceptor;
import com.gitegg.platform.mybatis.props.TenantProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@MapperScan("com.gitegg.**.mapper.**")
public class MybatisPlusConfig {

    private final TenantLineInnerInterceptor tenantLineInnerInterceptor;

    private final GitEggDataPermissionHandler gitEggDataPermissionHandler;

    private final TenantProperties tenantProperties;

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //多租户插件
        if (tenantProperties.getEnable()) {
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }

        //数据权限插件
        interceptor.addInnerInterceptor(new GitEggDataPermissionInterceptor(gitEggDataPermissionHandler));

        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        //防止全表更新与删除插件: BlockAttackInnerInterceptor
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);

        return interceptor;
    }

    /**
     * 乐观锁插件 当要更新一条记录的时候，希望这条记录没有被别人更新
     * https://mybatis.plus/guide/interceptor-optimistic-locker.html#optimisticlockerinnerinterceptor
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

}
