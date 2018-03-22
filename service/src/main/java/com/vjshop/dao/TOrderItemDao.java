package com.vjshop.dao;


import com.vjshop.generated.db.tables.records.TOrderItemRecord;
import com.vjshop.util.JooqUtils;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.vjshop.generated.db.tables.TGoods.T_GOODS;
import static com.vjshop.generated.db.tables.TOrder.T_ORDER;
import static com.vjshop.generated.db.tables.TOrderItem.T_ORDER_ITEM;
import static com.vjshop.generated.db.tables.TProduct.T_PRODUCT;


/**
 * 订单项DAO
 */
@Repository
public class TOrderItemDao extends JooqBaseDao<TOrderItemRecord, com.vjshop.entity.TOrderItem, Long> {

    public List<com.vjshop.entity.TOrderItem> findListByOrderId(Long orderId) {
        SelectQuery<Record> query = getDslContext().select()
                .from(T_ORDER_ITEM)
                .join(T_PRODUCT).on(T_ORDER_ITEM.PRODUCT.eq(T_PRODUCT.ID))
                .join(T_GOODS).on(T_PRODUCT.GOODS.eq(T_GOODS.ID))
                .where(T_ORDER_ITEM.ORDERS.eq(orderId)).getQuery();
        query.addSelect(T_ORDER_ITEM.fields());
        query.addSelect(JooqUtils.getFields(T_PRODUCT, "productVO"));
        query.addSelect(JooqUtils.getFields(T_GOODS, "productVO.goodsVO"));
        return resultSet2List(query.fetchResultSet());
    }


    /**
     * Create a new TOrderItemDao without any configuration
     */
    public TOrderItemDao() {
        super(T_ORDER_ITEM, com.vjshop.entity.TOrderItem.class);
    }

    /**
     * Create a new TOrderItemDao with an attached configuration
     */
    @Autowired
    public TOrderItemDao(Configuration configuration) {
        super(T_ORDER_ITEM, com.vjshop.entity.TOrderItem.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TOrderItem object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchById(Long... values) {
        return fetch(T_ORDER_ITEM.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.vjshop.entity.TOrderItem fetchOneById(Long value) {
        return fetchOne(T_ORDER_ITEM.ID, value);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByCreateDate(Timestamp... values) {
        return fetch(T_ORDER_ITEM.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>modify_date IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByModifyDate(Timestamp... values) {
        return fetch(T_ORDER_ITEM.MODIFY_DATE, values);
    }

    /**
     * Fetch records that have <code>version IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByVersion(Long... values) {
        return fetch(T_ORDER_ITEM.VERSION, values);
    }

    /**
     * Fetch records that have <code>is_delivery IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByIsDelivery(Boolean... values) {
        return fetch(T_ORDER_ITEM.IS_DELIVERY, values);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByName(String... values) {
        return fetch(T_ORDER_ITEM.NAME, values);
    }

    /**
     * Fetch records that have <code>price IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByPrice(BigDecimal... values) {
        return fetch(T_ORDER_ITEM.PRICE, values);
    }

    /**
     * Fetch records that have <code>quantity IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByQuantity(Integer... values) {
        return fetch(T_ORDER_ITEM.QUANTITY, values);
    }

    /**
     * Fetch records that have <code>returned_quantity IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByReturnedQuantity(Integer... values) {
        return fetch(T_ORDER_ITEM.RETURNED_QUANTITY, values);
    }

    /**
     * Fetch records that have <code>shipped_quantity IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByShippedQuantity(Integer... values) {
        return fetch(T_ORDER_ITEM.SHIPPED_QUANTITY, values);
    }

    /**
     * Fetch records that have <code>sn IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchBySn(String... values) {
        return fetch(T_ORDER_ITEM.SN, values);
    }

    /**
     * Fetch records that have <code>specifications IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchBySpecifications(String... values) {
        return fetch(T_ORDER_ITEM.SPECIFICATIONS, values);
    }

    /**
     * Fetch records that have <code>thumbnail IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByThumbnail(String... values) {
        return fetch(T_ORDER_ITEM.THUMBNAIL, values);
    }

    /**
     * Fetch records that have <code>type IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByType(Integer... values) {
        return fetch(T_ORDER_ITEM.TYPE, values);
    }

    /**
     * Fetch records that have <code>weight IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByWeight(Integer... values) {
        return fetch(T_ORDER_ITEM.WEIGHT, values);
    }

    /**
     * Fetch records that have <code>orders IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByOrders(Long... values) {
        return fetch(T_ORDER_ITEM.ORDERS, values);
    }

    /**
     * Fetch records that have <code>product IN (values)</code>
     */
    public List<com.vjshop.entity.TOrderItem> fetchByProduct(Long... values) {
        return fetch(T_ORDER_ITEM.PRODUCT, values);
    }

    public List<com.vjshop.entity.TOrderItem> findList(com.vjshop.entity.TOrderItem param){
        SelectQuery query = super.getQuery();

        if(param.getOrders() != null){
            query.addConditions(T_ORDER_ITEM.ORDERS.eq(param.getOrders()));
        }
        if(param.getProduct() != null){
            query.addConditions(T_ORDER_ITEM.PRODUCT.eq(param.getProduct()));
        }

        return super.resultSet2List(query.fetchResultSet());
    }
}
