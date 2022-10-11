package com.gitegg.oauth.handler;

/**
 * @author wanglei
 * @version V1.0
 * @Description: TODO
 * @date
 */

import com.anji.captcha.util.JsonUtil;
import com.gitegg.oauth.dto.LoginResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author
 */
@Slf4j
@Component("LoginFailHandler")
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws
		IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control", "no-cache");
		LoginResultDTO loginResult = new LoginResultDTO();
		loginResult.setSuccess(false);
		loginResult.setMessage(e.getMessage());
		response.getWriter().write(JsonUtil.toJSONString(loginResult));
	}
}
