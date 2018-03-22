
package com.vjshop.service;

import com.vjshop.entity.TAdPosition;

/**
 * Service - 广告位
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TAdPositionService extends TBaseService<TAdPosition, Long> {

	/**
	 * 查找广告位
	 * 
	 * @param id
	 *            ID
	 * @param useCache
	 *            是否使用缓存
	 * @return 广告位
	 */
	TAdPosition find(Long id, boolean useCache);

}