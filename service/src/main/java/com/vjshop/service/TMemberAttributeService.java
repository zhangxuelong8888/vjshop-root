
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TMemberAttribute;

import java.util.List;

/**
 * Service - 会员注册项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TMemberAttributeService extends TBaseService<TMemberAttribute, Long> {

	/**
	 * 查找未使用的对象属性序号
	 * 
	 * @return 未使用的对象属性序号，若无可用序号则返回null
	 */
	Integer findUnusedPropertyIndex();

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 会员注册项
	 */
	List<TMemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 会员注册项
	 */
	List<TMemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找会员注册项
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param useCache
	 *            是否使用缓存
	 * @return 会员注册项
	 */
	List<TMemberAttribute> findList(Boolean isEnabled, boolean useCache);

	/**
	 * 会员注册项值验证
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @param values
	 *            值
	 * @return 是否验证通过
	 */
	boolean isValid(TMemberAttribute memberAttribute, String[] values);

	/**
	 * 转换为会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 * @param values
	 *            值
	 * @return 会员注册项值
	 */
	Object toMemberAttributeValue(TMemberAttribute memberAttribute, String[] values);

}