
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TFriendLink;

import java.util.List;

/**
 * Service - 友情链接
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TFriendLinkService extends TBaseService<TFriendLink, Long> {

	/**
	 * 查找友情链接
	 * 
	 * @param type
	 *            类型
	 * @return 友情链接
	 */
	List<TFriendLink> findList(TFriendLink.Type type);

	/**
	 * 查找友情链接
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 友情链接
	 */
	List<TFriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}