
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.*;
import com.vjshop.entity.TConsultation;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.generated.db.tables.records.TConsultationRecord;
import com.vjshop.service.TConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Service - 咨询
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TConsultationServiceImpl extends TBaseServiceImpl<TConsultationRecord, TConsultation, Long>
		implements TConsultationService {

	@Autowired
	private TConsultationDao tConsultationDao;
	@Autowired
	private TMemberDao tMemberDao;
	@Autowired
	private TGoodsDao tGoodsDao;

	@Override
	public TConsultation findDetails(Long id) {
		return tConsultationDao.findDetails(id);
	}

	@Transactional(readOnly = true)
	public List<TConsultation> findList(Long memberId, Long goodsId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return tConsultationDao.findList(memberId, goodsId, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "consultation", condition = "#useCache")
	public List<TConsultation> findList(Long memberId, Long goodsId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		TMember member = tMemberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		TGoods goods = tGoodsDao.find(goodsId);
		if (goodsId != null && goods == null) {
			return Collections.emptyList();
		}
		return tConsultationDao.findList(memberId, goodsId, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<TConsultation> findPage(Long memberId, Long goodsId, Boolean isShow, Pageable pageable) {
		return tConsultationDao.findPage(memberId, goodsId, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Long memberId, Long goodsId, Boolean isShow) {
		return tConsultationDao.count(memberId, goodsId, isShow);
	}

	@CacheEvict(value = "consultation", allEntries = true)
	public void reply(TConsultation consultation, TConsultation replyConsultation) {
		if (consultation == null || replyConsultation == null) {
			return;
		}
		TConsultation updateCon = new TConsultation();
		updateCon.setId(consultation.getId());
		updateCon.setIsShow(true);
		this.updateSelective(updateCon);

		replyConsultation.setIsShow(true);
		replyConsultation.setGoods(consultation.getGoods());
		replyConsultation.setForConsultation(consultation.getId());
		save(replyConsultation);

		updateGoods(consultation.getGoods());
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public TConsultation save(TConsultation consultation) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		consultation.setModifyDate(now);
		if (consultation.getId() == null){
			consultation.setCreateDate(now);
			consultation.setVersion(0L);
		}
		super.save(consultation);
		updateGoods(consultation.getGoods());
		return consultation;
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public TConsultation update(TConsultation consultation) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		consultation.setModifyDate(now);
		consultation = tConsultationDao.update(consultation, null);
		updateGoods(consultation.getGoods());
		return consultation;
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public void delete(Long consultationId) {
		TConsultation consultation = tConsultationDao.find(consultationId);
		if (consultation == null){
			return;
		}
		tConsultationDao.deleteById(consultationId);
		updateGoods(consultation.getGoods());
	}

	/**
	 * 更新商品相关
	 * @param goodsId
	 * 			商品ID
	 */
	private void updateGoods(Long goodsId){
		if (goodsId != null){
			TGoods goods = tGoodsDao.find(goodsId);
			if (goods != null) {
				goods.setGenerateMethod(TGoods.GenerateMethod.lazy.ordinal());
			}
			Timestamp now = new Timestamp(System.currentTimeMillis());
			goods.setModifyDate(now);
			tGoodsDao.update(goods, null);
		}
	}

}