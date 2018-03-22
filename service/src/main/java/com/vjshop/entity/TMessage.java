
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 消息
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TMessage implements Serializable {

    private static final long serialVersionUID = -424609946;

    private Long      id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** ip */
    private String ip;

    /** 是否为草稿 */
    private Boolean isDraft;

    /** 发件人已读 */
    private Boolean senderRead;

    /** 收件人已读 */
    private Boolean receiverRead;

    /** 发件人删除 */
    private Boolean senderDelete;

    /** 收件人删除 */
    private Boolean receiverDelete;

    /** 发件人 */
    private Long sender;

    /** 收件人 */
    private Long receiver;

    /** 发件人 */
    private TMember senders;

    /** 收件人 */
    private TMember receivers;

    /** 原消息 */
    private Long      forMessage;

    /** 回复消息 */
    private Set<TMessage> replyMessages = new HashSet<TMessage>();

    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;



    public TMessage() {}

    public TMessage(TMessage value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.content = value.content;
        this.ip = value.ip;
        this.isDraft = value.isDraft;
        this.receiverDelete = value.receiverDelete;
        this.receiverRead = value.receiverRead;
        this.senderDelete = value.senderDelete;
        this.senderRead = value.senderRead;
        this.title = value.title;
        this.forMessage = value.forMessage;
        this.receiver = value.receiver;
        this.sender = value.sender;
    }

    public TMessage(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    content,
        String    ip,
        Boolean   isDraft,
        Boolean   receiverDelete,
        Boolean   receiverRead,
        Boolean   senderDelete,
        Boolean   senderRead,
        String    title,
        Long      forMessage,
        Long      receiver,
        Long      sender
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.content = content;
        this.ip = ip;
        this.isDraft = isDraft;
        this.receiverDelete = receiverDelete;
        this.receiverRead = receiverRead;
        this.senderDelete = senderDelete;
        this.senderRead = senderRead;
        this.title = title;
        this.forMessage = forMessage;
        this.receiver = receiver;
        this.sender = sender;
    }

    public TMember getSenders() {
        return senders;
    }

    public void setSenders(TMember senders) {
        this.senders = senders;
    }

    public TMember getReceivers() {
        return receivers;
    }

    public void setReceivers(TMember receivers) {
        this.receivers = receivers;
    }

    public Set<TMessage> getReplyMessages() {
        return replyMessages;
    }

    public void setReplyMessages(Set<TMessage> replyMessages) {
        this.replyMessages = replyMessages;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @NotEmpty
    @Length(max = 4000)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getReceiverDelete() {
        return this.receiverDelete;
    }

    public void setReceiverDelete(Boolean receiverDelete) {
        this.receiverDelete = receiverDelete;
    }

    public Boolean getReceiverRead() {
        return this.receiverRead;
    }

    public void setReceiverRead(Boolean receiverRead) {
        this.receiverRead = receiverRead;
    }

    public Boolean getSenderDelete() {
        return this.senderDelete;
    }

    public void setSenderDelete(Boolean senderDelete) {
        this.senderDelete = senderDelete;
    }

    public Boolean getSenderRead() {
        return this.senderRead;
    }

    public void setSenderRead(Boolean senderRead) {
        this.senderRead = senderRead;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getForMessage() {
        return this.forMessage;
    }

    public void setForMessage(Long forMessage) {
        this.forMessage = forMessage;
    }

    public Long getReceiver() {
        return this.receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getSender() {
        return this.sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMessage (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(content);
        sb.append(", ").append(ip);
        sb.append(", ").append(isDraft);
        sb.append(", ").append(receiverDelete);
        sb.append(", ").append(receiverRead);
        sb.append(", ").append(senderDelete);
        sb.append(", ").append(senderRead);
        sb.append(", ").append(title);
        sb.append(", ").append(forMessage);
        sb.append(", ").append(receiver);
        sb.append(", ").append(sender);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TMessage that = (TMessage) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
