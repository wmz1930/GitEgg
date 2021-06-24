package com.gitegg.gateway.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 请求头中未找到TenantId时的自定义异常（只在Gateway中使用，所以在此处定义）
 * 
 * @author GitEgg
 * @date 2020/12/09
 **/
public class TenantNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = -6924672580210633396L;

    public TenantNotFoundException(String msg) {
        super(msg);
    }

    public TenantNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
