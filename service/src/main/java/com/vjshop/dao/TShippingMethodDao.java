
package com.vjshop.dao;

import com.vjshop.generated.db.tables.records.TShippingMethodRecord;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TShippingMethod.T_SHIPPING_METHOD;
import static com.vjshop.generated.db.tables.TShippingPaymentMethod.T_SHIPPING_PAYMENT_METHOD;

/**
 * Dao - 配送方式
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TShippingMethodDao extends JooqBaseDao<TShippingMethodRecord, com.vjshop.entity.TShippingMethod, Long> {

    /**
     * 根据支付方式查询对应配送方式
     * @param payemntMethodId
     * @return
     */
    public List<com.vjshop.entity.TShippingMethod> findByPaymentMethodId(Long payemntMethodId) {
        SelectQuery<Record> query = getDslContext().select(T_SHIPPING_METHOD.fields()).from(T_SHIPPING_METHOD)
                .join(T_SHIPPING_PAYMENT_METHOD).on(T_SHIPPING_METHOD.ID.eq(T_SHIPPING_PAYMENT_METHOD.SHIPPING_METHODS))
                .where(T_SHIPPING_PAYMENT_METHOD.PAYMENT_METHODS.eq(payemntMethodId)).getQuery();
        return resultSet2List(query.fetchResultSet());
    }

    public TShippingMethodDao() {
        super(T_SHIPPING_METHOD, com.vjshop.entity.TShippingMethod.class);
    }

    @Autowired
    public TShippingMethodDao(Configuration configuration) {
        super(T_SHIPPING_METHOD, com.vjshop.entity.TShippingMethod.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TShippingMethod object) {
        return object.getId();
    }

}
