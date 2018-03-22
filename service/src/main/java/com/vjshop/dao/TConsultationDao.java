
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TConsultation;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TMember;
import com.vjshop.generated.db.tables.records.TConsultationRecord;
import com.vjshop.util.JooqUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 咨询
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TConsultationDao extends JooqBaseDao<TConsultationRecord, com.vjshop.entity.TConsultation, Long> {

    public TConsultationDao() {
        super(TConsultation.T_CONSULTATION, com.vjshop.entity.TConsultation.class);
    }

    @Autowired
    public TConsultationDao(Configuration configuration) {
        super(TConsultation.T_CONSULTATION, com.vjshop.entity.TConsultation.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TConsultation object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TConsultation> fetchByGoods(Long... values) {
        return fetch(TConsultation.T_CONSULTATION.GOODS, values);
    }

    /**
     * 查找咨询详情
     *
     * @param id
     * 			  咨询ID
     * @return 咨询
     */
    public com.vjshop.entity.TConsultation findDetails(Long id) {
        SelectQuery query = getDslContext().select(TConsultation.T_CONSULTATION.fields())
                .from(TConsultation.T_CONSULTATION)
                .where(TConsultation.T_CONSULTATION.ID.eq(id))
                .getQuery();
        addJoin(query);
        List<com.vjshop.entity.TConsultation> consultationList = resultSet2List(query.fetchResultSet());
        if (CollectionUtils.isNotEmpty(consultationList)){
            return consultationList.get(0);
        }
        return null;
    }

    /**
     * 查找咨询
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isShow
     *            是否显示
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 咨询，不包含咨询回复
     */
    public List<com.vjshop.entity.TConsultation> findList(Long memberId, Long goodsId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders){
        SelectQuery query = getDslContext().select(TConsultation.T_CONSULTATION.fields())
                .from(TConsultation.T_CONSULTATION)
                .getQuery();
        setQueryParam(query, memberId, goodsId, isShow);
        addJoin(query);
        return super.findList(query, null, count, filters, orders);
    }

    /**
     * 查找咨询分页
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isShow
     *            是否显示
     * @param pageable
     *            分页信息
     * @return 咨询分页，不包含咨询回复
     */
    public Page<com.vjshop.entity.TConsultation> findPage(Long memberId, Long goodsId, Boolean isShow, Pageable pageable) {
        SelectQuery query = getDslContext().select(TConsultation.T_CONSULTATION.fields())
                .from(TConsultation.T_CONSULTATION)
                .getQuery();
        setQueryParam(query, memberId, goodsId, isShow);
        addJoin(query);
        return super.findPage(query, pageable);
    }

    /**
     * 查找咨询数量
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @param isShow
     *            是否显示
     * @return 咨询数量，不包含咨询回复
     */
    public Long count(Long memberId, Long goodsId, Boolean isShow){
        SelectQuery query = getDslContext().selectCount()
                .from(TConsultation.T_CONSULTATION)
                .getQuery();
        query = setQueryParam(query, memberId, goodsId, isShow);
        return (Long) query.fetchOne(0, Long.class);
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
     * @param isShow
     *            是否显示
     * @return 查询条件
     */
    private SelectQuery setQueryParam(SelectQuery query, Long memberId, Long goodsId, Boolean isShow){
        query.addConditions(TConsultation.T_CONSULTATION.FOR_CONSULTATION.isNull());
        if (memberId != null){
            query.addConditions(TConsultation.T_CONSULTATION.MEMBER.eq(memberId));
        }
        if (goodsId != null){
            query.addConditions(TConsultation.T_CONSULTATION.GOODS.eq(goodsId));
        }
        if (isShow != null){
            query.addConditions(TConsultation.T_CONSULTATION.IS_SHOW.eq(isShow));
        }
        return query;
    }

    public void addJoin(SelectQuery query){
        query.addSelect(TGoods.T_GOODS.ID.as("goodsInfo.id"), TGoods.T_GOODS.NAME.as("goodsInfo.name"),
                TGoods.T_GOODS.PRODUCT_IMAGES.as("goodsInfo.productImages"));
        query.addSelect(TMember.T_MEMBER.ID.as("memberInfo.id"), TMember.T_MEMBER.USERNAME.as("memberInfo.username"),
                TMember.T_MEMBER.NICKNAME.as("memberInfo.nickname"));
        query.addSelect(TConsultation.T_CONSULTATION.as("fc").ID.as("forConsultationInfo.id"),
                TConsultation.T_CONSULTATION.as("fc").CONTENT.as("forConsultationInfo.content"),
                TConsultation.T_CONSULTATION.as("fc").CREATE_DATE.as("forConsultationInfo.createDate"));
        query.addSelect(TConsultation.T_CONSULTATION.as("rc").ID.as("replyConsultations.id"),
                TConsultation.T_CONSULTATION.as("rc").CONTENT.as("replyConsultations.content"),
                TConsultation.T_CONSULTATION.as("rc").CREATE_DATE.as("replyConsultations.createDate"));

        query.addJoin(TGoods.T_GOODS, JoinType.LEFT_OUTER_JOIN,
                TGoods.T_GOODS.ID.eq(TConsultation.T_CONSULTATION.GOODS));
        query.addJoin(TMember.T_MEMBER, JoinType.LEFT_OUTER_JOIN,
                TMember.T_MEMBER.ID.eq(TConsultation.T_CONSULTATION.MEMBER));
        query.addJoin(TConsultation.T_CONSULTATION.as("fc"), JoinType.LEFT_OUTER_JOIN,
                TConsultation.T_CONSULTATION.as("fc").ID.eq(TConsultation.T_CONSULTATION.FOR_CONSULTATION));
        query.addJoin(TConsultation.T_CONSULTATION.as("rc"), JoinType.LEFT_OUTER_JOIN,
                TConsultation.T_CONSULTATION.as("rc").FOR_CONSULTATION.eq(TConsultation.T_CONSULTATION.ID));
    }

}
