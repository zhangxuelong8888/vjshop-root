
package com.vjshop.service.impl;

import com.vjshop.dao.TAdDao;
import com.vjshop.dao.TAdPositionDao;
import com.vjshop.entity.TAd;
import com.vjshop.entity.TAdPosition;
import com.vjshop.generated.db.tables.records.TAdPositionRecord;
import com.vjshop.service.TAdPositionService;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;

/**
 * Service - 广告位
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TAdPositionServiceImpl extends TBaseServiceImpl<TAdPositionRecord, TAdPosition, Long> implements TAdPositionService {

	@Autowired
	private TAdPositionDao tAdPositionDao;

	@Autowired
	private TAdDao tAdDao;

	@Transactional(readOnly = true)
	@Cacheable(value = "adPosition", condition = "#useCache")
	public TAdPosition find(Long id, boolean useCache) {
		TAdPosition adPosition = tAdPositionDao.find(id);
		//查询广告内容
		SelectQuery query = tAdDao.getQuery();
		query.addConditions(com.vjshop.generated.db.tables.TAd.T_AD.AD_POSITION.eq(id));
		adPosition.setAds(new HashSet<TAd>(tAdDao.findList(query, null, null, null, null)));
		return adPosition;
	}

	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public TAdPosition save(TAdPosition adPosition) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		adPosition.setModifyDate(now);
		if (adPosition.getId() == null){
			adPosition.setCreateDate(now);
			adPosition.setVersion(0L);
		}
		return super.save(adPosition);
	}

	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public TAdPosition update(TAdPosition adPosition) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		adPosition.setModifyDate(now);
		return tAdPositionDao.update(adPosition, null);
	}

}