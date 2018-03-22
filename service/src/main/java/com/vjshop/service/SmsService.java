
package com.vjshop.service;

import java.util.Date;
import java.util.Map;

import com.vjshop.entity.TOrder;

/**
 * Service - 短信
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface SmsService {

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 * @param async
	 *            是否异步
	 */
	void send(String[] mobiles, String content, Date sendTime, boolean async);

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param sendTime
	 *            发送时间
	 * @param async
	 *            是否异步
	 */
	void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async);

	/**
	 * 发送短信(异步)
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            内容
	 */
	void send(String mobile, String content);

	/**
	 * 发送短信(异步)
	 * 
	 * @param mobile
	 *            手机号码
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 */
	void send(String mobile, String templatePath, Map<String, Object> model);

	/**
	 * 发送会员注册短信(异步)
	 * 
	 * @param memberId
	 *            会员ID
	 */
	void sendRegisterMemberSms(Long memberId);

	/**
	 * 发送订单创建短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCreateOrderSms(TOrder order);

	/**
	 * 发送订单更新短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendUpdateOrderSms(TOrder order);

	/**
	 * 发送订单取消短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCancelOrderSms(TOrder order);

	/**
	 * 发送订单审核短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReviewOrderSms(TOrder order);

	/**
	 * 发送订单收款短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendPaymentOrderSms(TOrder order);

	/**
	 * 发送订单退款短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendRefundsOrderSms(TOrder order);

	/**
	 * 发送订单发货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendShippingOrderSms(TOrder order);

	/**
	 * 发送订单退货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReturnsOrderSms(TOrder order);

	/**
	 * 发送订单收货短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendReceiveOrderSms(TOrder order);

	/**
	 * 发送订单完成短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendCompleteOrderSms(TOrder order);

	/**
	 * 发送订单失败短信(异步)
	 * 
	 * @param order
	 *            订单
	 */
	void sendFailOrderSms(TOrder order);

	/**
	 * 获取短信余额
	 * 
	 * @return 短信余额，查询失败则返回-1
	 */
	long getBalance();

}