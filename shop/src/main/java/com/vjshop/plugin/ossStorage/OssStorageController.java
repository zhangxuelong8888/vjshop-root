
package com.vjshop.plugin.ossStorage;

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
 * Controller - 阿里云存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminOssStorageController")
@RequestMapping("/admin/storage_plugin/oss_storage")
public class OssStorageController extends BaseController {

	@Resource(name = "ossStoragePlugin")
	private OssStoragePlugin ossStoragePlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!ossStoragePlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(ossStoragePlugin.getId());
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
		if (ossStoragePlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(ossStoragePlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = ossStoragePlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/ossStorage/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String endpoint, String accessId, String accessKey, String bucketName, String urlPrefix, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = ossStoragePlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("endpoint", endpoint);
		attributes.put("accessId", accessId);
		attributes.put("accessKey", accessKey);
		attributes.put("bucketName", bucketName);
		attributes.put("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttributes(JsonUtils.toJson(attributes));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/storage_plugin/list.jhtml";
	}

}