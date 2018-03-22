
package com.vjshop.plugin.tenpayEscowPayment;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.entity.TPaymentLog;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.plugin.PaymentPlugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

/**
 * Plugin - 财付通(担保交易)
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("tenpayEscowPaymentPlugin")
public class TenpayEscowPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "财付通(担保交易)";
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
		return "tenpay_escow_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "tenpay_escow_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "tenpay_escow_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://gw.tenpay.com/gateway/pay.htm";
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
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("trade_mode", "2");
		parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl(PaymentPlugin.NotifyMethod.sync));
		parameterMap.put("notify_url", getNotifyUrl(PaymentPlugin.NotifyMethod.async));
		parameterMap.put("out_trade_no", sn);
		parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 30));
		parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 30));
		parameterMap.put("trans_type", "1");
		parameterMap.put("seller_id", pluginConfig.getAttributesMap("partner"));
		parameterMap.put("total_fee", paymentLog.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		parameterMap.put("fee_type", "1");
		parameterMap.put("spbill_create_ip", request.getLocalAddr());
		parameterMap.put("attach", "vjshop");
		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		TPluginConfig pluginConfig = getPluginConfig();
		if (generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && pluginConfig.getAttributesMap("partner").equals(request.getParameter("partner")) && "0".equals(request.getParameter("trade_state"))) {
			try {
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("input_charset", "utf-8");
				parameterMap.put("sign_type", "MD5");
				parameterMap.put("partner", pluginConfig.getAttributesMap("partner"));
				parameterMap.put("notify_id", request.getParameter("notify_id"));
				String verifyUrl = "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml?input_charset=utf-8&sign_type=MD5&partner=" + pluginConfig.getAttributesMap("partner") + "&notify_id=" + request.getParameter("notify_id") + "&sign=" + generateSign(parameterMap);
				Document document = new SAXReader().read(new URL(verifyUrl));
				Node node = document.selectSingleNode("/root/retcode");
				if ("0".equals(node.getText().trim())) {
					return true;
				}
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e.getMessage(), e);
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
			return "Success";
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
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + pluginConfig.getAttributesMap("key"), "&", true, "sign")).toUpperCase();
	}

}