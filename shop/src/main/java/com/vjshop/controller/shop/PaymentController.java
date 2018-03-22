package com.vjshop.controller.shop;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.Setting;
import com.vjshop.entity.*;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.service.*;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 支付
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopPaymentController")
@RequestMapping("/payment")
public class PaymentController extends BaseController {

	@Autowired
	private TOrderService orderService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private PluginService pluginService;
	@Autowired
	private TPaymentLogService paymentLogService;

	/**
	 * 插件提交
	 */
	@RequestMapping(value = "/plugin_submit", method = RequestMethod.POST)
	public String pluginSubmit(TPaymentLog.Type type, String paymentPluginId, String sn, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if (type == null) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return ERROR_VIEW;
		}
		Setting setting = SystemUtils.getSetting();
		switch (type) {
		case recharge: {
			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return ERROR_VIEW;
			}
			TPaymentLog paymentLog = new TPaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type.ordinal());
			paymentLog.setStatus(TPaymentLog.Status.wait.ordinal());
			paymentLog.setFee(paymentPlugin.calculateFee(amount));
			paymentLog.setAmount(paymentPlugin.calculateAmount(amount));
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setMember(member.getId());
			Timestamp now = new Timestamp(System.currentTimeMillis());
			paymentLog.setCreateDate(now);
			paymentLog.setModifyDate(now);
			paymentLog.setVersion(0L);
			paymentLogService.save(paymentLog);
			model.addAttribute("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.rechargeDescription"), request));
			break;
		}
		case payment: {
			TOrder order = orderService.findBySn(sn);
			if (order == null || !member.getId().equals(order.getMember()) || orderService.isLocked(order, member, true)) {
				return ERROR_VIEW;
			}
			if (order.getPaymentMethodType() == null || TPaymentMethod.Method.online.ordinal() != order.getPaymentMethodType().intValue()) {
				return ERROR_VIEW;
			}
			if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
				return ERROR_VIEW;
			}
			TPaymentLog paymentLog = new TPaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type.ordinal());
			paymentLog.setStatus(TPaymentLog.Status.wait.ordinal());
			paymentLog.setFee(paymentPlugin.calculateFee(order.getAmount()));
			paymentLog.setAmount(paymentPlugin.calculateAmount(order.getAmount()));
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setOrders(order.getId());
			paymentLog.setMember(null);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			paymentLog.setCreateDate(now);
			paymentLog.setModifyDate(now);
			paymentLog.setVersion(0L);
			paymentLogService.save(paymentLog);
			model.addAttribute("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.paymentDescription", order.getSn()), request));
			break;
		}
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "/shop/${theme}/payment/plugin_submit";
	}

	/**
	 * 插件通知
	 */
	@RequestMapping("/plugin_notify/{pluginId}/{notifyMethod}")
	public String pluginNotify(@PathVariable String pluginId, @PathVariable PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request, ModelMap model) {
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(pluginId);
		if (paymentPlugin != null && paymentPlugin.verifyNotify(notifyMethod, request)) {
			String sn = paymentPlugin.getSn(request);
			TPaymentLog paymentLog = paymentLogService.findBySn(sn);
			if (paymentLog != null) {
				paymentLogService.handle(paymentLog);
				model.addAttribute("paymentLog", paymentLog);
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(notifyMethod, request));
			}
		}
		return "/shop/${theme}/payment/plugin_notify";
	}

}