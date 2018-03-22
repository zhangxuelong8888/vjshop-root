package com.vjshop.dao;

import com.vjshop.generated.db.tables.TOrderCoupon;
import com.vjshop.util.SpringUtils;
import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Dao - 订单项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TOrderCouponDao {
    @Autowired
    private DSLContext dslContext;

    private final JdbcMapper mapper = JdbcMapperFactory.newInstance().addKeys("id").newMapper(com.vjshop.entity.TOrderCoupon.class);


    public List<com.vjshop.entity.TOrderCoupon> fetchByOrders(Long... values){
        ResultSet res =  this.dslContext.selectFrom(TOrderCoupon.T_ORDER_COUPON)
                .where(TOrderCoupon.T_ORDER_COUPON.ORDERS.in(values))
                .fetchResultSet();
        return resultSet2List(res);
    }

    public List<com.vjshop.entity.TOrderCoupon> fetchByCoupons(Long... values) {
        ResultSet res =  this.dslContext.selectFrom(TOrderCoupon.T_ORDER_COUPON)
                .where(TOrderCoupon.T_ORDER_COUPON.COUPONS.in(values))
                .fetchResultSet();
        return resultSet2List(res);
    }

    public void delete(com.vjshop.entity.TOrderCoupon tOrderCoupon){
        this.dslContext.deleteFrom(TOrderCoupon.T_ORDER_COUPON).where(TOrderCoupon.T_ORDER_COUPON.COUPONS.eq(tOrderCoupon.getCoupons()).and(TOrderCoupon.T_ORDER_COUPON.ORDERS.eq(tOrderCoupon.getOrders()))).execute();
    }

    private List<com.vjshop.entity.TOrderCoupon> resultSet2List(ResultSet res){
        try{
            Stream<com.vjshop.entity.TOrderCoupon> stream = mapper.stream(res);
            List<com.vjshop.entity.TOrderCoupon> result = stream.collect(Collectors.toList());
            return result;
        }catch (SQLException e){
            throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
        }
    }
}
