package com.gitegg.oauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

@Component
public class GitEggOAuth2ExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception exception) throws Exception {
        if (exception instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new GitEggOAuth2Exception(oAuth2Exception.getMessage()));
        }else if(exception instanceof AuthenticationException){
            AuthenticationException authenticationException = (AuthenticationException) exception;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new GitEggOAuth2Exception(authenticationException.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GitEggOAuth2Exception(exception.getMessage()));
    }
}
