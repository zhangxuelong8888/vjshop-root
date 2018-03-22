
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 商品-优惠券 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TOrderCoupon implements Serializable {

    private static final long serialVersionUID = -1794852278;

    private Long orders;
    private Long coupons;

    public TOrderCoupon() {}

    public TOrderCoupon(TOrderCoupon value) {
        this.orders = value.orders;
        this.coupons = value.coupons;
    }

    public TOrderCoupon(
        Long orders,
        Long coupons
    ) {
        this.orders = orders;
        this.coupons = coupons;
    }

    public Long getOrders() {
        return this.orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    public Long getCoupons() {
        return this.coupons;
    }

    public void setCoupons(Long coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOrderCoupon (");

        sb.append(orders);
        sb.append(", ").append(coupons);

        sb.append(")");
        return sb.toString();
    }
}
