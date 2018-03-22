
package com.vjshop.service;

import com.vjshop.entity.TArticleCategory;

import java.util.List;

/**
 * Service - 文章分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TArticleCategoryService extends TBaseService<TArticleCategory, Long> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @return 顶级文章分类
	 */
	List<TArticleCategory> findRoots();

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<TArticleCategory> findRoots(Integer count);

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级文章分类
	 */
	List<TArticleCategory> findRoots(Integer count, boolean useCache);

	/**
	 * 查找上级文章分类
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<TArticleCategory> findParents(TArticleCategory articleCategory, boolean recursive, Integer count);

	/**
	 * 查找上级文章分类
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级文章分类
	 */
	List<TArticleCategory> findParents(Long articleCategoryId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找文章分类树
	 * 
	 * @return 文章分类树
	 */
	List<TArticleCategory> findTree();

	/**
	 * 查找下级文章分类
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<TArticleCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count);

}