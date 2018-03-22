
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TFriendLink;
import com.vjshop.service.TFriendLinkService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 友情链接
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminFriendLinkController")
@RequestMapping("/admin/friend_link")
public class FriendLinkController extends BaseController {

	@Autowired
	private TFriendLinkService friendLinkService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", TFriendLink.Type.values());
		return "/admin/friend_link/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TFriendLink friendLink, RedirectAttributes redirectAttributes) {
		if (!isValid(friendLink)) {
			return ERROR_VIEW;
		}
		if (friendLink.getType() != null && TFriendLink.Type.text.ordinal() == friendLink.getType()) {
			friendLink.setLogo(null);
		} else if (StringUtils.isEmpty(friendLink.getLogo())) {
			return ERROR_VIEW;
		}
		friendLinkService.save(friendLink);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", TFriendLink.Type.values());
		model.addAttribute("friendLink", friendLinkService.find(id));
		return "/admin/friend_link/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TFriendLink friendLink, RedirectAttributes redirectAttributes) {
		if (!isValid(friendLink)) {
			return ERROR_VIEW;
		}
		if (friendLink.getType() != null && TFriendLink.Type.text.ordinal() == friendLink.getType()) {
			friendLink.setLogo(null);
		} else if (StringUtils.isEmpty(friendLink.getLogo())) {
			return ERROR_VIEW;
		}
		friendLinkService.update(friendLink);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", friendLinkService.findPage(pageable));
		return "/admin/friend_link/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		friendLinkService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}