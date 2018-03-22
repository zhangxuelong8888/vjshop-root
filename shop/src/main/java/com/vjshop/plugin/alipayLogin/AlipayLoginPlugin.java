
package com.vjshop.plugin.alipayLogin;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.LoginPlugin;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 支付宝快捷登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("alipayLoginPlugin")
public class AlipayLoginPlugin extends LoginPlugin {

	@Override
	public String getName() {
		return "支付宝快捷登录";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "VJSHOP";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.vjshop.com";
	}

	@Override
	public String getInstallUrl() {
		return "alipay_login/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "alipay_login/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "alipay_login/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://mapi.alipay.com/gateway.do";
	}

	@Override
	public LoginPlugin.RequestMethod getRequestMethod() {
		return LoginPlugin.RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("service", "alipay.auth.authorize");
		parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("_input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl());
		parameterMap.put("target_service", "user.auth.quick.login");
		parameterMap.put("exter_invoke_ip", request.getLocalAddr());
		parameterMap.put("client_ip", request.getLocalAddr());
		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		if (generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && "T".equals(request.getParameter("is_success"))) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("service", "notify_verify");
			parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
			parameterMap.put("notify_id", request.getParameter("notify_id"));
			if ("true".equals(WebUtils.post("https://mapi.alipay.com/gateway.do", parameterMap))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getOpenId(HttpServletRequest request) {
		String userId = request.getParameter("user_id");
		if (StringUtils.isNotEmpty(userId)) {
			return userId;
		}
		return null;
	}

	@Override
	public String getEmail(HttpServletRequest request) {
		String email = request.getParameter("email");
		if (StringUtils.isEmpty(email) || !email.contains("@")) {
			return null;
		}
		return email;
	}

	@Override
	public String getNickname(HttpServletRequest request) {
		return request.getParameter("real_name");
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		TPluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, pluginConfig.getAttributesMap("key"), "&", true, "sign_type", "sign"));
	}

}