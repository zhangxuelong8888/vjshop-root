
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 促销-优惠券 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPromotionCoupon implements Serializable {

    private static final long serialVersionUID = -2007240419;

    private Long promotions;
    private Long coupons;

    public TPromotionCoupon() {}

    public TPromotionCoupon(TPromotionCoupon value) {
        this.promotions = value.promotions;
        this.coupons = value.coupons;
    }

    public TPromotionCoupon(
        Long promotions,
        Long coupons
    ) {
        this.promotions = promotions;
        this.coupons = coupons;
    }

    public Long getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Long promotions) {
        this.promotions = promotions;
    }

    public Long getCoupons() {
        return this.coupons;
    }

    public void setCoupons(Long coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPromotionCoupon (");

        sb.append(promotions);
        sb.append(", ").append(coupons);

        sb.append(")");
        return sb.toString();
    }
}
