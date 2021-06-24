package com.gitegg.oauth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;


@JsonSerialize(using = GitEggOAuth2ExceptionSerializer.class)
public class GitEggOAuth2Exception extends OAuth2Exception {

    public GitEggOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public GitEggOAuth2Exception(String msg) {
        super(msg);
    }
}
