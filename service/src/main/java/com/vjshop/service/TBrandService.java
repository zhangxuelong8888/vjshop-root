package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TBrand;

import java.util.List;

/**
 * Service - 品牌
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TBrandService extends TBaseService<TBrand,Long>{

	/**
	 * 查询品牌详情
	 *
	 * @param brandId
	 * 			  品牌ID
	 * @return 品牌
	 */
	TBrand findDetails(Long brandId);

	/**
	 * 查找品牌
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
	 * @return 品牌
	 */
	List<TBrand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找品牌
	 *
	 * @return 品牌
	 */
	List<TBrand> findAll();

	Page<TBrand> findPage(Pageable pageable);

}