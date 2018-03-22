
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 会员等级
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TMemberRank implements Serializable {

    private static final long serialVersionUID = -234595659;

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private BigDecimal amount;
    private Boolean    isDefault;
    private Boolean    isSpecial;
    private String     name;
    private Double     scale;

    public TMemberRank() {}

    public TMemberRank(TMemberRank value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.amount = value.amount;
        this.isDefault = value.isDefault;
        this.isSpecial = value.isSpecial;
        this.name = value.name;
        this.scale = value.scale;
    }

    public TMemberRank(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        BigDecimal amount,
        Boolean    isDefault,
        Boolean    isSpecial,
        String     name,
        Double     scale
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.amount = amount;
        this.isDefault = isDefault;
        this.isSpecial = isSpecial;
        this.name = name;
        this.scale = scale;
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
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @NotNull
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @NotNull
    public Boolean getIsSpecial() {
        return this.isSpecial;
    }

    public void setIsSpecial(Boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 3, fraction = 3)
    public Double getScale() {
        return this.scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMemberRank (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(amount);
        sb.append(", ").append(isDefault);
        sb.append(", ").append(isSpecial);
        sb.append(", ").append(name);
        sb.append(", ").append(scale);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TMemberRank that = (TMemberRank) o;

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
