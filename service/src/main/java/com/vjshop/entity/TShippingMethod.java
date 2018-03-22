
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity - 配送方式
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TShippingMethod implements Serializable {

    private static final long serialVersionUID = -943294898;

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Integer    orders;
    private Integer    continueWeight;
    private BigDecimal defaultContinuePrice;
    private BigDecimal defaultFirstPrice;
    private String     description;
    private Integer    firstWeight;
    private String     icon;
    private String     name;
    private Long       defaultDeliveryCorp;

    /** 支持支付方式 */
    private Set<TPaymentMethod> paymentMethods = new HashSet<TPaymentMethod>();

    /** 运费配置 */
    private Set<TFreightConfig> freightConfigs = new HashSet<TFreightConfig>();

    public TShippingMethod() {}

    public TShippingMethod(TShippingMethod value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.continueWeight = value.continueWeight;
        this.defaultContinuePrice = value.defaultContinuePrice;
        this.defaultFirstPrice = value.defaultFirstPrice;
        this.description = value.description;
        this.firstWeight = value.firstWeight;
        this.icon = value.icon;
        this.name = value.name;
        this.defaultDeliveryCorp = value.defaultDeliveryCorp;
    }

    public TShippingMethod(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        Integer    orders,
        Integer    continueWeight,
        BigDecimal defaultContinuePrice,
        BigDecimal defaultFirstPrice,
        String     description,
        Integer    firstWeight,
        String     icon,
        String     name,
        Long       defaultDeliveryCorp
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.continueWeight = continueWeight;
        this.defaultContinuePrice = defaultContinuePrice;
        this.defaultFirstPrice = defaultFirstPrice;
        this.description = description;
        this.firstWeight = firstWeight;
        this.icon = icon;
        this.name = name;
        this.defaultDeliveryCorp = defaultDeliveryCorp;
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

    @Min(0)
    public Integer getOrders() {
        return this.orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    /**
     * 获取续重量
     *
     * @return 续重量
     */
    @NotNull
    @Min(1)
    public Integer getContinueWeight() {
        return this.continueWeight;
    }

    public void setContinueWeight(Integer continueWeight) {
        this.continueWeight = continueWeight;
    }

    /**
     * 获取默认续重价格
     *
     * @return 默认续重价格
     */
    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getDefaultContinuePrice() {
        return this.defaultContinuePrice;
    }

    public void setDefaultContinuePrice(BigDecimal defaultContinuePrice) {
        this.defaultContinuePrice = defaultContinuePrice;
    }

    /**
     * 获取默认首重价格
     *
     * @return 默认首重价格
     */
    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getDefaultFirstPrice() {
        return this.defaultFirstPrice;
    }

    public void setDefaultFirstPrice(BigDecimal defaultFirstPrice) {
        this.defaultFirstPrice = defaultFirstPrice;
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
     * 获取首重量
     *
     * @return 首重量
     */
    @NotNull
    @Min(0)
    public Integer getFirstWeight() {
        return this.firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
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

    public Long getDefaultDeliveryCorp() {
        return this.defaultDeliveryCorp;
    }

    public void setDefaultDeliveryCorp(Long defaultDeliveryCorp) {
        this.defaultDeliveryCorp = defaultDeliveryCorp;
    }

    /**
     * 获取支持支付方式
     *
     * @return 支持支付方式
     */
    public Set<TPaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * 设置支持支付方式
     *
     * @param paymentMethods
     *            支持支付方式
     */
    public void setPaymentMethods(Set<TPaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    /**
     * 获取运费配置
     *
     * @return 运费配置
     */
    public Set<TFreightConfig> getFreightConfigs() {
        return freightConfigs;
    }

    /**
     * 设置运费配置
     *
     * @param freightConfigs
     *            运费配置
     */
    public void setFreightConfigs(Set<TFreightConfig> freightConfigs) {
        this.freightConfigs = freightConfigs;
    }

    /**
     * 判断是否支持支付方式
     *
     * @param paymentMethodId
     *            支付方式ID
     * @return 是否支持支付方式
     */
    @Transient
    public boolean isSupported(Long paymentMethodId) {
        if (paymentMethodId == null || CollectionUtils.isEmpty(getPaymentMethods())){
            return false;
        }
        for (TPaymentMethod paymentMethod : getPaymentMethods()){
            if (paymentMethodId.equals(paymentMethod.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取运费配置
     *
     * @param area
     *            地区
     * @return 运费配置
     */
    @Transient
    public TFreightConfig getFreightConfig(TArea area) {
        if (area == null || CollectionUtils.isEmpty(getFreightConfigs())) {
            return null;
        }

        for (TFreightConfig freightConfig : getFreightConfigs()) {
            if (freightConfig.getArea() != null && freightConfig.getShippingMethodArea().equals(area)) {
                return freightConfig;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TShippingMethod (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(continueWeight);
        sb.append(", ").append(defaultContinuePrice);
        sb.append(", ").append(defaultFirstPrice);
        sb.append(", ").append(description);
        sb.append(", ").append(firstWeight);
        sb.append(", ").append(icon);
        sb.append(", ").append(name);
        sb.append(", ").append(defaultDeliveryCorp);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TShippingMethod that = (TShippingMethod) o;

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
