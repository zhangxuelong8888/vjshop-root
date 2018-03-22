
package com.vjshop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.entity.TCart;
import com.vjshop.util.WebUtils;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 购物车数量
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class CartQuantityInterceptor extends HandlerInterceptorAdapter {

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
		WebUtils.removeCookie(request, response, TCart.QUANTITY_COOKIE_NAME);
		return super.preHandle(request, response, handler);
	}

}