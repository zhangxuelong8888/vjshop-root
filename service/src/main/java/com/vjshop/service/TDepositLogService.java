
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TDepositLog;

/**
 * Service - 预存款记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TDepositLogService extends TBaseService<TDepositLog, Long> {

	/**
	 * 查找预存款记录分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param pageable
	 *            分页信息
	 * @return 预存款记录分页
	 */
	Page<TDepositLog> findPage(Long memberId, Pageable pageable);

}