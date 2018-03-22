
package com.vjshop.entity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Entity - 促销
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPromotion implements Serializable {

    private static final long serialVersionUID = -39304340;

    /** 路径前缀 */
    private static final String PATH_PREFIX = "/promotion/content";

    /** 路径后缀 */
    private static final String PATH_SUFFIX = ".jhtml";

    /** 允许参加会员等级 */
    private Set<TMemberRank> memberRanks = new HashSet<TMemberRank>();

    /** 赠送优惠券 */
    private Set<TCoupon> coupons = new HashSet<TCoupon>();

    /** 赠品 */
    private Set<TProduct> gifts = new HashSet<TProduct>();

    /** 货品 */
    private Set<TGoods> goods = new HashSet<TGoods>();

    /** 商品分类 */
    private Set<TProductCategory> productCategories = new HashSet<TProductCategory>();

    /**
     * 获取允许参加会员等级
     *
     * @return 允许参加会员等级
     */
    public Set<TMemberRank> getMemberRanks() {
        return memberRanks;
    }

    /**
     * 设置允许参加会员等级
     *
     * @param memberRanks
     *            允许参加会员等级
     */
    public void setMemberRanks(Set<TMemberRank> memberRanks) {
        if (CollectionUtils.isEmpty(memberRanks)){
            this.memberRanks = null;
            return;
        }
        memberRanks.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TMemberRank that = (TMemberRank) o;
                return null == that.getId();
            }
        });
        this.memberRanks = memberRanks;
    }

    /**
     * 获取赠送优惠券
     *
     * @return 赠送优惠券
     */
    public Set<TCoupon> getCoupons() {
        return coupons;
    }

    /**
     * 设置赠送优惠券
     *
     * @param coupons
     *            赠送优惠券
     */
    public void setCoupons(Set<TCoupon> coupons) {
        if (CollectionUtils.isEmpty(coupons)){
            this.coupons = null;
            return;
        }
        coupons.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TCoupon that = (TCoupon) o;
                return null == that.getId();
            }
        });
        this.coupons = coupons;
    }

    /**
     * 获取赠品
     *
     * @return 赠品
     */
    public Set<TProduct> getGifts() {
        return gifts;
    }

    /**
     * 设置赠品
     *
     * @param gifts
     *            赠品
     */
    public void setGifts(Set<TProduct> gifts) {
        if (CollectionUtils.isEmpty(gifts)){
            this.gifts = null;
            return;
        }
        gifts.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TProduct that = (TProduct) o;
                return null == that.getId();
            }
        });
        this.gifts = gifts;
    }

    /**
     * 获取货品
     *
     * @return 货品
     */
    public Set<TGoods> getGoods() {
        return goods;
    }

    /**
     * 设置货品
     *
     * @param goods
     *            货品
     */
    public void setGoods(Set<TGoods> goods) {
        this.goods = goods;
    }

    /**
     * 获取商品分类
     *
     * @return 商品分类
     */
    public Set<TProductCategory> getProductCategories() {
        return productCategories;
    }

    /**
     * 设置商品分类
     *
     * @param productCategories
     *            商品分类
     */
    public void setProductCategories(Set<TProductCategory> productCategories) {
        this.productCategories = productCategories;
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

    /**
     * 判断是否已开始
     *
     * @return 是否已开始
     */
    @Transient
    public boolean hasBegun() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return getBeginDate() == null || !getBeginDate().after(now);
    }

    /**
     * 判断是否已结束
     *
     * @return 是否已结束
     */
    @Transient
    public boolean hasEnded() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return getEndDate() != null && !getEndDate().after(now);
    }

    /**
     * 计算促销价格
     *
     * @param price
     *            商品价格
     * @param quantity
     *            商品数量
     * @return 促销价格
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

    /**
     * 计算促销赠送积分
     *
     * @param point
     *            赠送积分
     * @param quantity
     *            商品数量
     * @return 促销赠送积分
     */
    @Transient
    public Long calculatePoint(Long point, Integer quantity) {
        if (point == null || quantity == null || StringUtils.isEmpty(getPointExpression())) {
            return point;
        }
        Long result = 0L;
        try {
            Binding binding = new Binding();
            binding.setVariable("quantity", quantity);
            binding.setVariable("point", point);
            GroovyShell groovyShell = new GroovyShell(binding);
            result = Long.valueOf(groovyShell.evaluate(getPointExpression()).toString());
        } catch (Exception e) {
            return point;
        }
        if (result < point) {
            return point;
        }
        return result > 0L ? result : 0L;
    }

    /**
     * 是否存在会员等级
     *
     * @param memberRankId
     *            会员等级ID
     * @return true 存在
     */
    public boolean hasMemberRank(Long memberRankId){
        if(memberRankId == null || CollectionUtils.isEmpty(memberRanks)) {
            return false;
        }
        for (TMemberRank memberRank : memberRanks){
            if (memberRankId.equals(memberRank.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在优惠券
     *
     * @param couponId
     *            优惠券ID
     * @return true 存在
     */
    public boolean hasCoupon(Long couponId){
        if(couponId == null || CollectionUtils.isEmpty(coupons)) {
            return false;
        }
        for (TCoupon coupon : coupons){
            if (couponId.equals(coupon.getId())){
                return true;
            }
        }
        return false;
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Integer    orders;
    private Timestamp  beginDate;
    private Timestamp  endDate;
    private String     image;
    private String     introduction;
    private Boolean    isCouponAllowed;
    private Boolean    isFreeShipping;
    private BigDecimal maximumPrice;
    private Integer    maximumQuantity;
    private BigDecimal minimumPrice;
    private Integer    minimumQuantity;
    private String     name;
    private String     pointExpression;
    private String     priceExpression;
    private String     title;

    public TPromotion() {}

    public TPromotion(TPromotion value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.beginDate = value.beginDate;
        this.endDate = value.endDate;
        this.image = value.image;
        this.introduction = value.introduction;
        this.isCouponAllowed = value.isCouponAllowed;
        this.isFreeShipping = value.isFreeShipping;
        this.maximumPrice = value.maximumPrice;
        this.maximumQuantity = value.maximumQuantity;
        this.minimumPrice = value.minimumPrice;
        this.minimumQuantity = value.minimumQuantity;
        this.name = value.name;
        this.pointExpression = value.pointExpression;
        this.priceExpression = value.priceExpression;
        this.title = value.title;
    }

    public TPromotion(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        Integer    orders,
        Timestamp  beginDate,
        Timestamp  endDate,
        String     image,
        String     introduction,
        Boolean    isCouponAllowed,
        Boolean    isFreeShipping,
        BigDecimal maximumPrice,
        Integer    maximumQuantity,
        BigDecimal minimumPrice,
        Integer    minimumQuantity,
        String     name,
        String     pointExpression,
        String     priceExpression,
        String     title
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.image = image;
        this.introduction = introduction;
        this.isCouponAllowed = isCouponAllowed;
        this.isFreeShipping = isFreeShipping;
        this.maximumPrice = maximumPrice;
        this.maximumQuantity = maximumQuantity;
        this.minimumPrice = minimumPrice;
        this.minimumQuantity = minimumQuantity;
        this.name = name;
        this.pointExpression = pointExpression;
        this.priceExpression = priceExpression;
        this.title = title;
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

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @NotNull
    public Boolean getIsCouponAllowed() {
        return this.isCouponAllowed;
    }

    public void setIsCouponAllowed(Boolean isCouponAllowed) {
        this.isCouponAllowed = isCouponAllowed;
    }

    @NotNull
    public Boolean getIsFreeShipping() {
        return this.isFreeShipping;
    }

    public void setIsFreeShipping(Boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
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

    public String getPointExpression() {
        return this.pointExpression;
    }

    public void setPointExpression(String pointExpression) {
        this.pointExpression = pointExpression;
    }

    public String getPriceExpression() {
        return this.priceExpression;
    }

    public void setPriceExpression(String priceExpression) {
        this.priceExpression = priceExpression;
    }

    @NotEmpty
    @Length(max = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPromotion (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(beginDate);
        sb.append(", ").append(endDate);
        sb.append(", ").append(image);
        sb.append(", ").append(introduction);
        sb.append(", ").append(isCouponAllowed);
        sb.append(", ").append(isFreeShipping);
        sb.append(", ").append(maximumPrice);
        sb.append(", ").append(maximumQuantity);
        sb.append(", ").append(minimumPrice);
        sb.append(", ").append(minimumQuantity);
        sb.append(", ").append(name);
        sb.append(", ").append(pointExpression);
        sb.append(", ").append(priceExpression);
        sb.append(", ").append(title);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TPromotion that = (TPromotion) o;

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
