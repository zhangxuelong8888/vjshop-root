
package com.vjshop.service.impl;

import com.vjshop.dao.TProductCategoryDao;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TProductCategoryRecord;
import com.vjshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Service - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tProductCategoryServiceImpl")
public class TProductCategoryServiceImpl extends TBaseServiceImpl<TProductCategoryRecord,TProductCategory,Long> implements TProductCategoryService {

	@Autowired
	private TProductCategoryDao tProductCategoryDao;

	@Autowired
	private TGoodsService tGoodsService;

	@Autowired
	private TProductCategoryBrandService tProductCategoryBrandService;
	@Autowired
	private TProductCategoryPromotionService tProductCategoryPromotionService;
	@Autowired
	private TBrandService tBrandService;
	@Autowired
	private TPromotionService tPromotionService;
	@Autowired
	private TParameterService tParameterService;
	@Autowired
	private TAttributeService tAttributeService;
	@Autowired
	private TSpecificationService tSpecificationService;

	@Override
	public TProductCategory find(Long id) {
		return tProductCategoryDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<TProductCategory> findRoots() {
		return tProductCategoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<TProductCategory> findRoots(Integer count) {
		return tProductCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<TProductCategory> findRoots(Integer count, boolean useCache) {
		return tProductCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<TProductCategory> findParents(TProductCategory productCategory, boolean recursive, Integer count) {
		return tProductCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<TProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		TProductCategory productCategory = tProductCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return tProductCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<TProductCategory> findTree() {
		return this.tProductCategoryDao.findChildren(null,true,null);
	}

	@Transactional(readOnly = true)
	public List<TProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count) {
		return this.tProductCategoryDao.findChildren(productCategoryId, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<TProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		return this.tProductCategoryDao.findChildren(productCategoryId, recursive, count);
	}
	public TProductCategory findById(Long id){
		return this.tProductCategoryDao.find(id);
	}

	public TProductCategory findDetailById(Long id){
		Assert.notNull(id);
		TProductCategory tProductCategory = findById(id);
		tProductCategory.setChildren(this.tProductCategoryDao.fetchByParent(id));
		tProductCategory.setGoods(this.tGoodsService.findByProductcategory(id));
		tProductCategory.setParentVO(this.find(tProductCategory.getParent()));
		tProductCategory.setAttributes(this.tAttributeService.findByProductCategoryId(tProductCategory.getId()));
		tProductCategory.setParameters(this.tParameterService.findByProductCategoryIds(tProductCategory.getId()));
		tProductCategory.setSpecifications(this.tSpecificationService.findByProductCategoryIds(tProductCategory.getId()));

		List<TProductCategoryBrand> tProductCategoryBrands = this.tProductCategoryBrandService.findByProductCategoryIds(id);
		Long[] brandIds = new Long[tProductCategoryBrands.size()];
		for(int i = 0 ; i < tProductCategoryBrands.size() ; i ++){
			brandIds[i] = tProductCategoryBrands.get(i).getBrands();
		}
		tProductCategory.setBrands(this.tBrandService.findList(brandIds));

		List<TProductCategoryPromotion> tProductCategoryPromotions = this.tProductCategoryPromotionService.findByProductCategoryIds(id);
		Long[] promotionIds = new Long[tProductCategoryBrands.size()];
		for(int i = 0 ; i < tProductCategoryPromotions.size() ; i ++){
			promotionIds[i] = tProductCategoryPromotions.get(i).getPromotions();
		}
		tProductCategory.setPromotions(this.tPromotionService.findList(promotionIds));
		return tProductCategory;
	}

	@Override
	@Transactional
	public TProductCategory save(TProductCategory tProductCategory){
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tProductCategory.setCreateDate(now);
		tProductCategory.setModifyDate(now);
		tProductCategory.setVersion(0L);
		this.setValue(tProductCategory);
		TProductCategory insertEntity = this.tProductCategoryDao.insertAndFetch(tProductCategory);
		this.tProductCategoryBrandService.updateProductcategoryBrand(insertEntity.getId(),tProductCategory.getBrands());
		this.tProductCategoryPromotionService.updateProductCategoryPromotions(insertEntity.getId(),tProductCategory.getPromotions());
		return tProductCategory;
	}

	@Override
	@Transactional
	public TProductCategory update(TProductCategory tProductCategory,String... ignoreProperties){
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tProductCategory.setModifyDate(now);
		this.setValue(tProductCategory);
		TProductCategory insertEntity = super.update(tProductCategory,ignoreProperties);
		this.tProductCategoryBrandService.updateProductcategoryBrand(insertEntity.getId(),tProductCategory.getBrands());
		this.tProductCategoryPromotionService.updateProductCategoryPromotions(insertEntity.getId(),tProductCategory.getPromotions());

		return insertEntity;

	}

	@Override
	@Transactional
	public void deleteCascade(Long... ids){
		List<TProductCategoryBrand> tProductCategoryBrands = this.tProductCategoryBrandService.findByProductCategoryIds(ids);
		List<TProductCategoryPromotion> tProductCategoryPromotions = this.tProductCategoryPromotionService.findByProductCategoryIds(ids);
		List<TParameter> tParameters = this.tParameterService.findByProductCategoryIds(ids);
		List<TAttribute> tAttributes = this.tAttributeService.findByProductCategoryId(ids);
		List<TSpecification> tSpecifications = this.tSpecificationService.findByProductCategoryIds(ids);
		if(!CollectionUtils.isEmpty(tProductCategoryBrands)) this.tProductCategoryBrandService.delete(tProductCategoryBrands);
		if(!CollectionUtils.isEmpty(tProductCategoryPromotions))this.tProductCategoryPromotionService.delete(tProductCategoryPromotions);
		if(!CollectionUtils.isEmpty(tParameters))this.tParameterService.delete(tParameters);
		if(!CollectionUtils.isEmpty(tAttributes))this.tAttributeService.delete(tAttributes);
		if(!CollectionUtils.isEmpty(tSpecifications))this.tSpecificationService.delete(tSpecifications);

		super.delete(ids);
	}


	/**
	 * 设置值(分类的 treePath  grade)
	 * 
	 * @param productCategory
	 *            商品分类
	 */
	private void setValue(TProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		TProductCategory parent = this.find(productCategory.getParent());
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + TProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(TProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

}