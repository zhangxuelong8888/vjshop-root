
package com.vjshop.entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Entity - 咨询
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TConsultation implements Serializable {

    private static final long serialVersionUID = 1690902682;

    /** 路径前缀 */
    private static final String PATH_PREFIX = "/consultation/content";

    /** 路径后缀 */
    private static final String PATH_SUFFIX = ".jhtml";

    /** 会员 */
    private TMember memberInfo;

    /** 货品 */
    private TGoods goodsInfo;

    /** 咨询 */
    private TConsultation forConsultationInfo;

    /** 回复 */
    private Set<TConsultation> replyConsultations = new HashSet<TConsultation>();

    /**
     * 获取路径
     *
     * @return 路径
     */
    @Transient
    public String getPath() {
        return getGoods() != null && getGoods() != null ? PATH_PREFIX + "/" + getGoods() + PATH_SUFFIX : null;
    }

    public TMember getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(TMember memberInfo) {
        this.memberInfo = memberInfo;
    }

    public TGoods getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(TGoods goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public TConsultation getForConsultationInfo() {
        return forConsultationInfo;
    }

    public void setForConsultationInfo(TConsultation forConsultationInfo) {
        this.forConsultationInfo = forConsultationInfo;
    }

    public Set<TConsultation> getReplyConsultations() {
        return replyConsultations;
    }

    public void setReplyConsultations(Set<TConsultation> replyConsultations) {
        if (CollectionUtils.isEmpty(replyConsultations)){
            this.replyConsultations = null;
            return;
        }
        replyConsultations.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TConsultation that = (TConsultation) o;
                return null == that.getId();
            }
        });
        this.replyConsultations = replyConsultations;
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    content;
    private String    ip;
    private Boolean   isShow;
    private Long      forConsultation;
    private Long      goods;
    private Long      member;

    public TConsultation() {}

    public TConsultation(TConsultation value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.content = value.content;
        this.ip = value.ip;
        this.isShow = value.isShow;
        this.forConsultation = value.forConsultation;
        this.goods = value.goods;
        this.member = value.member;
    }

    public TConsultation(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            String content,
            String ip,
            Boolean isShow,
            Long forConsultation,
            Long goods,
            Long member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.content = content;
        this.ip = ip;
        this.isShow = isShow;
        this.forConsultation = forConsultation;
        this.goods = goods;
        this.member = member;
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

    @NotEmpty
    @Length(max = 200)
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

    public Boolean getIsShow() {
        return this.isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Long getForConsultation() {
        return this.forConsultation;
    }

    public void setForConsultation(Long forConsultation) {
        this.forConsultation = forConsultation;
    }

    public Long getGoods() {
        return this.goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }

    public Long getMember() {
        return this.member;
    }

    public void setMember(Long member) {
        this.member = member;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TConsultation (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(content);
        sb.append(", ").append(ip);
        sb.append(", ").append(isShow);
        sb.append(", ").append(forConsultation);
        sb.append(", ").append(goods);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TConsultation that = (TConsultation) o;

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
