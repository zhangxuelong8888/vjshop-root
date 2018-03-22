
package com.vjshop.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 令牌
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	/** "令牌"属性名称 */
	private static final String TOKEN_ATTRIBUTE_NAME = "token";

	/** "令牌"Cookie名称 */
	private static final String TOKEN_COOKIE_NAME = "token";

	/** "令牌"参数名称 */
	private static final String TOKEN_PARAMETER_NAME = "token";

	/** 错误消息 */
	private static final String ERROR_MESSAGE = "Bad or missing token!";

	/**
	 * 请求前处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 是否继续执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = WebUtils.getCookie(request, TOKEN_COOKIE_NAME);
		if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
			if (StringUtils.isNotEmpty(token)) {
				String requestType = request.getHeader("X-Requested-With");
				if (StringUtils.equalsIgnoreCase(requestType, "XMLHttpRequest")) {
					if (StringUtils.equals(token, request.getHeader(TOKEN_PARAMETER_NAME))) {
						return true;
					} else {
						response.addHeader("tokenStatus", "accessDenied");
					}
				} else {
					if (StringUtils.equals(token, request.getParameter(TOKEN_PARAMETER_NAME))) {
						return true;
					}
				}
			} else {
				WebUtils.addCookie(request, response, TOKEN_COOKIE_NAME, DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
			return false;
		} else {
			if (StringUtils.isEmpty(token)) {
				token = DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
				WebUtils.addCookie(request, response, TOKEN_COOKIE_NAME, token);
			}
			request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
			return true;
		}
	}

}