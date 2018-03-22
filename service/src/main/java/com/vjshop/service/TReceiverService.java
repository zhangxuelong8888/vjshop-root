
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TReceiver;

import java.util.List;

/**
 * Service - 收货地址
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TReceiverService extends TBaseService<TReceiver, Long> {

	/**
	 * 查找默认收货地址
	 * 
	 * @param memberId
	 *            会员ID
	 * @return 默认收货地址，若不存在则返回最新收货地址
	 */
	TReceiver findDefault(Long memberId);

	/**
	 * 获取收货地址数量
	 *
	 * @param memberId
	 *            会员ID
	 * @return 收货地址数量
	 */
	Long count(Long memberId);

	/**
	 * 查找收货地址
	 *
	 * @param memberId
	 *            会员ID
	 * @return 收货地址
	 */
	List<TReceiver> findList(Long memberId);

	/**
	 * 查找收货地址分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param pageable
	 *            分页信息
	 * @return 收货地址分页
	 */
	Page<TReceiver> findPage(Long memberId, Pageable pageable);

}