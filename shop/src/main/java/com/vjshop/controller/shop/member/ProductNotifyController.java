
package com.vjshop.controller.shop.member;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TProductNotify;

import com.vjshop.service.TMemberService;
import com.vjshop.service.TProductNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller - 会员中心 - 到货通知
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberProductNotifyController")
@RequestMapping("/member/product_notify")
public class ProductNotifyController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TProductNotifyService productNotifyService;
	@Autowired
	private TMemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, Model model) {
		Long memberId = memberService.getCurrentUserId();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", productNotifyService.findPage(memberId, null, null, null, pageable));
		return "/shop/${theme}/member/product_notify/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody
	Message delete(Long id) {
		TProductNotify productNotify = productNotifyService.find(id);
		if (productNotify == null) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();

		List<TProductNotify> productNotifies = productNotifyService.findByMemberId(member.getId());
		if (!productNotifies.contains(productNotify)) {
			return ERROR_MESSAGE;
		}
		productNotifyService.delete(productNotify);
		return SUCCESS_MESSAGE;
	}

}