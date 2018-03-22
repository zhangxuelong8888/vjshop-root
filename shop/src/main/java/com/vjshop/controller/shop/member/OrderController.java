
package com.vjshop.controller.shop.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.*;
import com.vjshop.service.*;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 订单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberOrderController")
@RequestMapping("/member/order")
public class OrderController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
    private TOrderService orderService;
    @Autowired
    private TOrderItemService orderItemService;
	@Autowired
	private TShippingService shippingService;

	/**
	 * 检查锁定
	 */
	@RequestMapping(value = "/check_lock", method = RequestMethod.POST)
	public @ResponseBody
	Message checkLock(Long id) {
		TOrder order = orderService.find(id);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (!member.getId().equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/transit_step", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> transitStep(String shippingSn) {
		Map<String, Object> data = new HashMap<String, Object>();
		TShipping shipping = shippingService.findBySn(shippingSn);
		TOrder order = orderService.find(shipping.getOrders());
		if (shipping == null || order == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TMember member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("transitSteps", shippingService.getTransitSteps(shipping));
		return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, TOrder.Status status, ModelMap model) {
		Long memberId = memberService.getCurrentUserId();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", orderService.findPage(null, status, memberId, null, null, null, null, null, null, null, pageable));
		return "/shop/${theme}/member/order/list";
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		TOrder order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if (member == null || !member.getId().equals(order.getMember())) {
			return ERROR_VIEW;
		}
		order.setOrderItemsList(orderItemService.findListByOrderId(order.getId()));
        order.setShippingsList(shippingService.findByOrderIds(order.getId()));
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		model.addAttribute("order", order);
		return "/shop/${theme}/member/order/view";
	}

	/**
	 * 取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		TOrder order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (member == null || !member.getId().equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (order.hasExpired() || (TOrder.Status.pendingPayment.ordinal() != order.getStatus() && TOrder.Status.pendingReview.ordinal() != order.getStatus())) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		orderService.cancel(order);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 收货
	 */
	@RequestMapping(value = "/receive", method = RequestMethod.POST)
	public @ResponseBody
	Message receive(String sn) {
		TOrder order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (member == null || !member.getId().equals(order.getMember())) {
			return ERROR_MESSAGE;
		}
		if (order.hasExpired() || !Integer.valueOf(TOrder.Status.shipped.ordinal()).equals(order.getStatus())) {
			return ERROR_MESSAGE;
		}
		if (orderService.isLocked(order, member, true)) {
			return Message.warn("shop.member.order.locked");
		}
		orderService.receive(order, null);
		return SUCCESS_MESSAGE;
	}

}