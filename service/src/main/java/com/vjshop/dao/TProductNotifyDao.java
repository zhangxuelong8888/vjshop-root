
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.records.TProductNotifyRecord;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.vjshop.generated.db.tables.TProductNotify.T_PRODUCT_NOTIFY;

/**
 * Dao - 到货通知
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TProductNotifyDao extends JooqBaseDao<TProductNotifyRecord, com.vjshop.entity.TProductNotify, Long> {

    public List<com.vjshop.entity.TProductNotify> findByMemberId(Long memberId) {
        List<Filter> filters = new ArrayList<Filter>(1);
        filters.add(new Filter(T_PRODUCT_NOTIFY.MEMBER.getName(), Filter.Operator.eq, memberId));
        return findList(filters, null);
    }

    public TProductNotifyDao() {
        super(T_PRODUCT_NOTIFY, com.vjshop.entity.TProductNotify.class);
    }

    @Autowired
    public TProductNotifyDao(Configuration configuration) {
        super(T_PRODUCT_NOTIFY, com.vjshop.entity.TProductNotify.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TProductNotify object) {
        return object.getId();
    }

    /**
     * 查找到货通知分页
     *
     * @param memberId
     *            会员ID
     * @param isMarketable
     *            是否上架
     * @param isOutOfStock
     *            商品是否缺货
     * @param hasSent
     *            是否已发送.
     * @param pageable
     *            分页信息
     * @return 到货通知分页
     */
    public Page<com.vjshop.entity.TProductNotify> findPage(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
        if (StringUtils.isBlank(pageable.getOrderProperty())) {
            pageable.setOrderProperty("createDate");
            pageable.setOrderDirection(Order.Direction.desc);
        }
        SelectQuery query = this.getDslContext().select(T_PRODUCT_NOTIFY.fields()).from(T_PRODUCT_NOTIFY).getQuery();

        if(StringUtils.isNotBlank(pageable.getSearchProperty()) && StringUtils.isNotBlank(pageable.getSearchValue())){
            query.addConditions(getField(pageable.getSearchProperty()).eq(pageable.getSearchValue()));
        }
        setQueryParam(query, memberId, isMarketable, isOutOfStock, hasSent);

        return super.findPage(query, pageable);
    }

    /**
     * 查找到货通知数量
     *
     * @param memberId
     *            会员ID
     * @param isMarketable
     *            是否上架
     * @param isOutOfStock
     *            商品是否缺货
     * @param hasSent
     *            是否已发送.
     * @return 到货通知数量
     */
    public Long count(Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent){
        SelectQuery query = this.getDslContext().selectCount().from(T_PRODUCT_NOTIFY).getQuery();
        setQueryParam(query, memberId, isMarketable, isOutOfStock, hasSent);
        return (Long) query.fetchOne(0, Long.class);
    }

    /**
     * 判断到货通知是否存在
     *
     * @param productId
     *            商品ID
     * @param email
     *            E-mail(忽略大小写)
     * @return 到货通知是否存在
     */
    public boolean exists(Long productId, String email){
        if (productId == null || StringUtils.isEmpty(email)) {
            return false;
        }
        Long count = getDslContext().selectCount().from(T_PRODUCT_NOTIFY)
                .where(T_PRODUCT_NOTIFY.PRODUCT.eq(productId)
                        .and(T_PRODUCT_NOTIFY.EMAIL.lower().eq(DSL.lower(email)))
                        .and(T_PRODUCT_NOTIFY.HAS_SENT.eq(false)))
                .fetchOne(0, Long.class);
        return count != null && count.longValue() > 0;
    }

    private void setQueryParam(SelectQuery query, Long memberId, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent){
        if(memberId != null){
            query.addConditions(T_PRODUCT_NOTIFY.MEMBER.eq(memberId));
        }
        if (isMarketable != null) {
            query.addJoin(TProduct.T_PRODUCT,JoinType.JOIN,TProduct.T_PRODUCT.ID.eq(T_PRODUCT_NOTIFY.PRODUCT));
            query.addJoin(TGoods.T_GOODS,JoinType.JOIN,TProduct.T_PRODUCT.GOODS.eq(TGoods.T_GOODS.ID));
            query.addConditions(TGoods.T_GOODS.IS_MARKETABLE.eq(isMarketable));
        }
        if (isOutOfStock != null) {
            SelectQuery subQuery = null;
            if (isOutOfStock) {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.ID)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.ID.eq(T_PRODUCT_NOTIFY.PRODUCT)).and(TProduct.T_PRODUCT.STOCK.nullif(0).le(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            } else {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.ID)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.ID.eq(T_PRODUCT_NOTIFY.PRODUCT)).and(TProduct.T_PRODUCT.STOCK.nullif(0).gt(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            }
            query.addConditions(T_PRODUCT_NOTIFY.PRODUCT.in(subQuery));
        }
        if (hasSent != null) {
            query.addConditions(T_PRODUCT_NOTIFY.HAS_SENT.eq(hasSent));
        }
    }
}
