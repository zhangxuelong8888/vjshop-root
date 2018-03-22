
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.entity.TAttribute;
import com.vjshop.generated.db.tables.TGoodsPromotion;
import com.vjshop.generated.db.tables.TGoodsTag;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.TProductCategory;
import com.vjshop.generated.db.tables.records.TGoodsRecord;
import com.vjshop.util.JooqUtils;
import com.vjshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.vjshop.generated.db.tables.TGoods.T_GOODS;
import static com.vjshop.generated.db.tables.TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS;

/**
 * Dao - 货品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TGoodsDao extends JooqBaseDao<TGoodsRecord, com.vjshop.entity.TGoods, Long> {

    public TGoodsDao() {
        super(T_GOODS, com.vjshop.entity.TGoods.class);
    }

    @Autowired
    public TGoodsDao(Configuration configuration) {
        super(T_GOODS, com.vjshop.entity.TGoods.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TGoods object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TGoods> fetchByProductCategory(Long... values) {
        return fetch(T_GOODS.PRODUCT_CATEGORY, values);
    }

    /**
     * 获取商品
     *
     * @param sn
     *            商品编号
     * @return 商品
     */
    public com.vjshop.entity.TGoods findBySn(String sn){
        return fetchOne(T_GOODS.SN, sn);
    }

    public boolean snExists(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return false;
        }
        SelectQuery query = this.getDslContext().selectCount().from(T_GOODS).where(T_GOODS.SN.lower().eq(sn.toLowerCase())).getQuery();
        Long count = (Long)query.fetchOne(0,Long.class);
        return count > 0;
    }

    public Page<com.vjshop.entity.TGoods> findPage(com.vjshop.entity.TGoods.Type type, Long productCategory, Long brand, Long promotion, Long tag, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                                   Boolean isStockAlert, Boolean hasPromotion, com.vjshop.entity.TGoods.OrderType orderType, Pageable pageable) {
        SelectQuery query = this.getDslContext().select(T_GOODS.fields()).from(T_GOODS).getQuery();
        setQueryParam(query, type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, null, null, null, pageable);
        return super.findPage(query, pageable);
    }

    public Page<com.vjshop.entity.TGoods> findPage(com.vjshop.entity.TGoods.RankingType rankingType, Pageable pageable) {
        SelectQuery query = getQuery();
        if (rankingType != null) {
            switch (rankingType) {
                case score:
                    query.addOrderBy(T_GOODS.SCORE.desc(), T_GOODS.SCORE_COUNT.desc());
                    break;
                case scoreCount:
                    query.addOrderBy(T_GOODS.SCORE_COUNT.desc(), T_GOODS.SCORE.desc());
                    break;
                case weekHits:
                    query.addOrderBy(T_GOODS.WEEK_HITS.desc());
                    break;
                case monthHits:
                    query.addOrderBy(T_GOODS.MONTH_HITS.desc());
                    break;
                case hits:
                    query.addOrderBy(T_GOODS.HITS.desc());
                    break;
                case weekSales:
                    query.addOrderBy(T_GOODS.WEEK_SALES.desc());
                    break;
                case monthSales:
                    query.addOrderBy(T_GOODS.MONTH_SALES.desc());
                    break;
                case sales:
                    query.addOrderBy(T_GOODS.SALES.desc());
                    break;
            }
        }
        return super.findPage(query, pageable);
    }

    public Page<com.vjshop.entity.TGoods> findPage(Long memberId, Pageable pageable) {
        if (memberId == null) {
            return Page.emptyPage(pageable);
        }
        SelectQuery<Record> query = getDslContext()
                .select(T_GOODS.fields())
                .from(T_GOODS)
                .join(T_MEMBER_FAVORITE_GOODS).on(T_GOODS.ID.eq(T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS))
                .where(T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS.eq(memberId)).getQuery();
        return super.findPage(query, pageable);
    }

    public Long count(com.vjshop.entity.TGoods.Type type, Long favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
        SelectQuery<Record1<Integer>> query = getDslContext().selectCount().from(T_GOODS).getQuery();
        if (type != null) {
            query.addConditions(T_GOODS.TYPE.eq(type.ordinal()));
        }
        if (favoriteMember != null) {
            query.addJoin(T_MEMBER_FAVORITE_GOODS, JoinType.LEFT_OUTER_JOIN, T_GOODS.ID.eq(T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS).and(T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS.eq(favoriteMember)));
        }
        if (isMarketable != null) {
            query.addConditions(T_GOODS.IS_MARKETABLE.eq(isMarketable));
        }
        if (isList != null) {
            query.addConditions(T_GOODS.IS_LIST.eq(isList));
        }
        if (isTop != null) {
            query.addConditions(T_GOODS.IS_TOP.eq(isTop));
        }
        if (isOutOfStock != null) {
            SelectQuery subQuery = null;
            if (isOutOfStock) {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID))
                        .and(TProduct.T_PRODUCT.STOCK.nullif(0).le(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            } else {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).gt(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            }
            query.addConditions(T_GOODS.ID.in(subQuery));
        }

        if (isStockAlert != null) {
            SelectQuery subQuery = null;
            Setting setting = SystemUtils.getSetting();
            if (isStockAlert) {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).le(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0).add(setting.getAccountLockCount()))).getQuery();
            } else {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).gt(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0).add(setting.getAccountLockCount()))).getQuery();
            }
            query.addConditions(T_GOODS.ID.in(subQuery));
        }
        return query.fetchOne(0, Integer.class).longValue();
    }

    /**
     * 查找货品
     *
     * @param type
     *            类型
     * @param productCategory
     *            商品分类
     * @param brand
     *            品牌
     * @param promotion
     *            促销
     * @param tag
     *            标签
     * @param attributeValueMap
     *            属性值Map
     * @param startPrice
     *            最低价格
     * @param endPrice
     *            最高价格
     * @param isMarketable
     *            是否上架
     * @param isList
     *            是否列出
     * @param isTop
     *            是否置顶
     * @param isOutOfStock
     *            是否缺货
     * @param isStockAlert
     *            是否库存警告
     * @param hasPromotion
     *            是否存在促销
     * @param orderType
     *            排序类型
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 货品
     */
    public List<com.vjshop.entity.TGoods> findList(com.vjshop.entity.TGoods.Type type, Long productCategory, Long brand, Long promotion, Long tag, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, com.vjshop.entity.TGoods.OrderType orderType, Integer count, List<Filter> filters, List<com.vjshop.Order> orders){
        SelectQuery query = this.getDslContext().select(T_GOODS.fields()).from(T_GOODS).getQuery();
        setQueryParam(query, type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, null, null, null, null);
        return findList(query, null, count, filters, orders);
    }

    /**
     * 查找货品
     *
     * @param productCategory
     *            商品分类
     * @param isMarketable
     *            是否上架
     * @param generateMethod
     *            静态生成方式
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @param first
     *            起始记录
     * @param count
     *            数量
     * @return 货品
     */
    public List<com.vjshop.entity.TGoods> findList(Long productCategory, Boolean isMarketable, com.vjshop.entity.TGoods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count){
        SelectQuery query = this.getDslContext().select(T_GOODS.fields()).from(T_GOODS).getQuery();
        setQueryParam(query, null, productCategory, null, null, null, null, null, null, isMarketable, null, null, null, null, null, null, null, null, null, null);
        return findList(query, first, count, null, null);
    }

    /**
     * 清空货品属性值
     *
     * @param propertyIndex
     *            属性索引
     * @param productCategoryId
     *            商品分类ID
     */
    public int clearAttributeValue(Integer propertyIndex, Long productCategoryId){
        return getDslContext().update(T_GOODS)
                .set(JooqUtils.getField(com.vjshop.entity.TGoods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + propertyIndex), "")
                .where(T_GOODS.PRODUCT_CATEGORY.eq(productCategoryId))
                .execute();
    }

    public void setQueryParam(SelectQuery query, com.vjshop.entity.TGoods.Type type, Long productCategory, Long brand, Long promotion, Long tag, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                              Boolean isStockAlert, Boolean hasPromotion, com.vjshop.entity.TGoods.OrderType orderType, com.vjshop.entity.TGoods.GenerateMethod generateMethod, Timestamp beginDate, Timestamp endDate, Pageable pageable){
        if (promotion != null) {
            query.addJoin(TGoodsPromotion.T_GOODS_PROMOTION, JoinType.JOIN, TGoodsPromotion.T_GOODS_PROMOTION.GOODS.eq(T_GOODS.ID), TGoodsPromotion.T_GOODS_PROMOTION.PROMOTIONS.eq(promotion));
        }
        if (tag != null) {
            query.addJoin(TGoodsTag.T_GOODS_TAG, JoinType.JOIN, TGoodsTag.T_GOODS_TAG.GOODS.eq(T_GOODS.ID), TGoodsTag.T_GOODS_TAG.TAGS.eq(tag));
        }

        if (attributeValueMap != null) {
            for (Map.Entry<TAttribute, String> entry : attributeValueMap.entrySet()) {
                String propertyName = com.vjshop.entity.TGoods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                query.addConditions(getField(propertyName).eq(entry.getValue()));
            }
        }

        if (type != null)
            query.addConditions(T_GOODS.TYPE.eq(type.ordinal()));
        if (productCategory != null) {
            SelectQuery subQuery = this.getDslContext().select(TProductCategory.T_PRODUCT_CATEGORY.ID).from(TProductCategory.T_PRODUCT_CATEGORY).where(TProductCategory.T_PRODUCT_CATEGORY.ID.eq(productCategory).or(TProductCategory.T_PRODUCT_CATEGORY.TREE_PATH.like("%" + com.vjshop.entity.TProductCategory.TREE_PATH_SEPARATOR + productCategory + com.vjshop.entity.TProductCategory.TREE_PATH_SEPARATOR + "%"))).getQuery();
            query.addConditions(T_GOODS.PRODUCT_CATEGORY.in(subQuery));
        }
        if (brand != null)
            query.addConditions(T_GOODS.BRAND.eq(brand));

        if (pageable != null && StringUtils.isNotBlank(pageable.getSearchProperty()) && StringUtils.isNotBlank(pageable.getSearchValue())) {
            query.addConditions(getField(pageable.getSearchProperty()).like("%"+pageable.getSearchValue()+"%"));
        }

        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
            query.addConditions(T_GOODS.PRICE.gt(startPrice));
        }
        if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
            query.addConditions(T_GOODS.PRICE.lt(endPrice));
        }

        if (isOutOfStock != null) {
            SelectQuery subQuery = null;
            if (isOutOfStock) {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).le(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            } else {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).gt(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0))).getQuery();
            }
            query.addConditions(T_GOODS.ID.in(subQuery));
        }


        if (isStockAlert != null) {
            SelectQuery subQuery = null;
            Setting setting = SystemUtils.getSetting();
            if (isStockAlert) {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).le(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0).add(setting.getAccountLockCount()))).getQuery();
            } else {
                subQuery = this.getDslContext().select(TProduct.T_PRODUCT.GOODS)
                        .from(TProduct.T_PRODUCT)
                        .where(TProduct.T_PRODUCT.GOODS.eq(T_GOODS.ID)).and(TProduct.T_PRODUCT.STOCK.nullif(0).gt(TProduct.T_PRODUCT.ALLOCATED_STOCK.nullif(0).add(setting.getAccountLockCount()))).getQuery();
            }
            query.addConditions(T_GOODS.ID.in(subQuery));
        }

        if (isMarketable != null) {
            query.addConditions(T_GOODS.IS_MARKETABLE.eq(isMarketable));
        }
        if (isList != null) {
            query.addConditions(T_GOODS.IS_LIST.eq(isList));
        }
        if (isTop != null) {
            query.addConditions(T_GOODS.IS_TOP.eq(isTop));
        }
        if (orderType != null) {
            switch (orderType) {
                case topDesc:
                    query.addOrderBy(T_GOODS.IS_TOP.desc(), T_GOODS.CREATE_DATE.desc());
                    break;
                case priceAsc:
                    query.addOrderBy(T_GOODS.PRICE.asc(), T_GOODS.CREATE_DATE.desc());
                    break;
                case priceDesc:
                    query.addOrderBy(T_GOODS.PRICE.desc(), T_GOODS.CREATE_DATE.desc());
                    break;
                case salesDesc:
                    query.addOrderBy(T_GOODS.SALES.desc(), T_GOODS.CREATE_DATE.desc());
                    break;
                case scoreDesc:
                    query.addOrderBy(T_GOODS.SCORE.desc(), T_GOODS.CREATE_DATE.desc());
                    break;
                case dateDesc:
                    query.addOrderBy(T_GOODS.CREATE_DATE.desc());
                    break;
            }
        } else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
            query.addOrderBy(T_GOODS.IS_TOP.desc(), T_GOODS.CREATE_DATE.desc());
        }
        if (generateMethod != null) {
            query.addConditions(T_GOODS.GENERATE_METHOD.eq(generateMethod.ordinal()));
        }
        if (beginDate != null) {
            query.addConditions(T_GOODS.CREATE_DATE.ge(beginDate));
        }
        if (endDate != null) {
            query.addConditions(T_GOODS.CREATE_DATE.le(endDate));
        }
    }

}
