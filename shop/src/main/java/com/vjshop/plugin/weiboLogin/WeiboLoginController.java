package com.vjshop.plugin.weiboLogin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.controller.admin.BaseController;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.LoginPlugin;
import com.vjshop.plugin.PaymentPlugin;

import com.vjshop.service.TPluginConfigService;
import com.vjshop.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 新浪微博登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminWeiboLoginController")
@RequestMapping("/admin/login_plugin/weibo_login")
public class WeiboLoginController extends BaseController {

	@Resource(name = "weiboLoginPlugin")
	private WeiboLoginPlugin weiboLoginPlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!weiboLoginPlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(weiboLoginPlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 卸载
	 */
	@RequestMapping(value = "/uninstall", method = RequestMethod.POST)
	public @ResponseBody
	Message uninstall() {
		if (weiboLoginPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(weiboLoginPlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = weiboLoginPlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/weiboLogin/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String loginMethodName, String oauthKey, String oauthSecret, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = weiboLoginPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(LoginPlugin.LOGIN_METHOD_NAME_ATTRIBUTE_NAME, loginMethodName);
		attributes.put("oauthKey", oauthKey);
		attributes.put("oauthSecret", oauthSecret);
		attributes.put(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
		attributes.put(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
		pluginConfig.setAttributes(JsonUtils.toJson(attributes));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/login_plugin/list.jhtml";
	}

}