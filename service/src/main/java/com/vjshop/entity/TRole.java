
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Entity - 角色
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TRole implements Serializable {

    private static final long serialVersionUID = 82959234;

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    authorities;
    private String    description;
    private Boolean   isSystem;
    private String    name;

    private List<String> authoritiesList;

    public TRole() {}

    public TRole(TRole value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.authorities = value.authorities;
        this.description = value.description;
        this.isSystem = value.isSystem;
        this.name = value.name;
    }

    public TRole(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    authorities,
        String    description,
        Boolean   isSystem,
        String    name
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.authorities = authorities;
        this.description = description;
        this.isSystem = isSystem;
        this.name = name;
    }

    public List<String> getAuthoritiesList() {
        return authoritiesList;
    }

    public void setAuthoritiesList(List<String> authoritiesList) {
        this.authoritiesList = authoritiesList;
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

    @Length(max = 4000)
    public String getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Length(max = 200)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TRole (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(authorities);
        sb.append(", ").append(description);
        sb.append(", ").append(isSystem);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TRole that = (TRole) o;

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
