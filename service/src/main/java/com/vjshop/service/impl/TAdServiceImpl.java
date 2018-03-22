
package com.vjshop.service.impl;

import com.vjshop.dao.TAdDao;
import com.vjshop.entity.TAd;
import com.vjshop.generated.db.tables.records.TAdRecord;
import com.vjshop.service.TAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Service - 广告
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TAdServiceImpl extends TBaseServiceImpl<TAdRecord, TAd, Long> implements TAdService {

	@Autowired
	private TAdDao tAdDao;

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public TAd save(TAd ad) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ad.setModifyDate(now);
		if (ad.getId() == null){
			ad.setCreateDate(now);
			ad.setVersion(0L);
		}
		return super.save(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public TAd update(TAd ad) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ad.setModifyDate(now);
		return tAdDao.update(ad, null);
	}

}