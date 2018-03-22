
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TDepositLogDao;
import com.vjshop.generated.db.tables.records.TDepositLogRecord;
import com.vjshop.service.TDepositLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 预存款记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TDepositLogServiceImpl extends TBaseServiceImpl<TDepositLogRecord, com.vjshop.entity.TDepositLog, Long>
		implements TDepositLogService {

	@Autowired
	private TDepositLogDao tDepositLogDao;

	@Transactional(readOnly = true)
	public Page<com.vjshop.entity.TDepositLog> findPage(Long memberId, Pageable pageable) {
		return tDepositLogDao.findPage(memberId, pageable);
	}

}