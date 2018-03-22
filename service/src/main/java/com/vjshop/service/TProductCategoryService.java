
package com.vjshop.service;

import com.vjshop.entity.TProductCategory;

import java.util.List;

/**
 * Service - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TProductCategoryService extends TBaseService<TProductCategory,Long>{

	/**
	 * 查找商品分类
	 *
	 * @param id
	 * 			商品分类ID
	 * @return 商品分类
	 */
	TProductCategory find(Long id);

	/**
	 * 查找顶级商品分类
	 * 
	 * @return 顶级商品分类
	 */
	List<TProductCategory> findRoots();

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级商品分类
	 */
	List<TProductCategory> findRoots(Integer count);

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级商品分类
	 */
	List<TProductCategory> findRoots(Integer count, boolean useCache);

	/**
	 * 查找上级商品分类
	 * 
//	 * @param productCategoryId
	 *            商品分类
//	 * @param recursive
	 *            是否递归
//	 * @param count
	 *            数量
	 * @return 上级商品分类
	 */
	List<TProductCategory> findParents(TProductCategory productCategory, boolean recursive, Integer count);

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级商品分类
	 */
	List<TProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找商品分类树
	 * 
	 * @return 商品分类树
	 */
	List<TProductCategory> findTree();

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级商品分类
	 */
	List<TProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count);

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级商品分类
	 */
	List<TProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache);


	TProductCategory findById(Long id);

	TProductCategory findDetailById(Long id);

	TProductCategory save(TProductCategory tProductCategory);

	TProductCategory update(TProductCategory tProductCategory,String... ignoreProperties);

	void deleteCascade(Long... ids);
}