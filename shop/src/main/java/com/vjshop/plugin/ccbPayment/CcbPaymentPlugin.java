
package com.vjshop.plugin.ccbPayment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPaymentLog;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.util.RSAUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 中国建设银行网上支付
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("ccbPaymentPlugin")
public class CcbPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "中国建设银行网上支付";
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
		return "ccb_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "ccb_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "ccb_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://ibsbjstar.ccb.com.cn/app/ccbMain";
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
		parameterMap.put("MERCHANTID", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("POSID", pluginConfig.getAttributesMap("posId"));
		parameterMap.put("BRANCHID", pluginConfig.getAttributesMap("branchId"));
		parameterMap.put("ORDERID", sn);
		parameterMap.put("PAYMENT", paymentLog.getAmount().setScale(2).toString());
		parameterMap.put("CURCODE", "01");
		parameterMap.put("TXCODE", "520100");
		parameterMap.put("REMARK1", "vjshop");
		parameterMap.put("REMARK2", "");
		if (StringUtils.equals(pluginConfig.getAttributesMap("isPhishing"), "true")) {
			String key = pluginConfig.getAttributesMap("key");
			parameterMap.put("TYPE", "1");
			parameterMap.put("PUB", StringUtils.substring(key, -30, key.length()));
			parameterMap.put("GATEWAY", "");
			parameterMap.put("CLIENTIP", request.getRemoteAddr());
			parameterMap.put("REGINFO", "");
			parameterMap.put("PROINFO", "");
			parameterMap.put("REFERER", "");
		}
		parameterMap.put("MAC", generateSign(parameterMap));
		parameterMap.remove("PUB");
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		TPaymentLog paymentLog = getPaymentLog(request.getParameter("ORDERID"));
		Map<String, Object> signMap = new LinkedHashMap<String, Object>();
		signMap.put("POSID", request.getParameter("POSID"));
		signMap.put("BRANCHID", request.getParameter("BRANCHID"));
		signMap.put("ORDERID", request.getParameter("ORDERID"));
		signMap.put("PAYMENT", request.getParameter("PAYMENT"));
		signMap.put("CURCODE", request.getParameter("CURCODE"));
		signMap.put("REMARK1", request.getParameter("REMARK1"));
		signMap.put("REMARK2", request.getParameter("REMARK2"));
		if (PaymentPlugin.NotifyMethod.async.equals(notifyMethod)) {
			signMap.put("ACC_TYPE", request.getParameter("ACC_TYPE"));
		}
		signMap.put("SUCCESS", request.getParameter("SUCCESS"));
		if (StringUtils.equals(pluginConfig.getAttributesMap("isPhishing"), "true")) {
			signMap.put("TYPE", request.getParameter("TYPE"));
			signMap.put("REFERER", request.getParameter("REFERER"));
			signMap.put("CLIENTIP", request.getParameter("CLIENTIP"));
		}
		if (paymentLog != null && verifySign(signMap, request.getParameter("SIGN")) && "Y".equals(request.getParameter("SUCCESS")) && paymentLog.getAmount().compareTo(new BigDecimal(request.getParameter("PAYMENT"))) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return request.getParameter("ORDERID");
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
		return DigestUtils.md5Hex(joinKeyValue(new LinkedHashMap<String, Object>(parameterMap), null, null, "&", false, "MAC"));
	}

	/**
	 * 验证签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @param sign
	 *            签名
	 * @return 是否验证通过
	 */
	private boolean verifySign(Map<String, ?> parameterMap, String sign) {
		try {
			TPluginConfig pluginConfig = getPluginConfig();
			PublicKey publicKey = RSAUtils.generatePublicKey(toBytes(pluginConfig.getAttributesMap("key")));
			return RSAUtils.verify("MD5withRSA", publicKey, toBytes(sign), joinKeyValue(new LinkedHashMap<String, Object>(parameterMap), null, null, "&", false, "SIGN").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 转换字符串为字节数组
	 * 
	 * @param str
	 *            字符串
	 * @return 字节数组
	 */
	private byte[] toBytes(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

}