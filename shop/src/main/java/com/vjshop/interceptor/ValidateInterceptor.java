
package com.vjshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 验证
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class ValidateInterceptor extends HandlerInterceptorAdapter {

	/** 错误消息 */
	private static final String ERROR_MESSAGE = "illegal access!";

	/** 默认白名单 */
	private static final Whitelist DEFAULT_WHITELIST = Whitelist.none();

	/** 白名单 */
	private Whitelist whitelist = DEFAULT_WHITELIST;

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
		if (!isValid(request)) {
			String requestType = request.getHeader("X-Requested-With");
			if (StringUtils.equalsIgnoreCase(requestType, "XMLHttpRequest")) {
				response.addHeader("validateStatus", "accessDenied");
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * 白名单验证
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 验证是否通过
	 */
	private boolean isValid(HttpServletRequest request) {
		for (Object values : request.getParameterMap().values()) {
			if (values != null) {
				for (String value : (String[]) values) {
					if (!Jsoup.isValid(value, whitelist)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 获取白名单
	 * 
	 * @return 白名单
	 */
	public Whitelist getWhitelist() {
		return whitelist;
	}

	/**
	 * 设置白名单
	 * 
	 * @param whitelist
	 *            白名单
	 */
	public void setWhitelist(Whitelist whitelist) {
		this.whitelist = whitelist;
	}

}