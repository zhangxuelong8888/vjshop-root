package com.vjshop.plugin.qqLogin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.LoginPlugin;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Plugin - QQ登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("qqLoginPlugin")
public class QqLoginPlugin extends LoginPlugin {

	/** "状态"属性名称 */
	private static final String STATE_ATTRIBUTE_NAME = QqLoginPlugin.class.getName() + ".STATE";

	/** "openId"配比 */
	private static final Pattern OPEN_ID_PATTERN = Pattern.compile("\"openid\"\\s*:\\s*\"(\\S*?)\"");

	@Override
	public String getName() {
		return "QQ登录";
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
		return "qq_login/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "qq_login/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "qq_login/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://graph.qq.com/oauth2.0/authorize";
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
		String state = DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30));
		request.getSession().setAttribute(STATE_ATTRIBUTE_NAME, state);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("response_type", "code");
		parameterMap.put("client_id", pluginConfig.getAttributesMap("oauthKey"));
		parameterMap.put("redirect_uri", getNotifyUrl());
		parameterMap.put("state", state);
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(HttpServletRequest request) {
		String state = (String) request.getSession().getAttribute(STATE_ATTRIBUTE_NAME);
		if (StringUtils.isNotEmpty(state) && StringUtils.equals(state, request.getParameter("state")) && StringUtils.isNotEmpty(request.getParameter("code"))) {
			request.getSession().removeAttribute(STATE_ATTRIBUTE_NAME);
			TPluginConfig pluginConfig = getPluginConfig();
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("grant_type", "authorization_code");
			parameterMap.put("client_id", pluginConfig.getAttributesMap("oauthKey"));
			parameterMap.put("client_secret", pluginConfig.getAttributesMap("oauthSecret"));
			parameterMap.put("redirect_uri", getNotifyUrl());
			parameterMap.put("code", request.getParameter("code"));
			String content = WebUtils.get("https://graph.qq.com/oauth2.0/token", parameterMap);
			String accessToken = WebUtils.parse(content).get("access_token");
			if (StringUtils.isNotEmpty(accessToken)) {
				request.setAttribute("accessToken", accessToken);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getOpenId(HttpServletRequest request) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("access_token", request.getAttribute("accessToken"));
		String content = WebUtils.get("https://graph.qq.com/oauth2.0/me", parameterMap);
		Matcher matcher = OPEN_ID_PATTERN.matcher(content);
		if (matcher.find()) {
			String openId = matcher.group(1);
			request.setAttribute("openId", openId);
			return openId;
		}
		return null;
	}

	@Override
	public String getEmail(HttpServletRequest request) {
		return null;
	}

	@Override
	public String getNickname(HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("access_token", request.getAttribute("accessToken"));
		parameterMap.put("oauth_consumer_key", pluginConfig.getAttributesMap("oauthKey"));
		parameterMap.put("openid", request.getAttribute("openId"));
		String content = WebUtils.get("https://graph.qq.com/user/get_user_info", parameterMap);
		JsonNode jsonNode = JsonUtils.toTree(content);
		return jsonNode.has("nickname") ? jsonNode.get("nickname").textValue() : null;
	}

}