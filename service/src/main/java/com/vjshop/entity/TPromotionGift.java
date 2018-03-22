
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 促销-赠品 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TPromotionGift implements Serializable {

    private static final long serialVersionUID = 1133985115;

    private Long giftPromotions;
    private Long gifts;

    public TPromotionGift() {}

    public TPromotionGift(TPromotionGift value) {
        this.giftPromotions = value.giftPromotions;
        this.gifts = value.gifts;
    }

    public TPromotionGift(
        Long giftPromotions,
        Long gifts
    ) {
        this.giftPromotions = giftPromotions;
        this.gifts = gifts;
    }

    public Long getGiftPromotions() {
        return this.giftPromotions;
    }

    public void setGiftPromotions(Long giftPromotions) {
        this.giftPromotions = giftPromotions;
    }

    public Long getGifts() {
        return this.gifts;
    }

    public void setGifts(Long gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TPromotionGift (");

        sb.append(giftPromotions);
        sb.append(", ").append(gifts);

        sb.append(")");
        return sb.toString();
    }
}
