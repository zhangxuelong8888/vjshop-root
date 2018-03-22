
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 消息配置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TMessageConfig implements Serializable {

    private static final long serialVersionUID = 1093607147;

    /**
     * 类型
     */
    public enum Type {

        /** 会员注册 */
        registerMember,

        /** 订单创建 */
        createOrder,

        /** 订单更新 */
        updateOrder,

        /** 订单取消 */
        cancelOrder,

        /** 订单审核 */
        reviewOrder,

        /** 订单收款 */
        paymentOrder,

        /** 订单退款 */
        refundsOrder,

        /** 订单发货 */
        shippingOrder,

        /** 订单退货 */
        returnsOrder,

        /** 订单收货 */
        receiveOrder,

        /** 订单完成 */
        completeOrder,

        /** 订单失败 */
        failOrder;

        public static Type valueOf(int ordinal){
            if (ordinal < 0 || ordinal >= values().length) {
                throw null;
            }
            return values()[ordinal];
        }
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Boolean   isMailEnabled;
    private Boolean   isSmsEnabled;
    private Integer   type;

    public TMessageConfig() {}

    public TMessageConfig(TMessageConfig value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.isMailEnabled = value.isMailEnabled;
        this.isSmsEnabled = value.isSmsEnabled;
        this.type = value.type;
    }

    public TMessageConfig(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            Boolean isMailEnabled,
            Boolean isSmsEnabled,
            Integer type
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.isMailEnabled = isMailEnabled;
        this.isSmsEnabled = isSmsEnabled;
        this.type = type;
    }

    public String typeName() {
        return TMessageConfig.Type.valueOf(this.type).name();
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

    @NotNull
    public Boolean getIsMailEnabled() {
        return this.isMailEnabled;
    }

    public void setIsMailEnabled(Boolean isMailEnabled) {
        this.isMailEnabled = isMailEnabled;
    }

    @NotNull
    public Boolean getIsSmsEnabled() {
        return this.isSmsEnabled;
    }

    public void setIsSmsEnabled(Boolean isSmsEnabled) {
        this.isSmsEnabled = isSmsEnabled;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMessageConfig (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(isMailEnabled);
        sb.append(", ").append(isSmsEnabled);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TMessageConfig that = (TMessageConfig) o;

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
