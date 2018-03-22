package com.vjshop.plugin.unionpayPayment;

import java.math.BigDecimal;
import java.util.Date;
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
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 银联在线支付
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("unionpayPaymentPlugin")
public class UnionpayPaymentPlugin extends PaymentPlugin {

	/** 货币 */
	private static final String CURRENCY = "156";

	@Override
	public String getName() {
		return "银联在线支付";
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
		return "unionpay_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "unionpay_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "unionpay_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://unionpaysecure.com/api/Pay.action";
	}

	@Override
	public PaymentPlugin.RequestMethod getRequestMethod() {
		return PaymentPlugin.RequestMethod.post;
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
		parameterMap.put("version", "1.0.0");
		parameterMap.put("charset", "UTF-8");
		parameterMap.put("transType", "01");
		parameterMap.put("origQid", "");
		parameterMap.put("merId", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("merAbbr", StringUtils.abbreviate(setting.getSiteName().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 40));
		parameterMap.put("acqCode", "");
		parameterMap.put("merCode", "");
		parameterMap.put("commodityUrl", setting.getSiteUrl());
		parameterMap.put("commodityName", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 200));
		parameterMap.put("commodityUnitPrice", "");
		parameterMap.put("commodityQuantity", "");
		parameterMap.put("commodityDiscount", "");
		parameterMap.put("transferFee", "");
		parameterMap.put("orderNumber", sn);
		parameterMap.put("orderAmount", paymentLog.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		parameterMap.put("orderCurrency", CURRENCY);
		parameterMap.put("orderTime", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		parameterMap.put("customerIp", request.getLocalAddr());
		parameterMap.put("customerName", "");
		parameterMap.put("defaultPayType", "");
		parameterMap.put("defaultBankNumber", "");
		parameterMap.put("transTimeout", 10080000);
		parameterMap.put("frontEndUrl", getNotifyUrl(PaymentPlugin.NotifyMethod.sync));
		parameterMap.put("backEndUrl", getNotifyUrl(PaymentPlugin.NotifyMethod.async));
		parameterMap.put("merReserved", "");
		parameterMap.put("signMethod", "MD5");
		parameterMap.put("signature", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(request.getParameter("orderNumber"));
		if (paymentLog != null && generateSign(request.getParameterMap()).equals(request.getParameter("signature")) && pluginConfig.getAttributesMap("partner").equals(request.getParameter("merId")) && CURRENCY.equals(request.getParameter("orderCurrency"))
				&& "00".equals(request.getParameter("respCode")) && paymentLog.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(request.getParameter("orderAmount"))) == 0) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("version", "1.0.0");
			parameterMap.put("charset", "UTF-8");
			parameterMap.put("transType", "01");
			parameterMap.put("merId", pluginConfig.getAttributesMap("partner"));
			parameterMap.put("orderNumber", paymentLog.getSn());
			parameterMap.put("orderTime", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
			parameterMap.put("merReserved", "");
			parameterMap.put("signMethod", "MD5");
			parameterMap.put("signature", generateSign(parameterMap));
			String result = WebUtils.post("https://query.unionpaysecure.com/api/Query.action", parameterMap);
			if (ArrayUtils.contains(StringUtils.split(result, "&"), "respCode=00")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("orderNumber");
	}

	@Override
	public String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
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
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&" + DigestUtils.md5Hex(pluginConfig.getAttributesMap("key")), "&", false, "signMethod", "signature"));
	}

}