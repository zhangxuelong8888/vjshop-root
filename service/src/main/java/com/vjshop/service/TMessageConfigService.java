
package com.vjshop.service;

import com.vjshop.entity.TMessageConfig;

/**
 * Service - 消息配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TMessageConfigService extends TBaseService<TMessageConfig, Long> {

	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	TMessageConfig find(TMessageConfig.Type type);

}