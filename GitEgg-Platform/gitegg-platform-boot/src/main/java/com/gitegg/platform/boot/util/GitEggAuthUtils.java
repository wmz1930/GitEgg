package com.gitegg.platform.boot.util;

import cn.hutool.json.JSONUtil;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class GitEggAuthUtils {

    /**
     * 获取用户信息
     *
     * @return GitEggUser
     */
    public static GitEggUser getCurrentUser() {
        HttpServletRequest request = GitEggWebUtils.getRequest();
        if (request == null) {
            return null;
        }
        try {
            String user = request.getHeader(AuthConstant.HEADER_USER);
            if (StringUtils.isEmpty(user))
            {
                return null;
            }
            String userStr = URLDecoder.decode(user,"UTF-8");
            GitEggUser gitEggUser = JSONUtil.toBean(userStr, GitEggUser.class);
            return gitEggUser;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取租户Id
     *
     * @return tenantId
     */
    public static String getTenantId() {
        HttpServletRequest request = GitEggWebUtils.getRequest();
        if (request == null) {
            return null;
        }
        try {
            String tenantId = request.getHeader(AuthConstant.TENANT_ID);
            String user = request.getHeader(AuthConstant.HEADER_USER);
            //如果请求头中的tenantId为空，那么尝试是否能够从登陆用户中去获取租户id
            if (StringUtils.isEmpty(tenantId) && !StringUtils.isEmpty(user))
            {
                String userStr = URLDecoder.decode(user,"UTF-8");
                GitEggUser gitEggUser = JSONUtil.toBean(userStr, GitEggUser.class);
                if (null != gitEggUser)
                {
                    tenantId = gitEggUser.getTenantId();
                }
            }
            return tenantId;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
