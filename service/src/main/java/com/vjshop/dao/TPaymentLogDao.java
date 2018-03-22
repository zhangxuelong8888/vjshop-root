package com.vjshop.dao;


import com.vjshop.entity.TPaymentLog;
import com.vjshop.generated.db.tables.records.TPaymentLogRecord;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TPaymentLog.T_PAYMENT_LOG;


/**
 * 支付日志DAO
 */@Repository
public class TPaymentLogDao extends JooqBaseDao<TPaymentLogRecord, com.vjshop.entity.TPaymentLog, Long> {

    /**
     * Create a new TPaymentLogDao without any configuration
     */
    public TPaymentLogDao() {
        super(T_PAYMENT_LOG, com.vjshop.entity.TPaymentLog.class);
    }

    /**
     * Create a new TPaymentLogDao with an attached configuration
     */
    @Autowired
    public TPaymentLogDao(Configuration configuration) {
        super(T_PAYMENT_LOG, com.vjshop.entity.TPaymentLog.class, configuration);
    }

    public TPaymentLog findBySn(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return null;
        }
        return fetchOneBySn(sn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TPaymentLog object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchById(Long... values) {
        return fetch(T_PAYMENT_LOG.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.vjshop.entity.TPaymentLog fetchOneById(Long value) {
        return fetchOne(T_PAYMENT_LOG.ID, value);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByCreateDate(Timestamp... values) {
        return fetch(T_PAYMENT_LOG.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>modify_date IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByModifyDate(Timestamp... values) {
        return fetch(T_PAYMENT_LOG.MODIFY_DATE, values);
    }

    /**
     * Fetch records that have <code>version IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByVersion(Long... values) {
        return fetch(T_PAYMENT_LOG.VERSION, values);
    }

    /**
     * Fetch records that have <code>amount IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByAmount(BigDecimal... values) {
        return fetch(T_PAYMENT_LOG.AMOUNT, values);
    }

    /**
     * Fetch records that have <code>fee IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByFee(BigDecimal... values) {
        return fetch(T_PAYMENT_LOG.FEE, values);
    }

    /**
     * Fetch records that have <code>payment_plugin_id IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByPaymentPluginId(String... values) {
        return fetch(T_PAYMENT_LOG.PAYMENT_PLUGIN_ID, values);
    }

    /**
     * Fetch records that have <code>payment_plugin_name IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByPaymentPluginName(String... values) {
        return fetch(T_PAYMENT_LOG.PAYMENT_PLUGIN_NAME, values);
    }

    /**
     * Fetch records that have <code>sn IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchBySn(String... values) {
        return fetch(T_PAYMENT_LOG.SN, values);
    }

    /**
     * Fetch a unique record that has <code>sn = value</code>
     */
    public com.vjshop.entity.TPaymentLog fetchOneBySn(String value) {
        return fetchOne(T_PAYMENT_LOG.SN, value);
    }

    /**
     * Fetch records that have <code>status IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByStatus(Integer... values) {
        return fetch(T_PAYMENT_LOG.STATUS, values);
    }

    /**
     * Fetch records that have <code>type IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByType(Integer... values) {
        return fetch(T_PAYMENT_LOG.TYPE, values);
    }

    /**
     * Fetch records that have <code>member IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByMember(Long... values) {
        return fetch(T_PAYMENT_LOG.MEMBER, values);
    }

    /**
     * Fetch records that have <code>orders IN (values)</code>
     */
    public List<com.vjshop.entity.TPaymentLog> fetchByOrders(Long... values) {
        return fetch(T_PAYMENT_LOG.ORDERS, values);
    }
}
