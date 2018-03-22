package com.vjshop.plugin.alipayLogin;

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
 * Controller - 支付宝快捷登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminAlipayLoginController")
@RequestMapping("/admin/login_plugin/alipay_login")
public class AlipayLoginController extends BaseController {

	@Resource(name = "alipayLoginPlugin")
	private AlipayLoginPlugin alipayLoginPlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!alipayLoginPlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(alipayLoginPlugin.getId());
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
		if (alipayLoginPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(alipayLoginPlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = alipayLoginPlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/alipayLogin/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String loginMethodName, String partner, String key, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = alipayLoginPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(LoginPlugin.LOGIN_METHOD_NAME_ATTRIBUTE_NAME, loginMethodName);
		attributes.put("partner", partner);
		attributes.put("key", key);
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