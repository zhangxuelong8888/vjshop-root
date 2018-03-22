
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - SEO设置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TSeo implements Serializable {

    private static final long serialVersionUID = -638652409;

    /**
     * 类型
     */
    public enum Type {

        /** 首页 */
        index,

        /** 文章列表 */
        articleList,

        /** 文章搜索 */
        articleSearch,

        /** 文章内容 */
        articleContent,

        /** 商品列表 */
        goodsList,

        /** 商品搜索 */
        goodsSearch,

        /** 商品内容 */
        goodsContent,

        /** 品牌列表 */
        brandList,

        /** 品牌内容 */
        brandContent
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

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    description;
    private String    keywords;
    private String    title;
    private Integer   type;

    public TSeo() {}

    public TSeo(TSeo value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.description = value.description;
        this.keywords = value.keywords;
        this.title = value.title;
        this.type = value.type;
    }

    public TSeo(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            String description,
            String keywords,
            String title,
            Integer type
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.description = description;
        this.keywords = keywords;
        this.title = title;
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

    @Length(max = 200)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Length(max = 200)
    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        if (keywords != null) {
            keywords = keywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
        }
        this.keywords = keywords;
    }

    @Length(max = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TSeo (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(description);
        sb.append(", ").append(keywords);
        sb.append(", ").append(title);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TSeo that = (TSeo) o;

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
