package com.vjshop.plugin.alipayBankPayment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Setting;
import com.vjshop.entity.TPaymentLog;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.util.SystemUtils;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 支付宝(纯网关)
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("alipayBankPaymentPlugin")
public class AlipayBankPaymentPlugin extends PaymentPlugin {

	/** 默认银行 */
	private static final String DEFAULT_BANK = "ICBCB2C";

	/** 银行参数名称 */
	public static final String BANK_PARAMETER_NAME = "bank";

	@Override
	public String getName() {
		return "支付宝(纯网关)";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "VJSHOP";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.vjshop.com";
	}

	@Override
	public String getInstallUrl() {
		return "alipay_bank_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "alipay_bank_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "alipay_bank_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://mapi.alipay.com/gateway.do";
	}

	@Override
	public PaymentPlugin.RequestMethod getRequestMethod() {
		return PaymentPlugin.RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("service", "create_direct_pay_by_user");
		parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("_input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl(PaymentPlugin.NotifyMethod.sync));
		parameterMap.put("notify_url", getNotifyUrl(PaymentPlugin.NotifyMethod.async));
		parameterMap.put("out_trade_no", sn);
		parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
		parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
		parameterMap.put("payment_type", "1");
		String bank = request.getParameter(BANK_PARAMETER_NAME);
		parameterMap.put("defaultbank", StringUtils.isNotEmpty(bank) ? bank : DEFAULT_BANK);
		parameterMap.put("seller_id", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("total_fee", paymentLog.getAmount().setScale(2).toString());
		parameterMap.put("show_url", setting.getSiteUrl());
		parameterMap.put("paymethod", "bankPay");
		parameterMap.put("extend_param", "isv^1860648a1");
		parameterMap.put("exter_invoke_ip", request.getLocalAddr());
		parameterMap.put("extra_common_param", "vjshop");
		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(request.getParameter("out_trade_no"));
		if (paymentLog != null && generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && pluginConfig.getAttributesMap("partner").equals(request.getParameter("seller_id"))
				&& ("TRADE_SUCCESS".equals(request.getParameter("trade_status")) || "TRADE_FINISHED".equals(request.getParameter("trade_status"))) && paymentLog.getAmount().compareTo(new BigDecimal(request.getParameter("total_fee"))) == 0) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("service", "notify_verify");
			parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
			parameterMap.put("notify_id", request.getParameter("notify_id"));
			if ("true".equals(WebUtils.post("https://mapi.alipay.com/gateway.do", parameterMap))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("out_trade_no");
	}

	@Override
	public String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		if (PaymentPlugin.NotifyMethod.async.equals(notifyMethod)) {
			return "success";
		}
		return null;
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		TPluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, pluginConfig.getAttributesMap("key"), "&", true, "sign_type", "sign"));
	}

}