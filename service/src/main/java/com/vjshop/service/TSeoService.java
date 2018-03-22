
package com.vjshop.service;

import com.vjshop.entity.TSeo;

/**
 * Service - SEO设置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TSeoService extends TBaseService<TSeo, Long> {

	/**
	 * 查找SEO设置
	 * 
	 * @param type
	 *            类型
	 * @return SEO设置
	 */
	TSeo find(TSeo.Type type);

	/**
	 * 查找SEO设置
	 * 
	 * @param type
	 *            类型
	 * @param useCache
	 *            是否使用缓存
	 * @return SEO设置
	 */
	TSeo find(TSeo.Type type, boolean useCache);

}