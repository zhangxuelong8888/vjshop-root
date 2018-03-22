
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 退款单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TRefunds implements Serializable {

    private static final long serialVersionUID = -2007420061;

    /**
     * 方式
     */
    public enum Method {

        /** 在线支付 */
        online,

        /** 线下支付 */
        offline,

        /** 预存款支付 */
        deposit
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private String     account;
    private BigDecimal amount;
    private String     bank;
    private String     memo;
    private Integer    method;
    private String     operator;
    private String     payee;
    private String     paymentMethod;
    private String     sn;
    private Long       orders;

    private TOrder orderVO;

    public TRefunds() {}

    public TRefunds(TRefunds value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.account = value.account;
        this.amount = value.amount;
        this.bank = value.bank;
        this.memo = value.memo;
        this.method = value.method;
        this.operator = value.operator;
        this.payee = value.payee;
        this.paymentMethod = value.paymentMethod;
        this.sn = value.sn;
        this.orders = value.orders;
    }

    public TRefunds(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     account,
        BigDecimal amount,
        String     bank,
        String     memo,
        Integer    method,
        String     operator,
        String     payee,
        String     paymentMethod,
        String     sn,
        Long       orders
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.account = account;
        this.amount = amount;
        this.bank = bank;
        this.memo = memo;
        this.method = method;
        this.operator = operator;
        this.payee = payee;
        this.paymentMethod = paymentMethod;
        this.sn = sn;
        this.orders = orders;
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

    @Length(max = 200)
    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Length(max = 200)
    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @NotNull
    public Integer getMethod() {
        return this.method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Length(max = 200)
    public String getPayee() {
        return this.payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @NotNull
    public Long getOrders() {
        return this.orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    public TOrder getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(TOrder orderVO) {
        this.orderVO = orderVO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TRefunds (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(account);
        sb.append(", ").append(amount);
        sb.append(", ").append(bank);
        sb.append(", ").append(memo);
        sb.append(", ").append(method);
        sb.append(", ").append(operator);
        sb.append(", ").append(payee);
        sb.append(", ").append(paymentMethod);
        sb.append(", ").append(sn);
        sb.append(", ").append(orders);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TRefunds that = (TRefunds) o;

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

    /**
     * 判断是否为新建对象
     *
     * @return 是否为新建对象
     */
    @Transient
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * 设置支付方式
     *
     * @param paymentMethod
     *            支付方式
     */
    @Transient
    public void setPaymentMethod(TPaymentMethod paymentMethod) {
        setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
    }

    /**
     * 设置操作员
     *
     * @param operator
     *            操作员
     */
    @Transient
    public void setOperator(TAdmin operator) {
        setOperator(operator != null ? operator.getUsername() : null);
    }
}
