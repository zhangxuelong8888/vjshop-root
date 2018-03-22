
package com.vjshop.service;

import java.util.List;

import com.vjshop.entity.SpecificationItem;

/**
 * Service - 规格项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface SpecificationItemService {

	/**
	 * 规格项过滤
	 * 
	 * @param specificationItems
	 *            规格项
	 */
	void filter(List<SpecificationItem> specificationItems);

}