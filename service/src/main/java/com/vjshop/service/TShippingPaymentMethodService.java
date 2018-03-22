package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TShippingPaymentMethod;

import java.util.List;
import java.util.Set;

/**
 * TShippingPaymentMethod服务对象接口
 */
public interface TShippingPaymentMethodService {

	/**
	 * 新增实体信息
	 * @param tShippingPaymentMethod 实体对象
	 */
	void insert(TShippingPaymentMethod tShippingPaymentMethod);

	/**
	 * 更新实体信息
	 * @param tShippingPaymentMethod 实体对象
	 * @param ignoreProperties 忽略更新的属性
	 */
	void update(TShippingPaymentMethod tShippingPaymentMethod, String... ignoreProperties);

	/**
	 * 更新实体信息
	 * @param tShippingPaymentMethod 实体对象
	 */
	void updateSelective(TShippingPaymentMethod tShippingPaymentMethod);


	/**
	 * 更新实体信息
	 * @param tShippingPaymentMethods 实体对象数组
	 */
	void save(List<TShippingPaymentMethod> tShippingPaymentMethods);

	/**
	 * 删除实体信息
	 * @param tShippingPaymentMethods 实体对象数组
	 */
	void delete(List<TShippingPaymentMethod> tShippingPaymentMethods);


	/**
	 * 查询符合条件的实体
	 * @param pageable 分页对象
	 * @return 符合条件的分页对象
	 */
	Page<TShippingPaymentMethod> findPage(Pageable pageable);

	/**
	 * 根据shippingMethodsId查询实体
	 * @return 实体集合
	 */
    List<TShippingPaymentMethod> findByShippingMethodId(Long id);
}
