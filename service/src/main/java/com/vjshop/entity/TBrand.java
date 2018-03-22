
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Entity - 品牌
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBrand implements Serializable {

    private static final long serialVersionUID = 1897220345;

    /** 路径前缀 */
    private static final String PATH_PREFIX = "/brand/content";

    /** 路径后缀 */
    private static final String PATH_SUFFIX = ".jhtml";

    /**
     * 类型
     */
    public enum Type {

        /** 文本 */
        text,

        /** 图片 */
        image
    }

    /**
     * 获取枚举类型
     * @return
     */
    public Type getEnumType(){
        return this.type == null ? null : Type.values()[this.type.intValue()];
    }

    /** 货品 */
    private Set<TGoods> goods = new HashSet<TGoods>();

    /** 商品分类 */
    private Set<TProductCategory> productCategories = new HashSet<TProductCategory>();

    public Set<TGoods> getGoods() {
        return goods;
    }

    public void setGoods(Set<TGoods> goods) {
        if (CollectionUtils.isEmpty(goods)){
            this.goods = null;
            return;
        }
        goods.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TGoods that = (TGoods) o;
                return null == that.getId();
            }
        });
        this.goods = goods;
    }

    public Set<TProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(Set<TProductCategory> productCategories) {
        if (CollectionUtils.isEmpty(productCategories)){
            this.productCategories = null;
            return;
        }
        productCategories.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TProductCategory that = (TProductCategory) o;
                return null == that.getId();
            }
        });
        this.productCategories = productCategories;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    introduction;
    private String    logo;
    private String    name;
    private Integer   type;
    private String    url;

    public TBrand() {}

    public TBrand(TBrand value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.introduction = value.introduction;
        this.logo = value.logo;
        this.name = value.name;
        this.type = value.type;
        this.url = value.url;
    }

    public TBrand(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    introduction,
        String    logo,
        String    name,
        Integer   type,
        String    url
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.introduction = introduction;
        this.logo = logo;
        this.name = name;
        this.type = type;
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

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    /**
     * 获取路径
     *
     * @return 路径
     */
    @Transient
    public String getPath() {
        return getId() != null ? PATH_PREFIX + "/" + getId() + PATH_SUFFIX : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TBrand (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(introduction);
        sb.append(", ").append(logo);
        sb.append(", ").append(name);
        sb.append(", ").append(type);
        sb.append(", ").append(url);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TBrand that = (TBrand) o;

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
