
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TArticleCategory;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 文章
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TArticleService extends TBaseService<TArticle, Long> {

	/**
	 * 查找文章详细信息
	 *
	 * @return 文章详细信息
	 */
	TArticle findDetails(Long articleId);

	/**
	 * 查找文章
	 *
	 * @param join
	 *            是否关联信息
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param tagId
	 *            标签Id
	 * @param isPublication
	 *            是否发布
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 文章
	 */
	List<TArticle> findList(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找文章
	 *
	 * @param join
	 *            是否关联信息
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param tagId
	 *            标签ID
	 * @param isPublication
	 *            是否发布
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 文章
	 */
	List<TArticle> findList(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找文章
	 *
	 * @param join
	 *            是否关联信息
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param isPublication
	 *            是否发布
	 * @param generateMethod
	 *            静态生成方式
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 文章
	 */
	List<TArticle> findList(boolean join, Long articleCategoryId, Boolean isPublication, TArticle.GenerateMethod generateMethod,
							Timestamp beginDate, Timestamp endDate, Integer first, Integer count);

	/**
	 * 查找文章分页
	 *
	 * @param join
	 *            是否关联信息
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param tagId
	 *            标签ID
	 * @param isPublication
	 *            是否发布
	 * @param pageable
	 *            分页信息
	 * @return 文章分页
	 */
	Page<TArticle> findPage(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Pageable pageable);

	/**
	 * 查看点击数
	 * 
	 * @param id
	 *            ID
	 * @return 点击数
	 */
	long viewHits(Long id);

	/**
	 * 根据文章类目查找文章
	 * @param articleCategoryId
	 * @return
	 */
	List<TArticle> findByArticleCategory(Long articleCategoryId);

}