
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TNavigation;

import java.util.List;

/**
 * Service - 导航
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TNavigationService extends TBaseService<TNavigation, Long> {

	/**
	 * 查找导航
	 * 
	 * @param position
	 *            位置
	 * @return 导航
	 */
	List<TNavigation> findList(TNavigation.Position position);

	/**
	 * 查找导航
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 导航
	 */
	List<TNavigation> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}