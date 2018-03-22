package com.vjshop.entity;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * 系统用户角色关系实体类
 */
public class TAdminRole implements Serializable {

    private static final long serialVersionUID = -866600383;

    private Long admins;
    private Long roles;

    public TAdminRole() {}

    public TAdminRole(TAdminRole value) {
        this.admins = value.admins;
        this.roles = value.roles;
    }

    public TAdminRole(
        Long admins,
        Long roles
    ) {
        this.admins = admins;
        this.roles = roles;
    }

    public Long getAdmins() {
        return this.admins;
    }

    public void setAdmins(Long admins) {
        this.admins = admins;
    }

    public Long getRoles() {
        return this.roles;
    }

    public void setRoles(Long roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TAdminRole (");

        sb.append(admins);
        sb.append(", ").append(roles);

        sb.append(")");
        return sb.toString();
    }
}
