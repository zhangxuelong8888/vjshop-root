
package com.vjshop.controller.shop.member;

import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TPointLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 会员中心 - 我的积分
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberPointLogController")
@RequestMapping("/member/point_log")
public class PointLogController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TPointLogService pointLogService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		TMember member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", pointLogService.findPage(member.getId(), pageable));
		return "/shop/${theme}/member/point_log/list";
	}

}