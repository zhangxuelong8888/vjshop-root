
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TPromotion;

import java.util.List;

/**
 * Service - 促销
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TPromotionService extends TBaseService<TPromotion, Long>{

	/**
	 * 验证价格运算表达式
	 * 
	 * @param priceExpression
	 *            价格运算表达式
	 * @return 验证结果
	 */
	boolean isValidPriceExpression(String priceExpression);

	/**
	 * 验证积分运算表达式
	 * 
	 * @param pointExpression
	 *            积分运算表达式
	 * @return 验证结果
	 */
	boolean isValidPointExpression(String pointExpression);

	/**
	 * 查找促销详情
	 *
	 * @param id
	 * 				促销ID
	 * @return 促销
	 */
	TPromotion findDetails(Long id);

	/**
	 * 查找促销
	 * 
	 * @param memberRankId
	 *            会员等级ID
	 * @param productCategoryId
	 *            商品分类ID
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 促销
	 */
	List<TPromotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找促销
	 * 
	 * @param memberRankId
	 *            会员等级ID
	 * @param productCategoryId
	 *            商品分类ID
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 促销
	 */
	List<TPromotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);
}