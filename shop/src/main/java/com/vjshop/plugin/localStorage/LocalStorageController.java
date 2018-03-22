package com.vjshop.plugin.localStorage;

import javax.annotation.Resource;

import com.vjshop.controller.admin.BaseController;
import com.vjshop.entity.TPluginConfig;

import com.vjshop.service.TPluginConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 本地文件存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminLocalStorageController")
@RequestMapping("/admin/storage_plugin/local_storage")
public class LocalStorageController extends BaseController {

	@Resource(name = "localStoragePlugin")
	private LocalStoragePlugin localStoragePlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = localStoragePlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/localStorage/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = localStoragePlugin.getPluginConfig();
		pluginConfig.setIsEnabled(true);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/storage_plugin/list.jhtml";
	}

}