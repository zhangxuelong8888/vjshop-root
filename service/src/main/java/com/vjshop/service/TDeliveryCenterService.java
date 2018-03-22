
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryCenter;

/**
 * Service - 发货点
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TDeliveryCenterService extends TBaseService<TDeliveryCenter, Long> {

	/**
	 * 查找默认发货点
	 * 
	 * @return 默认发货点，若不存在则返回null
	 */
	TDeliveryCenter findDefault();

	Page<TDeliveryCenter> findPage(Pageable pageable);

	TDeliveryCenter findDetailById(Long id);

}