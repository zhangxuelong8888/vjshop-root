
package com.vjshop.service.impl;

import com.vjshop.dao.TSeoDao;
import com.vjshop.entity.TSeo;
import com.vjshop.generated.db.tables.records.TSeoRecord;
import com.vjshop.service.TSeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Service - SEO设置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TSeoServiceImpl extends TBaseServiceImpl<TSeoRecord, TSeo, Long> implements TSeoService {

	@Autowired
	private TSeoDao tSeoDao;

	@Transactional(readOnly = true)
	public TSeo find(TSeo.Type type) {
		return tSeoDao.find(type);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "seo", condition = "#useCache")
	public TSeo find(TSeo.Type type, boolean useCache) {
		return tSeoDao.find(type);
	}

	@Override
	@Transactional
	@CacheEvict(value = "seo", allEntries = true)
	public TSeo save(TSeo seo) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		seo.setModifyDate(now);
		if (seo.getId() == null){
			seo.setCreateDate(now);
			seo.setVersion(0L);
		}
		return super.save(seo);
	}

	@Override
	@Transactional
	@CacheEvict(value = "seo", allEntries = true)
	public TSeo update(TSeo seo) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		seo.setModifyDate(now);
		return tSeoDao.update(seo, null);
	}

}