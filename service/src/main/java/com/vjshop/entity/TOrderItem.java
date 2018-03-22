
package com.vjshop.entity;

import com.vjshop.util.JsonUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 订单项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TOrderItem implements Serializable {

    private static final long serialVersionUID = 1708919652;

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Long       orders;
    private Boolean    isDelivery;
    private String     name;
    private BigDecimal price;
    private Integer    quantity;
    private Integer    returnedQuantity;
    private Integer    shippedQuantity;
    private String     sn;
    private String     specifications;
    private String     thumbnail;
    private Integer    type;
    private Integer    weight;

    private Long       product;

    private TOrder orderVO;

    private TProduct productVO;

    /** 规格 */
    private List<String> specificationsList = new ArrayList<String>();

    public TOrderItem() {}

    public TOrderItem(TOrderItem value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.isDelivery = value.isDelivery;
        this.name = value.name;
        this.price = value.price;
        this.quantity = value.quantity;
        this.returnedQuantity = value.returnedQuantity;
        this.shippedQuantity = value.shippedQuantity;
        this.sn = value.sn;
        this.specifications = value.specifications;
        this.thumbnail = value.thumbnail;
        this.type = value.type;
        this.weight = value.weight;
        this.orders = value.orders;
        this.product = value.product;
    }

    public TOrderItem(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        Boolean    isDelivery,
        String     name,
        BigDecimal price,
        Integer    quantity,
        Integer    returnedQuantity,
        Integer    shippedQuantity,
        String     sn,
        String     specifications,
        String     thumbnail,
        Integer    type,
        Integer    weight,
        Long       orders,
        Long       product
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.isDelivery = isDelivery;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.returnedQuantity = returnedQuantity;
        this.shippedQuantity = shippedQuantity;
        this.sn = sn;
        this.specifications = specifications;
        this.thumbnail = thumbnail;
        this.type = type;
        this.weight = weight;
        this.orders = orders;
        this.product = product;

        this.specificationsList = JsonUtils.toObject(this.specifications,List.class);
    }

    public String typeName() {
        return TGoods.Type.valueOf(this.type).name();
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

    public Boolean getIsDelivery() {
        return this.isDelivery;
    }

    public void setIsDelivery(Boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReturnedQuantity() {
        return this.returnedQuantity;
    }

    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public Integer getShippedQuantity() {
        return this.shippedQuantity;
    }

    public void setShippedQuantity(Integer shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSpecifications() {
        return this.specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getOrders() {
        return this.orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    public Long getProduct() {
        return this.product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public TOrder getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(TOrder orderVO) {
        this.orderVO = orderVO;
    }

    public TProduct getProductVO() {
        return productVO;
    }

    public void setProductVO(TProduct productVO) {
        this.productVO = productVO;
    }

    public List<String> getSpecificationsList() {
        return specificationsList;
    }

    public void setSpecificationsList(List<String> specificationsList) {
        this.specificationsList = specificationsList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOrderItem (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(isDelivery);
        sb.append(", ").append(name);
        sb.append(", ").append(price);
        sb.append(", ").append(quantity);
        sb.append(", ").append(returnedQuantity);
        sb.append(", ").append(shippedQuantity);
        sb.append(", ").append(sn);
        sb.append(", ").append(specifications);
        sb.append(", ").append(thumbnail);
        sb.append(", ").append(type);
        sb.append(", ").append(weight);
        sb.append(", ").append(orders);
        sb.append(", ").append(product);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TOrderItem that = (TOrderItem) o;

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
     * 获取商品总重量
     *
     * @return 商品总重量
     */
    @Transient
    public int getTotalWeight() {
        if (getWeight() != null && getQuantity() != null) {
            return getWeight() * getQuantity();
        } else {
            return 0;
        }
    }

    /**
     * 获取小计
     *
     * @return 小计
     */
    @Transient
    public BigDecimal getSubtotal() {
        if (getPrice() != null && getQuantity() != null) {
            return getPrice().multiply(new BigDecimal(getQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取可发货数
     *
     * @return 可发货数
     */
    @Transient
    public int getShippableQuantity() {
        int shippableQuantity = getQuantity() - getShippedQuantity();
        return shippableQuantity >= 0 ? shippableQuantity : 0;
    }

    /**
     * 获取可退货数
     *
     * @return 可退货数
     */
    @Transient
    public int getReturnableQuantity() {
        int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
        return returnableQuantity >= 0 ? returnableQuantity : 0;
    }
}
