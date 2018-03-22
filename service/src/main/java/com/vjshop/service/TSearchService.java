
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;

import java.math.BigDecimal;

/**
 * Service - 搜索
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TSearchService {

	/**
	 * 创建索引
	 */
	void index();

	/**
	 * 创建索引
	 * 
	 * @param type
	 *            索引类型
	 */
	void index(Class<?> type);

	/**
	 * 创建索引
	 * @param type 索引类型
	 * @param isCreate 新建/更新索引
	 */
	void index(Class<?> type, Boolean isCreate);

	/**
	 * 创建索引
	 * 
	 * @param article
	 *            文章
	 * @param isCreate 新建/更新索引
	 */
	void index(TArticle article, Boolean isCreate);

	/**
	 * 创建索引
	 * 
	 * @param goods
	 *            货品
	 * @param isCreate 新建/更新索引
	 */
	void index(TGoods goods, Boolean isCreate);

	/**
	 * 删除索引
	 */
	void purge();

	/**
	 * 删除索引
	 * 
	 * @param type
	 *            索引类型
	 */
	void purge(Class<?> type);

	/**
	 * 删除索引
	 *
	 * @param type
	 *            索引类型
	 */
	void purge(Class<?> type, String id);

	/**
	 * 删除索引
	 * 
	 * @param article
	 *            文章
	 */
	void purge(TArticle article);

	/**
	 * 删除索引
	 * 
	 * @param goods
	 *            货品
	 */
	void purge(TGoods goods);

	/**
	 * 搜索文章分页
	 * 
	 * @param keyword
	 *            关键词
	 * @param pageable
	 *            分页信息
	 * @return 文章分页
	 */
	Page<TArticle> search(String keyword, Pageable pageable);

	/**
	 * 搜索货品分页
	 * 
	 * @param keyword
	 *            关键词
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param orderType
	 *            排序类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	Page<TGoods> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, TGoods.OrderType orderType, Pageable pageable);

}