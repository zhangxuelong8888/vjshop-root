
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 广告
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TAd implements Serializable {

    private static final long serialVersionUID = 1362178157;

    /**
     * 类型
     */
    public enum Type {

        /** 文本 */
        text,

        /** 图片 */
        image
    }

    private TAdPosition adPositionInfo;

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
     * 判断是否已结束
     *
     * @return 是否已结束
     */
    @Transient
    public boolean hasEnded() {
        return getEndDate() != null && !getEndDate().after(new Timestamp(System.currentTimeMillis()));
    }

    @Transient
    public Type getEnumType() {
        return this.type == null ? null : Type.values()[this.type.intValue()];
    }

    public TAdPosition getAdPositionInfo() {
        return adPositionInfo;
    }

    public void setAdPositionInfo(TAdPosition adPositionInfo) {
        this.adPositionInfo = adPositionInfo;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private Timestamp beginDate;
    private String    content;
    private Timestamp endDate;
    private String    path;
    private String    title;
    private Integer   type;
    private String    url;
    private Long      adPosition;

    public TAd() {}

    public TAd(TAd value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.beginDate = value.beginDate;
        this.content = value.content;
        this.endDate = value.endDate;
        this.path = value.path;
        this.title = value.title;
        this.type = value.type;
        this.url = value.url;
        this.adPosition = value.adPosition;
    }

    public TAd(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            Integer orders,
            Timestamp beginDate,
            String content,
            Timestamp endDate,
            String path,
            String title,
            Integer type,
            String url,
            Long adPosition
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.beginDate = beginDate;
        this.content = content;
        this.endDate = endDate;
        this.path = path;
        this.title = title;
        this.type = type;
        this.url = url;
        this.adPosition = adPosition;
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

    public Timestamp getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NotEmpty
    @Length(max = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|ftp:\\/\\/|mailto:|\\/|#).*$")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NotNull
    public Long getAdPosition() {
        return this.adPosition;
    }

    public void setAdPosition(Long adPosition) {
        this.adPosition = adPosition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TAd (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(beginDate);
        sb.append(", ").append(content);
        sb.append(", ").append(endDate);
        sb.append(", ").append(path);
        sb.append(", ").append(title);
        sb.append(", ").append(type);
        sb.append(", ").append(url);
        sb.append(", ").append(adPosition);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TAd that = (TAd) o;

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
