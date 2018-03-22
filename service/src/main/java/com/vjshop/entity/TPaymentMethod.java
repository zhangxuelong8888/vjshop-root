
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Entity - 支付方式
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TPaymentMethod implements Serializable {

    private static final long serialVersionUID = 797856101;

    /**
     * 类型
     */
    public enum Type {

        /** 款到发货 */
        deliveryAgainstPayment,

        /** 货到付款 */
        cashOnDelivery;

        public static Type valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return null;
            }
            return values()[ordinal];
        }
    }

    /**
     * 方式
     */
    public enum Method {

        /** 在线支付 */
        online,

        /** 线下支付 */
        offline;

        public static Method valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return null;
            }
            return values()[ordinal];
        }
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    content;
    private String    description;
    private String    icon;
    private Integer   method;
    private String    name;
    private Integer   timeout;
    private Integer   type;

    /** 配送方式 */
    private Set<TShippingMethod> shippingMethods = new HashSet<TShippingMethod>();

    public TPaymentMethod() {}

    public TPaymentMethod(TPaymentMethod value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.content = value.content;
        this.description = value.description;
        this.icon = value.icon;
        this.method = value.method;
        this.name = value.name;
        this.timeout = value.timeout;
        this.type = value.type;
    }

    public TPaymentMethod(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    content,
        String    description,
        String    icon,
        Integer   method,
        String    name,
        Integer   timeout,
        Integer   type
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.content = content;
        this.description = description;
        this.icon = icon;
        this.method = method;
        this.name = name;
        this.timeout = timeout;
        this.type = type;
    }

    public String typeName() {
        return Type.valueOf(this.type).name();
    }

    public String methodName() {
        return Method.valueOf(this.method).name();
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

    public Integer getOrders() {
        return this.orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取介绍
     *
     * @return 介绍
     */
    @Length(max = 200)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取图标
     *
     * @return 图标
     */
    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取方式
     *
     * @return 方式
     */
    @NotNull
    public Integer getMethod() {
        return this.method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取超时时间
     *
     * @return 超时时间
     */
    @Min(1)
    public Integer getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @NotNull
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPaymentMethod (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(content);
        sb.append(", ").append(description);
        sb.append(", ").append(icon);
        sb.append(", ").append(method);
        sb.append(", ").append(name);
        sb.append(", ").append(timeout);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TPaymentMethod that = (TPaymentMethod) o;

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

    public Set<TShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(Set<TShippingMethod> shippingMethods) {
        if (CollectionUtils.isEmpty(shippingMethods)){
            this.shippingMethods = null;
            return;
        }
        shippingMethods.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TShippingMethod that = (TShippingMethod) o;
                return null == that.getId();
            }
        });
        this.shippingMethods = shippingMethods;
    }
}
