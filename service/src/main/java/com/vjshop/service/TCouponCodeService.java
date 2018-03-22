
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TCouponCode;

import java.util.List;

/**
 * Service - 优惠码
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TCouponCodeService extends TBaseService<TCouponCode, Long> {

	/**
	 * 判断优惠码是否存在
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码是否存在
	 */
	boolean codeExists(String code);

	/**
	 * 根据号码查找优惠码
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	TCouponCode findByCode(String code);

	/**
	 * 生成优惠码
	 * 
	 * @param couponId
	 *            优惠券ID
	 * @param prefix
	 *            优惠券前缀
	 * @param memberId
	 *            会员ID
	 * @return 优惠码
	 */
	TCouponCode generate(Long couponId, String prefix, Long memberId);

	/**
	 * 生成优惠码
	 * 
	 * @param couponId
	 *            优惠券ID
	 * @param prefix
	 *            优惠券前缀
	 * @param memberId
	 *            会员ID
	 * @param count
	 *            数量
	 * @return 优惠码
	 */
	List<TCouponCode> generate(Long couponId, String prefix, Long memberId, Integer count);

	/**
	 * 兑换优惠码
	 * 
	 * @param couponId
	 *            优惠券ID
	 * @param memberId
	 *            会员ID
	 * @param operator
	 *            操作员
	 * @return 优惠码
	 */
	TCouponCode exchange(Long couponId, Long memberId, String operator);

	/**
	 * 查找优惠码分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	Page<TCouponCode> findPage(Long memberId, Pageable pageable);

	/**
	 * 查找优惠码数量
	 * 
	 * @param couponId
	 *            优惠券ID
	 * @param memberId
	 *            会员ID
	 * @param hasBegun
	 *            是否已开始
	 * @param hasExpired
	 *            是否已过期
	 * @param isUsed
	 *            是否已使用
	 * @return 优惠码数量
	 */
	Long count(Long couponId, Long memberId, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

	TCouponCode findDetailById(Long id);

}