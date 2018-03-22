package com.vjshop.plugin.tenpayBankPayment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.controller.admin.BaseController;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;

import com.vjshop.service.TPluginConfigService;
import com.vjshop.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 财付通(网银直连)
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminTenpayBankPaymentController")
@RequestMapping("/admin/payment_plugin/tenpay_bank_payment")
public class TenpayBankPaymentController extends BaseController {

	@Resource(name = "tenpayBankPaymentPlugin")
	private TenpayBankPaymentPlugin tenpayBankPaymentPlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!tenpayBankPaymentPlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(tenpayBankPaymentPlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 卸载
	 */
	@RequestMapping(value = "/uninstall", method = RequestMethod.POST)
	public @ResponseBody
	Message uninstall() {
		if (tenpayBankPaymentPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(tenpayBankPaymentPlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = tenpayBankPaymentPlugin.getPluginConfig();
		model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/tenpayBankPayment/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String paymentName, String partner, String key, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = tenpayBankPaymentPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(PaymentPlugin.PAYMENT_NAME_ATTRIBUTE_NAME, paymentName);
		attributes.put("partner", partner);
		attributes.put("key", key);
		attributes.put(PaymentPlugin.FEE_TYPE_ATTRIBUTE_NAME, feeType.toString());
		attributes.put(PaymentPlugin.FEE_ATTRIBUTE_NAME, fee.toString());
		attributes.put(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
		attributes.put(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
		pluginConfig.setAttributes(JsonUtils.toJson(attributes));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/payment_plugin/list.jhtml";
	}

}