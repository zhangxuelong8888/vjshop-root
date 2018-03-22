
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TAttribute;

import java.util.List;

/**
 * Service - 属性
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TAttributeService extends TBaseService<TAttribute,Long>{

	/**
	 * 查找未使用的属性序号
	 *
	 * @param productCategoryId
	 *            商品分类
	 * @return 未使用的属性序号，若不存在则返回null
	 */
	Integer findUnusedPropertyIndex(Long productCategoryId);
	/**
	 * 转换为属性值
	 * 
	 * @param attribute
	 *            属性
	 * @param value
	 *            值
	 * @return 属性值
	 */
	String toAttributeValue(TAttribute attribute, String value);

	List<TAttribute> findByProductCategoryId(Long... productCategordyIds);

	Page<TAttribute> findPage(Pageable pageable);

	TAttribute findDetailById(Long id);
	
	/**
	 * 查找属性
	 *
	 * @param productCategoryId
	 *            商品分类ID
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 属性
	 */
	List<TAttribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}