
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 订单记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TOrderLog implements Serializable {

    private static final long serialVersionUID = 586044155;
    /**
     * 类型
     */
    public enum Type {

        /** 订单创建 */
        create,

        /** 订单更新 */
        update,

        /** 订单取消 */
        cancel,

        /** 订单审核 */
        review,

        /** 订单收款 */
        payment,

        /** 订单退款 */
        refunds,

        /** 订单发货 */
        shipping,

        /** 订单退货 */
        returns,

        /** 订单收货 */
        receive,

        /** 订单完成 */
        complete,

        /** 订单失败 */
        fail
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    content;
    private String    operator;
    private Integer   type;
    private Long      orders;

    private TOrder orderVO;

    public TOrderLog() {}

    public TOrderLog(TOrderLog value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.content = value.content;
        this.operator = value.operator;
        this.type = value.type;
        this.orders = value.orders;
    }

    public TOrderLog(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    content,
        String    operator,
        Integer   type,
        Long      orders
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.content = content;
        this.operator = operator;
        this.type = type;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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
        StringBuilder sb = new StringBuilder("TOrderLog (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(content);
        sb.append(", ").append(operator);
        sb.append(", ").append(type);
        sb.append(", ").append(orders);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TOrderLog that = (TOrderLog) o;

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
