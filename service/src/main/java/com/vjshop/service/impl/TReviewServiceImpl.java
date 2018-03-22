package com.vjshop.service.impl;

import com.vjshop.*;
import com.vjshop.dao.*;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TReview;
import com.vjshop.generated.db.tables.records.TReviewRecord;
import com.vjshop.service.TReviewService;
import com.vjshop.util.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Service - 评论
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TReviewServiceImpl extends TBaseServiceImpl<TReviewRecord, TReview, Long>
		implements TReviewService {

	@Autowired
	private TReviewDao tReviewDao;
	@Autowired
	private TMemberDao tMemberDao;
	@Autowired
	private TGoodsDao tGoodsDao;
	@Autowired
	private TOrderDao orderDao;

	@Override
	public TReview findDetails(Long id) {
		return tReviewDao.findDetails(id);
	}

	@Transactional(readOnly = true)
	public List<TReview> findList(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return tReviewDao.findList(memberId, goodsId, type, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "review", condition = "#useCache")
	public List<TReview> findList(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		TMember member = tMemberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		TGoods goods = tGoodsDao.find(goodsId);
		if (goodsId != null && goods == null) {
			return Collections.emptyList();
		}
		return tReviewDao.findList(memberId, goodsId, type, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<TReview> findPage(Long memberId, Long goodsId, TReview.Type type, Boolean isShow, Pageable pageable) {
		return tReviewDao.findPage(memberId, goodsId, type, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Long memberId, Long goodsId, TReview.Type type, Boolean isShow) {
		return tReviewDao.count(memberId, goodsId, type, isShow);
	}

	@Transactional(readOnly = true)
	public boolean hasPermission(Long memberId, Long goodsId) {
		Assert.notNull(memberId);
		Assert.notNull(goodsId);

		Setting setting = SystemUtils.getSetting();
		if (Setting.ReviewAuthority.purchased.equals(setting.getReviewAuthority())) {
			long reviewCount = tReviewDao.count(memberId, goodsId, null, null);
			long orderCount = orderDao.count(null, com.vjshop.entity.TOrder.Status.completed, memberId, goodsId, null, null, null, null, null, null);
			return orderCount > reviewCount;
		}
		return true;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public TReview save(TReview review) {
		Assert.notNull(review);
		Assert.notNull(review.getGoods());

		Timestamp now = new Timestamp(System.currentTimeMillis());
		review.setModifyDate(now);
		if (review.getId() == null){
			review.setCreateDate(now);
			review.setVersion(0L);
		}
		review = super.save(review);
		updateGoods(review.getGoods());
		return review;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public TReview update(TReview review) {
		Assert.notNull(review);
		Assert.notNull(review.getGoods());

		Timestamp now = new Timestamp(System.currentTimeMillis());
		review.setModifyDate(now);
		review = tReviewDao.update(review, null);
		updateGoods(review.getGoods());
		return review;
	}

	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Long reviewId) {
		TReview review = tReviewDao.find(reviewId);
		if (review != null) {
			tReviewDao.deleteById(reviewId);
			updateGoods(review.getGoods());
		}
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
				Long totalScore = tReviewDao.calculateTotalScore(goods.getId());
				Long scoreCount = tReviewDao.calculateScoreCount(goods.getId());
				goods.setTotalScore(totalScore);
				goods.setScoreCount(scoreCount);
				goods.setGenerateMethod(TGoods.GenerateMethod.lazy.ordinal());
				Timestamp now = new Timestamp(System.currentTimeMillis());
				goods.setModifyDate(now);
				tGoodsDao.update(goods, null);
			}
		}
	}

}