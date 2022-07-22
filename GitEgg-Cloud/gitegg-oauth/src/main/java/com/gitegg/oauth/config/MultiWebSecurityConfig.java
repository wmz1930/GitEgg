package com.gitegg.oauth.config;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * 多WebSecurity配置
 * @author GitEgg
 * @date 2022/7/19
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MultiWebSecurityConfig {
    
    private final UserDetailsService userDetailsService;
    
    private final SecurityProperties securityProperties;
    
    @Value("${management.endpoints.web.base-path:}")
    private String actuatorPath;
    
    @Bean
    public static PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return  delegatingPasswordEncoder;
    }
    
    /**
     * 内存用户Basic校验，主要应用于actuator等权限校验
     */
    @ConditionalOnProperty( value = {"management.security.enabled", "management.endpoints.enabled-by-default"}, havingValue = "true")
    @Configuration
    @Order(1)
    public class InMemoryConfigurationAdapter extends WebSecurityConfigurerAdapter {
        
        public InMemoryConfigurationAdapter() {
            super();
        }
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            auth.inMemoryAuthentication().withUser(securityProperties.getUser().getName())
                    .password(AuthConstant.BCRYPT + bcryptPasswordEncoder.encode(securityProperties.getUser().getPassword()))
                    .roles(securityProperties.getUser().getRoles().toArray(new String[0]));
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (StringUtils.isEmpty(actuatorPath))
            {
                throw new BusinessException("当启用健康检查时，不允许健康检查的路径为空");
            }
            http.antMatcher(actuatorPath + "/**").authorizeRequests().anyRequest().hasAnyRole(securityProperties.getUser().getRoles().toArray(new String[0]))
                    .and().httpBasic().and().csrf().disable();
        }
    }
    
    /**
     * JDBC用户权限校验
     */
    @Configuration
    @Order(2)
    public class DaoConfigurationAdapter extends WebSecurityConfigurerAdapter {
        
        public DaoConfigurationAdapter() {
            super();
        }
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(daoAuthenticationProvider());
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().cors().and().csrf().disable();
        }
        
        /**
         *  如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
         */
        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
        
        @Bean
        public DaoAuthenticationProvider daoAuthenticationProvider(){
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            // 设置userDetailsService
            provider.setUserDetailsService(userDetailsService);
            // 禁止隐藏用户未找到异常，这里是在登录的时候显示用户名或密码错误
//        provider.setHideUserNotFoundExceptions(false);
            // 使用BCrypt进行密码的hash
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }
    }
    
}

