
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.dao.TNavigationDao;
import com.vjshop.entity.TNavigation;
import com.vjshop.generated.db.tables.records.TNavigationRecord;
import com.vjshop.service.TNavigationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 导航
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TNavigationServiceImpl extends TBaseServiceImpl<TNavigationRecord, TNavigation, Long>
		implements TNavigationService {

	@Autowired
	private TNavigationDao tNavigationDao;

	@Transactional(readOnly = true)
	public List<TNavigation> findList(TNavigation.Position position) {
		return tNavigationDao.findList(position);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "navigation", condition = "#useCache")
	public List<TNavigation> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		if (CollectionUtils.isEmpty(orders)) {
			orders.add(new Order(com.vjshop.generated.db.tables.TNavigation.T_NAVIGATION.ORDERS.getName(), Order.Direction.asc));
		}
		return tNavigationDao.findList(null, null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public TNavigation save(TNavigation navigation) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		navigation.setModifyDate(now);
		if (navigation.getId() == null){
			navigation.setCreateDate(now);
			navigation.setVersion(0L);
		}
		return super.save(navigation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "navigation", allEntries = true)
	public TNavigation update(TNavigation navigation) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		navigation.setModifyDate(now);
		return tNavigationDao.update(navigation, null);
	}

}