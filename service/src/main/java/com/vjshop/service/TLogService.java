package com.vjshop.service;

import com.vjshop.entity.TLog;

/**
 * Service - 日志
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TLogService extends TBaseService<TLog, Long> {

	/**
	 * 清空日志
	 */
	void clear();

}