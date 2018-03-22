
package com.vjshop.entity;

import com.fasterxml.jackson.databind.JavaType;
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
 * Entity - 规格
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TSpecification implements Serializable {

    private static final long serialVersionUID = -1151627476;

    private final JavaType optionListType = JsonUtils.getCollectionType(List.class, String.class);

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    name;
    private String    options;
    private Long      productCategory;

    /** 绑定分类 */
    private TProductCategory productCategoryVO;

    /** 可选项 */
    private List<String> optionsList = new ArrayList<String>();

    public TSpecification() {}

    public TSpecification(TSpecification value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.name = value.name;
        this.options = value.options;
        this.productCategory = value.productCategory;
    }

    public TSpecification(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    name,
        String    options,
        Long      productCategory
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.name = name;
        this.options = options;
        this.productCategory = productCategory;

        if(StringUtils.isNotBlank(this.options))
            this.optionsList = JsonUtils.toObject(this.options, optionListType);
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
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty
    public String getOptions() {
        return this.options;
    }

    public void setOptions(String options) {
        this.options = options;
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

    public List<String> getOptionsList() {
        if(StringUtils.isNotBlank(this.options))
            this.optionsList = JsonUtils.toObject(this.options, optionListType);
        return this.optionsList;
    }

    public void setOptionsList(List<String> optionsList) {
        this.optionsList = optionsList;
        if (!CollectionUtils.isEmpty(optionsList)){
            this.options = JsonUtils.toJson(optionsList);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TSpecification (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(name);
        sb.append(", ").append(options);
        sb.append(", ").append(productCategory);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TSpecification that = (TSpecification) o;

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
