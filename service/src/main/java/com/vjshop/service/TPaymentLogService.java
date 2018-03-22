
package com.vjshop.service;

import com.vjshop.entity.TPaymentLog;

import java.util.List;

/**
 * Service - 支付记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TPaymentLogService extends TBaseService<TPaymentLog, Long> {

	/**
	 * 根据编号查找支付记录
	 *
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	TPaymentLog findBySn(String sn);

	/**
	 * 支付处理
	 *
	 * @param paymentLog
	 *            支付记录
	 */
	void handle(TPaymentLog paymentLog);

	List<TPaymentLog> findByOrderId(Long... orderIds);

}