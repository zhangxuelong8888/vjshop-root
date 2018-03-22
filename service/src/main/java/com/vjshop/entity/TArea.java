
package com.vjshop.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 地区
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TArea implements Serializable {

    private static final long serialVersionUID = 2121951687;

    /** 树路径分隔符 */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 获取所有上级地区ID
     *
     * @return 所有上级地区ID
     */
    @Transient
    public Long[] getParentIds() {
        String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
        Long[] result = new Long[parentIds.length];
        for (int i = 0; i < parentIds.length; i++) {
            result[i] = Long.valueOf(parentIds[i]);
        }
        return result;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    fullName;
    private Integer   grade;
    private String    name;
    private String    treePath;
    private Long      parent;

    private TArea     parentArea;
    public TArea() {}

    public TArea(TArea value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.fullName = value.fullName;
        this.grade = value.grade;
        this.name = value.name;
        this.treePath = value.treePath;
        this.parent = value.parent;
    }

    public TArea(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    fullName,
        Integer   grade,
        String    name,
        String    treePath,
        Long      parent
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.fullName = fullName;
        this.grade = grade;
        this.name = name;
        this.treePath = treePath;
        this.parent = parent;
    }


    public TArea getParentArea() {
        return parentArea;
    }

    public void setParentArea(TArea parentArea) {
        this.parentArea = parentArea;
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

    public Integer getOrders() {
        return this.orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreePath() {
        return this.treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public Long getParent() {
        return this.parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TArea (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(fullName);
        sb.append(", ").append(grade);
        sb.append(", ").append(name);
        sb.append(", ").append(treePath);
        sb.append(", ").append(parent);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TArea that = (TArea) o;

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
