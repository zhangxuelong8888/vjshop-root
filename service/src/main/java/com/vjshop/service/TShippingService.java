
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TShipping;

import java.util.List;
import java.util.Map;

/**
 * Service - 发货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TShippingService extends TBaseService<TShipping, Long> {

	/**
	 * 根据编号查找发货单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 发货单，若不存在则返回null
	 */
	TShipping findBySn(String sn);

	/**
	 * 获取物流动态
	 * 
	 * @param shipping
	 *            发货单
	 * @return 物流动态
	 */
	List<Map<String, String>> getTransitSteps(TShipping shipping);

	List<TShipping> findByOrderIds(Long... ids);

	TShipping findDetailById(Long id);

	TShipping insertAndFetch(TShipping tShipping);

	Page<TShipping> findPage(Pageable pageable);

}