
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TReview;

import java.util.List;

/**
 * Service - 评论
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TReviewService extends TBaseService<TReview, Long> {

	/**
	 * 查找评论详情
	 *
	 * @param id
	 * 			  评论ID
	 * @return
	 * 			  评论
	 */
	TReview findDetails(Long id);

	/**
	 * 查找评论
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 评论
	 */
	List<TReview> findList(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找评论
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 评论
	 */
	List<TReview> findList(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找评论分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	Page<TReview> findPage(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Pageable pageable);

	/**
	 * 查找评论数量
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @return 评论数量
	 */
	Long count(Long memberId, Long goodsId, TReview.Type type, Boolean isShow);

	/**
	 * 判断是否拥有评论权限
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @return 是否拥有评论权限
	 */
	boolean hasPermission(Long memberId, Long goodsId);

}