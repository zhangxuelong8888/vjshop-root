
package com.vjshop.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Setting;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.service.TPluginConfigService;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Plugin - 登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public abstract class LoginPlugin implements Comparable<LoginPlugin> {

	/** "登录方式名称"属性名称 */
	public static final String LOGIN_METHOD_NAME_ATTRIBUTE_NAME = "loginMethodName";

	/** "LOGO"属性名称 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/** "描述"属性名称 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

	/**
	 * 请求方法
	 */
	public enum RequestMethod {

		/** POST */
		post,

		/** GET */
		get
	}

	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public abstract String getSiteUrl();

	/**
	 * 获取安装URL
	 * 
	 * @return 安装URL
	 */
	public abstract String getInstallUrl();

	/**
	 * 获取卸载URL
	 * 
	 * @return 卸载URL
	 */
	public abstract String getUninstallUrl();

	/**
	 * 获取设置URL
	 * 
	 * @return 设置URL
	 */
	public abstract String getSettingUrl();

	/**
	 * 获取是否已安装
	 * 
	 * @return 是否已安装
	 */
	public boolean getIsInstalled() {
		return pluginConfigService.pluginIdExists(getId());
	}

	/**
	 * 获取插件配置
	 * 
	 * @return 插件配置
	 */
	public TPluginConfig getPluginConfig() {
		TPluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
		if (pluginConfig != null && StringUtils.isNotEmpty(pluginConfig.getAttributes())) {
			pluginConfig.setAttributesMap(JsonUtils.toObject(pluginConfig.getAttributes(), Map.class));
		}
		return pluginConfig;
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttributesMap(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrders() : null;
	}

	/**
	 * 获取登录方式名称
	 * 
	 * @return 登录方式名称
	 */
	public String getLoginMethodName() {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttributesMap(LOGIN_METHOD_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取LOGO
	 * 
	 * @return LOGO
	 */
	public String getLogo() {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttributesMap(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		TPluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttributesMap(DESCRIPTION_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取请求URL
	 * 
	 * @return 请求URL
	 */
	public abstract String getRequestUrl();

	/**
	 * 获取请求方法
	 * 
	 * @return 请求方法
	 */
	public abstract LoginPlugin.RequestMethod getRequestMethod();

	/**
	 * 获取请求字符编码
	 * 
	 * @return 请求字符编码
	 */
	public abstract String getRequestCharset();

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 请求参数
	 */
	public abstract Map<String, Object> getParameterMap(HttpServletRequest request);

	/**
	 * 验证通知是否合法
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 通知是否合法
	 */
	public abstract boolean verifyNotify(HttpServletRequest request);

	/**
	 * 获取openID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return openID
	 */
	public abstract String getOpenId(HttpServletRequest request);

	/**
	 * 获取E-mail
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return E-mail
	 */
	public abstract String getEmail(HttpServletRequest request);

	/**
	 * 获取昵称
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 昵称
	 */
	public abstract String getNickname(HttpServletRequest request);

	/**
	 * 获取通知URL
	 * 
	 * @return 通知URL
	 */
	protected String getNotifyUrl() {
		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + "/login/plugin_notify/" + getId() + ".jhtml";
	}

	/**
	 * 连接Map键值对
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(key + "=" + (value != null ? value : ""));
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 连接Map值
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(value != null ? value : "");
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		LoginPlugin other = (LoginPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param loginPlugin
	 *            登录插件
	 * @return 比较结果
	 */
	public int compareTo(LoginPlugin loginPlugin) {
		if (loginPlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), loginPlugin.getOrder()).append(getId(), loginPlugin.getId()).toComparison();
	}

}