
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.dao.TFriendLinkDao;
import com.vjshop.entity.TFriendLink;
import com.vjshop.generated.db.tables.records.TFriendLinkRecord;
import com.vjshop.service.TFriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 友情链接
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TFriendLinkServiceImpl extends TBaseServiceImpl<TFriendLinkRecord, TFriendLink, Long>
		implements TFriendLinkService {

	@Autowired
	private TFriendLinkDao tFriendLinkDao;

	@Transactional(readOnly = true)
	public List<TFriendLink> findList(TFriendLink.Type type) {
		return tFriendLinkDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "friendLink", condition = "#useCache")
	public List<TFriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tFriendLinkDao.findList(null, null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public TFriendLink save(TFriendLink friendLink) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		friendLink.setModifyDate(now);
		if (friendLink.getId() == null){
			friendLink.setCreateDate(now);
			friendLink.setVersion(0L);
		}
		return super.save(friendLink);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public TFriendLink update(TFriendLink friendLink) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		friendLink.setModifyDate(now);
		return tFriendLinkDao.update(friendLink, null);
	}

}