
package com.vjshop.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TPointLog;
import com.vjshop.service.TAdminService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TPointLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 积分
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminPointController")
@RequestMapping("/admin/point")
public class PointController extends BaseController {

	@Autowired
	private TPointLogService pointLogService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TAdminService adminService;

	/**
	 * 检查会员
	 */
	@RequestMapping(value = "/check_member", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> checkMember(String username) {
		Map<String, Object> data = new HashMap<String, Object>();
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.point.memberNotExist"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("point", member.getPoint());
		return data;
	}

	/**
	 * 调整
	 */
	@RequestMapping(value = "/adjust", method = RequestMethod.GET)
	public String adjust() {
		return "/admin/point/adjust";
	}

	/**
	 * 调整
	 */
	@RequestMapping(value = "/adjust", method = RequestMethod.POST)
	public String adjust(String username, long amount, String memo, RedirectAttributes redirectAttributes) {
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		if (amount == 0) {
			return ERROR_VIEW;
		}
		if (member.getPoint() == null || member.getPoint() + amount < 0) {
			return ERROR_VIEW;
		}
		TAdmin admin = adminService.getCurrent();
		memberService.addPoint(member.getId(), amount, TPointLog.Type.adjustment, admin.getUsername(), memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log.jhtml";
	}

	/**
	 * 记录
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Long memberId, Pageable pageable, ModelMap model) {
		if (memberId != null) {
			model.addAttribute("member", memberService.find(memberId));
			model.addAttribute("page", pointLogService.findPage(memberId, pageable));
		} else {
			model.addAttribute("page", pointLogService.findPage(null, pageable));
		}
		return "/admin/point/log";
	}

}