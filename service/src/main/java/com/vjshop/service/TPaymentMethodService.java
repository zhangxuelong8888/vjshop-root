package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TPaymentMethod;

import java.util.List;

/**
 * TPaymentMethod服务对象接口
 */
public interface TPaymentMethodService {

	/**
	 * 新增实体信息
	 * @param tPaymentMethod 实体对象
	 */
	void insert(TPaymentMethod tPaymentMethod);

	/**
	 * 更新实体信息
	 * @param tPaymentMethod 实体对象
	 * @param ignoreProperties 忽略更新的属性
	 */
	void update(TPaymentMethod tPaymentMethod, String... ignoreProperties);

	/**
	 * 更新实体信息
	 * @param tPaymentMethod 实体对象
	 */
	void updateSelective(TPaymentMethod tPaymentMethod);


	/**
	 * 更新实体信息
	 * @param tPaymentMethod 实体对象
	 */
	void save(TPaymentMethod tPaymentMethod);

	/**
	 * 删除实体信息
	 * @param ids id数组
	 */
	void delete(Long... ids);

	/**
	 * 查询符合条件的记录数
	 * @return
	 */
	Long count();

	/**
	 * 查询符合条件的实体
	 * @param pageable 分页对象
	 * @return 符合条件的分页对象
	 */
	Page<TPaymentMethod> findPage(Pageable pageable);

	/**
	 * 根据id查询实体
	 * @param id id
	 * @return 实体
	 */
	TPaymentMethod find(Long id);

	/**
	 * 查询符合条件的实体
	 * @param ids id数组
	 * @return 符合条件的分页对象
	 */
	List<TPaymentMethod> findListByIds(Long[] ids);

	/**
	 * 查询符合条件的实体
	 * @param id
	 * @return 符合条件的对象集合
	 */
	List<TPaymentMethod> findListByShippingMethodId(Long id);

	/**
	 * 查询所有记录
	 * @return 符合条件的集合对象
	 */
	List<TPaymentMethod> findAll();
}
