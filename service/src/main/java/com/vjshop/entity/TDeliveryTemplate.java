
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
 * Entity - 快递单模板
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TDeliveryTemplate implements Serializable {

    private static final long serialVersionUID = 866164320;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    background;
    private String    content;
    private Integer   height;
    private Boolean   isDefault;
    private String    memo;
    private String    name;
    private Integer   offsetx;
    private Integer   offsety;
    private Integer   width;

    public TDeliveryTemplate() {}

    public TDeliveryTemplate(TDeliveryTemplate value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.background = value.background;
        this.content = value.content;
        this.height = value.height;
        this.isDefault = value.isDefault;
        this.memo = value.memo;
        this.name = value.name;
        this.offsetx = value.offsetx;
        this.offsety = value.offsety;
        this.width = value.width;
    }

    public TDeliveryTemplate(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    background,
        String    content,
        Integer   height,
        Boolean   isDefault,
        String    memo,
        String    name,
        Integer   offsetx,
        Integer   offsety,
        Integer   width
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.background = background;
        this.content = content;
        this.height = height;
        this.isDefault = isDefault;
        this.memo = memo;
        this.name = name;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.width = width;
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
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getBackground() {
        return this.background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @NotEmpty
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NotNull
    @Min(1)
    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @NotNull
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    @NotNull
    public Integer getOffsetx() {
        return this.offsetx;
    }

    public void setOffsetx(Integer offsetx) {
        this.offsetx = offsetx;
    }

    @NotNull
    public Integer getOffsety() {
        return this.offsety;
    }

    public void setOffsety(Integer offsety) {
        this.offsety = offsety;
    }

    @NotNull
    @Min(1)
    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TDeliveryTemplate (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(background);
        sb.append(", ").append(content);
        sb.append(", ").append(height);
        sb.append(", ").append(isDefault);
        sb.append(", ").append(memo);
        sb.append(", ").append(name);
        sb.append(", ").append(offsetx);
        sb.append(", ").append(offsety);
        sb.append(", ").append(width);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TDeliveryTemplate that = (TDeliveryTemplate) o;

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
