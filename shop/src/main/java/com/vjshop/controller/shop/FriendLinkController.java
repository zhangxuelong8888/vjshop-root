
package com.vjshop.controller.shop;

import com.vjshop.entity.TFriendLink;
import com.vjshop.service.TFriendLinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 友情链接
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopFriendLinkController")
@RequestMapping("/friend_link")
public class FriendLinkController extends BaseController {

	@Autowired
	private TFriendLinkService friendLinkService;

	/**
	 * 首页
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("textFriendLinks", friendLinkService.findList(TFriendLink.Type.text));
		model.addAttribute("imageFriendLinks", friendLinkService.findList(TFriendLink.Type.image));
		return "/shop/${theme}/friend_link/index";
	}

}