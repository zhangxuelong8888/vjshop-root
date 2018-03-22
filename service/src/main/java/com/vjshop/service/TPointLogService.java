
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TPointLog;

/**
 * Service - 积分记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TPointLogService extends TBaseService<TPointLog, Long> {

	/**
	 * 查找积分记录分页
	 * 
	 * @param memberId
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	Page<TPointLog> findPage(Long memberId, Pageable pageable);

}