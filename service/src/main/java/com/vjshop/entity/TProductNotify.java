
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 到货通知
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TProductNotify implements Serializable {

    private static final long serialVersionUID = -971517324;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    email;
    private Boolean   hasSent;
    private Long      member;
    private Long      product;

    private TMember memberVO;
    private TProduct productVO;

    public TProductNotify() {}

    public TProductNotify(TProductNotify value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.email = value.email;
        this.hasSent = value.hasSent;
        this.member = value.member;
        this.product = value.product;
    }

    public TProductNotify(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    email,
        Boolean   hasSent,
        Long      member,
        Long      product
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.email = email;
        this.hasSent = hasSent;
        this.member = member;
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

    @NotEmpty
    @Email
    @Length(max = 200)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasSent() {
        return this.hasSent;
    }

    public void setHasSent(Boolean hasSent) {
        this.hasSent = hasSent;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    public Long getProduct() {
        return this.product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public TMember getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(TMember memberVO) {
        this.memberVO = memberVO;
    }

    public TProduct getProductVO() {
        return productVO;
    }

    public void setProductVO(TProduct productVO) {
        this.productVO = productVO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TProductNotify (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(email);
        sb.append(", ").append(hasSent);
        sb.append(", ").append(member);
        sb.append(", ").append(product);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TProductNotify that = (TProductNotify) o;

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
