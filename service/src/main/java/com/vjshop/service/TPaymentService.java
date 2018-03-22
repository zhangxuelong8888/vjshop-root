package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TPayment;

import java.util.List;

/**
 * TPayment服务对象接口
 */
public interface TPaymentService extends  TBaseService<TPayment,Long>{

	/**
	 * 新增实体信息
	 * @param tPayment 实体对象
	 */
	void insert(TPayment tPayment);

	/**
	 * 更新实体信息
	 * @param tPayment 实体对象
	 * @param ignoreProperties 忽略更新的属性
	 */
	TPayment update(TPayment tPayment, String... ignoreProperties);

	/**
	 * 更新实体信息
	 * @param tPayment 实体对象
	 */
	TPayment updateSelective(TPayment tPayment);


	/**
	 * 更新实体信息
	 * @param tPayment 实体对象
	 */
	TPayment save(TPayment tPayment);

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
	Page<TPayment> findPage(Pageable pageable);

	/**
	 * 根据id查询实体
	 * @param id id
	 * @return 实体
	 */
	TPayment findDetailById(Long id);

	List<TPayment> findByOrderIds(Long... ids);

}
