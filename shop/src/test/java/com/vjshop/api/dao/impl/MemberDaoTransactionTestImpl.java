package com.vjshop.api.dao.impl;

import com.vjshop.api.dao.IMemberDao;
import com.vjshop.generated.db.Tables;
import com.vjshop.generated.db.tables.records.TMemberRecord;
import org.jooq.Configuration;
import org.jooq.Table;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

/**
 * Created by moss-zc on 2017/5/23.
 *
 * @author yishop term
 * @since 0.1
 */
@Repository
public class MemberDaoTransactionTestImpl extends DAOImpl<TMemberRecord,TMemberRecord, Long> implements IMemberDao {

    public MemberDaoTransactionTestImpl() {
        super(com.vjshop.generated.db.tables.TMember.T_MEMBER, TMemberRecord.class);
    }
    protected MemberDaoTransactionTestImpl(Table<TMemberRecord> table, Class<TMemberRecord> type) {
        super(table, type);
    }

    protected MemberDaoTransactionTestImpl(Table<TMemberRecord> table, Class<TMemberRecord> type, Configuration configuration) {
        super(table, type, configuration);
    }

    @Override
    public TMemberRecord getUserByAccount(String account) {

        com.vjshop.generated.db.tables.TMember example = com.vjshop.generated.db.tables.TMember.T_MEMBER ;
        TMemberRecord tMembers = DSL.using(this.configuration()).selectFrom(Tables.T_MEMBER)
                .where(example.MOBILE.eq(account))
                .fetchOne()
                ;

        return tMembers;
    }

    @Override
    public void update(TMemberRecord tMember) {
        TMemberRecord record = new TMemberRecord();
        record.from(tMember);
        com.vjshop.generated.db.tables.TMember tMember1 = com.vjshop.generated.db.tables.TMember.T_MEMBER;
        tMember1.from(record);
        DSL.using(this.configuration()).update(tMember1).set(record).execute();
    }

    @Override
    public TMemberRecord save(TMemberRecord record) {
        return null;
    }

    @Override
    protected Long getId(TMemberRecord object) {
        return object.getId();
    }

    public TMemberRecord getUserById(long id)
    {
        return null;
    }

}
