
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryTemplate;

/**
 * Service - 快递单模板
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TDeliveryTemplateService extends TBaseService<TDeliveryTemplate, Long> {

	/**
	 * 查找默认快递单模板
	 * 
	 * @return 默认快递单模板，若不存在则返回null
	 */
	TDeliveryTemplate findDefault();

	Page<TDeliveryTemplate> findPage(Pageable pageable);
}