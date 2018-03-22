package com.vjshop.plugin.weiboLogin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * Plugin - 新浪微博登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("weiboLoginPlugin")
public class WeiboLoginPlugin extends LoginPlugin {

	/** "状态"属性名称 */
	private static final String STATE_ATTRIBUTE_NAME = WeiboLoginPlugin.class.getName() + ".STATE";

	@Override
	public String getName() {
		return "新浪微博登录";
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
		return "weibo_login/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "weibo_login/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "weibo_login/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://api.weibo.com/oauth2/authorize";
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
			String content = WebUtils.post("https://api.weibo.com/oauth2/access_token", parameterMap);
			JsonNode tree = JsonUtils.toTree(content);
			String accessToken = tree.get("access_token").textValue();
			String uid = tree.get("uid").textValue();
			if (StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(uid)) {
				request.setAttribute("accessToken", tree.get("access_token").textValue());
				request.setAttribute("uid", uid);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getOpenId(HttpServletRequest request) {
		String uid = (String) request.getAttribute("uid");
		if (StringUtils.isNotEmpty(uid)) {
			return uid;
		}
		return null;
	}

	@Override
	public String getEmail(HttpServletRequest request) {
		return null;
	}

	@Override
	public String getNickname(HttpServletRequest request) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("access_token", request.getAttribute("accessToken"));
		parameterMap.put("uid", request.getAttribute("uid"));
		String content = WebUtils.get("https://api.weibo.com/2/users/show.json", parameterMap);
		JsonNode jsonNode = JsonUtils.toTree(content);
		return jsonNode.has("screen_name") ? jsonNode.get("screen_name").textValue() : null;
	}

}