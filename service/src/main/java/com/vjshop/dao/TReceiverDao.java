
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TReceiver;
import com.vjshop.generated.db.tables.records.TReceiverRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Dao - 收货地址
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TReceiverDao extends JooqBaseDao<TReceiverRecord, com.vjshop.entity.TReceiver, Long> {

    public TReceiverDao() {
        super(TReceiver.T_RECEIVER, com.vjshop.entity.TReceiver.class);
    }

    @Autowired
    public TReceiverDao(Configuration configuration) {
        super(TReceiver.T_RECEIVER, com.vjshop.entity.TReceiver.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TReceiver object) {
        return object.getId();
    }

    /**
     * 查找默认收货地址
     *
     * @param memberId
     *            会员ID
     * @return 默认收货地址，若不存在则返回最新收货地址
     */
    public com.vjshop.entity.TReceiver findDefault(Long memberId){
        if (memberId == null) {
            return null;
        }
        TReceiverRecord receiverRecord = getDslContext().selectFrom(TReceiver.T_RECEIVER)
                .where(TReceiver.T_RECEIVER.MEMBER.eq(memberId)
                        .and(TReceiver.T_RECEIVER.IS_DEFAULT.eq(true)))
                .fetchOne();
        com.vjshop.entity.TReceiver receiver = record2Pojo(receiverRecord);
        if (receiver == null){
            receiverRecord = getDslContext().selectFrom(TReceiver.T_RECEIVER)
                    .where(TReceiver.T_RECEIVER.MEMBER.eq(memberId))
                    .orderBy(TReceiver.T_RECEIVER.MODIFY_DATE.desc())
                    .fetchOne();
            receiver = record2Pojo(receiverRecord);
        }
        return receiver;
    }

    /**
     * 查找收货地址
     *
     * @param memberId
     *            会员ID
     * @return 收货地址
     */
    public List<com.vjshop.entity.TReceiver> findList(Long memberId){
        if (memberId == null) {
            return Collections.emptyList();
        }
        return fetch(TReceiver.T_RECEIVER.MEMBER, memberId);
    }

    /**
     * 获取收货地址数量
     *
     * @param memberId
     *            会员ID
     * @return 收货地址数量
     */
    public Long count(Long memberId){
        if (memberId == null) {
            return 0L;
        }
        return count(Filter.eq(TReceiver.T_RECEIVER.MEMBER.getName(), memberId));
    }

    /**
     * 查找收货地址分页
     *
     * @param memberId
     *            会员ID
     * @param pageable
     *            分页信息
     * @return 收货地址分页
     */
    public Page<com.vjshop.entity.TReceiver> findPage(Long memberId, Pageable pageable){
        SelectQuery query = getQuery();
        if (memberId != null){
            query.addConditions(TReceiver.T_RECEIVER.MEMBER.eq(memberId));
        }
        return findPage(query, pageable);
    }

    /**
     * 设置默认收货地址
     *
     * @param receiver
     *            收货地址
     */
    public void setDefault(com.vjshop.entity.TReceiver receiver){
        Assert.notNull(receiver);
        Assert.notNull(receiver.getMember());

        receiver.setIsDefault(true);
        if (receiver.getId() == null){
            getDslContext().update(TReceiver.T_RECEIVER)
                    .set(TReceiver.T_RECEIVER.IS_DEFAULT, false)
                    .where(TReceiver.T_RECEIVER.MEMBER.eq(receiver.getMember())
                            .and(TReceiver.T_RECEIVER.IS_DEFAULT.eq(true)))
                    .execute();
        }else {
            getDslContext().update(TReceiver.T_RECEIVER)
                    .set(TReceiver.T_RECEIVER.IS_DEFAULT, false)
                    .where(TReceiver.T_RECEIVER.MEMBER.eq(receiver.getMember())
                            .and(TReceiver.T_RECEIVER.IS_DEFAULT.eq(true)
                                    .and(TReceiver.T_RECEIVER.ID.ne(receiver.getId()))))
                    .execute();
        }
    }

}
