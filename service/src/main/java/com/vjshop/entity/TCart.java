
package com.vjshop.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vjshop.Setting;
import com.vjshop.util.SystemUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

/**
 * Entity - 购物车
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TCart implements Serializable {

    private static final long serialVersionUID = 1772299173;

    /** 超时时间 */
    public static final int TIMEOUT = 604800;

    /** 最大购物车项数量 */
    public static final Integer MAX_CART_ITEM_COUNT = 100;

    /** "密钥"Cookie名称 */
    public static final String KEY_COOKIE_NAME = "cartKey";

    /** "数量"Cookie名称 */
    public static final String QUANTITY_COOKIE_NAME = "cartQuantity";

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Timestamp expire;
    private String    cartKey;
    private Long      member;

    private TMember memberVO;

    /** 购物车项 */
    private Set<TCartItem> cartItems = new HashSet<TCartItem>();

    public TCart() {}

    public TCart(TCart value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.expire = value.expire;
        this.cartKey = value.cartKey;
        this.member = value.member;
    }

    public TCart(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Timestamp expire,
        String    cartKey,
        Long      member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.expire = expire;
        this.cartKey = cartKey;
        this.member = member;
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

    public Timestamp getExpire() {
        return this.expire;
    }

    public void setExpire(Timestamp expire) {
        this.expire = expire;
    }

    public String getCartKey() {
        return this.cartKey;
    }

    public void setCartKey(String cartKey) {
        this.cartKey = cartKey;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public TMember getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(TMember memberVO) {
        this.memberVO = memberVO;
    }

    public Set<TCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<TCartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TCart (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(expire);
        sb.append(", ").append(cartKey);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TCart that = (TCart) o;

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

    /**
     * 获取商品重量
     *
     * @return 商品重量
     */
    @Transient
    public int getProductWeight() {
        int productWeight = 0;
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                productWeight += cartItem.getWeight();
            }
        }
        return productWeight;
    }

    /**
     * 获取商品数量
     *
     * @return 商品数量
     */
    @Transient
    public int getProductQuantity() {
        int productQuantity = 0;
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (cartItem.getQuantity() != null) {
                    productQuantity += cartItem.getQuantity();
                }
            }
        }
        return productQuantity;
    }

    /**
     * 获取赠品重量
     *
     * @return 赠品重量
     */
    @Transient
    public int getGiftWeight() {
        int giftWeight = 0;
        for (TProduct gift : getGifts()) {
            if (gift.getWeight() != null) {
                giftWeight += gift.getWeight();
            }
        }
        return giftWeight;
    }

    /**
     * 获取赠品数量
     *
     * @return 赠品数量
     */
    @Transient
    public int getGiftQuantity() {
        return getGifts().size();
    }

    /**
     * 获取总重量
     *
     * @return 总重量
     */
    @Transient
    public int getWeight() {
        return getProductWeight() + getGiftWeight();
    }

    /**
     * 获取总数量
     *
     * @return 总数量
     */
    @Transient
    public int getQuantity() {
        return getProductQuantity() + getGiftQuantity();
    }

    /**
     * 获取赠送积分
     *
     * @return 赠送积分
     */
    @Transient
    public long getRewardPoint() {
        long rewardPoint = 0L;
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (BooleanUtils.isTrue(cartItem.getIsSelected())) {
                    rewardPoint += cartItem.getRewardPoint();
                }
            }
        }
        return rewardPoint;
    }

    /**
     * 获取兑换积分
     *
     * @return 兑换积分
     */
    @Transient
    public long getExchangePoint() {
        long exchangePoint = 0L;
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                exchangePoint += cartItem.getExchangePoint();
            }
        }
        return exchangePoint;
    }

    /**
     * 获取赠送积分增加值
     *
     * @return 赠送积分增加值
     */
    @Transient
    public long getAddedRewardPoint() {
        Map<TCartItem, Long> cartItemRewardPointMap = new HashMap<TCartItem, Long>();
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (BooleanUtils.isTrue(cartItem.getIsSelected())) {
                    cartItemRewardPointMap.put(cartItem, cartItem.getRewardPoint());
                }
            }
        }
        Long addedRewardPoint = 0L;
        for (TPromotion promotion : getPromotions()) {
            long originalRewardPoint = 0;
            Set<TCartItem> cartItems = getCartItems(promotion);
            for (TCartItem cartItem : cartItems) {
                originalRewardPoint += cartItemRewardPointMap.get(cartItem);
            }
            int quantity = getQuantity(promotion);
            long currentRewardPoint = promotion.calculatePoint(originalRewardPoint, quantity);
            if (originalRewardPoint > 0) {
                BigDecimal rate = new BigDecimal(currentRewardPoint).divide(new BigDecimal(originalRewardPoint), RoundingMode.DOWN);
                for (TCartItem cartItem : cartItems) {
                    cartItemRewardPointMap.put(cartItem, new BigDecimal(cartItemRewardPointMap.get(cartItem)).multiply(rate).longValue());
                }
            } else {
                for (TCartItem cartItem : cartItems) {
                    cartItemRewardPointMap.put(cartItem, new BigDecimal(currentRewardPoint).divide(new BigDecimal(quantity)).longValue());
                }
            }
            addedRewardPoint += currentRewardPoint - originalRewardPoint;
        }
        return addedRewardPoint;
    }

    /**
     * 获取有效赠送积分
     *
     * @return 有效赠送积分
     */
    @Transient
    public long getEffectiveRewardPoint() {
        long effectiveRewardPoint = getRewardPoint() + getAddedRewardPoint();
        return effectiveRewardPoint >= 0L ? effectiveRewardPoint : 0L;
    }

    /**
     * 获取商品价格
     * @param filterSelected 是否过滤已选中购物车项
     * @return 商品价格
     */
    @Transient
    public BigDecimal getPrice(Boolean filterSelected) {
        BigDecimal price = BigDecimal.ZERO;
        if (getCartItems() != null) {
            if (filterSelected) {
                for (TCartItem cartItem : getCartItems()) {
                    if (BooleanUtils.isTrue(cartItem.getIsSelected())) {
                        price = price.add(cartItem.getSubtotal());
                    }
                }
            } else {
                for (TCartItem cartItem : getCartItems()) {
                    price = price.add(cartItem.getSubtotal());
                }
            }

        }
        return price;
    }

    /**
     * 获取折扣
     * @param filterSelected 是否过滤已选中购物车项
     * @return 折扣
     */
    @Transient
    public BigDecimal getDiscount(Boolean filterSelected) {
        Map<TCartItem, BigDecimal> cartItemPriceMap = new HashMap<TCartItem, BigDecimal>();
        if (getCartItems() != null) {
            if (filterSelected) {
                for (TCartItem cartItem : getCartItems()) {
                    if (BooleanUtils.isTrue(cartItem.getIsSelected())) {
                        cartItemPriceMap.put(cartItem, cartItem.getSubtotal());
                    }
                }
            } else {
                for (TCartItem cartItem : getCartItems()) {
                    cartItemPriceMap.put(cartItem, cartItem.getSubtotal());
                }
            }

        }
        BigDecimal discount = BigDecimal.ZERO;
        for (TPromotion promotion : getPromotions()) {
            BigDecimal originalPrice = BigDecimal.ZERO;
            BigDecimal currentPrice = BigDecimal.ZERO;
            Set<TCartItem> cartItems = getCartItems(promotion);
            for (TCartItem cartItem : cartItems) {
                originalPrice = originalPrice.add(cartItemPriceMap.get(cartItem));
            }
            if (originalPrice.compareTo(BigDecimal.ZERO) > 0) {
                int quantity = getQuantity(promotion);
                currentPrice = promotion.calculatePrice(originalPrice, quantity);
                BigDecimal rate = currentPrice.divide(originalPrice, RoundingMode.DOWN);
                for (TCartItem cartItem : cartItems) {
                    cartItemPriceMap.put(cartItem, cartItemPriceMap.get(cartItem).multiply(rate));
                }
            } else {
                for (TCartItem cartItem : cartItems) {
                    cartItemPriceMap.put(cartItem, BigDecimal.ZERO);
                }
            }
            discount = discount.add(originalPrice.subtract(currentPrice));
        }
        Setting setting = SystemUtils.getSetting();
        return setting.setScale(discount);
    }

    /**
     * 获取有效商品价格
     * @param filterSelected 是否过滤已选中购物车项
     * @return 有效商品价格
     */
    @Transient
    public BigDecimal getEffectivePrice(Boolean filterSelected) {
        BigDecimal effectivePrice = getPrice(filterSelected).subtract(getDiscount(filterSelected));
        return effectivePrice.compareTo(BigDecimal.ZERO) >= 0 ? effectivePrice : BigDecimal.ZERO;
    }

    /**
     * 获取赠品
     *
     * @return 赠品
     */
    @Transient
    public Set<TProduct> getGifts() {
        Set<TProduct> gifts = new HashSet<TProduct>();
        for (TPromotion promotion : getPromotions()) {
            if (CollectionUtils.isNotEmpty(promotion.getGifts())) {
                for (TProduct gift : promotion.getGifts()) {
                    if (gift.getIsMarketable() && !gift.getIsOutOfStock()) {
                        gifts.add(gift);
                    }
                }
            }
        }
        return gifts;
    }

    /**
     * 获取赠品名称
     *
     * @return 赠品名称
     */
    @Transient
    public List<String> getGiftNames() {
        List<String> giftNames = new ArrayList<String>();
        for (TProduct gift : getGifts()) {
            giftNames.add(gift.getName());
        }
        return giftNames;
    }

    /**
     * 获取促销
     *
     * @return 促销
     */
    @Transient
    public Set<TPromotion> getPromotions() {
        Set<TPromotion> allPromotions = new HashSet<TPromotion>();
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (cartItem.getProductVO() != null) {
                    allPromotions.addAll(cartItem.getProductVO().getValidPromotions());
                }
            }
        }
        Set<TPromotion> promotions = new TreeSet<TPromotion>();
        for (TPromotion promotion : allPromotions) {
            if (isValid(promotion)) {
                promotions.add(promotion);
            }
        }
        return promotions;
    }

    /**
     * 获取促销名称
     *
     * @return 促销名称
     */
    @Transient
    public List<String> getPromotionNames() {
        List<String> promotionNames = new ArrayList<String>();
        for (TPromotion promotion : getPromotions()) {
            promotionNames.add(promotion.getName());
        }
        return promotionNames;
    }

    /**
     * 获取赠送优惠券
     *
     * @return 赠送优惠券
     */
    @Transient
    public Set<TCoupon> getCoupons() {
        Set<TCoupon> coupons = new HashSet<TCoupon>();
        for (TPromotion promotion : getPromotions()) {
            if (CollectionUtils.isNotEmpty(promotion.getCoupons())) {
                coupons.addAll(promotion.getCoupons());
            }
        }
        return coupons;
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    @Transient
    public boolean getIsDelivery() {
        return CollectionUtils.exists(getCartItems(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TCartItem cartItem = (TCartItem) object;
                return cartItem != null && cartItem.getIsDelivery();
            }
        }) || CollectionUtils.exists(getGifts(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product != null && product.getIsDelivery();
            }
        });
    }

    /**
     * 获取是否库存不足
     *
     * @return 是否库存不足
     */
    @Transient
    public boolean getIsLowStock() {
        return CollectionUtils.exists(getCartItems(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TCartItem cartItem = (TCartItem) object;
                return cartItem != null && cartItem.getIsLowStock();
            }
        });
    }

    /**
     * 获取令牌
     *
     * @return 令牌
     */
    @Transient
    public String getToken() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37).append(getCartKey());
        if (getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                hashCodeBuilder.append(cartItem.getProduct()).append(cartItem.getIsMarketable()).append(cartItem.getQuantity()).append(cartItem.getPrice());
            }
        }
        return DigestUtils.md5Hex(hashCodeBuilder.toString());
    }

    /**
     * 获取购物车项
     *
     * @param productId
     *            商品ID
     * @return 购物车项
     */
    @Transient
    public TCartItem getCartItemByProductId(Long productId) {
        if (productId != null && getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (cartItem.getProduct() != null && productId.equals(cartItem.getProduct())) {
                    return cartItem;
                }
            }
        }
        return null;
    }

    /**
     * 获取购物车项
     *
     * @param cartItemId
     *            购物车项ID
     * @return 购物车项
     */
    @Transient
    public TCartItem getCartItem(Long cartItemId) {
        if (cartItemId != null && getCartItems() != null) {
            for (TCartItem ci : getCartItems()) {
                if (ci.getId() != null && ci.getId().equals(cartItemId)){
                    return ci;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否包含商品
     *
     * @param productId
     *            商品ID
     * @return 是否包含商品
     */
    @Transient
    public boolean containsProduct(Long productId) {
        return getCartItemByProductId(productId) != null;
    }

    /**
     * 判断是否包含购物车项
     *
     * @param cartItemId
     *            购物车项ID
     * @return 是否包含购物车项
     */
    @Transient
    public boolean contains(Long cartItemId) {
        return getCartItem(cartItemId) != null;
    }

    /**
     * 移除购物车项
     *
     * @param cartItemId
     *            购物车项ID
     */
    @Transient
    public void remove(Long cartItemId) {
        if (getCartItems() != null){
            getCartItems().remove(getCartItem(cartItemId));
        }
    }

    /**
     * 获取购物车项
     *
     * @param promotion
     *            促销
     * @return 购物车项
     */
    @Transient
    private Set<TCartItem> getCartItems(TPromotion promotion) {
        Set<TCartItem> cartItems = new HashSet<TCartItem>();
        if (promotion != null && getCartItems() != null) {
            for (TCartItem cartItem : getCartItems()) {
                if (cartItem.getProduct() != null && cartItem.getProductVO().isValid(promotion)) {
                    cartItems.add(cartItem);
                }
            }
        }
        return cartItems;
    }

    /**
     * 获取商品数量
     *
     * @param promotion
     *            促销
     * @return 商品数量
     */
    @Transient
    private int getQuantity(TPromotion promotion) {
        int quantity = 0;
        for (TCartItem cartItem : getCartItems(promotion)) {
            if (cartItem.getQuantity() != null) {
                quantity += cartItem.getQuantity();
            }
        }
        return quantity;
    }

    /**
     * 获取赠送积分
     *
     * @param promotion
     *            促销
     * @return 赠送积分
     */
    @Transient
    private long getRewardPoint(TPromotion promotion) {
        long rewardPoint = 0L;
        for (TCartItem cartItem : getCartItems(promotion)) {
            rewardPoint += cartItem.getRewardPoint();
        }
        return rewardPoint;
    }

    /**
     * 获取商品价格
     *
     * @param promotion
     *            促销
     * @return 商品价格
     */
    @Transient
    private BigDecimal getPrice(TPromotion promotion) {
        BigDecimal price = BigDecimal.ZERO;
        for (TCartItem cartItem : getCartItems(promotion)) {
            price = price.add(cartItem.getSubtotal());
        }
        return price;
    }

    /**
     * 判断促销是否有效
     *
     * @param promotion
     *            促销
     * @return 促销是否有效
     */
    @Transient
    private boolean isValid(TPromotion promotion) {
        if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
            return false;
        }
        if (CollectionUtils.isEmpty(promotion.getMemberRanks()) || getMemberVO() == null || getMemberVO().getMemberRank() == null || !promotion.getMemberRanks().contains(getMemberVO().getMemberRankInfo())) {
            return false;
        }
        Integer quantity = getQuantity(promotion);
        if ((promotion.getMinimumQuantity() != null && promotion.getMinimumQuantity() > quantity) || (promotion.getMaximumQuantity() != null && promotion.getMaximumQuantity() < quantity)) {
            return false;
        }
        BigDecimal price = getPrice(promotion);
        if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
            return false;
        }
        return true;
    }

    /**
     * 判断优惠券是否有效
     *
     * @param coupon
     *            优惠券
     * @return 优惠券是否有效
     */
    @Transient
    public boolean isValid(TCoupon coupon) {
        if (coupon == null || !coupon.getIsEnabled() || !coupon.hasBegun() || coupon.hasExpired()) {
            return false;
        }
        if ((coupon.getMinimumQuantity() != null && coupon.getMinimumQuantity() > getProductQuantity()) || (coupon.getMaximumQuantity() != null && coupon.getMaximumQuantity() < getProductQuantity())) {
            return false;
        }
        if ((coupon.getMinimumPrice() != null && coupon.getMinimumPrice().compareTo(getEffectivePrice(true)) > 0) || (coupon.getMaximumPrice() != null && coupon.getMaximumPrice().compareTo(getEffectivePrice(true)) < 0)) {
            return false;
        }
        if (!isCouponAllowed()) {
            return false;
        }
        return true;
    }

    /**
     * 判断优惠码是否有效
     *
     * @param couponCode
     *            优惠码
     * @return 优惠码是否有效
     */
    @Transient
    public boolean isValid(TCouponCode couponCode) {
        if (couponCode == null || couponCode.getIsUsed() || couponCode.getCoupon() == null) {
            return false;
        }
        return isValid(couponCode.getCouponInfo());
    }

    /**
     * 判断是否存在已下架商品
     *
     * @return 是否存在已下架商品
     */
    @Transient
    public boolean hasNotMarketable() {
        return CollectionUtils.exists(getCartItems(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TCartItem cartItem = (TCartItem) object;
                return cartItem != null && !cartItem.getIsMarketable();
            }
        });
    }

    /**
     * 判断是否免运费
     *
     * @return 是否免运费
     */
    @Transient
    public boolean isFreeShipping() {
        return CollectionUtils.exists(getPromotions(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TPromotion promotion = (TPromotion) object;
                return promotion != null && BooleanUtils.isTrue(promotion.getIsFreeShipping());
            }
        });
    }

    /**
     * 判断是否允许使用优惠券
     *
     * @return 是否允许使用优惠券
     */
    @Transient
    public boolean isCouponAllowed() {
        return !CollectionUtils.exists(getPromotions(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TPromotion promotion = (TPromotion) object;
                return promotion != null && BooleanUtils.isFalse(promotion.getIsCouponAllowed());
            }
        });
    }

    /**
     * 判断是否为空
     *
     * @return 是否为空
     */
    @Transient
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(getCartItems());
    }

}
