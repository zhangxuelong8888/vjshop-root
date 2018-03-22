package com.vjshop.dao;

import com.vjshop.generated.db.tables.TMemberRank;
import com.vjshop.generated.db.tables.records.TMemberRankRecord;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.vjshop.generated.db.tables.TMemberRank.T_MEMBER_RANK;


/**
 * DAO - 会员等级
 */
@Repository
public class TMemberRankDao extends JooqBaseDao<TMemberRankRecord, com.vjshop.entity.TMemberRank, Long> {

    public TMemberRankDao() {
        super(T_MEMBER_RANK, com.vjshop.entity.TMemberRank.class);
    }

    @Autowired
    public TMemberRankDao(Configuration configuration) {
        super(T_MEMBER_RANK, com.vjshop.entity.TMemberRank.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TMemberRank object) {
        return object.getId();
    }

    /**
     * 查找实体对象
     *
     * @param id
     * @return 实体对象，若不存在则返回null
     */
    public com.vjshop.entity.TMemberRank find(Long id) {
        return fetchOne(T_MEMBER_RANK.ID, id);
    }

    /**
     * 判断名称是否存在
     *
     * @param name 名称(忽略大小写)
     * @return 名称是否存在
     */
    public boolean nameExists(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return getDslContext().fetchExists(T_MEMBER_RANK, T_MEMBER_RANK.NAME.eq(name));
    }

    /**
     * 判断消费金额是否存在
     *
     * @param amount 消费金额
     * @return 消费金额是否存在
     */
    public boolean amountExists(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        return getDslContext().fetchExists(T_MEMBER_RANK, T_MEMBER_RANK.AMOUNT.eq(amount));
    }

    /**
     * 查找默认会员等级
     *
     * @return 默认会员等级，若不存在则返回null
     */
    public com.vjshop.entity.TMemberRank findDefault() {
        return fetchOne(T_MEMBER_RANK.IS_DEFAULT, true);
    }

    /**
     * 根据消费金额查找符合此条件的最高会员等级
     *
     * @param amount 消费金额
     * @return 会员等级，不包含特殊会员等级，若不存在则返回null
     */
    public com.vjshop.entity.TMemberRank findByAmount(BigDecimal amount) {
        TMemberRankRecord record = getDslContext().selectFrom(T_MEMBER_RANK)
                .where(T_MEMBER_RANK.IS_SPECIAL.eq(false), T_MEMBER_RANK.AMOUNT.lessOrEqual(amount))
                .orderBy(T_MEMBER_RANK.AMOUNT.desc())
                .limit(1)
                .fetchOne();
        return this.record2Pojo(record);
    }

    /**
     * 设置默认会员等级
     *
     * @param id 会员等级ID
     */
    public void setDefault(Long id) {
        if (id == null) {//去除所有默认
            return;
        }
        //将默认项设置为非默认
        getDslContext().update(T_MEMBER_RANK)
                .set(T_MEMBER_RANK.IS_DEFAULT, false)
                .where(T_MEMBER_RANK.IS_DEFAULT.eq(true))
                .execute();
        //将当前项设置为默认
        getDslContext().update(T_MEMBER_RANK)
                .set(T_MEMBER_RANK.IS_DEFAULT, true)
                .where(T_MEMBER_RANK.ID.eq(id))
                .execute();
    }
}
