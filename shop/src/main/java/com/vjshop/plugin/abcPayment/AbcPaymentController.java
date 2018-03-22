
package com.vjshop.plugin.abcPayment;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.controller.admin.BaseController;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.service.TPluginConfigService;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.RSAUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 中国农业银行网上支付
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminAbcPaymentController")
@RequestMapping("/admin/payment_plugin/abc_payment")
public class AbcPaymentController extends BaseController {

	@Resource(name = "abcPaymentPlugin")
	private AbcPaymentPlugin abcPaymentPlugin;
	@Autowired
	private TPluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!abcPaymentPlugin.getIsInstalled()) {
			TPluginConfig pluginConfig = new TPluginConfig();
			pluginConfig.setPluginId(abcPaymentPlugin.getId());
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
		if (abcPaymentPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(abcPaymentPlugin.getId());
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		TPluginConfig pluginConfig = abcPaymentPlugin.getPluginConfig();
		model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
		model.addAttribute("pluginConfig", pluginConfig);
		return "/com/vjshop/plugin/abcPayment/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String paymentName, String merchantId, MultipartFile keyFile, String keyPassword, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		TPluginConfig pluginConfig = abcPaymentPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(PaymentPlugin.PAYMENT_NAME_ATTRIBUTE_NAME, paymentName);
		attributes.put("merchantId", merchantId);
		if (keyFile != null && !keyFile.isEmpty()) {
			InputStream inputStream = null;
			try {
				inputStream = keyFile.getInputStream();
				PrivateKey privateKey = (PrivateKey) RSAUtils.getKey("PKCS12", inputStream, keyPassword);
				attributes.put("key", RSAUtils.getKeyString(privateKey));
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (RuntimeException e) {
				addFlashMessage(redirectAttributes, Message.warn("admin.plugin.abcPayment.keyInvalid"));
				return "redirect:setting.jhtml";
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		} else {
			attributes.put("key", pluginConfig.getAttributesMap("key"));
		}
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