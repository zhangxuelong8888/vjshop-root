
package com.vjshop.controller.admin;

import com.vjshop.entity.TMessageConfig;

import com.vjshop.service.TMessageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 消息配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminMessageConfigController")
@RequestMapping("/admin/message_config")
public class MessageConfigController extends BaseController {

	@Autowired
	private TMessageConfigService messageConfigService;

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("messageConfig", messageConfigService.find(id));
		return "/admin/message_config/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TMessageConfig messageConfig, RedirectAttributes redirectAttributes) {
		if (!isValid(messageConfig)) {
			return ERROR_VIEW;
		}
		messageConfigService.update(messageConfig, "type");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("messageConfigs", messageConfigService.findAll());
		return "/admin/message_config/list";
	}

}