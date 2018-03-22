
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 收货地址
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TReceiver implements Serializable {

    private static final long serialVersionUID = -1730138804;

    /** 收货地址最大保存数 */
    public static final Integer MAX_RECEIVER_COUNT = 10;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    /** 收货人 */
    private String consignee;

    /** 地区名称 */
    private String areaName;

    /** 地址 */
    private String address;

    /** 邮编 */
    private String zipCode;

    /** 电话 */
    private String phone;

    /** 是否默认 */
    private Boolean isDefault;

    /** 地区 */
    private Long      area;

    /** 会员 */
    private Long      member;

    private TArea areaVO;

    private TMember memberVO;

    public TReceiver() {}

    public TReceiver(TReceiver value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.areaName = value.areaName;
        this.consignee = value.consignee;
        this.isDefault = value.isDefault;
        this.phone = value.phone;
        this.zipCode = value.zipCode;
        this.area = value.area;
        this.member = value.member;
    }

    public TReceiver(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    address,
        String    areaName,
        String    consignee,
        Boolean   isDefault,
        String    phone,
        String    zipCode,
        Long      area,
        Long      member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.address = address;
        this.areaName = areaName;
        this.consignee = consignee;
        this.isDefault = isDefault;
        this.phone = phone;
        this.zipCode = zipCode;
        this.area = area;
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

    @NotEmpty
    @Length(max = 200)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @NotEmpty
    @Length(max = 200)
    public String getConsignee() {
        return this.consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @NotNull
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @NotEmpty
    @Length(max = 200)
    @Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NotEmpty
    @Length(max = 200)
    @Pattern(regexp = "^\\d{6}$")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @NotNull
    public Long getArea() {
        return this.area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public TArea getAreaVO() {
        return areaVO;
    }

    public void setAreaVO(TArea areaVO) {
        this.areaVO = areaVO;
    }

    public TMember getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(TMember memberVO) {
        this.memberVO = memberVO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TReceiver (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(areaName);
        sb.append(", ").append(consignee);
        sb.append(", ").append(isDefault);
        sb.append(", ").append(phone);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(area);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TReceiver that = (TReceiver) o;

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
