
package com.vjshop.service.impl;

import java.util.List;

import com.vjshop.entity.SpecificationItem;
import com.vjshop.entity.SpecificationValue;
import com.vjshop.service.SpecificationValueService;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service - 规格值
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("specificationValueServiceImpl")
public class SpecificationValueServiceImpl implements SpecificationValueService {

	public boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues) {
		Assert.notEmpty(specificationItems);
		Assert.notEmpty(specificationValues);

		if (specificationValues.size() != specificationValues.size()) {
			return false;
		}
		for (int i = 0; i < specificationValues.size(); i++) {
			SpecificationItem specificationItem = specificationItems.get(i);
			SpecificationValue specificationValue = specificationValues.get(i);
			if (specificationItem == null || specificationValue == null || !specificationItem.isValid(specificationValue)) {
				return false;
			}
		}
		return true;
	}

}