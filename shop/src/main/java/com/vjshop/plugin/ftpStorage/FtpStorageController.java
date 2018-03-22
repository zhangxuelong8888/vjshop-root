package com.vjshop.plugin.ftpStorage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.controller.admin.BaseController;
import com.vjshop.entity.TPluginConfig;

import com.vjshop.service.TPluginConfigService;
import com.vjshop.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - FTP存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminFtpStorageController")
@RequestMapping("/admin/storage_plugin/ftp_storage")
public class FtpStorageController extends BaseController {

	@Resource(name = "ftpStoragePlugin")
	private FtpStoragePlugin ftpStoragePlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!ftpStoragePlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(ftpStoragePlugin.getId());
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
		if (ftpStoragePlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(ftpStoragePlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = ftpStoragePlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/ftpStorage/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = ftpStoragePlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("host", host);
		attributes.put("port", String.valueOf(port));
		attributes.put("username", username);
		attributes.put("password", password);
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttributes(JsonUtils.toJson(attributes));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/storage_plugin/list.jhtml";
	}

}