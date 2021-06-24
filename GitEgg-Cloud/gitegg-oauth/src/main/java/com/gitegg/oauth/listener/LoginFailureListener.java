package com.gitegg.oauth.listener;

import com.gitegg.oauth.service.GitEggUserDetails;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 当登录失败时的调用，当密码错误过多时，则锁定账户
 * @author GitEgg
 * @date 2021-03-12 17:57:05
 **/
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserDetailsService userDetailsService;

    private final RedisTemplate redisTemplate;

    @Value("${system.maxTryTimes}")
    private int maxTryTimes;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        if (event.getException().getClass().equals(UsernameNotFoundException.class)) {
            return;
        }

        String userName = event.getAuthentication().getName();

        GitEggUserDetails user = (GitEggUserDetails) userDetailsService.loadUserByUsername(userName);

        if (null != user) {
            Object lockTimes = redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + user.getId()).get();
            if(null == lockTimes || (int)lockTimes <= maxTryTimes){
                redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + user.getId()).increment(GitEggConstant.Number.ONE);
                redisTemplate.expire(AuthConstant.LOCK_ACCOUNT_PREFIX + user.getId(),60 * 60 * 2 , TimeUnit.SECONDS);
            }
        }
    }
}
