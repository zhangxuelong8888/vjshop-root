
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TConsultation;

import java.util.List;

/**
 * Service - 咨询
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TConsultationService extends TBaseService<TConsultation, Long> {

	/**
	 * 查找咨询详情
	 *
	 * @param id
	 * 			  咨询ID
	 * @return 咨询
	 */
	TConsultation findDetails(Long id);

	/**
	 * 查找咨询
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 咨询，不包含咨询回复
	 */
	List<TConsultation> findList(Long memberId, Long goodsId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找咨询
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 咨询，不包含咨询回复
	 */
	List<TConsultation> findList(Long memberId, Long goodsId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找咨询分页
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 咨询分页，不包含咨询回复
	 */
	Page<TConsultation> findPage(Long memberId, Long goodsId, Boolean isShow, Pageable pageable);

	/**
	 * 查找咨询数量
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
	 * @param isShow
	 *            是否显示
	 * @return 咨询数量，不包含咨询回复
	 */
	Long count(Long memberId, Long goodsId, Boolean isShow);

	/**
	 * 咨询回复
	 * 
	 * @param consultation
	 *            咨询
	 * @param replyConsultation
	 *            回复咨询
	 */
	void reply(TConsultation consultation, TConsultation replyConsultation);

}