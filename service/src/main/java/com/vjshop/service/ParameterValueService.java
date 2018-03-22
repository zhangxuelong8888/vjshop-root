
package com.vjshop.service;

import java.util.List;

import com.vjshop.entity.ParameterValue;

/**
 * Service - 参数值
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface ParameterValueService {

	/**
	 * 参数值过滤
	 * 
	 * @param parameterValues
	 *            参数值
	 */
	void filter(List<ParameterValue> parameterValues);

}