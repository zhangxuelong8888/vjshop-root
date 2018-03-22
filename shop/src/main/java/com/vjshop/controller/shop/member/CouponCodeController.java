
package com.vjshop.controller.shop.member;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TMember;
import com.vjshop.service.TCouponCodeService;
import com.vjshop.service.TCouponService;
import com.vjshop.service.TMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 优惠码
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberCouponCodeController")
@RequestMapping("/member/coupon_code")
public class CouponCodeController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TCouponService couponService;
	@Autowired
	private TCouponCodeService couponCodeService;

	/**
	 * 兑换
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponService.findPage(true, true, false, pageable));
		return "/shop/${theme}/member/coupon_code/exchange";
	}

	/**
	 * 兑换
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.POST)
	public @ResponseBody Message exchange(Long id) {
		TCoupon coupon = couponService.find(id);
		if (coupon == null || !coupon.getIsEnabled() || !coupon.getIsExchange() || coupon.hasExpired()) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (member.getPoint() < coupon.getPoint()) {
			return Message.warn("shop.member.couponCode.point");
		}
		couponCodeService.exchange(id, member.getId(), null);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		TMember member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponCodeService.findPage(member.getId(), pageable));
		return "/shop/${theme}/member/coupon_code/list";
	}

}