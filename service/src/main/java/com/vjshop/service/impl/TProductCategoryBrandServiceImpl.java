
package com.vjshop.service.impl;

import com.vjshop.dao.TProductCategoryBrandDao;
import com.vjshop.entity.TBrand;
import com.vjshop.entity.TProductCategoryBrand;
import com.vjshop.generated.db.tables.records.TProductCategoryBrandRecord;
import com.vjshop.service.TProductCategoryBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tProductCategoryBrandServiceImpl")
public class TProductCategoryBrandServiceImpl extends TBaseServiceImpl<TProductCategoryBrandRecord,TProductCategoryBrand,Long> implements TProductCategoryBrandService{

	@Autowired
	private TProductCategoryBrandDao tProductCategoryBrandDao;

	@Transactional
	public void updateProductcategoryBrand(Long productcategoryId, List<TBrand> tBrands){
		if(CollectionUtils.isEmpty(tBrands)) return;
		List<TProductCategoryBrand> tProductCategoryBrands = this.tProductCategoryBrandDao.fetchByProductCategories(productcategoryId);
		this.tProductCategoryBrandDao.delete(tProductCategoryBrands);
		List<TProductCategoryBrand> tProductCategoryBrandsNew = new ArrayList<>();
		for(TBrand tBrand : tBrands){
			TProductCategoryBrand tProductCategoryBrand = new TProductCategoryBrand();
			tProductCategoryBrand.setBrands(tBrand.getId());
			tProductCategoryBrand.setProductCategories(productcategoryId);
			tProductCategoryBrandsNew.add(tProductCategoryBrand);
		}
		this.tProductCategoryBrandDao.insert(tProductCategoryBrandsNew);
	}

	public List<TProductCategoryBrand> findByProductCategoryIds(Long... productCategoryIds){
		return this.tProductCategoryBrandDao.fetchByProductCategories(productCategoryIds);
	}

}