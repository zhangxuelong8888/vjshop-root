
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.dao.TTagDao;
import com.vjshop.entity.TTag;
import com.vjshop.generated.db.tables.records.TTagRecord;
import com.vjshop.service.TTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 标签
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TTagServiceImpl extends TBaseServiceImpl<TTagRecord, TTag, Long>
		implements TTagService {

	@Autowired
	private TTagDao tTagDao;

	@Transactional(readOnly = true)
	public List<TTag> findList(TTag.Type type) {
		return tTagDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "tag", condition = "#useCache")
	public List<TTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tTagDao.findList(null, null, count, filters, orders);
	}

	@Override
	public List<TTag> findByTypes(Integer... types){
		return tTagDao.fetch(com.vjshop.generated.db.tables.TTag.T_TAG.TYPE, types);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public TTag save(TTag tag) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tag.setModifyDate(now);
		if (tag.getId() == null){
			tag.setCreateDate(now);
			tag.setVersion(0L);
		}
		return super.save(tag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "tag", allEntries = true)
	public TTag update(TTag tag) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tag.setModifyDate(now);
		return tTagDao.update(tag, null);
	}

}