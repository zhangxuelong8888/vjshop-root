
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TPromotionMemberRank;
import com.vjshop.generated.db.tables.records.TPromotionMemberRankRecord;
import org.jooq.Configuration;
import org.jooq.Record2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * Entity - 促销-会员等级 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TPromotionMemberRankDao extends JooqBaseDao<TPromotionMemberRankRecord, com.vjshop.entity.TPromotionMemberRank, Record2<Long, Long>> {

    public TPromotionMemberRankDao() {
        super(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK, com.vjshop.entity.TPromotionMemberRank.class);
    }

    @Autowired
    public TPromotionMemberRankDao(Configuration configuration) {
        super(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK, com.vjshop.entity.TPromotionMemberRank.class, configuration);
    }

    @Override
    protected Record2<Long, Long> getId(com.vjshop.entity.TPromotionMemberRank object) {
        return compositeKeyRecord(object.getPromotions(), object.getMemberRanks());
    }

    /**
     * 获取关联
     *
     * @param promotionId
     *            促销ID
     * @return 关联
     */
    public List<com.vjshop.entity.TPromotionMemberRank> find(Long promotionId) {
        ResultSet rs = getDslContext().selectFrom(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK)
                .where(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.PROMOTIONS.eq(promotionId)).fetchResultSet();
        return resultSet2List(rs);
    }

    /**
     * 删除关联
     *
     * @param promotionId
     *            促销ID
     * @return 影响条数
     */
    public int delete(Long promotionId){
        return getDslContext().deleteFrom(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK)
                .where(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.PROMOTIONS.eq(promotionId))
                .execute();
    }

    /**
     * 删除关联
     *
     * @param promotionIds
     *            促销ID
     * @return 影响条数
     */
    public int delete(Long... promotionIds){
        return getDslContext().deleteFrom(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK)
                .where(TPromotionMemberRank.T_PROMOTION_MEMBER_RANK.PROMOTIONS.in(promotionIds))
                .execute();
    }

}
