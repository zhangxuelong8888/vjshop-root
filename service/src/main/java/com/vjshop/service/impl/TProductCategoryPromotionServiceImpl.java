
package com.vjshop.service.impl;

import com.vjshop.dao.TProductCategoryPromotionDao;
import com.vjshop.entity.TProductCategoryPromotion;
import com.vjshop.entity.TPromotion;
import com.vjshop.generated.db.tables.records.TProductCategoryPromotionRecord;
import com.vjshop.service.TProductCategoryPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service - 商品分类
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tProductCategoryServicePromotionImpl")
public class TProductCategoryPromotionServiceImpl extends TBaseServiceImpl<TProductCategoryPromotionRecord, TProductCategoryPromotion, Long> implements TProductCategoryPromotionService {

    @Autowired
    private TProductCategoryPromotionDao tProductCategoryPromotionDao;

    @Override
    public void updateProductCategoryPromotions(Long productCategoryId, List<TPromotion> tPromotions) {
        if (CollectionUtils.isEmpty(tPromotions)) return;
        List<TProductCategoryPromotion> tProductCategoryPromotions = this.tProductCategoryPromotionDao.fetchByProductCategories(productCategoryId);
        this.tProductCategoryPromotionDao.delete(tProductCategoryPromotions);
        List<TProductCategoryPromotion> tProductCategoryPromotionsList = new ArrayList<>();
        for(TPromotion tPromotion : tPromotions){
            TProductCategoryPromotion tProductCategoryPromotion = new TProductCategoryPromotion();
            tProductCategoryPromotion.setProductCategories(productCategoryId);
            tProductCategoryPromotion.setPromotions(tPromotion.getId());
            tProductCategoryPromotionsList.add(tProductCategoryPromotion);
        }
        this.tProductCategoryPromotionDao.insert(tProductCategoryPromotionsList);
    }

    public List<TProductCategoryPromotion> findByProductCategoryIds(Long... productCategoryIds){
        return this.tProductCategoryPromotionDao.fetchByProductCategories(productCategoryIds);
    }


}