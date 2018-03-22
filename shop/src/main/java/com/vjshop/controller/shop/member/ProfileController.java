
package com.vjshop.controller.shop.member;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Setting;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberAttribute;
import com.vjshop.service.TMemberAttributeService;
import com.vjshop.service.TMemberService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 个人资料
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberProfileController")
@RequestMapping("/member/profile")
public class ProfileController extends BaseController {

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TMemberAttributeService memberAttributeService;

	/**
	 * 检查E-mail是否唯一
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		TMember member = memberService.getCurrent();
		return memberService.emailUnique(member.getEmail(), email);
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		model.addAttribute("member", memberService.findDetails(memberService.getCurrentUserId()));
		model.addAttribute("genders", TMember.Gender.values());
		return "/shop/${theme}/member/profile/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String email, String nickname, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TMember.class, "email", email) || !isValid(TMember.class, "nickname", nickname)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		TMember member = memberService.getCurrent();
		if (!setting.getIsDuplicateEmail() && !memberService.emailUnique(member.getEmail(), email)) {
			return ERROR_VIEW;
		}
		member.setEmail(email);
		member.setNickname(nickname);
		member.removeAttributeValue();
		for (TMemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return ERROR_VIEW;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute.getEnumType(), memberAttribute.getPropertyIndex(), memberAttributeValue);
		}
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.jhtml";
	}

}