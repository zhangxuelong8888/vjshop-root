
package com.vjshop.service.impl;

import com.vjshop.dao.TMessageConfigDao;
import com.vjshop.entity.TMessageConfig;
import com.vjshop.generated.db.tables.records.TMessageConfigRecord;
import com.vjshop.service.TMessageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Service - 消息配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TMessageConfigServiceImpl extends TBaseServiceImpl<TMessageConfigRecord, TMessageConfig, Long>
		implements TMessageConfigService {

	@Autowired
	private TMessageConfigDao tMessageConfigDao;

	@Transactional(readOnly = true)
	@Cacheable("messageConfig")
	public TMessageConfig find(TMessageConfig.Type type) {
		return tMessageConfigDao.findByType(type);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public TMessageConfig save(TMessageConfig messageConfig) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		messageConfig.setModifyDate(now);
		if (messageConfig.getId() == null){
			messageConfig.setCreateDate(now);
			messageConfig.setVersion(0L);
			messageConfig = tMessageConfigDao.insertAndFetch(messageConfig);
		}else{
			tMessageConfigDao.update(messageConfig, null);
		}
		return messageConfig;
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public TMessageConfig update(TMessageConfig messageConfig) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		messageConfig.setModifyDate(now);
		tMessageConfigDao.update(messageConfig, null);
		return messageConfig;
	}

}