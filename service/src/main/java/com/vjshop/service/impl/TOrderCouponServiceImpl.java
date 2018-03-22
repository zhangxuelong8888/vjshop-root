
package com.vjshop.service.impl;

import com.vjshop.dao.TOrderCouponDao;
import com.vjshop.entity.TOrderCoupon;
import com.vjshop.service.TOrderCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service - 订单项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tOrderCouponServiceImpl")
public class TOrderCouponServiceImpl implements TOrderCouponService {

    @Autowired
    private TOrderCouponDao tOrderCouponDao;

    public List<TOrderCoupon> findByOrders(Long... ids){
        return this.tOrderCouponDao.fetchByOrders(ids);
    }

    public void delete(TOrderCoupon tOrderCoupon){
        this.tOrderCouponDao.delete(tOrderCoupon);
    }

}