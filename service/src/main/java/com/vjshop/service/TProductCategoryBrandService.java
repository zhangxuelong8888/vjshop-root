
package com.vjshop.service;

import com.vjshop.entity.TBrand;
import com.vjshop.entity.TProductCategoryBrand;

import java.util.List;

/**
 * Service - 角色
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TProductCategoryBrandService extends TBaseService<TProductCategoryBrand, Long> {

    void updateProductcategoryBrand(Long productcategoryId, List<TBrand> tBrands);

    List<TProductCategoryBrand> findByProductCategoryIds(Long... productCategoryIds);
}