
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 日志
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TLog implements Serializable {

    private static final long serialVersionUID = 1412009003;

    /** "日志内容"属性名称 */
    public static final String LOG_CONTENT_ATTRIBUTE_NAME = TLog.class.getName() + ".CONTENT";

    /**
     * 设置操作员
     *
     * @param operator
     *            操作员
     */
    @Transient
    public void setOperator(TAdmin operator) {
        setOperator(operator != null ? operator.getUsername() : null);
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    /** 内容 */
    private String    content;
    private String    ip;
    /** 操作 */
    private String    operation;
    /** 操作员 */
    private String    operator;
    /** 请求参数 */
    private String    parameter;

    public TLog() {}

    public TLog(TLog value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.content = value.content;
        this.ip = value.ip;
        this.operation = value.operation;
        this.operator = value.operator;
        this.parameter = value.parameter;
    }

    public TLog(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    content,
        String    ip,
        String    operation,
        String    operator,
        String    parameter
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.content = content;
        this.ip = ip;
        this.operation = operation;
        this.operator = operator;
        this.parameter = parameter;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getParameter() {
        return this.parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TLog (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(content);
        sb.append(", ").append(ip);
        sb.append(", ").append(operation);
        sb.append(", ").append(operator);
        sb.append(", ").append(parameter);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TLog that = (TLog) o;

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
