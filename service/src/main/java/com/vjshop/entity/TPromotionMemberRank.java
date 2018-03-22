
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 促销-会员等级 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPromotionMemberRank implements Serializable {

    private static final long serialVersionUID = -2060896931;

    private Long promotions;
    private Long memberRanks;

    public TPromotionMemberRank() {}

    public TPromotionMemberRank(TPromotionMemberRank value) {
        this.promotions = value.promotions;
        this.memberRanks = value.memberRanks;
    }

    public TPromotionMemberRank(
        Long promotions,
        Long memberRanks
    ) {
        this.promotions = promotions;
        this.memberRanks = memberRanks;
    }

    public Long getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Long promotions) {
        this.promotions = promotions;
    }

    public Long getMemberRanks() {
        return this.memberRanks;
    }

    public void setMemberRanks(Long memberRanks) {
        this.memberRanks = memberRanks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPromotionMemberRank (");

        sb.append(promotions);
        sb.append(", ").append(memberRanks);

        sb.append(")");
        return sb.toString();
    }
}
