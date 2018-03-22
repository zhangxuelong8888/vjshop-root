package com.vjshop.controller.shop.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.service.TDepositLogService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.PluginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 预存款
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberDepositController")
@RequestMapping("/member/deposit")
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TDepositLogService depositLogService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> calculateFee(String paymentPluginId, BigDecimal amount) {
		Map<String, Object> data = new HashMap<String, Object>();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled() || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(amount));
		return data;
	}

	/**
	 * 检查余额
	 */
	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		TMember member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		return data;
	}

	/**
	 * 充值
	 */
	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public String recharge(ModelMap model) {
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		if (!paymentPlugins.isEmpty()) {
			model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(0));
			model.addAttribute("paymentPlugins", paymentPlugins);
		}
		return "/shop/${theme}/member/deposit/recharge";
	}

	/**
	 * 记录
	 */
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Integer pageNumber, ModelMap model) {
		TMember member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", depositLogService.findPage(member.getId(), pageable));
		return "/shop/${theme}/member/deposit/log";
	}

}