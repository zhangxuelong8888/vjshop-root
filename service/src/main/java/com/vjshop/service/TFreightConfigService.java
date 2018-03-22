package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TArea;
import com.vjshop.entity.TFreightConfig;
import com.vjshop.entity.TShippingMethod;

/**
 * Service - 运费配置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TFreightConfigService extends TBaseService<TFreightConfig, Long> {

	/**
	 * 判断运费配置是否存在
	 * 
	 * @param shippingMethodId
	 *            配送方式
	 * @param areaId
	 *            地区
	 * @return 运费配置是否存在
	 */
	boolean exists(Long shippingMethodId, Long areaId);

	/**
	 * 判断运费配置是否唯一
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param previousArea
	 *            修改前地区
	 * @param currentArea
	 *            当前地区
	 * @return 运费配置是否唯一
	 */
	boolean unique(TShippingMethod shippingMethod, TArea previousArea, TArea currentArea);

	/**
	 * 查找运费配置分页
	 * 
	 * @param shippingMethodId
	 *            配送方式
	 * @param pageable
	 *            分页信息
	 * @return 运费配置分页
	 */
	Page<TFreightConfig> findPage(Long shippingMethodId, Pageable pageable);

}