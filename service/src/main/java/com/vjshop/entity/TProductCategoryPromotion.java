
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 商品分类-促销 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TProductCategoryPromotion implements Serializable {

    private static final long serialVersionUID = -1633974821;

    private Long productCategories;
    private Long promotions;

    public TProductCategoryPromotion() {}

    public TProductCategoryPromotion(TProductCategoryPromotion value) {
        this.productCategories = value.productCategories;
        this.promotions = value.promotions;
    }

    public TProductCategoryPromotion(
        Long productCategories,
        Long promotions
    ) {
        this.productCategories = productCategories;
        this.promotions = promotions;
    }

    public Long getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(Long productCategories) {
        this.productCategories = productCategories;
    }

    public Long getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Long promotions) {
        this.promotions = promotions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TProductCategoryPromotion (");

        sb.append(productCategories);
        sb.append(", ").append(promotions);

        sb.append(")");
        return sb.toString();
    }
}
