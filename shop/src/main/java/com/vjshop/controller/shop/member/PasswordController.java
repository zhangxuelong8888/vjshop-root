
package com.vjshop.controller.shop.member;

import com.vjshop.Setting;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.service.TMemberService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberPasswordController")
@RequestMapping("/member/password")
public class PasswordController extends BaseController {

	@Autowired
	private TMemberService memberService;

	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		TMember member = memberService.getCurrent();
		return StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword());
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit() {
		return "/shop/${theme}/member/password/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String currentPassword, String password, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			return ERROR_VIEW;
		}
		if (!isValid(TMember.class, "password", password)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
			return ERROR_VIEW;
		}
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.jhtml";
	}

}