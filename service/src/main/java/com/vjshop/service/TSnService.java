
package com.vjshop.service;

import com.vjshop.entity.TSn;

/**
 * Service - 序列号
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TSnService {

	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	String generate(TSn.Type type);

}