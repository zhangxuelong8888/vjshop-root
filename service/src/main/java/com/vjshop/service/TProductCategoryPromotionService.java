
package com.vjshop.service;

import com.vjshop.entity.TProductCategoryPromotion;
import com.vjshop.entity.TPromotion;

import java.util.List;

/**
 * Service - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TProductCategoryPromotionService extends TBaseService<TProductCategoryPromotion,Long>{
    void updateProductCategoryPromotions(Long productCategoryId,List<TPromotion> tPromotions);

    List<TProductCategoryPromotion> findByProductCategoryIds(Long... productCategoryIds);
}