
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 导航
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TNavigation implements Serializable {

    private static final long serialVersionUID = -1868593776;

    /**
     * 位置
     */
    public enum Position {

        /** 顶部 */
        top,

        /** 中间 */
        middle,

        /** 底部 */
        bottom
    }

    /**
     * 获取位置枚举
     *
     * @return
     *          位置
     */
    public Position getEnumPosition(){
        return this.position == null ? null : Position.values()[this.position.intValue()];
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private Boolean   isBlankTarget;
    private String    name;
    private Integer   position;
    private String    url;

    public TNavigation() {}

    public TNavigation(TNavigation value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.isBlankTarget = value.isBlankTarget;
        this.name = value.name;
        this.position = value.position;
        this.url = value.url;
    }

    public TNavigation(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            Integer orders,
            Boolean isBlankTarget,
            String name,
            Integer position,
            String url
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.isBlankTarget = isBlankTarget;
        this.name = name;
        this.position = position;
        this.url = url;
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

    @NotNull
    public Boolean getIsBlankTarget() {
        return this.isBlankTarget;
    }

    public void setIsBlankTarget(Boolean isBlankTarget) {
        this.isBlankTarget = isBlankTarget;
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
    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @NotEmpty
    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|ftp:\\/\\/|mailto:|\\/|#).*$")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TNavigation (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(isBlankTarget);
        sb.append(", ").append(name);
        sb.append(", ").append(position);
        sb.append(", ").append(url);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TNavigation that = (TNavigation) o;

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
