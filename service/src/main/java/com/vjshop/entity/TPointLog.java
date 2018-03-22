
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 积分记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPointLog implements Serializable {

    private static final long serialVersionUID = -1554839450;

    /**
     * 类型
     */
    public enum Type {

        /** 积分赠送 */
        reward,

        /** 积分兑换 */
        exchange,

        /** 积分兑换撤销 */
        undoExchange,

        /** 积分调整 */
        adjustment
    }
    /** 会员信息 */
    private TMember memberInfo;

    /**
     * 获取类型枚举
     *
     * @return
     *            类型
     */
    public Type getEnumType(){
        return this.type == null ? null : Type.values()[this.type.intValue()];
    }

    public TMember getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(TMember memberInfo) {
        this.memberInfo = memberInfo;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Long      balance;
    private Long      credit;
    private Long      debit;
    private String    memo;
    private String    operator;
    private Integer   type;
    private Long      member;

    public TPointLog() {}

    public TPointLog(TPointLog value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.balance = value.balance;
        this.credit = value.credit;
        this.debit = value.debit;
        this.memo = value.memo;
        this.operator = value.operator;
        this.type = value.type;
        this.member = value.member;
    }

    public TPointLog(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Long      balance,
        Long      credit,
        Long      debit,
        String    memo,
        String    operator,
        Integer   type,
        Long      member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.balance = balance;
        this.credit = credit;
        this.debit = debit;
        this.memo = memo;
        this.operator = operator;
        this.type = type;
        this.member = member;
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

    public Long getBalance() {
        return this.balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getCredit() {
        return this.credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public Long getDebit() {
        return this.debit;
    }

    public void setDebit(Long debit) {
        this.debit = debit;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPointLog (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(balance);
        sb.append(", ").append(credit);
        sb.append(", ").append(debit);
        sb.append(", ").append(memo);
        sb.append(", ").append(operator);
        sb.append(", ").append(type);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TPointLog that = (TPointLog) o;

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
