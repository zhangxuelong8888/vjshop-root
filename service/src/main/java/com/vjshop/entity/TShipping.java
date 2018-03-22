
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 发货单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TShipping implements Serializable {

    private static final long serialVersionUID = 1312264762;

    /**
     * 配送验证组
     */
    public interface Delivery extends Default {

    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Long       orders;
    /** 编号 */
    private String sn;

    /** 配送方式 */
    private String shippingMethod;

    /** 物流公司 */
    private String deliveryCorp;

    /** 物流公司网址 */
    private String deliveryCorpUrl;

    /** 物流公司代码 */
    private String deliveryCorpCode;

    /** 运单号 */
    private String trackingNo;

    /** 物流费用 */
    private BigDecimal freight;

    /** 收货人 */
    private String consignee;

    /** 地区 */
    private String area;

    /** 地址 */
    private String address;

    /** 邮编 */
    private String zipCode;

    /** 电话 */
    private String phone;

    /** 操作员 */
    private String operator;

    /** 备注 */
    private String memo;

    private TOrder orderVO;

    /** 发货项 */
    private List<TShippingItem> shippingItems = new ArrayList<TShippingItem>();

    public TShipping() {}

    public TShipping(TShipping value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.area = value.area;
        this.consignee = value.consignee;
        this.deliveryCorp = value.deliveryCorp;
        this.deliveryCorpCode = value.deliveryCorpCode;
        this.deliveryCorpUrl = value.deliveryCorpUrl;
        this.freight = value.freight;
        this.memo = value.memo;
        this.operator = value.operator;
        this.phone = value.phone;
        this.shippingMethod = value.shippingMethod;
        this.sn = value.sn;
        this.trackingNo = value.trackingNo;
        this.zipCode = value.zipCode;
        this.orders = value.orders;
    }

    public TShipping(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     address,
        String     area,
        String     consignee,
        String     deliveryCorp,
        String     deliveryCorpCode,
        String     deliveryCorpUrl,
        BigDecimal freight,
        String     memo,
        String     operator,
        String     phone,
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
        this.consignee = consignee;
        this.deliveryCorp = deliveryCorp;
        this.deliveryCorpCode = deliveryCorpCode;
        this.deliveryCorpUrl = deliveryCorpUrl;
        this.freight = freight;
        this.memo = memo;
        this.operator = operator;
        this.phone = phone;
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

    @NotEmpty(groups = TShipping.Delivery.class)
    @Length(max = 200)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotEmpty(groups = Delivery.class)
    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @NotEmpty(groups = Delivery.class)
    @Length(max = 200)
    public String getConsignee() {
        return this.consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @NotEmpty(groups = Delivery.class)
    public String getDeliveryCorp() {
        return this.deliveryCorp;
    }

    public void setDeliveryCorp(String deliveryCorp) {
        this.deliveryCorp = deliveryCorp;
    }

    public String getDeliveryCorpCode() {
        return this.deliveryCorpCode;
    }

    public void setDeliveryCorpCode(String deliveryCorpCode) {
        this.deliveryCorpCode = deliveryCorpCode;
    }

    public String getDeliveryCorpUrl() {
        return this.deliveryCorpUrl;
    }

    public void setDeliveryCorpUrl(String deliveryCorpUrl) {
        this.deliveryCorpUrl = deliveryCorpUrl;
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

    @NotEmpty(groups = TShipping.Delivery.class)
    @Length(max = 200)
    @Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @NotEmpty(groups = TShipping.Delivery.class)
    @Length(max = 200)
    @Pattern(regexp = "^\\d{6}$")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

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

    public List<TShippingItem> getShippingItems() {
        return shippingItems;
    }

    public void setShippingItems(List<TShippingItem> shippingItems) {
        this.shippingItems = shippingItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TShipping (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(area);
        sb.append(", ").append(consignee);
        sb.append(", ").append(deliveryCorp);
        sb.append(", ").append(deliveryCorpCode);
        sb.append(", ").append(deliveryCorpUrl);
        sb.append(", ").append(freight);
        sb.append(", ").append(memo);
        sb.append(", ").append(operator);
        sb.append(", ").append(phone);
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

        TShipping that = (TShipping) o;

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
    public boolean isNew() {
        return getId() == null;
    }
    /**
     * 获取数量
     *
     * @return 数量
     */
    public int getQuantity() {
        int quantity = 0;
        if (getShippingItems() != null) {
            for (TShippingItem shippingItem : getShippingItems()) {
                if (shippingItem != null && shippingItem.getQuantity() != null) {
                    quantity += shippingItem.getQuantity();
                }
            }
        }
        return quantity;
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    public boolean getIsDelivery() {
        return CollectionUtils.exists(getShippingItems(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TShippingItem shippingItem = (TShippingItem) object;
                return shippingItem != null && BooleanUtils.isTrue(shippingItem.getIsDelivery());
            }
        });
    }

    /**
     * 设置配送方式
     *
     * @param shippingMethod
     *            配送方式
     */
    public void setShippingMethod(TShippingMethod shippingMethod) {
        setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
    }

    /**
     * 设置物流公司
     *
     * @param deliveryCorp
     *            物流公司
     */
    public void setDeliveryCorp(TDeliveryCorp deliveryCorp) {
        setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
        setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp.getUrl() : null);
        setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
    }

    /**
     * 设置地区
     *
     * @param area
     *            地区
     */
    public void setArea(TArea area) {
        setArea(area != null ? area.getFullName() : null);
    }

    /**
     * 设置操作员
     *
     * @param operator
     *            操作员
     */
    public void setOperator(TAdmin operator) {
        setOperator(operator != null ? operator.getUsername() : null);
    }

}
