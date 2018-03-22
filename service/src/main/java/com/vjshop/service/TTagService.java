
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TTag;

import java.util.List;

/**
 * Service - 标签
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TTagService extends TBaseService<TTag, Long> {

	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签
	 */
	List<TTag> findList(TTag.Type type);

	/**
	 * 查找标签
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 标签
	 */
	List<TTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找标签
	 *
	 * @param types
	 *            标签类型
	 * @return 标签
	 */
	List<TTag> findByTypes(Integer... types);
}