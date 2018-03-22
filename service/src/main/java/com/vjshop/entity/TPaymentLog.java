
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 支付记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TPaymentLog implements Serializable {

    private static final long serialVersionUID = 449911818;

    /**
     * 类型
     */
    public enum Type {

        /** 预存款充值 */
        recharge,

        /** 订单支付 */
        payment
    }

    /**
     * 状态
     */
    public enum Status {

        /** 等待支付 */
        wait,

        /** 支付成功 */
        success,

        /** 支付失败 */
        failure
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private BigDecimal amount;
    private BigDecimal fee;
    private String     paymentPluginId;
    private String     paymentPluginName;
    private String     sn;
    private Integer    status;
    private Integer    type;
    private Long       member;
    private Long       orders;

    /** 会员 */
    private TMember memberObj;

    /** 订单 */
    private TOrder orderObj;

    public TPaymentLog() {}

    public TPaymentLog(TPaymentLog value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.amount = value.amount;
        this.fee = value.fee;
        this.paymentPluginId = value.paymentPluginId;
        this.paymentPluginName = value.paymentPluginName;
        this.sn = value.sn;
        this.status = value.status;
        this.type = value.type;
        this.member = value.member;
        this.orders = value.orders;
    }

    public TPaymentLog(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        BigDecimal amount,
        BigDecimal fee,
        String     paymentPluginId,
        String     paymentPluginName,
        String     sn,
        Integer    status,
        Integer    type,
        Long       member,
        Long       orders
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.amount = amount;
        this.fee = fee;
        this.paymentPluginId = paymentPluginId;
        this.paymentPluginName = paymentPluginName;
        this.sn = sn;
        this.status = status;
        this.type = type;
        this.member = member;
        this.orders = orders;
    }

    /**
     * 获取有效金额
     *
     * @return 有效金额
     */
    public BigDecimal getEffectiveAmount() {
        BigDecimal effectiveAmount = getAmount().subtract(getFee());
        return effectiveAmount.compareTo(BigDecimal.ZERO) >= 0 ? effectiveAmount : BigDecimal.ZERO;
    }

    public TMember getMemberObj() {
        return memberObj;
    }

    public void setMemberObj(TMember memberObj) {
        this.memberObj = memberObj;
    }

    public TOrder getOrderObj() {
        return orderObj;
    }

    public void setOrderObj(TOrder orderObj) {
        this.orderObj = orderObj;
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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getPaymentPluginId() {
        return this.paymentPluginId;
    }

    public void setPaymentPluginId(String paymentPluginId) {
        this.paymentPluginId = paymentPluginId;
    }

    public String getPaymentPluginName() {
        return this.paymentPluginName;
    }

    public void setPaymentPluginName(String paymentPluginName) {
        this.paymentPluginName = paymentPluginName;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getOrders() {
        return this.orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPaymentLog (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(amount);
        sb.append(", ").append(fee);
        sb.append(", ").append(paymentPluginId);
        sb.append(", ").append(paymentPluginName);
        sb.append(", ").append(sn);
        sb.append(", ").append(status);
        sb.append(", ").append(type);
        sb.append(", ").append(member);
        sb.append(", ").append(orders);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TPaymentLog that = (TPaymentLog) o;

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
