
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.dao.*;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TPromotionRecord;
import com.vjshop.service.TPromotionService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Service - 促销
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TPromotionServiceImpl extends TBaseServiceImpl<TPromotionRecord, TPromotion, Long>
        implements TPromotionService {

    /**
     * 价格表达式变量
     */
    private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();

    /**
     * 积分表达式变量
     */
    private static final List<Map<String, Object>> POINT_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();

    @Autowired
    private TPromotionDao tPromotionDao;
    @Autowired
    private TMemberRankDao tMemberRankDao;
    @Autowired
    private TProductCategoryDao tProductCategoryDao;
    @Autowired
    private TPromotionMemberRankDao tPromotionMemberRankDao;
    @Autowired
    private TPromotionCouponDao tPromotionCouponDao;
    @Autowired
    private TPromotionGiftDao tPromotionGiftDao;

    static {
        Map<String, Object> variable0 = new HashMap<String, Object>();
        Map<String, Object> variable1 = new HashMap<String, Object>();
        Map<String, Object> variable2 = new HashMap<String, Object>();
        Map<String, Object> variable3 = new HashMap<String, Object>();
        variable0.put("quantity", 99);
        variable0.put("price", new BigDecimal("99"));
        variable1.put("quantity", 99);
        variable1.put("price", new BigDecimal("9.9"));
        variable2.put("quantity", 99);
        variable2.put("price", new BigDecimal("0.99"));
        variable3.put("quantity", 99);
        variable3.put("point", 99L);
        PRICE_EXPRESSION_VARIABLES.add(variable0);
        PRICE_EXPRESSION_VARIABLES.add(variable1);
        PRICE_EXPRESSION_VARIABLES.add(variable2);
        POINT_EXPRESSION_VARIABLES.add(variable3);
    }

    @Transactional(readOnly = true)
    public boolean isValidPriceExpression(String priceExpression) {
        Assert.hasText(priceExpression);

        for (Map<String, Object> variable : PRICE_EXPRESSION_VARIABLES) {
            try {
                Binding binding = new Binding();
                for (Map.Entry<String, Object> entry : variable.entrySet()) {
                    binding.setVariable(entry.getKey(), entry.getValue());
                }
                GroovyShell groovyShell = new GroovyShell(binding);
                Object result = groovyShell.evaluate(priceExpression);
                new BigDecimal(result.toString());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = true)
    public boolean isValidPointExpression(String pointExpression) {
        Assert.hasText(pointExpression);

        for (Map<String, Object> variable : POINT_EXPRESSION_VARIABLES) {
            try {
                Binding binding = new Binding();
                for (Map.Entry<String, Object> entry : variable.entrySet()) {
                    binding.setVariable(entry.getKey(), entry.getValue());
                }
                GroovyShell groovyShell = new GroovyShell(binding);
                Object result = groovyShell.evaluate(pointExpression);
                Long.valueOf(result.toString());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TPromotion findDetails(Long id) {
        return tPromotionDao.findDetails(id);
    }

    @Transactional(readOnly = true)
    public List<TPromotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
        return tPromotionDao.findList(memberRankId, productCategoryId, hasBegun, hasEnded, count, filters, orders);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "promotion", condition = "#useCache")
    public List<TPromotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
        if (memberRankId != null && tMemberRankDao.existsById(memberRankId)) {
            return Collections.emptyList();
        }
        if (productCategoryId != null && tProductCategoryDao.existsById(productCategoryId)) {
            return Collections.emptyList();
        }
        return tPromotionDao.findList(memberRankId, productCategoryId, hasBegun, hasEnded, count, filters, orders);
    }

    @Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public TPromotion save(TPromotion promotion) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        promotion.setModifyDate(now);
        if (promotion.getId() == null){
            promotion.setCreateDate(now);
            promotion.setVersion(0L);
        }
        Long promotionId = super.save(promotion).getId();
        promotion.setId(promotionId);
        updateOthers(promotion);
        return promotion;
	}

    @Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public TPromotion update(TPromotion promotion) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        promotion.setModifyDate(now);
        tPromotionDao.update(promotion, "goods", "productCategories");
        updateOthers(promotion);
        return promotion;
	}

	@Override
    @Transactional
	public void delete(Long promotionId){
        tPromotionMemberRankDao.delete(promotionId);
        tPromotionCouponDao.delete(promotionId);
        tPromotionGiftDao.delete(promotionId);
        super.delete(promotionId);
    }

    @Override
    @Transactional
    public void delete(Long[] promotionIds){
        tPromotionMemberRankDao.delete(promotionIds);
        tPromotionCouponDao.delete(promotionIds);
        tPromotionGiftDao.delete(promotionIds);
        super.delete(promotionIds);
    }

    /**
     * 促销信息修改时，其他需要修改的内容
     *
     * @param promotion
     *            促销
     */
	private void updateOthers(TPromotion promotion){
        //促销-会员等级
        tPromotionMemberRankDao.delete(promotion.getId());
        Set<TMemberRank> memberRankSet = promotion.getMemberRanks();
        if (!CollectionUtils.isEmpty(memberRankSet)){
            List<TPromotionMemberRank> pmrList = new ArrayList<TPromotionMemberRank>(memberRankSet.size());
            for (TMemberRank memberRank : memberRankSet){
                pmrList.add(new TPromotionMemberRank(promotion.getId(), memberRank.getId()));
            }
            tPromotionMemberRankDao.insert(pmrList);
        }
        //促销-优惠券
        tPromotionCouponDao.delete(promotion.getId());
        Set<TCoupon> couponSet = promotion.getCoupons();
        if (!CollectionUtils.isEmpty(couponSet)){
            List<TPromotionCoupon> pcList = new ArrayList<TPromotionCoupon>(couponSet.size());
            for (TCoupon coupon : couponSet){
                pcList.add(new TPromotionCoupon(promotion.getId(), coupon.getId()));
            }
            tPromotionCouponDao.insert(pcList);
        }
        //促销-赠品
        tPromotionGiftDao.delete(promotion.getId());
        Set<TProduct> giftSet = promotion.getGifts();
        if (!CollectionUtils.isEmpty(giftSet)){
            List<TPromotionGift> giftList = new ArrayList<TPromotionGift>(giftSet.size());
            for (TProduct gift : giftSet){
                giftList.add(new TPromotionGift(promotion.getId(), gift.getId()));
            }
            tPromotionGiftDao.insert(giftList);
        }
    }

}