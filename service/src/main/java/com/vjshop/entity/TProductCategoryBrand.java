
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 商品分类-品牌 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TProductCategoryBrand implements Serializable {

    private static final long serialVersionUID = -1039178889;

    private Long productCategories;
    private Long brands;

    public TProductCategoryBrand() {}

    public TProductCategoryBrand(TProductCategoryBrand value) {
        this.productCategories = value.productCategories;
        this.brands = value.brands;
    }

    public TProductCategoryBrand(
        Long productCategories,
        Long brands
    ) {
        this.productCategories = productCategories;
        this.brands = brands;
    }

    public Long getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(Long productCategories) {
        this.productCategories = productCategories;
    }

    public Long getBrands() {
        return this.brands;
    }

    public void setBrands(Long brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TProductCategoryBrand (");

        sb.append(productCategories);
        sb.append(", ").append(brands);

        sb.append(")");
        return sb.toString();
    }
}
