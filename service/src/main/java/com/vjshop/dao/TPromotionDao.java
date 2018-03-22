
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.generated.db.tables.*;
import com.vjshop.generated.db.tables.records.TPromotionRecord;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Dao - 促销
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TPromotionDao extends JooqBaseDao<TPromotionRecord, com.vjshop.entity.TPromotion, Long> {

    public TPromotionDao() {
        super(TPromotion.T_PROMOTION, com.vjshop.entity.TPromotion.class);
    }

    @Autowired
    public TPromotionDao(Configuration configuration) {
        super(TPromotion.T_PROMOTION, com.vjshop.entity.TPromotion.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TPromotion object) {
        return object.getId();
    }

    /**
     * 查找促销
     *
     * @param ids
     *          促销ID
     * @return 促销
     */
    public List<com.vjshop.entity.TPromotion> fetchById(Long... ids){
        return fetch(TPromotion.T_PROMOTION.ID, ids);
    }

    /**
     * 查找促销详情
     *
     * @param id
     *          促销ID
     * @return 促销
     */
    public com.vjshop.entity.TPromotion findDetails(Long id){
        SelectQuery query = getDslContext().select(TPromotion.T_PROMOTION.fields()).from(TPromotion.T_PROMOTION).getQuery();

        query.addSelect(TMemberRank.T_MEMBER_RANK.ID.as("memberRanks.id"),
                TMemberRank.T_MEMBER_RANK.NAME.as("memberRanks.name"));
        query.addSelect(TCoupon.T_COUPON.ID.as("coupons.id"),
                TCoupon.T_COUPON.NAME.as("coupons.name"));
        query.addSelect(TProduct.T_PRODUCT.ID.as("gifts.id"),
                TProduct.T_PRODUCT.GOODS.as("gifts.goods"),
                TProduct.T_PRODUCT.SN.as("gifts.sn"),
                TProduct.T_PRODUCT.SPECIFICATION_VALUES.as("gifts.specificationValues"));
        query.addSelect(TGoods.T_GOODS.ID.as("gifts.goodsVO.id"),
                TGoods.T_GOODS.NAME.as("gifts.goodsVO.name"),
                TGoods.T_GOODS.PRODUCT_IMAGES.as("goodsInfo.productImages"));

        query.addJoin(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK, JoinType.LEFT_OUTER_JOIN,
                TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.PROMOTIONS.eq(TPromotion.T_PROMOTION.ID));
        query.addJoin(TMemberRank.T_MEMBER_RANK, JoinType.LEFT_OUTER_JOIN,
                TMemberRank.T_MEMBER_RANK.ID.eq(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.MEMBER_RANKS));
        query.addJoin(TPromotionCoupon.T_PROMOTION_COUPON, JoinType.LEFT_OUTER_JOIN,
                TPromotionCoupon.T_PROMOTION_COUPON.PROMOTIONS.eq(TPromotion.T_PROMOTION.ID));
        query.addJoin(TCoupon.T_COUPON, JoinType.LEFT_OUTER_JOIN,
                TCoupon.T_COUPON.ID.eq(TPromotionCoupon.T_PROMOTION_COUPON.COUPONS));
        query.addJoin(TPromotionGift.T_PROMOTION_GIFT, JoinType.LEFT_OUTER_JOIN,
                TPromotionGift.T_PROMOTION_GIFT.GIFT_PROMOTIONS.eq(TPromotion.T_PROMOTION.ID));
        query.addJoin(TProduct.T_PRODUCT, JoinType.LEFT_OUTER_JOIN,
                TProduct.T_PRODUCT.ID.eq(TPromotionGift.T_PROMOTION_GIFT.GIFTS));
        query.addJoin(TGoods.T_GOODS, JoinType.LEFT_OUTER_JOIN,
                TGoods.T_GOODS.ID.eq(TProduct.T_PRODUCT.GOODS));

        query.addConditions(TPromotion.T_PROMOTION.ID.eq(id));
        List<com.vjshop.entity.TPromotion> promotionList = resultSet2List(query.fetchResultSet(),
                this.ID_PROPERTY_NAME, "memberRanks.id", "coupons.id", "gifts.id", "gifts.goodsVO.id");
        if (CollectionUtils.isNotEmpty(promotionList)){
            return promotionList.get(0);
        }
        return null;
    }

    /**
     * 查找促销
     *
     * @param memberRankId
     *            会员等级ID
     * @param productCategoryId
     *            商品分类ID
     * @param hasBegun
     *            是否已开始
     * @param hasEnded
     *            是否已结束
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 促销
     */
    public List<com.vjshop.entity.TPromotion> findList(Long memberRankId, Long productCategoryId,
                                                Boolean hasBegun, Boolean hasEnded, Integer count,
                                                List<Filter> filters, List<Order> orders){
        SelectQuery query = getDslContext().select(TPromotion.T_PROMOTION.fields()).from(TPromotion.T_PROMOTION).getQuery();
        if (memberRankId != null){
            query.addJoin(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK, JoinType.LEFT_OUTER_JOIN,
                    TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.PROMOTIONS.eq(TPromotion.T_PROMOTION.ID));
            query.addConditions(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.MEMBER_RANKS.eq(memberRankId));
        }
        if (productCategoryId != null){
            query.addJoin(TProductCategoryPromotion.T_PRODUCT_CATEGORY_PROMOTION, JoinType.LEFT_OUTER_JOIN,
                    TProductCategoryPromotion.T_PRODUCT_CATEGORY_PROMOTION.PROMOTIONS.eq(TPromotion.T_PROMOTION.ID));
            query.addConditions(TProductCategoryPromotion.T_PRODUCT_CATEGORY_PROMOTION.PRODUCT_CATEGORIES.eq(productCategoryId));
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (hasBegun != null){
            if (hasBegun){
                query.addConditions(TPromotion.T_PROMOTION.BEGIN_DATE.isNull().or(TPromotion.T_PROMOTION.BEGIN_DATE.le(now)));
            } else {
                query.addConditions(TPromotion.T_PROMOTION.BEGIN_DATE.isNotNull().or(TPromotion.T_PROMOTION.BEGIN_DATE.gt(now)));
            }
        }
        if (hasEnded != null){
            if (hasEnded){
                query.addConditions(TPromotion.T_PROMOTION.END_DATE.isNotNull().or(TPromotion.T_PROMOTION.END_DATE.le(now)));
            } else {
                query.addConditions(TPromotion.T_PROMOTION.END_DATE.isNull().or(TPromotion.T_PROMOTION.END_DATE.gt(now)));
            }
        }
        return super.findList(query, null, count, filters, orders);
    }
}
