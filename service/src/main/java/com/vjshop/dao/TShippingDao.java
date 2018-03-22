package com.vjshop.dao;


import com.vjshop.generated.db.tables.TShipping;
import com.vjshop.generated.db.tables.records.TShippingRecord;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * 发货单DAO
 */
@Repository
public class TShippingDao extends JooqBaseDao<TShippingRecord, com.vjshop.entity.TShipping, Long> {

    public com.vjshop.entity.TShipping findBySn(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return null;
        }
        return fetchOneBySn(sn);
    }

    /**
     * Create a new TShippingDao without any configuration
     */
    public TShippingDao() {
        super(TShipping.T_SHIPPING, com.vjshop.entity.TShipping.class);
    }

    /**
     * Create a new TShippingDao with an attached configuration
     */
    @Autowired
    public TShippingDao(Configuration configuration) {
        super(TShipping.T_SHIPPING, com.vjshop.entity.TShipping.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TShipping object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchById(Long... values) {
        return fetch(TShipping.T_SHIPPING.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.vjshop.entity.TShipping fetchOneById(Long value) {
        return fetchOne(TShipping.T_SHIPPING.ID, value);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByCreateDate(Timestamp... values) {
        return fetch(TShipping.T_SHIPPING.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>modify_date IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByModifyDate(Timestamp... values) {
        return fetch(TShipping.T_SHIPPING.MODIFY_DATE, values);
    }

    /**
     * Fetch records that have <code>version IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByVersion(Long... values) {
        return fetch(TShipping.T_SHIPPING.VERSION, values);
    }

    /**
     * Fetch records that have <code>address IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByAddress(String... values) {
        return fetch(TShipping.T_SHIPPING.ADDRESS, values);
    }

    /**
     * Fetch records that have <code>area IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByArea(String... values) {
        return fetch(TShipping.T_SHIPPING.AREA, values);
    }

    /**
     * Fetch records that have <code>consignee IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByConsignee(String... values) {
        return fetch(TShipping.T_SHIPPING.CONSIGNEE, values);
    }

    /**
     * Fetch records that have <code>delivery_corp IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByDeliveryCorp(String... values) {
        return fetch(TShipping.T_SHIPPING.DELIVERY_CORP, values);
    }

    /**
     * Fetch records that have <code>delivery_corp_code IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByDeliveryCorpCode(String... values) {
        return fetch(TShipping.T_SHIPPING.DELIVERY_CORP_CODE, values);
    }

    /**
     * Fetch records that have <code>delivery_corp_url IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByDeliveryCorpUrl(String... values) {
        return fetch(TShipping.T_SHIPPING.DELIVERY_CORP_URL, values);
    }

    /**
     * Fetch records that have <code>freight IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByFreight(BigDecimal... values) {
        return fetch(TShipping.T_SHIPPING.FREIGHT, values);
    }

    /**
     * Fetch records that have <code>memo IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByMemo(String... values) {
        return fetch(TShipping.T_SHIPPING.MEMO, values);
    }

    /**
     * Fetch records that have <code>operator IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByOperator(String... values) {
        return fetch(TShipping.T_SHIPPING.OPERATOR, values);
    }

    /**
     * Fetch records that have <code>phone IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByPhone(String... values) {
        return fetch(TShipping.T_SHIPPING.PHONE, values);
    }

    /**
     * Fetch records that have <code>shipping_method IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByShippingMethod(String... values) {
        return fetch(TShipping.T_SHIPPING.SHIPPING_METHOD, values);
    }

    /**
     * Fetch records that have <code>sn IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchBySn(String... values) {
        return fetch(TShipping.T_SHIPPING.SN, values);
    }

    /**
     * Fetch a unique record that has <code>sn = value</code>
     */
    public com.vjshop.entity.TShipping fetchOneBySn(String value) {
        return fetchOne(TShipping.T_SHIPPING.SN, value);
    }

    /**
     * Fetch records that have <code>tracking_no IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByTrackingNo(String... values) {
        return fetch(TShipping.T_SHIPPING.TRACKING_NO, values);
    }

    /**
     * Fetch records that have <code>zip_code IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByZipCode(String... values) {
        return fetch(TShipping.T_SHIPPING.ZIP_CODE, values);
    }

    /**
     * Fetch records that have <code>orders IN (values)</code>
     */
    public List<com.vjshop.entity.TShipping> fetchByOrders(Long... values) {
        return fetch(TShipping.T_SHIPPING.ORDERS, values);
    }
}
