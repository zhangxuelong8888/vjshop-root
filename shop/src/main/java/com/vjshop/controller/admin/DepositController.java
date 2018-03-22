
package com.vjshop.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TDepositLog;
import com.vjshop.entity.TMember;
import com.vjshop.service.TAdminService;
import com.vjshop.service.TDepositLogService;
import com.vjshop.service.TMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 预存款
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminDepositController")
@RequestMapping("/admin/deposit")
public class DepositController extends BaseController {

	@Autowired
	private TDepositLogService depositLogService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TAdminService adminService;

	/**
	 * 检查会员
	 */
	@RequestMapping(value = "/check_member", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> checkMember(String username) {
		Map<String, Object> data = new HashMap<String, Object>();
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.deposit.memberNotExist"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("balance", member.getBalance());
		return data;
	}

	/**
	 * 调整
	 */
	@RequestMapping(value = "/adjust", method = RequestMethod.GET)
	public String adjust() {
		return "/admin/deposit/adjust";
	}

	/**
	 * 调整
	 */
	@RequestMapping(value = "/adjust", method = RequestMethod.POST)
	public String adjust(String username, BigDecimal amount, String memo, RedirectAttributes redirectAttributes) {
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return ERROR_VIEW;
		}
		if (member.getBalance() == null || member.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			return ERROR_VIEW;
		}
		TAdmin admin = adminService.getCurrent();
		memberService.addBalance(member.getId(), amount, TDepositLog.Type.adjustment, admin.getUsername(), memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 记录
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Long memberId, Pageable pageable, ModelMap model) {
		TMember member = memberService.find(memberId);
		if (member != null) {
			model.addAttribute("member", member);
			model.addAttribute("page", depositLogService.findPage(memberId, pageable));
		} else {
			model.addAttribute("page", depositLogService.findPage(null, pageable));
		}
		return "/admin/deposit/log";
	}

}