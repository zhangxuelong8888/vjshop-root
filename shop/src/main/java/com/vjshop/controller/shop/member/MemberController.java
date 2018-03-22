
package com.vjshop.controller.shop.member;

import com.vjshop.Order;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TOrder;
import com.vjshop.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 会员中心
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberController")
@RequestMapping("/member")
public class MemberController extends BaseController {

	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TOrderService orderService;
	@Autowired
	private TCouponCodeService couponCodeService;
	@Autowired
	private TMessageService messageService;
	@Autowired
	private TMemberFavoriteGoodsService memberFavoriteGoodsService;
	@Autowired
	private TProductNotifyService productNotifyService;
	@Autowired
	private TReviewService reviewService;
	@Autowired
	private TConsultationService consultationService;

	/**
	 * 首页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Integer pageNumber, ModelMap model) {
		Long memberId = memberService.getCurrentUserId();
		model.addAttribute("pendingPaymentOrderCount", orderService.count(null, TOrder.Status.pendingPayment, memberId, null, null, null, null, null, null, false));
		model.addAttribute("pendingShipmentOrderCount", orderService.count(null, TOrder.Status.pendingShipment, memberId, null, null, null, null, null, null, null));
		model.addAttribute("messageCount", messageService.count(memberId, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, memberId, null, false, false));
		model.addAttribute("favoriteCount", memberFavoriteGoodsService.count(memberId, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(memberId, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(memberId, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(memberId, null, null));
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("modifyDate"));
		model.addAttribute("newOrders", orderService.findList(null, null, memberId, null, null, null, null, null, null, null, NEW_ORDER_COUNT, null, orders));
		return "/shop/${theme}/member/index";
	}

}