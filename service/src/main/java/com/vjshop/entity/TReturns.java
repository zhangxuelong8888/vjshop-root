
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 退货单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TReturns implements Serializable {

    private static final long serialVersionUID = -785255462;

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private String     address;
    private String     area;
    private String     deliveryCorp;
    private BigDecimal freight;
    private String     memo;
    private String     operator;
    private String     phone;
    private String     shipper;
    private String     shippingMethod;
    private String     sn;
    private String     trackingNo;
    private String     zipCode;
    private Long       orders;

    private TOrder orderVO;

    /** 退货项 */
    private List<TReturnsItem> returnsItems = new ArrayList<TReturnsItem>();

    public TReturns() {}

    public TReturns(TReturns value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.area = value.area;
        this.deliveryCorp = value.deliveryCorp;
        this.freight = value.freight;
        this.memo = value.memo;
        this.operator = value.operator;
        this.phone = value.phone;
        this.shipper = value.shipper;
        this.shippingMethod = value.shippingMethod;
        this.sn = value.sn;
        this.trackingNo = value.trackingNo;
        this.zipCode = value.zipCode;
        this.orders = value.orders;
    }

    public TReturns(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     address,
        String     area,
        String     deliveryCorp,
        BigDecimal freight,
        String     memo,
        String     operator,
        String     phone,
        String     shipper,
        String     shippingMethod,
        String     sn,
        String     trackingNo,
        String     zipCode,
        Long       orders
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.address = address;
        this.area = area;
        this.deliveryCorp = deliveryCorp;
        this.freight = freight;
        this.memo = memo;
        this.operator = operator;
        this.phone = phone;
        this.shipper = shipper;
        this.shippingMethod = shippingMethod;
        this.sn = sn;
        this.trackingNo = trackingNo;
        this.zipCode = zipCode;
        this.orders = orders;
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
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDeliveryCorp() {
        return this.deliveryCorp;
    }

    public void setDeliveryCorp(String deliveryCorp) {
        this.deliveryCorp = deliveryCorp;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getFreight() {
        return this.freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Length(max = 200)
    @Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(max = 200)
    public String getShipper() {
        return this.shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Length(max = 200)
    public String getTrackingNo() {
        return this.trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    @Length(max = 200)
    @Pattern(regexp = "^\\d{6}$")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @NotNull
    public Long getOrders() {
        return this.orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    public TOrder getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(TOrder orderVO) {
        this.orderVO = orderVO;
    }

    public List<TReturnsItem> getReturnsItems() {
        return returnsItems;
    }

    public void setReturnsItems(List<TReturnsItem> returnsItems) {
        this.returnsItems = returnsItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TReturns (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(area);
        sb.append(", ").append(deliveryCorp);
        sb.append(", ").append(freight);
        sb.append(", ").append(memo);
        sb.append(", ").append(operator);
        sb.append(", ").append(phone);
        sb.append(", ").append(shipper);
        sb.append(", ").append(shippingMethod);
        sb.append(", ").append(sn);
        sb.append(", ").append(trackingNo);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(orders);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TReturns that = (TReturns) o;

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
     * 判断是否为新建对象
     *
     * @return 是否为新建对象
     */
    @Transient
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * 获取数量
     *
     * @return 数量
     */
    @Transient
    public int getQuantity() {
        int quantity = 0;
        if (getReturnsItems() != null) {
            for (TReturnsItem returnsItem : getReturnsItems()) {
                if (returnsItem != null && returnsItem.getQuantity() != null) {
                    quantity += returnsItem.getQuantity();
                }
            }
        }
        return quantity;
    }

    /**
     * 设置配送方式
     *
     * @param shippingMethod
     *            配送方式
     */
    @Transient
    public void setShippingMethod(TShippingMethod shippingMethod) {
        setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
    }

    /**
     * 设置物流公司
     *
     * @param deliveryCorp
     *            物流公司
     */
    @Transient
    public void setDeliveryCorp(TDeliveryCorp deliveryCorp) {
        setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
    }

    /**
     * 设置地区
     *
     * @param area
     *            地区
     */
    @Transient
    public void setArea(TArea area) {
        setArea(area != null ? area.getFullName() : null);
    }

    /**
     * 设置操作员
     *
     * @param operator
     *            操作员
     */
    @Transient
    public void setOperator(TAdmin operator) {
        setOperator(operator != null ? operator.getUsername() : null);
    }
}
