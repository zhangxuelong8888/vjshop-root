
package com.vjshop.dao;

import com.vjshop.entity.TPaymentMethod;
import com.vjshop.generated.db.tables.records.TPaymentMethodRecord;

import java.util.List;

import com.vjshop.util.JooqUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TPaymentMethod.T_PAYMENT_METHOD;
import static com.vjshop.generated.db.tables.TShippingPaymentMethod.T_SHIPPING_PAYMENT_METHOD;
import static com.vjshop.generated.db.tables.TShippingMethod.T_SHIPPING_METHOD;

/**
 * Dao - 支付方式
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TPaymentMethodDao extends JooqBaseDao<TPaymentMethodRecord, com.vjshop.entity.TPaymentMethod, Long> {

    public TPaymentMethodDao() {
        super(T_PAYMENT_METHOD, com.vjshop.entity.TPaymentMethod.class);
    }

    @Autowired
    public TPaymentMethodDao(Configuration configuration) {
        super(T_PAYMENT_METHOD, com.vjshop.entity.TPaymentMethod.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TPaymentMethod object) {
        return object.getId();
    }

    /**
     * 查询符合条件的实体
     * @param id
     * @return 符合条件的对象集合
     */
    public List<com.vjshop.entity.TPaymentMethod> findListByShippingMethodId(Long id){
        SelectQuery selectQuery = getDslContext().select(T_PAYMENT_METHOD.fields()).from(T_PAYMENT_METHOD).getQuery();
        selectQuery.addJoin(T_SHIPPING_PAYMENT_METHOD, JoinType.JOIN, T_PAYMENT_METHOD.ID.eq(T_SHIPPING_PAYMENT_METHOD.PAYMENT_METHODS));
        selectQuery.addConditions(T_SHIPPING_PAYMENT_METHOD.SHIPPING_METHODS.eq(id));
        return findList(selectQuery, null, null, null, null);
    }

}
