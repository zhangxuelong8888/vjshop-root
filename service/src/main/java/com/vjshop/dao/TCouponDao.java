
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TCoupon;
import com.vjshop.generated.db.tables.records.TCouponRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;


/**
 * Dao - 优惠券
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TCouponDao extends JooqBaseDao<TCouponRecord, com.vjshop.entity.TCoupon, Long> {

    public TCouponDao() {
        super(TCoupon.T_COUPON, com.vjshop.entity.TCoupon.class);
    }

    @Autowired
    public TCouponDao(Configuration configuration) {
        super(TCoupon.T_COUPON, com.vjshop.entity.TCoupon.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TCoupon object) {
        return object.getId();
    }

    /**
     * 查找优惠券分页
     *
     * @param isEnabled
     *            是否启用
     * @param isExchange
     *            是否允许积分兑换
     * @param hasExpired
     *            是否已过期
     * @param pageable
     *            分页信息
     * @return 优惠券分页
     */
    public Page<com.vjshop.entity.TCoupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable){
        SelectQuery query = getQuery();
        if (isEnabled != null){
            query.addConditions(TCoupon.T_COUPON.IS_ENABLED.eq(isEnabled));
        }
        if (isExchange != null){
            query.addConditions(TCoupon.T_COUPON.IS_EXCHANGE.eq(isExchange));
        }
        if (hasExpired != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (hasExpired){
                query.addConditions(TCoupon.T_COUPON.END_DATE.isNotNull().and(TCoupon.T_COUPON.END_DATE.le(now)));
            } else {
                query.addConditions(TCoupon.T_COUPON.END_DATE.isNull().or(TCoupon.T_COUPON.END_DATE.gt(now)));
            }
        }
        return findPage(query, pageable);
    }


}
