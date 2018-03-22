
package com.vjshop.entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity - 管理员
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TAdmin implements Serializable {

    private static final long serialVersionUID = 277914999;

    /** "登录令牌"Cookie名称 */
    public static final String LOGIN_TOKEN_COOKIE_NAME = "adminLoginToken";

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;

    private Set<TRole> rules = new HashSet<>();

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** E-mail */
    private String email;

    /** 姓名 */
    private String name;

    /** 部门 */
    private String department;

    /** 是否启用 */
    private Boolean isEnabled;

    /** 是否锁定 */
    private Boolean isLocked;

    /** 连续登录失败次数 */
    private Integer loginFailureCount;

    /** 锁定日期 */
    private Timestamp lockedDate;

    /** 最后登录日期 */
    private Timestamp loginDate;

    /** 最后登录IP */
    private String loginIp;

    /** 锁定KEY */
    private String lockKey;

    public TAdmin() {}

    public TAdmin(TAdmin value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.department = value.department;
        this.email = value.email;
        this.isEnabled = value.isEnabled;
        this.isLocked = value.isLocked;
        this.lockKey = value.lockKey;
        this.lockedDate = value.lockedDate;
        this.loginDate = value.loginDate;
        this.loginFailureCount = value.loginFailureCount;
        this.loginIp = value.loginIp;
        this.name = value.name;
        this.password = value.password;
        this.username = value.username;
    }

    public TAdmin(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    department,
        String    email,
        Boolean   isEnabled,
        Boolean   isLocked,
        String    lockKey,
        Timestamp lockedDate,
        Timestamp loginDate,
        Integer   loginFailureCount,
        String    loginIp,
        String    name,
        String    password,
        String    username
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.department = department;
        this.email = email;
        this.isEnabled = isEnabled;
        this.isLocked = isLocked;
        this.lockKey = lockKey;
        this.lockedDate = lockedDate;
        this.loginDate = loginDate;
        this.loginFailureCount = loginFailureCount;
        this.loginIp = loginIp;
        this.name = name;
        this.password = password;
        this.username = username;
    }


    public Set<TRole> getRules() {
        return rules;
    }

    public void setRules(Set<TRole> rules) {
        this.rules = rules;
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

    @Length(max = 200)
    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @NotEmpty
    @Email
    @Length(max = 200)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = StringUtils.isNotBlank(email) ? StringUtils.lowerCase(email) : email;
    }

    @NotNull
    public Boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Timestamp getLockedDate() {
        return this.lockedDate;
    }

    public void setLockedDate(Timestamp lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Timestamp getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public Integer getLoginFailureCount() {
        return this.loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(groups = ValidGroup.Save.class)
    @Length(min = 4, max = 20)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(groups = ValidGroup.Save.class)
    @Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$")
    @Length(min = 2, max = 20)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TAdmin (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(department);
        sb.append(", ").append(email);
        sb.append(", ").append(isEnabled);
        sb.append(", ").append(isLocked);
        sb.append(", ").append(lockKey);
        sb.append(", ").append(lockedDate);
        sb.append(", ").append(loginDate);
        sb.append(", ").append(loginFailureCount);
        sb.append(", ").append(loginIp);
        sb.append(", ").append(name);
        sb.append(", ").append(password);
        sb.append(", ").append(username);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TAdmin that = (TAdmin) o;

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
