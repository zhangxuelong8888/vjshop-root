
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 评论
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TReview implements Serializable {

    private static final long serialVersionUID = 879674444;

    /** 路径前缀 */
    private static final String PATH_PREFIX = "/review/content";

    /** 路径后缀 */
    private static final String PATH_SUFFIX = ".jhtml";

    /**
     * 类型
     */
    public enum Type {

        /** 好评 */
        positive,

        /** 中评 */
        moderate,

        /** 差评 */
        negative
    }

    /** 会员 */
    private TMember memberInfo;

    /** 货品 */
    private TGoods goodsInfo;

    /**
     * 获取路径
     *
     * @return 路径
     */
    public String getPath() {
        return getGoods() != null ? PATH_PREFIX + "/" + getGoods() + PATH_SUFFIX : null;
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

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    content;
    private String    ip;
    private Boolean   isShow;
    private Integer   score;
    private Long      goods;
    private Long      member;

    public TReview() {}

    public TReview(TReview value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.content = value.content;
        this.ip = value.ip;
        this.isShow = value.isShow;
        this.score = value.score;
        this.goods = value.goods;
        this.member = value.member;
    }

    public TReview(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        String    content,
        String    ip,
        Boolean   isShow,
        Integer   score,
        Long      goods,
        Long      member
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.content = content;
        this.ip = ip;
        this.isShow = isShow;
        this.score = score;
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

    @NotNull
    @Min(1)
    @Max(5)
    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
        StringBuilder sb = new StringBuilder("TReview (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(content);
        sb.append(", ").append(ip);
        sb.append(", ").append(isShow);
        sb.append(", ").append(score);
        sb.append(", ").append(goods);
        sb.append(", ").append(member);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TReview that = (TReview) o;

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
