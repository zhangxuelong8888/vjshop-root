
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity - 运费配置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TFreightConfig implements Serializable {

    private static final long serialVersionUID = 485885337;

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private BigDecimal continuePrice;
    private BigDecimal firstPrice;
    private Long       area;
    private Long       shippingMethod;

    private TArea       shippingMethodArea;

    public TFreightConfig() {}

    public TFreightConfig(TFreightConfig value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.continuePrice = value.continuePrice;
        this.firstPrice = value.firstPrice;
        this.area = value.area;
        this.shippingMethod = value.shippingMethod;
    }

    public TFreightConfig(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        BigDecimal continuePrice,
        BigDecimal firstPrice,
        Long       area,
        Long       shippingMethod
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.continuePrice = continuePrice;
        this.firstPrice = firstPrice;
        this.area = area;
        this.shippingMethod = shippingMethod;
    }

    public TArea getShippingMethodArea() {
        return shippingMethodArea;
    }

    public void setShippingMethodArea(TArea shippingMethodArea) {
        this.shippingMethodArea = shippingMethodArea;
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
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getContinuePrice() {
        return this.continuePrice;
    }

    public void setContinuePrice(BigDecimal continuePrice) {
        this.continuePrice = continuePrice;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getFirstPrice() {
        return this.firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    @NotNull
    public Long getArea() {
        return this.area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    @NotNull(groups = ValidGroup.Save.class)
    public Long getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(Long shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TFreightConfig (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(continuePrice);
        sb.append(", ").append(firstPrice);
        sb.append(", ").append(area);
        sb.append(", ").append(shippingMethod);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TFreightConfig that = (TFreightConfig) o;

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
