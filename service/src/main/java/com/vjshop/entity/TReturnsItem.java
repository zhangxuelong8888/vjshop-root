
package com.vjshop.entity;

import com.vjshop.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 退货项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TReturnsItem implements Serializable {

    private static final long serialVersionUID = -667634491;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    name;
    private Integer   quantity;
    private String    sn;
    private String    specifications;
    private Long      returns;


    private TReturns returnsVO;
    /** 规格 */
    private List<String> specificationsList = new ArrayList<String>();
    public TReturnsItem() {}

    public TReturnsItem(TReturnsItem value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.name = value.name;
        this.quantity = value.quantity;
        this.sn = value.sn;
        this.specifications = value.specifications;
        this.returns = value.returns;
    }

    public TReturnsItem(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    name,
        Integer   quantity,
        String    sn,
        String    specifications,
        Long      returns
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.name = name;
        this.quantity = quantity;
        this.sn = sn;
        this.specifications = specifications;
        this.returns = returns;
        if(StringUtils.isNotBlank(this.specifications)){
            this.specificationsList = JsonUtils.toObject(this.specifications,List.class);
        }
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Min(1)
    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Long getReturns() {
        return this.returns;
    }

    public void setReturns(Long returns) {
        this.returns = returns;
    }

    public TReturns getReturnsVO() {
        return returnsVO;
    }

    public void setReturnsVO(TReturns returnsVO) {
        this.returnsVO = returnsVO;
    }

    public List<String> getSpecificationsList() {
        return specificationsList;
    }

    public void setSpecificationsList(List<String> specificationsList) {
        this.specificationsList = specificationsList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TReturnsItem (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(name);
        sb.append(", ").append(quantity);
        sb.append(", ").append(sn);
        sb.append(", ").append(specifications);
        sb.append(", ").append(returns);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TReturnsItem that = (TReturnsItem) o;

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
