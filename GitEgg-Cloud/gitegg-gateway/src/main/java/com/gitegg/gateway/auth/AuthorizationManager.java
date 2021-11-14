package com.gitegg.gateway.auth;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.oauth2.props.AuthUrlWhiteListProperties;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import com.gitegg.platform.base.constant.AuthConstant;

import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权管理器
 * @author GitEgg
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisTemplate redisTemplate;

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getURI().getPath();
        PathMatcher pathMatcher = new AntPathMatcher();

        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // token为空拒绝访问
        String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        //如果token被加入到黑名单，就是执行了退出登录操作，那么拒绝访问
        String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        try {
            JWSObject jwsObject = JWSObject.parse(realToken);
            Payload payload = jwsObject.getPayload();
            JSONObject jsonObject = payload.toJSONObject();
            String jti = jsonObject.getAsString(TokenConstant.JTI);
            String blackListToken = (String)redisTemplate.opsForValue().get(AuthConstant.TOKEN_BLACKLIST + jti);
            if (!StringUtils.isEmpty(blackListToken)) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch (ParseException e) {
            log.error("获取token黑名单时发生错误：{}", e);
        }

        // 如果开启了租户模式，但是请求头里没有租户信息，那么拒绝访问
        String tenantId = request.getHeaders().getFirst(AuthConstant.TENANT_ID);
        if (enable && StringUtils.isEmpty(tenantId)) {
            return Mono.just(new AuthorizationDecision(false));
        }

        // 需要鉴权但是每一个角色都需要的url，统一配置，不需要单个配置
        List<String> authUrls = authUrlWhiteListProperties.getAuthUrls();
        String urls = authUrls.stream().filter(url -> pathMatcher.match(url, path)).findAny().orElse(null);

        // 当配置了功能鉴权url时，直接放行，用户都有的功能，但是必须要登录才能用，例：退出登录功能是每个用户都有的权限，但是这个必须要登录才能够调用
        if (null != urls) {
            return mono.filter(Authentication::isAuthenticated)
                    .map(auth -> new AuthorizationDecision(true))
                    .defaultIfEmpty(new AuthorizationDecision(false));
        }

        String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
        // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
        if (enable) {
            redisRoleKey += tenantId;
        } else {
            redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
        }

        // 缓存取资源权限角色关系列表
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();

        // 请求路径匹配到的资源需要的角色权限集合authorities统计
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
            if (pathMatcher.match(pattern, path)) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }

        Mono<AuthorizationDecision> authorizationDecisionMono = mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(roleId -> {
                    // roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
                    log.info("访问路径：{}", path);
                    log.info("用户角色roleId：{}", roleId);
                    log.info("资源需要权限authorities：{}", authorities);
                    return authorities.contains(roleId);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;
    }
}

