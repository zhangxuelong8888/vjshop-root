
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 预存款记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TDepositLog implements Serializable {

    private static final long serialVersionUID = 816852245;

    /**
     * 类型
     */
    public enum Type {

        /** 预存款充值 */
        recharge,

        /** 预存款调整 */
        adjustment,

        /** 订单支付 */
        payment,

        /** 订单退款 */
        refunds
    }

    private TMember memberInfo;

    /**
     * 获取类型枚举
     *
     * @return
     *          类型枚举
     */
    public Type getEnumType(){
        return this.type == null ? null : Type.values()[this.type];
    }

    public TMember getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(TMember memberInfo) {
        this.memberInfo = memberInfo;
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private BigDecimal balance;
    private BigDecimal credit;
    private BigDecimal debit;
    private String     memo;
    private String     operator;
    private Integer    type;
    private Long       member;

    public TDepositLog() {}

    public TDepositLog(TDepositLog value) {
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

    public TDepositLog(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        BigDecimal balance,
        BigDecimal credit,
        BigDecimal debit,
        String     memo,
        String     operator,
        Integer    type,
        Long       member
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

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCredit() {
        return this.credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public void setDebit(BigDecimal debit) {
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
        StringBuilder sb = new StringBuilder("TDepositLog (");

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

        TDepositLog that = (TDepositLog) o;

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
