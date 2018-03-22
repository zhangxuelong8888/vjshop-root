
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TOrder;
import com.vjshop.generated.db.tables.TOrderItem;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.records.TOrderRecord;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Dao - 订单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TOrderDao extends JooqBaseDao<TOrderRecord, com.vjshop.entity.TOrder, Long> {

    public TOrderDao() {
        super(TOrder.T_ORDER, com.vjshop.entity.TOrder.class);
    }

    @Autowired
    public TOrderDao(Configuration configuration) {
        super(TOrder.T_ORDER, com.vjshop.entity.TOrder.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TOrder object) {
        return object.getId();
    }

    /**
     * 根据编号查找订单
     *
     * @param sn 编号
     * @return 订单
     */
    public com.vjshop.entity.TOrder findBySn(String sn) {
        return fetchOne(TOrder.T_ORDER.SN, sn);
    }

    /**
     * 查找订单分页
     *
     * @param type
     *            类型
     * @param status
     *            状态
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isPendingReceive
     *            是否等待收款
     * @param isPendingRefunds
     *            是否等待退款
     * @param isUseCouponCode
     *            是否已使用优惠码
     * @param isExchangePoint
     *            是否已兑换积分
     * @param isAllocatedStock
     *            是否已分配库存
     * @param hasExpired
     *            是否已过期
     * @param pageable
     *            分页信息
     * @return 订单分页
     */
    public Page<com.vjshop.entity.TOrder> findPage(com.vjshop.entity.TOrder.Type type, com.vjshop.entity.TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
        if (StringUtils.isBlank(pageable.getOrderProperty())) {
            pageable.setOrderProperty("createDate");
            pageable.setOrderDirection(Order.Direction.desc);
        }

        SelectQuery query = this.getDslContext().select(TOrder.T_ORDER.fields()).from(TOrder.T_ORDER).getQuery();

        this.generateCondition(query, type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);


        return super.findPage(query, pageable);
    }

    /**
     * 查找订单
     *
     * @param type
     *            类型
     * @param status
     *            状态
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isPendingReceive
     *            是否等待收款
     * @param isPendingRefunds
     *            是否等待退款
     * @param isUseCouponCode
     *            是否已使用优惠码
     * @param isExchangePoint
     *            是否已兑换积分
     * @param isAllocatedStock
     *            是否已分配库存
     * @param hasExpired
     *            是否已过期
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 订单
     */
    public List<com.vjshop.entity.TOrder> findList(com.vjshop.entity.TOrder.Type type, com.vjshop.entity.TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                                                   List<com.vjshop.Order> orders) {

        SelectQuery query = this.getDslContext().select(TOrder.T_ORDER.fields()).from(TOrder.T_ORDER).getQuery();

        this.generateCondition(query, type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);

        return super.findList(query, null, count, filters, orders);

    }

    /**
     * 查询订单数量
     *
     * @param type
     *            类型
     * @param status
     *            状态
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isPendingReceive
     *            是否等待收款
     * @param isPendingRefunds
     *            是否等待退款
     * @param isUseCouponCode
     *            是否已使用优惠码
     * @param isExchangePoint
     *            是否已兑换积分
     * @param isAllocatedStock
     *            是否已分配库存
     * @param hasExpired
     *            是否已过期
     * @return 订单数量
     */
    public Long count(com.vjshop.entity.TOrder.Type type, com.vjshop.entity.TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive,
                      Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {

        SelectQuery query = this.getDslContext().selectCount().from(TOrder.T_ORDER).getQuery();

        this.generateCondition(query, type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);

        return (Long) query.fetchOne(0, Long.class);

    }

    public void generateCondition(SelectQuery query, com.vjshop.entity.TOrder.Type type, com.vjshop.entity.TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
        if (type != null) {
            query.addConditions(TOrder.T_ORDER.TYPE.eq(type.ordinal()));
        }
        if (status != null) {
            query.addConditions(TOrder.T_ORDER.STATUS.eq(status.ordinal()));
        }
        if (memberId != null) {
            query.addConditions(TOrder.T_ORDER.MEMBER.eq(memberId));
        }

        if (goodsId != null) {
            SelectQuery goodsSubQuery = this.getDslContext().select(TProduct.T_PRODUCT.ID).from(TProduct.T_PRODUCT).where(TProduct.T_PRODUCT.GOODS.eq(goodsId)).getQuery();
            SelectQuery orderItemSubQuery = this.getDslContext().select(TOrderItem.T_ORDER_ITEM.ORDERS).from(TOrderItem.T_ORDER_ITEM).where(TOrderItem.T_ORDER_ITEM.ORDERS.eq(TOrder.T_ORDER.ID)).getQuery();
            orderItemSubQuery.addConditions(TOrderItem.T_ORDER_ITEM.PRODUCT.in(goodsSubQuery));
            query.addConditions(TOrder.T_ORDER.ID.in(orderItemSubQuery));
        }

        if (isPendingReceive != null) {
            Condition condition = TOrder.T_ORDER.EXPIRE.isNotNull().or(TOrder.T_ORDER.EXPIRE.gt(new Timestamp(System.currentTimeMillis())))
                    .and(TOrder.T_ORDER.PAYMENT_METHOD_TYPE.eq(com.vjshop.entity.TPaymentMethod.Type.cashOnDelivery.ordinal()))
                    .and(TOrder.T_ORDER.STATUS.ne(com.vjshop.entity.TOrder.Status.completed.ordinal()))
                    .and(TOrder.T_ORDER.STATUS.ne(com.vjshop.entity.TOrder.Status.failed.ordinal()))
                    .and(TOrder.T_ORDER.STATUS.ne(com.vjshop.entity.TOrder.Status.canceled.ordinal()))
                    .and(TOrder.T_ORDER.STATUS.ne(com.vjshop.entity.TOrder.Status.denied.ordinal()))
                    .and(TOrder.T_ORDER.AMOUNT_PAID.nullif(BigDecimal.ZERO).lt(TOrder.T_ORDER.AMOUNT.nullif(BigDecimal.ZERO)));

            if (isPendingReceive) {
                query.addConditions(condition);
            } else {
                query.addConditions(condition.not());
            }
        }

        if (isPendingRefunds != null) {
            Condition or1_1 = TOrder.T_ORDER.EXPIRE.isNotNull().and(TOrder.T_ORDER.EXPIRE.le(new Timestamp(System.currentTimeMillis())))
                    .or(TOrder.T_ORDER.STATUS.eq(com.vjshop.entity.TOrder.Status.failed.ordinal()))
                    .or(TOrder.T_ORDER.STATUS.eq(com.vjshop.entity.TOrder.Status.canceled.ordinal()))
                    .or(TOrder.T_ORDER.STATUS.eq(com.vjshop.entity.TOrder.Status.denied.ordinal()));
            Condition or1_2 = TOrder.T_ORDER.AMOUNT_PAID.nullif(BigDecimal.ZERO).gt(BigDecimal.ZERO);

            Condition or1 = or1_1.and(or1_2);

            Condition or2 = TOrder.T_ORDER.STATUS.eq(com.vjshop.entity.TOrder.Status.completed.ordinal())
                    .and(TOrder.T_ORDER.AMOUNT_PAID.gt(TOrder.T_ORDER.AMOUNT));

            Condition condition = or1.or(or2);

            if (isPendingRefunds) {
                query.addConditions(condition);
            } else {
                query.addConditions(condition.not());
            }
        }

        if (isUseCouponCode != null) {
            query.addConditions(TOrder.T_ORDER.IS_USE_COUPON_CODE.eq(isUseCouponCode));
        }
        if (isExchangePoint != null) {
            query.addConditions(TOrder.T_ORDER.IS_EXCHANGE_POINT.eq(isExchangePoint));
        }
        if (isAllocatedStock != null) {
            query.addConditions(TOrder.T_ORDER.IS_ALLOCATED_STOCK.eq(isAllocatedStock));
        }
        if (hasExpired != null) {
            if (hasExpired) {
                query.addConditions(TOrder.T_ORDER.EXPIRE.isNotNull().and(TOrder.T_ORDER.EXPIRE.le(new Timestamp(System.currentTimeMillis()))));
            } else {
                query.addConditions(TOrder.T_ORDER.EXPIRE.isNull().and(TOrder.T_ORDER.EXPIRE.gt(new Timestamp(System.currentTimeMillis()))));
            }
        }
    }

    /**
     * 查询订单创建数
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 订单创建数
     */
    public Long createOrderCount(Date beginDate, Date endDate) {
        SelectQuery query = this.getDslContext().selectCount().from(TOrder.T_ORDER).getQuery();
        if (beginDate != null) {
            query.addConditions(TOrder.T_ORDER.CREATE_DATE.ge(new Timestamp(beginDate.getTime())));
        }
        if (endDate != null) {
            query.addConditions(TOrder.T_ORDER.CREATE_DATE.le(new Timestamp(endDate.getTime())));
        }

        Object res = super.aggregateQuery(query, null);

        return res == null ? 0L : Long.valueOf(res.toString());
    }

    /**
     * 查询订单完成数
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 订单完成数
     */
    public Long completeOrderCount(Date beginDate, Date endDate) {
        SelectQuery query = this.getDslContext().selectCount().from(TOrder.T_ORDER).getQuery();
        if (beginDate != null) {
            query.addConditions(TOrder.T_ORDER.COMPLETE_DATE.ge(new Timestamp(beginDate.getTime())));
        }
        if (endDate != null) {
            query.addConditions(TOrder.T_ORDER.COMPLETE_DATE.le(new Timestamp(endDate.getTime())));
        }

        Object res = super.aggregateQuery(query, null);

        return res == null ? 0L : Long.valueOf(res.toString());
    }

    /**
     * 查询订单创建金额
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 订单创建金额
     */
    public BigDecimal createOrderAmount(Date beginDate, Date endDate) {
        SelectQuery query = this.getDslContext().select(TOrder.T_ORDER.AMOUNT.sum()).from(TOrder.T_ORDER).getQuery();
        if (beginDate != null) {
            query.addConditions(TOrder.T_ORDER.CREATE_DATE.ge(new Timestamp(beginDate.getTime())));
        }
        if (endDate != null) {
            query.addConditions(TOrder.T_ORDER.CREATE_DATE.le(new Timestamp(endDate.getTime())));
        }

        Object res = super.aggregateQuery(query, null);

        return res == null ? BigDecimal.ZERO : new BigDecimal(res.toString());
    }

    /**
     * 查询订单完成金额
     *
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 订单完成金额
     */
    public BigDecimal completeOrderAmount(Date beginDate, Date endDate) {
        SelectQuery query = this.getDslContext().select(TOrder.T_ORDER.AMOUNT.sum()).from(TOrder.T_ORDER).getQuery();
        if (beginDate != null) {
            query.addConditions(TOrder.T_ORDER.COMPLETE_DATE.ge(new Timestamp(beginDate.getTime())));
        }
        if (endDate != null) {
            query.addConditions(TOrder.T_ORDER.COMPLETE_DATE.le(new Timestamp(endDate.getTime())));
        }

        Object res = super.aggregateQuery(query, null);

        return res == null ? BigDecimal.ZERO : new BigDecimal(res.toString());
    }
}
