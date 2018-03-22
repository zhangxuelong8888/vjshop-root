
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TDepositLog;
import com.vjshop.generated.db.tables.TMember;
import com.vjshop.generated.db.tables.records.TDepositLogRecord;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao 预存款记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TDepositLogDao extends JooqBaseDao<TDepositLogRecord, com.vjshop.entity.TDepositLog, Long> {

    public TDepositLogDao() {
        super(TDepositLog.T_DEPOSIT_LOG, com.vjshop.entity.TDepositLog.class);
    }

    @Autowired
    public TDepositLogDao(Configuration configuration) {
        super(TDepositLog.T_DEPOSIT_LOG, com.vjshop.entity.TDepositLog.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TDepositLog object) {
        return object.getId();
    }

    /**
     * 获取预存款
     *
     * @param pageable
     *            分页信息
     * @return
     *          预存款
     */
    public Page<com.vjshop.entity.TDepositLog> findPage(Long memberId, Pageable pageable) {
        SelectQuery query = getDslContext().select(TDepositLog.T_DEPOSIT_LOG.fields())
                .from(TDepositLog.T_DEPOSIT_LOG).getQuery();

        TMember memberTable = TMember.T_MEMBER.as("memberInfo");

        query.addSelect(memberTable.ID.as("memberInfo.id"), memberTable.USERNAME.as("memberInfo.username"),
                memberTable.NICKNAME.as("memberInfo.nickname"));
        query.addJoin(memberTable, JoinType.LEFT_OUTER_JOIN, memberTable.ID.eq(TDepositLog.T_DEPOSIT_LOG.MEMBER));

        if (memberId != null){
            query.addConditions(TDepositLog.T_DEPOSIT_LOG.MEMBER.eq(memberId));
        }
        return super.findPage(query, pageable);
    }

}
