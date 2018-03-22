
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TCoupon;

/**
 * Service - 优惠券
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TCouponService extends TBaseService<TCoupon, Long> {

	/**
	 * 验证价格运算表达式
	 * 
	 * @param priceExpression
	 *            价格运算表达式
	 * @return 验证结果
	 */
	boolean isValidPriceExpression(String priceExpression);

	/**
	 * 查找优惠券分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	Page<TCoupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

}