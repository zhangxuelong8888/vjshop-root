
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TPromotionCoupon;
import com.vjshop.generated.db.tables.records.TPromotionCouponRecord;
import org.jooq.Configuration;
import org.jooq.Record2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Entity - 促销-优惠券 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TPromotionCouponDao extends JooqBaseDao<TPromotionCouponRecord, com.vjshop.entity.TPromotionCoupon, Record2<Long, Long>> {

    public TPromotionCouponDao() {
        super(TPromotionCoupon.T_PROMOTION_COUPON, com.vjshop.entity.TPromotionCoupon.class);
    }

    @Autowired
    public TPromotionCouponDao(Configuration configuration) {
        super(TPromotionCoupon.T_PROMOTION_COUPON, com.vjshop.entity.TPromotionCoupon.class, configuration);
    }

    @Override
    protected Record2<Long, Long> getId(com.vjshop.entity.TPromotionCoupon object) {
        return compositeKeyRecord(object.getPromotions(), object.getCoupons());
    }

    /**
     * 获取关联信息
     *
     * @param promotionId
     *            促销ID
     * @return 关联
     */
    public List<com.vjshop.entity.TPromotionCoupon> find(Long promotionId) {
        return fetch(TPromotionCoupon.T_PROMOTION_COUPON.PROMOTIONS, promotionId);
    }

    /**
     * 删除关联信息
     *
     * @param promotionId
     *            促销ID
     * @return 影响条数
     */
    public int delete(Long promotionId){
        return getDslContext().deleteFrom(TPromotionCoupon.T_PROMOTION_COUPON)
                .where(TPromotionCoupon.T_PROMOTION_COUPON.PROMOTIONS.eq(promotionId))
                .execute();
    }

    /**
     * 删除关联信息
     *
     * @param promotionIds
     *            促销ID
     * @return 影响条数
     */
    public int delete(Long... promotionIds){
        return getDslContext().deleteFrom(TPromotionCoupon.T_PROMOTION_COUPON)
                .where(TPromotionCoupon.T_PROMOTION_COUPON.PROMOTIONS.in(promotionIds))
                .execute();
    }
}
