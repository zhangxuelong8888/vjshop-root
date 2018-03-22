
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Entity - 广告位
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TAdPosition implements Serializable {

    private static final long serialVersionUID = -1147759641;

    /** 广告 */
    private Set<TAd> ads = new HashSet<TAd>();

    /**
     * 获取广告
     * @return 广告
     */
    public Set<TAd> getAds() {
        return ads;
    }

    /**
     * 设置广告
     * @param ads 广告
     */
    public void setAds(Set<TAd> ads) {
        if (CollectionUtils.isEmpty(ads)){
            this.ads = null;
            return;
        }
        ads.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TAd that = (TAd) o;
                return null == that.getId();
            }
        });
        this.ads = ads;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    description;
    private Integer   height;
    private String    name;
    private String    template;
    private Integer   width;

    public TAdPosition() {}

    public TAdPosition(TAdPosition value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.description = value.description;
        this.height = value.height;
        this.name = value.name;
        this.template = value.template;
        this.width = value.width;
    }

    public TAdPosition(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            String description,
            Integer height,
            String name,
            String template,
            Integer width
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.description = description;
        this.height = height;
        this.name = name;
        this.template = template;
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
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Min(1)
    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty
    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
        StringBuilder sb = new StringBuilder("TAdPosition (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(description);
        sb.append(", ").append(height);
        sb.append(", ").append(name);
        sb.append(", ").append(template);
        sb.append(", ").append(width);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TAdPosition that = (TAdPosition) o;

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
