
package com.vjshop.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Entity - 会员注册项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TMemberAttribute implements Serializable {

    private static final long serialVersionUID = 1488036360;

    /**
     * 类型
     */
    public enum Type {

        /** 姓名 */
        name,

        /** 性别 */
        gender,

        /** 出生日期 */
        birth,

        /** 地区 */
        area,

        /** 地址 */
        address,

        /** 邮编 */
        zipCode,

        /** 电话 */
        phone,

        /** 手机 */
        mobile,

        /** 文本 */
        text,

        /** 单选项 */
        select,

        /** 多选项 */
        checkbox
    }

    /**
     * 获取类型（枚举）
     *
     * @return 类型
     */
    public Type getEnumType(){
        if (this.type == null || this.type < 0 || this.type >= Type.values().length) {
            return null;
        }
        return Type.values()[this.type];
    }

    /** 可选项 */
    private List<String> optionList = new ArrayList<String>();

    /**
     * 获取可选项
     *
     * @return 可选项
     */
    public List<String> getOptionList() {
        return optionList;
    }

    /**
     * 设置可选项
     */
    public void setOptionList(){
        if (StringUtils.isEmpty(this.options)) {
            this.optionList =  Collections.EMPTY_LIST;
        } else {
            this.optionList =  Arrays.asList(this.options.split(","));
        }
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private Boolean   isEnabled;
    private Boolean   isRequired;
    private String    name;
    private String    options;
    private String    pattern;
    private Integer   propertyIndex;
    private Integer   type;

    public TMemberAttribute() {}

    public TMemberAttribute(TMemberAttribute value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.isEnabled = value.isEnabled;
        this.isRequired = value.isRequired;
        this.name = value.name;
        this.options = value.options;
        this.pattern = value.pattern;
        this.propertyIndex = value.propertyIndex;
        this.type = value.type;
        setOptionList();
    }

    public TMemberAttribute(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        Boolean   isEnabled,
        Boolean   isRequired,
        String    name,
        String    options,
        String    pattern,
        Integer   propertyIndex,
        Integer   type
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.isEnabled = isEnabled;
        this.isRequired = isRequired;
        this.name = name;
        this.options = options;
        this.pattern = pattern;
        this.propertyIndex = propertyIndex;
        this.type = type;
        setOptionList();
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

    @NotNull
    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @NotNull
    public Boolean getIsRequired() {
        return this.isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return this.options;
    }

    public void setOptions(String options) {
        this.options = options;
        setOptionList();
    }

    @Length(max = 200)
    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getPropertyIndex() {
        return this.propertyIndex;
    }

    public void setPropertyIndex(Integer propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    @NotNull(groups = ValidGroup.Save.class)
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMemberAttribute (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(isEnabled);
        sb.append(", ").append(isRequired);
        sb.append(", ").append(name);
        sb.append(", ").append(options);
        sb.append(", ").append(pattern);
        sb.append(", ").append(propertyIndex);
        sb.append(", ").append(type);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TMemberAttribute that = (TMemberAttribute) o;

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
