
package com.vjshop.service;

import com.vjshop.entity.TCart;

/**
 * Service - 购物车
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TCartService extends TBaseService<TCart, Long> {

	/**
	 * 获取当前购物车
	 * 
	 * @return 当前购物车，若不存在则返回null
	 */
	TCart getCurrent();

	/**
	 * 获取当前已选中商品购物车
	 *
	 * @return 当前购物车，若不存在则返回null
	 */
	TCart getCurrentSelected();

	/**
	 * 获取购物车
	 *
	 * @param memberId
	 *            会员ID
	 * @return 购物车，若不存在则返回null
	 */
	TCart findByMemberId(Long memberId);

	/**
	 * 添加商品至当前购物车
	 * 
	 * @param productId
	 *            商品ID
	 * @param quantity
	 *            数量
	 * @return 当前购物车
	 */
	TCart add(Long productId, int quantity);

	/**
	 * 合并临时购物车至会员
	 * 
	 * @param memberId
	 *            会员ID
	 * @param cart
	 *            临时购物车
	 */
	void merge(Long memberId, TCart cart);

	/**
	 * 清除过期购物车
	 */
	void evictExpired();

	/**
	 * 清空购物车(级联删除)
	 * @param cartId
	 */
	void deleteCascade(Long cartId);

	/**
	 * 删除购物车项
	 * @param cartId
	 */
	void deleteSelected(Long cartId);

	/**
	 * 更新购物车item为已选中
	 * @param cartItemId 购物车项id
	 * @param isSelected 是否选中
	 */
	void updateSelectStatus(Long cartItemId, Boolean isSelected);

	/**
	 * 更新购物车item为已选中
	 * @param cartItemIds 购物车项ids
	 * @param isSelected 是否选中
	 */
	void updateSelectStatus(String cartItemIds, Boolean isSelected);
}