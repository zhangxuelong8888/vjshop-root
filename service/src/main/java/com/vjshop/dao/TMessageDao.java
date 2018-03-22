
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMessage;
import com.vjshop.generated.db.tables.records.TMessageRecord;
import java.util.List;

import com.vjshop.util.JooqUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TMember.T_MEMBER;
import static com.vjshop.generated.db.tables.TMessage.T_MESSAGE;

/**
 * Dao - 消息
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TMessageDao extends JooqBaseDao<TMessageRecord, com.vjshop.entity.TMessage, Long> {

    public TMessageDao() {
        super(T_MESSAGE, com.vjshop.entity.TMessage.class);
    }

    @Autowired
    public TMessageDao(Configuration configuration) {
        super(T_MESSAGE, com.vjshop.entity.TMessage.class, configuration);
    }

    private SelectQuery<Record> commonQuery() {
        com.vjshop.generated.db.tables.TMember member1 = T_MEMBER.as("t1");
        com.vjshop.generated.db.tables.TMember member2 = T_MEMBER.as("t2");

        Field<?>[] fields = T_MESSAGE.fields();
        Field<?>[] senderFields = JooqUtils.getFields(member1, "senders");
        Field<?>[] receiverFields = JooqUtils.getFields(member2, "receivers");
        Field<?>[] mergeFields = (Field<?>[]) ArrayUtils.addAll(fields, senderFields);
        Field<?>[] allFields = (Field<?>[]) ArrayUtils.addAll(mergeFields, receiverFields);

        SelectQuery<Record> query = getDslContext().select(allFields).from(T_MESSAGE)
                .leftJoin(member1).on(T_MESSAGE.SENDER.eq(member1.ID))
                .leftJoin(member2).on(T_MESSAGE.RECEIVER.eq(member2.ID)).getQuery();
        return query;
    }

    /**
     * 查找消息分页
     *
     * @param memberId
     *            会员ID，null表示管理员
     * @param pageable
     *            分页信息
     * @return 消息分页
     */
    public Page<TMessage> findPage(Long memberId, Pageable pageable) {
        SelectQuery<Record> query = commonQuery();
        Condition condition = T_MESSAGE.FOR_MESSAGE.isNull().and(T_MESSAGE.IS_DRAFT.eq(false));

        if (memberId != null) {
            condition = condition.and(T_MESSAGE.SENDER.eq(memberId).and(T_MESSAGE.SENDER_DELETE.eq(false)).or(T_MESSAGE.RECEIVER.eq(memberId).and(T_MESSAGE.RECEIVER_DELETE.eq(false))));
        } else {
            condition = condition.and(T_MESSAGE.SENDER.isNull().and(T_MESSAGE.SENDER_DELETE.eq(false)).or(T_MESSAGE.RECEIVER.isNull().and(T_MESSAGE.RECEIVER_DELETE.eq(false))));
        }
        query.addConditions(condition);
        return super.findPage(query, pageable);
    }

    /**
     * 查找草稿分页
     *
     * @param senderId
     *            发件人ID，null表示管理员
     * @param pageable
     *            分页信息
     * @return 草稿分页
     */
    public Page<TMessage> findDraftPage(Long senderId, Pageable pageable) {
        SelectQuery<Record> query = commonQuery();
        Condition condition = T_MESSAGE.FOR_MESSAGE.isNull().and(T_MESSAGE.IS_DRAFT.eq(true));
        if (senderId != null) {
            condition = condition.and(T_MESSAGE.SENDER.eq(senderId));
        } else {
            condition = condition.and(T_MESSAGE.SENDER.isNull());
        }
        query.addConditions(condition);
        return super.findPage(query, pageable);
    }

    /**
     * 查找消息数量
     *
     * @param memberId
     *            会员ID，null表示管理员
     * @param read
     *            是否已读
     * @return 消息数量，不包含草稿
     */
    public Long count(Long memberId, Boolean read) {
        SelectQuery<Record1<Integer>> query = getDslContext().selectCount().from(T_MESSAGE)
                .where(T_MESSAGE.FOR_MESSAGE.isNull().and(T_MESSAGE.IS_DRAFT.eq(false))).getQuery();
        if (memberId != null) {
            if (read != null) {
                query.addConditions(T_MESSAGE.SENDER.eq(memberId).and(T_MESSAGE.SENDER_DELETE.eq(false)).and(T_MESSAGE.SENDER_READ.eq(read))
                        .or(T_MESSAGE.RECEIVER.eq(memberId).and(T_MESSAGE.RECEIVER_DELETE.eq(false)).and(T_MESSAGE.RECEIVER_READ.eq(read))));
            } else {
                query.addConditions(T_MESSAGE.SENDER.eq(memberId).and(T_MESSAGE.SENDER_DELETE.eq(false))
                        .or(T_MESSAGE.RECEIVER.eq(memberId).and(T_MESSAGE.RECEIVER_DELETE.eq(false))));
            }
        } else {
            if (read != null) {
                query.addConditions(T_MESSAGE.SENDER.isNull().and(T_MESSAGE.SENDER_DELETE.eq(false)).and(T_MESSAGE.SENDER_READ.eq(read))
                        .or(T_MESSAGE.RECEIVER.isNull().and(T_MESSAGE.RECEIVER_DELETE.eq(false)).and(T_MESSAGE.RECEIVER_READ.eq(read))));
            } else {
                query.addConditions(T_MESSAGE.SENDER.isNull().and(T_MESSAGE.SENDER_DELETE.eq(false))
                        .or(T_MESSAGE.RECEIVER.isNull().and(T_MESSAGE.RECEIVER_DELETE.eq(false))));
            }
        }
        return query.fetchOne(0, Long.class).longValue();
    }

    /**
     * 删除消息
     *
     * @param id
     *            ID
     * @param memberId
     *            执行人ID，null表示管理员
     */
    public void remove(Long id, Long memberId) {
        TMessage message = super.find(id);
        if (message == null || message.getForMessage() != null) {
            return;
        }
        if ((memberId != null && memberId.equals(message.getReceiver())) || (memberId == null && message.getReceiver() == null)) {
            if (!message.getIsDraft()) {
                if (message.getSenderDelete()) {
                    this.delete(message);
                } else {
                    message.setReceiverDelete(true);
                    super.updateSelective(message);
                }
            }
        } else if ((memberId != null && memberId.equals(message.getSender())) || (memberId == null && message.getSender() == null)) {
            if (message.getIsDraft()) {
                this.delete(message);
            } else {
                if (message.getReceiverDelete()) {
                    this.delete(message);
                } else {
                    message.setSenderDelete(true);
                    super.updateSelective(message);
                }
            }
        }
    }

    public void delete(TMessage message) {
        //先删除回复消息
        getDslContext().deleteFrom(T_MESSAGE).where(T_MESSAGE.FOR_MESSAGE.eq(message.getId())).execute();
        //再删除当前消息
        deleteById(message.getId());
    }

    public List<com.vjshop.entity.TMessage> fetchByForMessage(Long values) {
        SelectQuery<Record> query = commonQuery();
        query.addConditions(T_MESSAGE.FOR_MESSAGE.eq(values));
        query.addOrderBy(T_MESSAGE.MODIFY_DATE.asc());
        return resultSet2List(query.fetchResultSet());
    }

    @Override
    protected Long getId(com.vjshop.entity.TMessage object) {
        return object.getId();
    }

}
