
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
 * Entity - 发货点
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TDeliveryCenter implements Serializable {

    private static final long serialVersionUID = -1365681466;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    address;
    private String    areaName;
    private String    contact;
    private Boolean   isDefault;
    private String    memo;
    private String    mobile;
    private String    name;
    private String    phone;
    private String    zipCode;
    private Long      area;

    private TArea areaVO;

    public TDeliveryCenter() {}

    public TDeliveryCenter(TDeliveryCenter value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.address = value.address;
        this.areaName = value.areaName;
        this.contact = value.contact;
        this.isDefault = value.isDefault;
        this.memo = value.memo;
        this.mobile = value.mobile;
        this.name = value.name;
        this.phone = value.phone;
        this.zipCode = value.zipCode;
        this.area = value.area;
    }

    public TDeliveryCenter(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    address,
        String    areaName,
        String    contact,
        Boolean   isDefault,
        String    memo,
        String    mobile,
        String    name,
        String    phone,
        String    zipCode,
        Long      area
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.address = address;
        this.areaName = areaName;
        this.contact = contact;
        this.isDefault = isDefault;
        this.memo = memo;
        this.mobile = mobile;
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.area = area;
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
    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @NotNull
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Length(max = 200)
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TArea getAreaVO() {
        return areaVO;
    }

    public void setAreaVO(TArea areaVO) {
        this.areaVO = areaVO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TDeliveryCenter (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(address);
        sb.append(", ").append(areaName);
        sb.append(", ").append(contact);
        sb.append(", ").append(isDefault);
        sb.append(", ").append(memo);
        sb.append(", ").append(mobile);
        sb.append(", ").append(name);
        sb.append(", ").append(phone);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(area);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TDeliveryCenter that = (TDeliveryCenter) o;

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
     * 持久化前处理
     */
    public void prePersist() {
        if (getAreaVO() != null) {
            setAreaName(getAreaVO().getFullName());
        }
    }


}
