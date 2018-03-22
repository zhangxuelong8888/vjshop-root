
package com.vjshop.controller.admin;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.entity.ValidGroup;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberAttribute;
import com.vjshop.service.TMemberAttributeService;
import com.vjshop.service.TMemberRankService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.PluginService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController extends BaseController {

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TMemberRankService memberRankService;
	@Autowired
	private TMemberAttributeService memberAttributeService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 检查用户名是否被禁用或已存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !memberService.usernameDisabled(username) && !memberService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否唯一
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmail(String previousEmail, String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return memberService.emailUnique(previousEmail, email);
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		TMember member = memberService.findDetails(id);
		model.addAttribute("genders", TMember.Gender.values());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		model.addAttribute("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		return "/admin/member/view";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("genders", TMember.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		return "/admin/member/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TMember member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankId);
		if (!isValid(member, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		if (member.getUsername().length() < setting.getUsernameMinLength() || member.getUsername().length() > setting.getUsernameMaxLength()) {
			return ERROR_VIEW;
		}
		if (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		if (memberService.usernameDisabled(member.getUsername()) || memberService.usernameExists(member.getUsername())) {
			return ERROR_VIEW;
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(member.getEmail())) {
			return ERROR_VIEW;
		}
		member.removeAttributeValue();
		for (TMemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return ERROR_VIEW;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute.getEnumType(), memberAttribute.getPropertyIndex(), memberAttributeValue);
		}
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setPoint(0L);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(null);
		memberService.save(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TMember member = memberService.findDetails(id);
		model.addAttribute("genders", TMember.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		model.addAttribute("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		return "/admin/member/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TMember member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankId);
		if (!isValid(member)) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		if (member.getPassword() != null && (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength())) {
			return ERROR_VIEW;
		}
		TMember pMember = memberService.find(member.getId());
		if (pMember == null) {
			return ERROR_VIEW;
		}
		if (!setting.getIsDuplicateEmail() && !memberService.emailUnique(pMember.getEmail(), member.getEmail())) {
			return ERROR_VIEW;
		}
		member.removeAttributeValue();
		for (TMemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return ERROR_VIEW;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute.getEnumType(), memberAttribute.getPropertyIndex(), memberAttributeValue);
		}
		if (StringUtils.isEmpty(member.getPassword())) {
			member.setPassword(pMember.getPassword());
		} else {
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		}
		if (pMember.getIsLocked() && !member.getIsLocked()) {
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
		} else {
			member.setIsLocked(pMember.getIsLocked());
			member.setLoginFailureCount(pMember.getLoginFailureCount());
			member.setLockedDate(pMember.getLockedDate());
		}

		memberService.update(member, "username", "point", "balance", "amount", "registerIp", "loginIp", "loginDate", "loginPluginId", "openId", "lockKey", "safeKey", "cart", "orders", "paymentLogs", "depositLogs", "couponCodes", "receivers", "reviews", "consultations", "favoriteGoods",
				"productNotifies", "inMessages", "outMessages", "pointLogs");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findAll());
		model.addAttribute("page", memberService.findPage(null, pageable));
		return "/admin/member/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				TMember member = memberService.find(id);
				if (member != null && member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
					return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
				}
			}
			memberService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}