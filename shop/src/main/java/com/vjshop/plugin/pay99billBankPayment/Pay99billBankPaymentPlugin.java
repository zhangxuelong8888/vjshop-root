package com.vjshop.plugin.pay99billBankPayment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPaymentLog;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 快钱支付(网银直连)
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("pay99billBankPaymentPlugin")
public class Pay99billBankPaymentPlugin extends PaymentPlugin {

	/** 默认银行 */
	private static final String DEFAULT_BANK = "ICBC";

	/** 银行参数名称 */
	public static final String BANK_PARAMETER_NAME = "bank";

	@Override
	public String getName() {
		return "快钱支付(网银直连)";
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
		return "pay_99bill_bank_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "pay_99bill_bank_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "pay_99bill_bank_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://www.99bill.com/gateway/recvMerchantInfoAction.htm";
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
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(sn);
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("inputCharset", "1");
		parameterMap.put("pageUrl", getNotifyUrl(PaymentPlugin.NotifyMethod.sync));
		parameterMap.put("bgUrl", getNotifyUrl(PaymentPlugin.NotifyMethod.async));
		parameterMap.put("version", "v2.0");
		parameterMap.put("language", "1");
		parameterMap.put("signType", "1");
		parameterMap.put("merchantAcctId", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("payerIP", request.getLocalAddr());
		parameterMap.put("orderId", sn);
		parameterMap.put("orderAmount", paymentLog.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		parameterMap.put("orderTime", DateFormatUtils.format(new Date(), "yyyyMMddhhmmss"));
		parameterMap.put("orderTimestamp", DateFormatUtils.format(new Date(), "yyyyMMddhhmmss"));
		parameterMap.put("productName", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 100));
		parameterMap.put("productDesc", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 400));
		parameterMap.put("ext1", "vjshop");
		parameterMap.put("payType", "10");
		String bank = request.getParameter(BANK_PARAMETER_NAME);
		parameterMap.put("bankId", StringUtils.isNotEmpty(bank) ? bank : DEFAULT_BANK);
		parameterMap.put("signMsg", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(request.getParameter("orderId"));
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("merchantAcctId", request.getParameter("merchantAcctId"));
		parameterMap.put("version", request.getParameter("version"));
		parameterMap.put("language", request.getParameter("language"));
		parameterMap.put("signType", request.getParameter("signType"));
		parameterMap.put("payType", request.getParameter("payType"));
		parameterMap.put("bankId", request.getParameter("bankId"));
		parameterMap.put("orderId", request.getParameter("orderId"));
		parameterMap.put("orderTime", request.getParameter("orderTime"));
		parameterMap.put("orderAmount", request.getParameter("orderAmount"));
		parameterMap.put("bindCard", request.getParameter("bindCard"));
		parameterMap.put("bindMobile", request.getParameter("bindMobile"));
		parameterMap.put("dealId", request.getParameter("dealId"));
		parameterMap.put("bankDealId", request.getParameter("bankDealId"));
		parameterMap.put("dealTime", request.getParameter("dealTime"));
		parameterMap.put("payAmount", request.getParameter("payAmount"));
		parameterMap.put("fee", request.getParameter("fee"));
		parameterMap.put("ext1", request.getParameter("ext1"));
		parameterMap.put("ext2", request.getParameter("ext2"));
		parameterMap.put("payResult", request.getParameter("payResult"));
		parameterMap.put("errCode", request.getParameter("errCode"));
		parameterMap.put("signMsg", request.getParameter("signMsg"));
		if (paymentLog != null && generateSign(parameterMap).equals(request.getParameter("signMsg")) && pluginConfig.getAttributesMap("partner").equals(request.getParameter("merchantAcctId")) && "10".equals(request.getParameter("payResult"))
				&& paymentLog.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(request.getParameter("payAmount"))) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("orderId");
	}

	@Override
	public String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		if (PaymentPlugin.NotifyMethod.async.equals(notifyMethod)) {
			return "<result>1</result><redirecturl>" + getNotifyUrl(PaymentPlugin.NotifyMethod.sync) + "</redirecturl>";
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
	private String generateSign(Map<String, Object> parameterMap) {
		TPluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(parameterMap, null, "&key=" + pluginConfig.getAttributesMap("key"), "&", true, "signMsg")).toUpperCase();
	}

}