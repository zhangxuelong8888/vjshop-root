package com.vjshop.plugin.yeepayPayment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPaymentLog;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.util.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 易宝支付
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("yeepayPaymentPlugin")
public class YeepayPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "易宝支付";
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
		return "yeepay_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "yeepay_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "yeepay_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://www.yeepay.com/app-merchant-proxy/node";
	}

	@Override
	public PaymentPlugin.RequestMethod getRequestMethod() {
		return PaymentPlugin.RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "GBK";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(sn);
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("p0_Cmd", "Buy");
		parameterMap.put("p1_MerId", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("p2_Order", sn);
		parameterMap.put("p3_Amt", paymentLog.getAmount().setScale(2).toString());
		parameterMap.put("p4_Cur", "CNY");
		parameterMap.put("p5_Pid", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 20));
		parameterMap.put("p7_Pdesc", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 20));
		parameterMap.put("p8_Url", getNotifyUrl(PaymentPlugin.NotifyMethod.general));
		parameterMap.put("p9_SAF", "0");
		parameterMap.put("pa_MP", "vjshop");
		parameterMap.put("pr_NeedResponse", "1");
		parameterMap.put("hmac", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(request.getParameter("r6_Order"));
		Map<String, String> parameterValuesMap = WebUtils.parse(request.getQueryString(), "GBK");
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("p1_MerId", parameterValuesMap.get("p1_MerId"));
		parameterMap.put("r0_Cmd", parameterValuesMap.get("r0_Cmd"));
		parameterMap.put("r1_Code", parameterValuesMap.get("r1_Code"));
		parameterMap.put("r2_TrxId", parameterValuesMap.get("r2_TrxId"));
		parameterMap.put("r3_Amt", parameterValuesMap.get("r3_Amt"));
		parameterMap.put("r4_Cur", parameterValuesMap.get("r4_Cur"));
		parameterMap.put("r5_Pid", parameterValuesMap.get("r5_Pid"));
		parameterMap.put("r6_Order", parameterValuesMap.get("r6_Order"));
		parameterMap.put("r7_Uid", parameterValuesMap.get("r7_Uid"));
		parameterMap.put("r8_MP", parameterValuesMap.get("r8_MP"));
		parameterMap.put("r9_BType", parameterValuesMap.get("r9_BType"));
		if (paymentLog != null && generateSign(parameterMap).equals(parameterValuesMap.get("hmac")) && pluginConfig.getAttributesMap("partner").equals(parameterValuesMap.get("p1_MerId")) && "1".equals(parameterValuesMap.get("r1_Code"))
				&& paymentLog.getAmount().compareTo(new BigDecimal(parameterValuesMap.get("r3_Amt"))) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("r6_Order");
	}

	@Override
	public String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		if ("2".equals(WebUtils.parse(request.getQueryString(), "GBK").get("r9_BType"))) {
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
	private String generateSign(Map<String, Object> parameterMap) {
		TPluginConfig pluginConfig = getPluginConfig();
		return hmacDigest(joinValue(parameterMap, null, null, null, false, "hmac"), pluginConfig.getAttributesMap("key"));
	}

	/**
	 * Hmac加密
	 * 
	 * @param value
	 *            值
	 * @param key
	 *            密钥
	 * @return 密文
	 */
	private String hmacDigest(String value, String key) {
		try {
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacMD5"));
			byte[] bytes = mac.doFinal(value.getBytes("UTF-8"));

			StringBuilder digest = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					digest.append("0");
				}
				digest.append(hex);
			}
			return digest.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}