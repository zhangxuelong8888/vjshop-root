
package com.vjshop.entity;

import com.vjshop.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 参数
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TParameter implements Serializable {

    private static final long serialVersionUID = -1082998739;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    parameterGroup;
    private String    names;
    private Long      productCategory;

    private TProductCategory productCategoryVO;

    private List<String> nameList = new ArrayList<>();

    public TParameter() {}

    public TParameter(TParameter value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.parameterGroup = value.parameterGroup;
        this.names = value.names;
        this.productCategory = value.productCategory;
    }

    public TParameter(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    parameterGroup,
        String    names,
        Long      productCategory
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.parameterGroup = parameterGroup;
        this.names = names;
        this.productCategory = productCategory;
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

    @Min(0)
    public Integer getOrders() {
        return this.orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    @NotEmpty
    @Length(max = 200)
    public String getParameterGroup() {
        return this.parameterGroup;
    }

    public void setParameterGroup(String parameterGroup) {
        this.parameterGroup = parameterGroup;
    }

    @NotEmpty
    public String getNames() {
        return this.names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    @NotNull(groups = ValidGroup.Save.class)
    public Long getProductCategory() {
        return this.productCategory;
    }

    public void setProductCategory(Long productCategory) {
        this.productCategory = productCategory;
    }

    public TProductCategory getProductCategoryVO() {
        return productCategoryVO;
    }

    public void setProductCategoryVO(TProductCategory productCategoryVO) {
        this.productCategoryVO = productCategoryVO;
    }

    public List<String> getNameList() {
        if(StringUtils.isNotBlank(this.names)){
            nameList = JsonUtils.toObject(this.names, List.class);
        }
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
        if(!CollectionUtils.isEmpty(nameList)){
            this.names = JsonUtils.toJson(nameList);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TParameter (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(parameterGroup);
        sb.append(", ").append(names);
        sb.append(", ").append(productCategory);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TParameter that = (TParameter) o;

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
