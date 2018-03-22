
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TAttributeDao;
import com.vjshop.dao.TGoodsDao;
import com.vjshop.entity.TAttribute;
import com.vjshop.generated.db.tables.records.TAttributeRecord;
import com.vjshop.service.TAttributeService;
import com.vjshop.service.TProductCategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 属性
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tAttributeServiceImpl")
public class TAttributeServiceImpl extends TBaseServiceImpl<TAttributeRecord,TAttribute,Long> implements TAttributeService {

	@Autowired
	private TAttributeDao tAttributeDao;
	@Autowired
	private TProductCategoryService tProductCategoryService;
	@Autowired
	private TGoodsDao tGoodsDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex(Long productCategoryId) {
		return this.tAttributeDao.findUnusedPropertyIndex(productCategoryId);
	}

	@Transactional(readOnly = true)
	public String toAttributeValue(TAttribute attribute, String value) {
		Assert.notNull(attribute);

		if (StringUtils.isEmpty(value) || CollectionUtils.isEmpty(attribute.getOptionsList()) || !attribute.getOptionsList().contains(value)) {
			return null;
		}
		return value;
	}

	@Override
	public List<TAttribute> findByProductCategoryId(Long... productCategordyIds){
		return this.tAttributeDao.findByProductCategory(productCategordyIds);
	}

	@Override
	public Page<TAttribute> findPage(Pageable pageable){
		if(StringUtils.isBlank(pageable.getOrderProperty())){
			pageable.setOrderProperty("createDate");
			pageable.setOrderDirection(Order.Direction.desc);
		}
		Page<TAttribute> tAttributePage = this.tAttributeDao.findPage(pageable);
		List<TAttribute> newContent = new ArrayList<>(tAttributePage.getContent().size());
		for(TAttribute tAttribute : tAttributePage.getContent()){
			tAttribute.setProductCategoryVO(this.tProductCategoryService.find(tAttribute.getProductCategory()));
			newContent.add(tAttribute);
		}
		return new Page<TAttribute>(newContent,tAttributePage.getTotal(),pageable);
	}

	public TAttribute findDetailById(Long id){
		if(id == null) return null;
		TAttribute tAttribute = super.find(id);
		tAttribute.setProductCategoryVO(this.tProductCategoryService.find(tAttribute.getProductCategory()));
		return tAttribute;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "attribute", condition = "#useCache")
	public List<TAttribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tAttributeDao.findList(productCategoryId, count, filters, orders);
	}

	public void delete(Long attributeId) {
		if(attributeId == null) return;
		TAttribute attribute = super.find(attributeId);
		if(attribute == null) return;
		tGoodsDao.clearAttributeValue(attribute.getPropertyIndex(), attribute.getProductCategory());
		super.delete(attributeId);
	}

}