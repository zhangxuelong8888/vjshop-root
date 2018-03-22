
package com.vjshop.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity - 插件配置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TPluginConfig implements Serializable {

    private static final long serialVersionUID = -455640310;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   orders;
    private String    attributes;
    private Boolean   isEnabled;
    private String    pluginId;

    /** 属性 */
    private Map<String, String> attributesMap = new HashMap<String, String>();

    public TPluginConfig() {}

    public TPluginConfig(TPluginConfig value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.orders = value.orders;
        this.attributes = value.attributes;
        this.isEnabled = value.isEnabled;
        this.pluginId = value.pluginId;
    }

    public TPluginConfig(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   orders,
        String    attributes,
        Boolean   isEnabled,
        String    pluginId
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.orders = orders;
        this.attributes = attributes;
        this.isEnabled = isEnabled;
        this.pluginId = pluginId;
    }

    public Map<String, String> getAttributesMap() {
        return attributesMap;
    }

    public String getAttributesMap(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return getAttributesMap() != null ? getAttributesMap().get(name) : null;
    }

    public void setAttributesMap(Map<String, String> attributesMap) {
        this.attributesMap = attributesMap;
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

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getPluginId() {
        return this.pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPluginConfig (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(orders);
        sb.append(", ").append(attributes);
        sb.append(", ").append(isEnabled);
        sb.append(", ").append(pluginId);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TPluginConfig that = (TPluginConfig) o;

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
