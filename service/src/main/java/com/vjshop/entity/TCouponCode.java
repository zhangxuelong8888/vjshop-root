
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 优惠码
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TCouponCode implements Serializable {

    private static final long serialVersionUID = -265968338;

    /** 优惠券 */
    private TCoupon couponInfo;

    public TCoupon getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(TCoupon couponInfo) {
        this.couponInfo = couponInfo;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    code;
    private Boolean   isUsed;
    private Timestamp usedDate;
    private Long      coupon;
    private Long      member;

    public TCouponCode() {}

    public TCouponCode(TCouponCode value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.code = value.code;
        this.isUsed = value.isUsed;
        this.usedDate = value.usedDate;
        this.coupon = value.coupon;
        this.member = value.member;
    }

    public TCouponCode(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            String code,
            Boolean isUsed,
            Timestamp usedDate,
            Long coupon,
            Long member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.code = code;
        this.isUsed = isUsed;
        this.usedDate = usedDate;
        this.coupon = coupon;
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

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Timestamp getUsedDate() {
        return this.usedDate;
    }

    public void setUsedDate(Timestamp usedDate) {
        this.usedDate = usedDate;
    }

    public Long getCoupon() {
        return this.coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TCouponCode (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(code);
        sb.append(", ").append(isUsed);
        sb.append(", ").append(usedDate);
        sb.append(", ").append(coupon);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TCouponCode that = (TCouponCode) o;

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
