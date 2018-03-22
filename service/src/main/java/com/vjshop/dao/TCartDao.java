
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TCart;
import com.vjshop.generated.db.tables.records.TCartRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Dao - 购物车
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TCartDao extends JooqBaseDao<TCartRecord, com.vjshop.entity.TCart, Long> {

    public TCartDao() {
        super(TCart.T_CART, com.vjshop.entity.TCart.class);
    }

    @Autowired
    public TCartDao(Configuration configuration) {
        super(TCart.T_CART, com.vjshop.entity.TCart.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TCart object) {
        return object.getId();
    }

    /**
     * 获取购物车
     *
     * @param key
     *            密钥
     * @return 购物车
     */
    public com.vjshop.entity.TCart findByKey(String key) {
        return fetchOne(TCart.T_CART.CART_KEY, key);
    }

    /**
     * 获取购物车
     *
     * @param memberId
     *            会员ID
     * @return 购物车
     */
    public com.vjshop.entity.TCart findByMemberId(Long memberId) {

//        List l=  fetch(TCart.T_CART.MEMBER,memberId);
        return fetchOne(TCart.T_CART.MEMBER, memberId);
//        return (com.vjshop.entity.TCart)l.get(l.size()-1);
    }

    /**
     * 获取购物车
     *
     * @param hasExpired
     *            是否过期
     * @param count
     *            数量
     * @return 购物车
     */
    public List<com.vjshop.entity.TCart> findList(Boolean hasExpired, Integer count) {
        SelectQuery query = getQuery();
        if (hasExpired != null) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (hasExpired) {
                query.addConditions(TCart.T_CART.EXPIRE.isNotNull().and(TCart.T_CART.EXPIRE.le(now)));
            } else {
                query.addConditions(TCart.T_CART.EXPIRE.isNull().or(TCart.T_CART.EXPIRE.gt(now)));
            }
        }
        return findList(query, null, count, null, null);
    }
}
