
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TMember;
import com.vjshop.generated.db.tables.TReview;
import com.vjshop.generated.db.tables.records.TReviewRecord;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao 评论
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TReviewDao extends JooqBaseDao<TReviewRecord, com.vjshop.entity.TReview, Long> {

    public TReviewDao() {
        super(TReview.T_REVIEW, com.vjshop.entity.TReview.class);
    }

    @Autowired
    public TReviewDao(Configuration configuration) {
        super(TReview.T_REVIEW, com.vjshop.entity.TReview.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TReview object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TReview> fetchByGoods(Long... values) {
        return fetch(TReview.T_REVIEW.GOODS, values);
    }

    /**
     * 查找评论详情
     *
     * @return
     *          评论
     */
    public com.vjshop.entity.TReview findDetails(Long id){
        SelectQuery query = getDslContext().select(TReview.T_REVIEW.fields()).from(TReview.T_REVIEW).getQuery();
        addJoin(query);
        query.addConditions(TReview.T_REVIEW.ID.eq(id));
        List<com.vjshop.entity.TReview> reviewList = resultSet2List(query.fetchResultSet());
        if (CollectionUtils.isNotEmpty(reviewList)){
            return reviewList.get(0);
        }
        return null;
    }

    /**
     * 查找评论
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param type
     *            类型
     * @param isShow
     *            是否显示
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 评论
     */
    public List<com.vjshop.entity.TReview> findList(Long memberId, Long goodsId, com.vjshop.entity.TReview.Type type, Boolean isShow,
                                                                Integer count, List<Filter> filters, List<Order> orders){
        SelectQuery query = getDslContext().select(TReview.T_REVIEW.fields()).from(TReview.T_REVIEW).getQuery();
        addJoin(query);
        setQueryParam(query, memberId, goodsId, type, isShow);
        return super.findList(query, null, count, filters, orders);
    }

    /**
     * 查找评论分页
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param type
     *            类型
     * @param isShow
     *            是否显示
     * @param pageable
     *            分页信息
     * @return 评论分页
     */
    public Page<com.vjshop.entity.TReview> findPage(Long memberId, Long goodsId, com.vjshop.entity.TReview.Type type, Boolean isShow, Pageable pageable){
        SelectQuery query = getDslContext().select(TReview.T_REVIEW.fields()).from(TReview.T_REVIEW).getQuery();
        addJoin(query);
        setQueryParam(query, memberId, goodsId, type, isShow);
        return super.findPage(query, pageable);
    }

    /**
     * 查找评论数量
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param type
     *            类型
     * @param isShow
     *            是否显示
     * @return 评论数量
     */
    public Long count(Long memberId, Long goodsId, com.vjshop.entity.TReview.Type type, Boolean isShow){
        SelectQuery query = getDslContext().selectCount()
                .from(TReview.T_REVIEW)
                .getQuery();
        query = setQueryParam(query, memberId, goodsId, type, isShow);
        return (Long) query.fetchOne(0, Long.class);
    }

    /**
     * 计算货品总评分
     *
     * @param goodsId
     *            货品ID
     * @return 货品总评分，仅计算显示评论
     */
    public Long calculateTotalScore(Long goodsId){
        if (goodsId == null){
            return 0L;
        }
        return getDslContext().select(TReview.T_REVIEW.SCORE.sum())
                .from(TReview.T_REVIEW)
                .where(TReview.T_REVIEW.GOODS.eq(goodsId)
                        .and(TReview.T_REVIEW.IS_SHOW.eq(true)))
                .fetchOne(0, Long.class);
    }

    /**
     * 计算货品评分次数
     *
     * @param goodsId
     *            货品ID
     * @return 货品评分次数，仅计算显示评论
     */
    public Long calculateScoreCount(Long goodsId){
        if (goodsId == null){
            return 0L;
        }
        return getDslContext().selectCount()
                .from(TReview.T_REVIEW)
                .where(TReview.T_REVIEW.GOODS.eq(goodsId)
                        .and(TReview.T_REVIEW.IS_SHOW.eq(true)))
                .fetchOne(0, Long.class);
    }

    /**
     * 设置相关查询条件
     *
     * @param query
     *            查询条件
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param type
     *            类型
     * @param isShow
     *            是否显示
     * @return 查询条件
     */
    private SelectQuery setQueryParam(SelectQuery query, Long memberId, Long goodsId, com.vjshop.entity.TReview.Type type, Boolean isShow){
        if (memberId != null){
            query.addConditions(TReview.T_REVIEW.MEMBER.eq(memberId));
        }
        if (goodsId != null){
            query.addConditions(TReview.T_REVIEW.GOODS.eq(goodsId));
        }
        if (type != null) {
            switch (type) {
                case positive:
                    query.addConditions(TReview.T_REVIEW.SCORE.ge(4));
                    break;
                case moderate:
                    query.addConditions(TReview.T_REVIEW.SCORE.eq(3));
                    break;
                case negative:
                    query.addConditions(TReview.T_REVIEW.SCORE.le(2));
                    break;
            }
        }
        if (isShow != null){
            query.addConditions(TReview.T_REVIEW.IS_SHOW.eq(isShow));
        }
        return query;
    }

    private void addJoin(SelectQuery query){
        query.addSelect(TGoods.T_GOODS.as("goodsInfo").ID.as("goodsInfo.id"), TGoods.T_GOODS.as("goodsInfo").NAME.as("goodsInfo.name"),
                TGoods.T_GOODS.as("goodsInfo").PRODUCT_IMAGES.as("goodsInfo.productImages"));
        query.addSelect(TMember.T_MEMBER.as("memberInfo").ID.as("memberInfo.id"), TMember.T_MEMBER.as("memberInfo").USERNAME.as("memberInfo.username"));

        query.addJoin(TGoods.T_GOODS.as("goodsInfo"), JoinType.LEFT_OUTER_JOIN, TGoods.T_GOODS.as("goodsInfo").ID.eq(TReview.T_REVIEW.GOODS));
        query.addJoin(TMember.T_MEMBER.as("memberInfo"), JoinType.LEFT_OUTER_JOIN, TMember.T_MEMBER.as("memberInfo").ID.eq(TReview.T_REVIEW.MEMBER));
    }

}
