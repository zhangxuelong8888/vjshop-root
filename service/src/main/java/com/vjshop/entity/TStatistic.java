
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Entity - 统计
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TStatistic implements Serializable {

    private static final long serialVersionUID = -1169997225;

    /**
     * 周期
     */
    public enum Period {

        /** 年 */
        year,

        /** 月 */
        month,

        /** 日 */
        day
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    /** 年 */
    private Integer year;

    /** 月 */
    private Integer month;

    /** 日 */
    private Integer day;

    /** 会员注册数 */
    private Long registerMemberCount;

    /** 订单创建数 */
    private Long createOrderCount;

    /** 订单完成数 */
    private Long completeOrderCount;

    /** 订单创建金额 */
    private BigDecimal createOrderAmount;

    /** 订单完成金额 */
    private BigDecimal completeOrderAmount;

    public TStatistic() {}

    /**
     * 构造方法
     *
     * @param year
     *            年
     * @param registerMemberCount
     *            会员注册数
     * @param createOrderCount
     *            订单创建数
     * @param completeOrderCount
     *            订单完成数
     * @param createOrderAmount
     *            订单创建金额
     * @param completeOrderAmount
     *            订单完成金额
     */
    public TStatistic(Integer year, Long registerMemberCount, Long createOrderCount, Long completeOrderCount, BigDecimal createOrderAmount, BigDecimal completeOrderAmount) {
        this.year = year;
        this.registerMemberCount = registerMemberCount;
        this.createOrderCount = createOrderCount;
        this.completeOrderCount = completeOrderCount;
        this.createOrderAmount = createOrderAmount;
        this.completeOrderAmount = completeOrderAmount;
    }

    /**
     * 构造方法
     *
     * @param year
     *            年
     * @param month
     *            月
     * @param registerMemberCount
     *            会员注册数
     * @param createOrderCount
     *            订单创建数
     * @param completeOrderCount
     *            订单完成数
     * @param createOrderAmount
     *            订单创建金额
     * @param completeOrderAmount
     *            订单完成金额
     */
    public TStatistic(Integer year, Integer month, Long registerMemberCount, Long createOrderCount, Long completeOrderCount, BigDecimal createOrderAmount, BigDecimal completeOrderAmount) {
        this.year = year;
        this.month = month;
        this.registerMemberCount = registerMemberCount;
        this.createOrderCount = createOrderCount;
        this.completeOrderCount = completeOrderCount;
        this.createOrderAmount = createOrderAmount;
        this.completeOrderAmount = completeOrderAmount;
    }

    /**
     * 构造方法
     *
     * @param year
     *            年
     * @param month
     *            月
     * @param day
     *            日
     * @param registerMemberCount
     *            会员注册数
     * @param createOrderCount
     *            订单创建数
     * @param completeOrderCount
     *            订单完成数
     * @param createOrderAmount
     *            订单创建金额
     * @param completeOrderAmount
     *            订单完成金额
     */
    public TStatistic(Integer year, Integer month, Integer day, Long registerMemberCount, Long createOrderCount, Long completeOrderCount, BigDecimal createOrderAmount, BigDecimal completeOrderAmount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.registerMemberCount = registerMemberCount;
        this.createOrderCount = createOrderCount;
        this.completeOrderCount = completeOrderCount;
        this.createOrderAmount = createOrderAmount;
        this.completeOrderAmount = completeOrderAmount;
    }

    public TStatistic(TStatistic value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.completeOrderAmount = value.completeOrderAmount;
        this.completeOrderCount = value.completeOrderCount;
        this.createOrderAmount = value.createOrderAmount;
        this.createOrderCount = value.createOrderCount;
        this.day = value.day;
        this.month = value.month;
        this.registerMemberCount = value.registerMemberCount;
        this.year = value.year;
    }

    public TStatistic(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        BigDecimal completeOrderAmount,
        Long       completeOrderCount,
        BigDecimal createOrderAmount,
        Long       createOrderCount,
        Integer    day,
        Integer    month,
        Long       registerMemberCount,
        Integer    year
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.completeOrderAmount = completeOrderAmount;
        this.completeOrderCount = completeOrderCount;
        this.createOrderAmount = createOrderAmount;
        this.createOrderCount = createOrderCount;
        this.day = day;
        this.month = month;
        this.registerMemberCount = registerMemberCount;
        this.year = year;
    }

    /**
     * 获取日期
     *
     * @return 日期
     */
    @Transient
    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        if (getYear() != null) {
            calendar.set(Calendar.YEAR, getYear());
        }
        if (getMonth() != null) {
            calendar.set(Calendar.MONTH, getMonth());
        }
        if (getDay() != null) {
            calendar.set(Calendar.DAY_OF_MONTH, getDay());
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
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

    public BigDecimal getCompleteOrderAmount() {
        return this.completeOrderAmount;
    }

    public void setCompleteOrderAmount(BigDecimal completeOrderAmount) {
        this.completeOrderAmount = completeOrderAmount;
    }

    public Long getCompleteOrderCount() {
        return this.completeOrderCount;
    }

    public void setCompleteOrderCount(Long completeOrderCount) {
        this.completeOrderCount = completeOrderCount;
    }

    public BigDecimal getCreateOrderAmount() {
        return this.createOrderAmount;
    }

    public void setCreateOrderAmount(BigDecimal createOrderAmount) {
        this.createOrderAmount = createOrderAmount;
    }

    public Long getCreateOrderCount() {
        return this.createOrderCount;
    }

    public void setCreateOrderCount(Long createOrderCount) {
        this.createOrderCount = createOrderCount;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getRegisterMemberCount() {
        return this.registerMemberCount;
    }

    public void setRegisterMemberCount(Long registerMemberCount) {
        this.registerMemberCount = registerMemberCount;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TStatistic (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(completeOrderAmount);
        sb.append(", ").append(completeOrderCount);
        sb.append(", ").append(createOrderAmount);
        sb.append(", ").append(createOrderCount);
        sb.append(", ").append(day);
        sb.append(", ").append(month);
        sb.append(", ").append(registerMemberCount);
        sb.append(", ").append(year);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TStatistic that = (TStatistic) o;

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
