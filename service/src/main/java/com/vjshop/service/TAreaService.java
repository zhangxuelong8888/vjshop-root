
package com.vjshop.service;

import com.vjshop.entity.TArea;

import java.util.List;

/**
 * Service - 地区
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TAreaService extends TBaseService<TArea, Long> {

	/**
	 * 查找顶级地区
	 * 
	 * @return 顶级地区
	 */
	List<TArea> findRoots();

	/**
	 * 查找顶级地区
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	List<TArea> findRoots(Integer count);

	/**
	 * 查找上级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级地区
	 */
	List<TArea> findParents(TArea area, boolean recursive, Integer count);

	/**
	 * 查找下级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级地区
	 */
	List<TArea> findChildren(TArea area, boolean recursive, Integer count);

}