
package com.vjshop.controller.shop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vjshop.entity.TMember;
import com.vjshop.service.TMemberService;
import com.vjshop.util.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 会员注销
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopLogoutController")
public class LogoutController extends BaseController {

	@Autowired
	private TMemberService memberService;

	/**
	 * 注销
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		WebUtils.removeCookie(request, response, TMember.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(request, response, TMember.NICKNAME_COOKIE_NAME);
		if (memberService.isAuthenticated()) {
			session.removeAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME);
		}
		return "redirect:/";
	}

}