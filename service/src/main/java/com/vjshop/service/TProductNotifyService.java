
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.*;

import java.util.List;

/**
 * Service - 到货通知
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TProductNotifyService extends TBaseService<TProductNotify, Long> {

	/**
	 * 查找到货通知分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            商品是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @param pageable
	 *            分页信息
	 * @return 到货通知分页
	 */
	Page<TProductNotify> findPage(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable);

	/**
	 * 查找到货通知数量
	 *
	 * @param memberId
	 *            会员ID
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            商品是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @return 到货通知数量
	 */
	Long count(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent);

	/**
	 * 发送到货通知
	 *
	 * @param productNotifies
	 *            到货通知
	 * @return 发送到货通知数
	 */
	int send(List<TProductNotify> productNotifies);

	/**
	 * 判断到货通知是否存在
	 *
	 * @param productId
	 *            商品ID
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 到货通知是否存在
	 */
	boolean exists(Long productId, String email);

	List<TProductNotify> findByMemberId(Long memberId);

}