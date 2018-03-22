
package com.vjshop.entity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 优惠券
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TCoupon implements Serializable {

    private static final long serialVersionUID = -918844656;

    /**
     * 判断是否已开始
     *
     * @return 是否已开始
     */
    @Transient
    public boolean hasBegun() {
        return getBeginDate() == null || !getBeginDate().after(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 判断是否已过期
     *
     * @return 是否已过期
     */
    @Transient
    public boolean hasExpired() {
        return getEndDate() != null && !getEndDate().after(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 计算优惠价格
     *
     * @param price
     *            商品价格
     * @param quantity
     *            商品数量
     * @return 优惠价格
     */
    @Transient
    public BigDecimal calculatePrice(BigDecimal price, Integer quantity) {
        if (price == null || quantity == null || StringUtils.isEmpty(getPriceExpression())) {
            return price;
        }
        BigDecimal result = BigDecimal.ZERO;
        try {
            Binding binding = new Binding();
            binding.setVariable("quantity", quantity);
            binding.setVariable("price", price);
            GroovyShell groovyShell = new GroovyShell(binding);
            result = new BigDecimal(groovyShell.evaluate(getPriceExpression()).toString());
        } catch (Exception e) {
            return price;
        }
        if (result.compareTo(price) > 0) {
            return price;
        }
        return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Timestamp  beginDate;
    private Timestamp  endDate;
    private String     introduction;
    private Boolean    isEnabled;
    private Boolean    isExchange;
    private BigDecimal maximumPrice;
    private Integer    maximumQuantity;
    private BigDecimal minimumPrice;
    private Integer    minimumQuantity;
    private String     name;
    private Long       point;
    private String     prefix;
    private String     priceExpression;

    public TCoupon() {}

    public TCoupon(TCoupon value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.beginDate = value.beginDate;
        this.endDate = value.endDate;
        this.introduction = value.introduction;
        this.isEnabled = value.isEnabled;
        this.isExchange = value.isExchange;
        this.maximumPrice = value.maximumPrice;
        this.maximumQuantity = value.maximumQuantity;
        this.minimumPrice = value.minimumPrice;
        this.minimumQuantity = value.minimumQuantity;
        this.name = value.name;
        this.point = value.point;
        this.prefix = value.prefix;
        this.priceExpression = value.priceExpression;
    }

    public TCoupon(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            Timestamp beginDate,
            Timestamp endDate,
            String introduction,
            Boolean isEnabled,
            Boolean isExchange,
            BigDecimal maximumPrice,
            Integer maximumQuantity,
            BigDecimal minimumPrice,
            Integer minimumQuantity,
            String name,
            Long point,
            String prefix,
            String priceExpression
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.introduction = introduction;
        this.isEnabled = isEnabled;
        this.isExchange = isExchange;
        this.maximumPrice = maximumPrice;
        this.maximumQuantity = maximumQuantity;
        this.minimumPrice = minimumPrice;
        this.minimumQuantity = minimumQuantity;
        this.name = name;
        this.point = point;
        this.prefix = prefix;
        this.priceExpression = priceExpression;
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

    public Timestamp getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @NotNull
    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @NotNull
    public Boolean getIsExchange() {
        return this.isExchange;
    }

    public void setIsExchange(Boolean isExchange) {
        this.isExchange = isExchange;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getMaximumPrice() {
        return this.maximumPrice;
    }

    public void setMaximumPrice(BigDecimal maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    @Min(0)
    public Integer getMaximumQuantity() {
        return this.maximumQuantity;
    }

    public void setMaximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getMinimumPrice() {
        return this.minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    @Min(0)
    public Integer getMinimumQuantity() {
        return this.minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(0)
    public Long getPoint() {
        return this.point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    @NotEmpty
    @Length(max = 200)
    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPriceExpression() {
        return this.priceExpression;
    }

    public void setPriceExpression(String priceExpression) {
        this.priceExpression = priceExpression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TCoupon (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(beginDate);
        sb.append(", ").append(endDate);
        sb.append(", ").append(introduction);
        sb.append(", ").append(isEnabled);
        sb.append(", ").append(isExchange);
        sb.append(", ").append(maximumPrice);
        sb.append(", ").append(maximumQuantity);
        sb.append(", ").append(minimumPrice);
        sb.append(", ").append(minimumQuantity);
        sb.append(", ").append(name);
        sb.append(", ").append(point);
        sb.append(", ").append(prefix);
        sb.append(", ").append(priceExpression);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TCoupon that = (TCoupon) o;

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
