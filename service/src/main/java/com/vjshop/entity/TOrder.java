
package com.vjshop.entity;

import com.vjshop.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
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
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 订单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TOrder implements Serializable {

    private static final long serialVersionUID = 203061011;
    /** 锁定过期时间 */
    public static final int LOCK_EXPIRE = 60;

    /**
     * 配送验证组
     */
    public interface Delivery extends Default {

    }

    /**
     * 类型
     */
    public enum Type {

        /** 普通订单 */
        general,

        /** 兑换订单 */
        exchange;

        public static Type valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return null;
            }
            return values()[ordinal];
        }
    }

    /**
     * 状态
     */
    public enum Status {

        /** 等待付款 */
        pendingPayment,

        /** 等待审核 */
        pendingReview,

        /** 等待发货 */
        pendingShipment,

        /** 已发货 */
        shipped,

        /** 已收货 */
        received,

        /** 已完成 */
        completed,

        /** 已失败 */
        failed,

        /** 已取消 */
        canceled,

        /** 已拒绝 */
        denied;

        public static Status valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return null;
            }
            return values()[ordinal];
        }
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

    /**
     * 获取状态枚举
     *
     * @return
     *            类型
     */
    public Status getEnumStatus(){
        return this.status == null ? null : Status.values()[this.status.intValue()];
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private String     address;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private String     areaName;
    private Timestamp  completeDate;
    private String     consignee;
    private BigDecimal couponDiscount;
    private Long       exchangePoint;
    private Timestamp  expire;
    private BigDecimal fee;
    private BigDecimal freight;
    private String     invoiceContent;
    private String     invoiceTitle;
    private Boolean    isAllocatedStock;
    private Boolean    isExchangePoint;
    private Boolean    isUseCouponCode;
    private Timestamp  lockExpire;
    private String     lockKey;
    private String     memo;
    private BigDecimal offsetAmount;
    private String     paymentMethodName;
    private Integer    paymentMethodType;
    private String     phone;
    private BigDecimal price;
    private BigDecimal promotionDiscount;
    private String     promotionNames;
    private Integer    quantity;
    private BigDecimal refundAmount;
    private Integer    returnedQuantity;
    private Long       rewardPoint;
    private Integer    shippedQuantity;
    private String     shippingMethodName;
    private String     sn;
    private Integer    status;
    private BigDecimal tax;
    private Integer    type;
    private Integer    weight;
    private String     zipCode;
    private Long       area;
    private Long       couponCode;
    private Long       member;
    private Long       paymentMethod;
    private Long       shippingMethod;

    private TArea areaVO;

    private TPaymentMethod paymentMethodVO;

    private TShippingMethod shippingMethodVO;

    private TMember memberVO;

    private TCouponCode couponCodeVO;

    /** 促销名称 */
    private List<String> promotionNamesList = new ArrayList<String>();

    /** 赠送优惠券 */
    private List<TCoupon> couponsList = new ArrayList<TCoupon>();

    /** 订单项 */
    private List<TOrderItem> orderItemsList = new ArrayList<TOrderItem>();

    /** 支付记录 */
    private List<TPaymentLog> paymentLogsList = new ArrayList<TPaymentLog>();

    /** 收款单 */
    private List<TPayment> paymentsList = new ArrayList<TPayment>();

    /** 退款单 */
    private List<TRefunds> refundsList = new ArrayList<TRefunds>();

    /** 发货单 */
    private List<TShipping> shippingsList = new ArrayList<TShipping>();

    /** 退货单 */
    private List<TReturns> returnsList = new ArrayList<TReturns>();

    /** 订单记录 */
    private List<TOrderLog> orderLogsList = new ArrayList<TOrderLog>();

    public TOrder() {}

    public TOrder(TOrder value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.amount = value.amount;
        this.amountPaid = value.amountPaid;
        this.areaName = value.areaName;
        this.completeDate = value.completeDate;
        this.consignee = value.consignee;
        this.couponDiscount = value.couponDiscount;
        this.exchangePoint = value.exchangePoint;
        this.expire = value.expire;
        this.fee = value.fee;
        this.freight = value.freight;
        this.invoiceContent = value.invoiceContent;
        this.invoiceTitle = value.invoiceTitle;
        this.isAllocatedStock = value.isAllocatedStock;
        this.isExchangePoint = value.isExchangePoint;
        this.isUseCouponCode = value.isUseCouponCode;
        this.lockExpire = value.lockExpire;
        this.lockKey = value.lockKey;
        this.memo = value.memo;
        this.offsetAmount = value.offsetAmount;
        this.paymentMethodName = value.paymentMethodName;
        this.paymentMethodType = value.paymentMethodType;
        this.phone = value.phone;
        this.price = value.price;
        this.promotionDiscount = value.promotionDiscount;
        this.promotionNames = value.promotionNames;
        this.quantity = value.quantity;
        this.refundAmount = value.refundAmount;
        this.returnedQuantity = value.returnedQuantity;
        this.rewardPoint = value.rewardPoint;
        this.shippedQuantity = value.shippedQuantity;
        this.shippingMethodName = value.shippingMethodName;
        this.sn = value.sn;
        this.status = value.status;
        this.tax = value.tax;
        this.type = value.type;
        this.weight = value.weight;
        this.zipCode = value.zipCode;
        this.area = value.area;
        this.couponCode = value.couponCode;
        this.member = value.member;
        this.paymentMethod = value.paymentMethod;
        this.shippingMethod = value.shippingMethod;
    }

    public TOrder(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     address,
        BigDecimal amount,
        BigDecimal amountPaid,
        String     areaName,
        Timestamp  completeDate,
        String     consignee,
        BigDecimal couponDiscount,
        Long       exchangePoint,
        Timestamp  expire,
        BigDecimal fee,
        BigDecimal freight,
        String     invoiceContent,
        String     invoiceTitle,
        Boolean    isAllocatedStock,
        Boolean    isExchangePoint,
        Boolean    isUseCouponCode,
        Timestamp  lockExpire,
        String     lockKey,
        String     memo,
        BigDecimal offsetAmount,
        String     paymentMethodName,
        Integer    paymentMethodType,
        String     phone,
        BigDecimal price,
        BigDecimal promotionDiscount,
        String     promotionNames,
        Integer    quantity,
        BigDecimal refundAmount,
        Integer    returnedQuantity,
        Long       rewardPoint,
        Integer    shippedQuantity,
        String     shippingMethodName,
        String     sn,
        Integer    status,
        BigDecimal tax,
        Integer    type,
        Integer    weight,
        String     zipCode,
        Long       area,
        Long       couponCode,
        Long       member,
        Long       paymentMethod,
        Long       shippingMethod
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.address = address;
        this.amount = amount;
        this.amountPaid = amountPaid;
        this.areaName = areaName;
        this.completeDate = completeDate;
        this.consignee = consignee;
        this.couponDiscount = couponDiscount;
        this.exchangePoint = exchangePoint;
        this.expire = expire;
        this.fee = fee;
        this.freight = freight;
        this.invoiceContent = invoiceContent;
        this.invoiceTitle = invoiceTitle;
        this.isAllocatedStock = isAllocatedStock;
        this.isExchangePoint = isExchangePoint;
        this.isUseCouponCode = isUseCouponCode;
        this.lockExpire = lockExpire;
        this.lockKey = lockKey;
        this.memo = memo;
        this.offsetAmount = offsetAmount;
        this.paymentMethodName = paymentMethodName;
        this.paymentMethodType = paymentMethodType;
        this.phone = phone;
        this.price = price;
        this.promotionDiscount = promotionDiscount;
        this.promotionNames = promotionNames;
        this.quantity = quantity;
        this.refundAmount = refundAmount;
        this.returnedQuantity = returnedQuantity;
        this.rewardPoint = rewardPoint;
        this.shippedQuantity = shippedQuantity;
        this.shippingMethodName = shippingMethodName;
        this.sn = sn;
        this.status = status;
        this.tax = tax;
        this.type = type;
        this.weight = weight;
        this.zipCode = zipCode;
        this.area = area;
        this.couponCode = couponCode;
        this.member = member;
        this.paymentMethod = paymentMethod;
        this.shippingMethod = shippingMethod;

        this.promotionNamesList = JsonUtils.toObject(this.promotionNames,List.class);
    }

    public String statusName(){
        return TOrder.Status.valueOf(this.status).name();
    }

    public String typeName() {
        return TOrder.Type.valueOf(this.type).name();
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

    @NotEmpty(groups = Delivery.class)
    @Length(max = 200)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountPaid() {
        return this.amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Timestamp getCompleteDate() {
        return this.completeDate;
    }

    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
    }

    @NotEmpty(groups = Delivery.class)
    @Length(max = 200)
    public String getConsignee() {
        return this.consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public BigDecimal getCouponDiscount() {
        return this.couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Long getExchangePoint() {
        return this.exchangePoint;
    }

    public void setExchangePoint(Long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Timestamp getExpire() {
        return this.expire;
    }

    public void setExpire(Timestamp expire) {
        this.expire = expire;
    }

    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @NotNull(groups = Delivery.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getFreight() {
        return this.freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public String getInvoiceContent() {
        return this.invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceTitle() {
        return this.invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public Boolean getIsAllocatedStock() {
        return this.isAllocatedStock;
    }

    public void setIsAllocatedStock(Boolean isAllocatedStock) {
        this.isAllocatedStock = isAllocatedStock;
    }

    public Boolean getIsExchangePoint() {
        return this.isExchangePoint;
    }

    public void setIsExchangePoint(Boolean isExchangePoint) {
        this.isExchangePoint = isExchangePoint;
    }

    public Boolean getIsUseCouponCode() {
        return this.isUseCouponCode;
    }

    public void setIsUseCouponCode(Boolean isUseCouponCode) {
        this.isUseCouponCode = isUseCouponCode;
    }

    public Timestamp getLockExpire() {
        return this.lockExpire;
    }

    public void setLockExpire(Timestamp lockExpire) {
        this.lockExpire = lockExpire;
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @NotNull
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getOffsetAmount() {
        return this.offsetAmount;
    }

    public void setOffsetAmount(BigDecimal offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public String getPaymentMethodName() {
        return this.paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public Integer getPaymentMethodType() {
        return this.paymentMethodType;
    }

    public void setPaymentMethodType(Integer paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    @NotEmpty(groups = Delivery.class)
    @Length(max = 200)
    @Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPromotionDiscount() {
        return this.promotionDiscount;
    }

    public void setPromotionDiscount(BigDecimal promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public String getPromotionNames() {
        return this.promotionNames;
    }

    public void setPromotionNames(String promotionNames) {
        this.promotionNames = promotionNames;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getReturnedQuantity() {
        return this.returnedQuantity;
    }

    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    @Min(0)
    public Long getRewardPoint() {
        return this.rewardPoint;
    }

    public void setRewardPoint(Long rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public Integer getShippedQuantity() {
        return this.shippedQuantity;
    }

    public void setShippedQuantity(Integer shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    public String getShippingMethodName() {
        return this.shippingMethodName;
    }

    public void setShippingMethodName(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getTax() {
        return this.tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
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

    @NotEmpty(groups = Delivery.class)
    @Length(max = 200)
    @Pattern(regexp = "^\\d{6}$")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @NotNull(groups = Delivery.class)
    public Long getArea() {
        return this.area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Long getCouponCode() {
        return this.couponCode;
    }

    public void setCouponCode(Long couponCode) {
        this.couponCode = couponCode;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public Long getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(Long paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(Long shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public TArea getAreaVO() {
        return areaVO;
    }

    public void setAreaVO(TArea areaVO) {
        this.areaVO = areaVO;
    }

    public TPaymentMethod getPaymentMethodVO() {
        return paymentMethodVO;
    }

    public void setPaymentMethodVO(TPaymentMethod paymentMethodVO) {
        this.paymentMethodVO = paymentMethodVO;
    }

    public TMember getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(TMember memberVO) {
        this.memberVO = memberVO;
    }

    public TCouponCode getCouponCodeVO() {
        return couponCodeVO;
    }

    public void setCouponCodeVO(TCouponCode couponCodeVO) {
        this.couponCodeVO = couponCodeVO;
    }

    public TShippingMethod getShippingMethodVO() {
        return shippingMethodVO;
    }

    public void setShippingMethodVO(TShippingMethod shippingMethodVO) {
        this.shippingMethodVO = shippingMethodVO;
    }

    public List<String> getPromotionNamesList() {
        return promotionNamesList;
    }

    public void setPromotionNamesList(List<String> promotionNamesList) {
        this.promotionNamesList = promotionNamesList;
    }

    public List<TCoupon> getCouponsList() {
        return couponsList;
    }

    public void setCouponsList(List<TCoupon> couponsList) {
        this.couponsList = couponsList;
    }

    public List<TOrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<TOrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    public List<TPaymentLog> getPaymentLogsList() {
        return paymentLogsList;
    }

    public void setPaymentLogsList(List<TPaymentLog> paymentLogsList) {
        this.paymentLogsList = paymentLogsList;
    }

    public List<TPayment> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<TPayment> paymentsList) {
        this.paymentsList = paymentsList;
    }

    public List<TRefunds> getRefundsList() {
        return refundsList;
    }

    public void setRefundsList(List<TRefunds> refundsList) {
        this.refundsList = refundsList;
    }

    public List<TShipping> getShippingsList() {
        return shippingsList;
    }

    public void setShippingsList(List<TShipping> shippingsList) {
        this.shippingsList = shippingsList;
    }

    public List<TReturns> getReturnsList() {
        return returnsList;
    }

    public void setReturnsList(List<TReturns> returnsList) {
        this.returnsList = returnsList;
    }

    public List<TOrderLog> getOrderLogsList() {
        return orderLogsList;
    }

    public void setOrderLogsList(List<TOrderLog> orderLogsList) {
        this.orderLogsList = orderLogsList;
    }


    /**
     * 判断是否为新建对象
     *
     * @return 是否为新建对象
     */
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOrder (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(amount);
        sb.append(", ").append(amountPaid);
        sb.append(", ").append(areaName);
        sb.append(", ").append(completeDate);
        sb.append(", ").append(consignee);
        sb.append(", ").append(couponDiscount);
        sb.append(", ").append(exchangePoint);
        sb.append(", ").append(expire);
        sb.append(", ").append(fee);
        sb.append(", ").append(freight);
        sb.append(", ").append(invoiceContent);
        sb.append(", ").append(invoiceTitle);
        sb.append(", ").append(isAllocatedStock);
        sb.append(", ").append(isExchangePoint);
        sb.append(", ").append(isUseCouponCode);
        sb.append(", ").append(lockExpire);
        sb.append(", ").append(lockKey);
        sb.append(", ").append(memo);
        sb.append(", ").append(offsetAmount);
        sb.append(", ").append(paymentMethodName);
        sb.append(", ").append(paymentMethodType);
        sb.append(", ").append(phone);
        sb.append(", ").append(price);
        sb.append(", ").append(promotionDiscount);
        sb.append(", ").append(promotionNames);
        sb.append(", ").append(quantity);
        sb.append(", ").append(refundAmount);
        sb.append(", ").append(returnedQuantity);
        sb.append(", ").append(rewardPoint);
        sb.append(", ").append(shippedQuantity);
        sb.append(", ").append(shippingMethodName);
        sb.append(", ").append(sn);
        sb.append(", ").append(status);
        sb.append(", ").append(tax);
        sb.append(", ").append(type);
        sb.append(", ").append(weight);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(area);
        sb.append(", ").append(couponCode);
        sb.append(", ").append(member);
        sb.append(", ").append(paymentMethod);
        sb.append(", ").append(shippingMethod);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TOrder that = (TOrder) o;

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
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    @Transient
    public boolean getIsDelivery() {
        return CollectionUtils.exists(getOrderItemsList(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                TOrderItem orderItem = (TOrderItem) object;
                return orderItem != null && BooleanUtils.isTrue(orderItem.getIsDelivery());
            }
        });
    }

    /**
     * 获取应付金额
     *
     * @return 应付金额
     */
    @Transient
    public BigDecimal getAmountPayable() {
        if (!hasExpired() && !Integer.valueOf(TOrder.Status.completed.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.failed.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.canceled.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.denied.ordinal()).equals(getStatus())) {
            BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
            return amountPayable.compareTo(BigDecimal.ZERO) >= 0 ? amountPayable : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取应收金额
     *
     * @return 应收金额
     */
    @Transient
    public BigDecimal getAmountReceivable() {
        if (!hasExpired() && Integer.valueOf(TPaymentMethod.Type.cashOnDelivery.ordinal()).equals(getPaymentMethodType()) && !Integer.valueOf(TOrder.Status.completed.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.failed.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.canceled.ordinal()).equals(getStatus()) && !Integer.valueOf(TOrder.Status.denied.ordinal()).equals(getStatus())) {
            BigDecimal amountReceivable = getAmount().subtract(getAmountPaid());
            return amountReceivable.compareTo(BigDecimal.ZERO) >= 0 ? amountReceivable : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取应退金额
     *
     * @return 应退金额
     */
    @Transient
    public BigDecimal getRefundableAmount() {
        if (hasExpired() || Integer.valueOf(TOrder.Status.failed.ordinal()).equals(getStatus()) || Integer.valueOf(TOrder.Status.canceled.ordinal()).equals(getStatus()) || Integer.valueOf(TOrder.Status.denied.ordinal()).equals(getStatus())) {
            BigDecimal refundableAmount = getAmountPaid();
            return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
        }
        if (TOrder.Status.completed.equals(getStatus())) {
            BigDecimal refundableAmount = getAmountPaid().subtract(getAmount());
            return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取可发货数
     *
     * @return 可发货数
     */
    @Transient
    public int getShippableQuantity() {
        if (!hasExpired() && Integer.valueOf(TOrder.Status.pendingShipment.ordinal()).equals(getStatus())) {
            int shippableQuantity = getQuantity() - getShippedQuantity();
            return shippableQuantity >= 0 ? shippableQuantity : 0;
        }
        return 0;
    }

    /**
     * 获取可退货数
     *
     * @return 可退货数
     */
    @Transient
    public int getReturnableQuantity() {
        if (!hasExpired() && Integer.valueOf(TOrder.Status.failed.ordinal()).equals(getStatus())) {
            int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
            return returnableQuantity >= 0 ? returnableQuantity : 0;
        }
        return 0;
    }

    /**
     * 判断是否已过期
     *
     * @return 是否已过期
     */
    @Transient
    public boolean hasExpired() {
        return getExpire() != null && !getExpire().after(new Date());
    }

    /**
     * 获取订单项
     *
     * @param sn
     *            商品编号
     * @return 订单项
     */
    @Transient
    public TOrderItem getOrderItem(String sn) {
        if (StringUtils.isEmpty(sn) || CollectionUtils.isEmpty(getOrderItemsList())) {
            return null;
        }
        for (TOrderItem orderItem : getOrderItemsList()) {
            if (orderItem != null && StringUtils.equalsIgnoreCase(orderItem.getSn(), sn)) {
                return orderItem;
            }
        }
        return null;
    }
}
