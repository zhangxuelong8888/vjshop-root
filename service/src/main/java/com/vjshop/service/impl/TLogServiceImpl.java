package com.vjshop.service.impl;

import com.vjshop.dao.TLogDao;
import com.vjshop.entity.TLog;
import com.vjshop.generated.db.tables.records.TLogRecord;
import com.vjshop.service.TLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Service - 日志
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TLogServiceImpl extends TBaseServiceImpl<TLogRecord, TLog, Long> implements TLogService {

	@Autowired
	private TLogDao logDao;

	public void clear() {
		logDao.removeAll();
	}

	@Override
	public TLog save(TLog log) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		log.setCreateDate(now);
		log.setModifyDate(now);
		log.setVersion(0L);
		return logDao.insertAndFetch(log);
	}

}