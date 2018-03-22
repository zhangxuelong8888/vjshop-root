package com.vjshop.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.LogConfig;
import com.vjshop.entity.TLog;
import com.vjshop.service.TAdminService;
import com.vjshop.service.TLogService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 日志
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	/** 默认忽略参数 */
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	/** AntPathMatcher */
	private static AntPathMatcher antPathMatcher = new AntPathMatcher();

	/** 忽略参数 */
	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Autowired
	private TLogService logService;
	@Autowired
	private TAdminService adminService;

	/**
	 * 请求后处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @param modelAndView
	 *            数据视图
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		List<LogConfig> logConfigs = SystemUtils.getLogConfigs();
		if (logConfigs != null) {
			String path = request.getServletPath();
			for (LogConfig logConfig : logConfigs) {
				if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
					String username = adminService.getCurrentUsername();
					String operation = logConfig.getOperation();
					String operator = username;
					String content = (String) request.getAttribute(TLog.LOG_CONTENT_ATTRIBUTE_NAME);
					String ip = request.getRemoteAddr();
					request.removeAttribute(TLog.LOG_CONTENT_ATTRIBUTE_NAME);
					StringBuilder parameter = new StringBuilder();
					Map<String, String[]> parameterMap = request.getParameterMap();
					if (parameterMap != null) {
						for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
							String parameterName = entry.getKey();
							if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
								String[] parameterValues = entry.getValue();
								if (parameterValues != null) {
									for (String parameterValue : parameterValues) {
										parameter.append(parameterName + " = " + parameterValue + "\n");
									}
								}
							}
						}
					}
					TLog log = new TLog();
					log.setOperation(operation);
					log.setOperator(operator);
					log.setContent(content);
					log.setParameter(parameter.toString());
					log.setIp(ip);
					logService.save(log);
					break;
				}
			}
		}
	}

	/**
	 * 设置忽略参数
	 * 
	 * @return 忽略参数
	 */
	public String[] getIgnoreParameters() {
		return ignoreParameters;
	}

	/**
	 * 设置忽略参数
	 * 
	 * @param ignoreParameters
	 *            忽略参数
	 */
	public void setIgnoreParameters(String[] ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}

}