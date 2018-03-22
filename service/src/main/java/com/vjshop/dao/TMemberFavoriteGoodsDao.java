
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TMemberFavoriteGoods;
import com.vjshop.generated.db.tables.records.TMemberFavoriteGoodsRecord;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO - 会员-收藏商品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TMemberFavoriteGoodsDao extends JooqBaseDao<TMemberFavoriteGoodsRecord, com.vjshop.entity.TMemberFavoriteGoods, Record2<Long, Long>> {

    public TMemberFavoriteGoodsDao() {
        super(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS, com.vjshop.entity.TMemberFavoriteGoods.class);
    }

    @Autowired
    public TMemberFavoriteGoodsDao(Configuration configuration) {
        super(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS, com.vjshop.entity.TMemberFavoriteGoods.class, configuration);
    }

    @Override
    protected Record2<Long, Long> getId(com.vjshop.entity.TMemberFavoriteGoods object) {
        return compositeKeyRecord(object.getFavoriteMembers(), object.getFavoriteGoods());
    }

    public List<com.vjshop.entity.TMemberFavoriteGoods> fetchByFavoriteMembers(Long... memberId) {
        return fetch(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS, memberId);
    }

    public List<com.vjshop.entity.TMemberFavoriteGoods> fetchByFavoriteGoods(Long... goodsId) {
        return fetch(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS, goodsId);
    }

    /**
     * 查找收藏数量
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            货品ID
     * @return 收藏数量
     */
    public Long count(Long memberId, Long goodsId){
        SelectQuery query = getDslContext().selectCount()
                .from(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS)
                .getQuery();
        if (memberId != null){
            query.addConditions(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS.eq(memberId));
        }
        if (goodsId != null){
            query.addConditions(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS.eq(goodsId));
        }
        return (Long) query.fetchOne(0, Long.class);
    }

    /**
     * 删除收藏
     *
     * @param memberId
     *            会员ID
     * @param goodsId
     *            商品ID
     * @return 影响数量
     */
    public int delete(Long memberId, Long goodsId){
        List<Condition> conditions = new ArrayList<Condition>();
        if (memberId != null) {
            conditions.add(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS.eq(memberId));
        }
        if (goodsId != null){
            conditions.add(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS.eq(goodsId));
        }
        return getDslContext().deleteFrom(TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS)
                .where(conditions).execute();
    }
}
