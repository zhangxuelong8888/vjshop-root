
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 标签
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TTag implements Serializable {

    private static final long serialVersionUID = 2048804456;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    icon;
    private String    memo;
    private String    name;
    private Integer   type;

    /**
     * 类型
     */
    public enum Type {

        /** 文章标签 */
        article,

        /** 商品标签 */
        goods
    }

    /**
     * 获取类型枚举
     *
     * @return
     *            类型
     */
    public Type getEnumType(){
        return this.type == null ? null : Type.values()[this.type.intValue()];
    }

    public TTag() {}

    public TTag(TTag value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.icon = value.icon;
        this.memo = value.memo;
        this.name = value.name;
        this.type = value.type;
    }

    public TTag(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    icon,
        String    memo,
        String    name,
        Integer   type
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.icon = icon;
        this.memo = memo;
        this.name = name;
        this.type = type;
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

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(groups = ValidGroup.Save.class)
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TTag (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(icon);
        sb.append(", ").append(memo);
        sb.append(", ").append(name);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TTag that = (TTag) o;

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
