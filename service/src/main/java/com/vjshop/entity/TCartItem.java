
package com.vjshop.entity;

import com.vjshop.Setting;
import com.vjshop.util.SystemUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity - 购物车项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TCartItem implements Serializable {

    private static final long serialVersionUID = -1025524631;

    /** 最大数量 */
    public static final Integer MAX_QUANTITY = 10000;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   quantity;
    private Long      cart;
    private Long      product;
    private Boolean   isSelected;

    private TProduct productVO;

    private TCart cartVO;

    public TCartItem() {}

    public TCartItem(TCartItem value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.quantity = value.quantity;
        this.cart = value.cart;
        this.product = value.product;
    }

    public TCartItem(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   quantity,
        Long      cart,
        Long      product
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.quantity = quantity;
        this.cart = cart;
        this.product = product;
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

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getCart() {
        return this.cart;
    }

    public void setCart(Long cart) {
        this.cart = cart;
    }

    public Long getProduct() {
        return this.product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public TProduct getProductVO() {
        return productVO;
    }

    public void setProductVO(TProduct productVO) {
        this.productVO = productVO;
    }

    public TCart getCartVO() {
        return cartVO;
    }

    public void setCartVO(TCart cartVO) {
        this.cartVO = cartVO;
    }

    @NotNull
    public Boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TCartItem (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(quantity);
        sb.append(", ").append(cart);
        sb.append(", ").append(product);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TCartItem that = (TCartItem) o;

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
    public int getWeight() {
        if (getProductVO() != null && getProductVO().getWeight() != null && getQuantity() != null) {
            return getProductVO().getWeight() * getQuantity();
        } else {
            return 0;
        }
    }

    /**
     * 获取赠送积分
     *
     * @return 赠送积分
     */
    @Transient
    public long getRewardPoint() {
        if (getProductVO() != null && getProductVO().getRewardPoint() != null && getQuantity() != null) {
            return getProductVO().getRewardPoint() * getQuantity();
        } else {
            return 0L;
        }
    }

    /**
     * 获取兑换积分
     *
     * @return 兑换积分
     */
    @Transient
    public long getExchangePoint() {
        if (getProductVO() != null && getProductVO().getExchangePoint() != null && getQuantity() != null) {
            return getProductVO().getExchangePoint() * getQuantity();
        } else {
            return 0L;
        }
    }

    /**
     * 获取价格
     *
     * @return 价格
     */
    @Transient
    public BigDecimal getPrice() {
        if (getProductVO() != null && getProductVO().getPrice() != null) {
            Setting setting = SystemUtils.getSetting();
            if (getCartVO() != null && getCartVO().getMember() != null && getCartVO().getMemberVO().getMemberRankInfo() != null) {
                TMemberRank memberRank = getCartVO().getMemberVO().getMemberRankInfo();
                if (memberRank.getScale() != null) {
                    return setting.setScale(getProductVO().getPrice().multiply(new BigDecimal(String.valueOf(memberRank.getScale()))));
                }
            }
            return setting.setScale(getProductVO().getPrice());
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取小计
     *
     * @return 小计
     */
    @Transient
    public BigDecimal getSubtotal() {
        if (getQuantity() != null) {
            return getPrice().multiply(new BigDecimal(getQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取是否上架
     *
     * @return 是否上架
     */
    @Transient
    public boolean getIsMarketable() {
        return getProductVO() != null && getProductVO().getIsMarketable();
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    @Transient
    public boolean getIsDelivery() {
        return getProductVO() != null && getProductVO().getIsDelivery();
    }

    /**
     * 获取是否库存不足
     *
     * @return 是否库存不足
     */
    @Transient
    public boolean getIsLowStock() {
        return getQuantity() != null && getProductVO() != null && getQuantity() > getProductVO().getAvailableStock();
    }

    /**
     * 增加商品数量
     *
     * @param quantity
     *            数量
     */
    @Transient
    public void add(int quantity) {
        if (quantity < 1) {
            return;
        }
        if (getQuantity() != null) {
            setQuantity(getQuantity() + quantity);
        } else {
            setQuantity(quantity);
        }
    }
}
