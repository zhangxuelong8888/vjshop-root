package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TBrandDao;
import com.vjshop.entity.TBrand;
import com.vjshop.generated.db.tables.records.TBrandRecord;
import com.vjshop.service.TBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service - 品牌
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tBrandServiceImpl")
public class TBrandServiceImpl extends TBaseServiceImpl<TBrandRecord,TBrand,Long> implements TBrandService {

    @Autowired
    private TBrandDao tBrandDao;

    @Override
    public TBrand findDetails(Long brandId) {
        return tBrandDao.findDetails(brandId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "brand", condition = "#useCache")
    public List<TBrand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
        return tBrandDao.findList(productCategoryId, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TBrand> findAll() {
        return tBrandDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TBrand> findPage(Pageable pageable){
        if(StringUtils.isBlank(pageable.getOrderProperty())){
            pageable.setOrderProperty("orders");
            pageable.setOrderDirection(Order.Direction.asc);
        }
        return this.tBrandDao.findPage(pageable);
    }

}