package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TArea;
import com.vjshop.entity.TReceiver;
import com.vjshop.entity.TShippingMethod;

import java.math.BigDecimal;
import java.util.List;

/**
 * TShippingMethod服务对象接口
 */
public interface TShippingMethodService extends TBaseService<TShippingMethod,Long>{

	/**
	 * 新增实体信息
	 * @param tShippingMethod 实体对象
	 */
	TShippingMethod insertAndFetch(TShippingMethod tShippingMethod);

	/**
	 * 更新实体信息
	 * @param tShippingMethod 实体对象
	 */
	TShippingMethod updateSelective(TShippingMethod tShippingMethod);

	/**
	 * 更新实体信息
	 * @param tShippingMethod 实体对象
	 */
	TShippingMethod save(TShippingMethod tShippingMethod);

	/**
	 * 删除实体信息
	 * @param ids id数组
	 */
	void delete(Long... ids);

	/**
	 * 查询符合条件的实体
	 * @param pageable 分页对象
	 * @return 符合条件的分页对象
	 */
	Page<TShippingMethod> findPage(Pageable pageable);

	/**
	 * 根据id查询实体
	 * @param id id
	 * @return 实体
	 */
	TShippingMethod findDetails(Long id);

	/**
	 * 查询实体记录数
	 * @return
	 */
	long count();

	/**
	 * 计算运费
	 *
	 * @param shippingMethod
	 *            配送方式
	 * @param area
	 *            地区
	 * @param weight
	 *            重量
	 * @return 运费
	 */
	BigDecimal calculateFreight(TShippingMethod shippingMethod, TArea area, Integer weight);

	/**
	 * 计算运费
	 *
	 * @param shippingMethod
	 *            配送方式
	 * @param receiver
	 *            收货地址
	 * @param weight
	 *            重量
	 * @return 运费
	 */
	BigDecimal calculateFreight(TShippingMethod shippingMethod, TReceiver receiver, Integer weight);

	/**
	 * 根据支付方式查询对应配送方式
	 * @param payemntMethodId
	 * @return
	 */
	List<TShippingMethod> findByPaymentMethodId(Long payemntMethodId);

	/**
	 * 查询所有记录
	 * @return 符合条件的集合对象
	 */
	List<TShippingMethod> findAll();
}
