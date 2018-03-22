
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 商品-促销 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TGoodsPromotion implements Serializable {

    private static final long serialVersionUID = -47413796;

    private Long goods;
    private Long promotions;

    public TGoodsPromotion() {}

    public TGoodsPromotion(TGoodsPromotion value) {
        this.goods = value.goods;
        this.promotions = value.promotions;
    }

    public TGoodsPromotion(
        Long goods,
        Long promotions
    ) {
        this.goods = goods;
        this.promotions = promotions;
    }

    public Long getGoods() {
        return this.goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }

    public Long getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Long promotions) {
        this.promotions = promotions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TGoodsPromotion (");

        sb.append(goods);
        sb.append(", ").append(promotions);

        sb.append(")");
        return sb.toString();
    }
}
