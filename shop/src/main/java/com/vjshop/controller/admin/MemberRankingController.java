
package com.vjshop.controller.admin;

import com.vjshop.Pageable;
import com.vjshop.entity.TMember;
import com.vjshop.service.TMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 会员排名
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminMemberRankingController")
@RequestMapping("/admin/member_ranking")
public class MemberRankingController extends BaseController {

	@Autowired
	private TMemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(TMember.RankingType rankingType, Pageable pageable, Model model) {
		if (rankingType == null) {
			rankingType = TMember.RankingType.amount;
		}
		model.addAttribute("rankingTypes", TMember.RankingType.values());
		model.addAttribute("rankingType", rankingType);
		model.addAttribute("page", memberService.findPage(rankingType, pageable));
		return "/admin/member_ranking/list";
	}

}