
package com.vjshop.service;

import com.vjshop.entity.TOrderCoupon;

import java.util.List;

/**
 * Service - 购物车项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TOrderCouponService  {

    List<TOrderCoupon> findByOrders(Long... ids);

    void delete(TOrderCoupon tOrderCoupon);

}