
package com.vjshop.service;

import java.util.List;

import com.vjshop.entity.SpecificationItem;
import com.vjshop.entity.SpecificationValue;

/**
 * Service - 规格值
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface SpecificationValueService {

	/**
	 * 规格值验证
	 * 
	 * @param specificationItems
	 *            规格项
	 * @param specificationValues
	 *            规格值
	 * @return 验证结果
	 */
	boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues);

}