
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TSpecificationDao;
import com.vjshop.entity.TSpecification;
import com.vjshop.generated.db.tables.records.TSpecificationRecord;
import com.vjshop.service.TProductCategoryService;
import com.vjshop.service.TSpecificationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 规格
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tSpecificationServiceImpl")
public class TSpecificationServiceImpl extends TBaseServiceImpl<TSpecificationRecord,TSpecification,Long> implements TSpecificationService {

    @Autowired
    private TSpecificationDao tSpecificationDao;

    @Autowired
    private TProductCategoryService tProductCategoryService;

    @Override
    public List<TSpecification> findAll() {
        List<TSpecification> tSpecifications = this.tSpecificationDao.findAll();
        for(TSpecification tSpecification : tSpecifications){
            tSpecification.setProductCategoryVO(this.tProductCategoryService.find(tSpecification.getProductCategory()));
        }
        return tSpecifications;
    }

    @Override
    public List<TSpecification> findByProductCategoryIds(Long... productCategoryIds){
        return this.tSpecificationDao.fetchByProductCategory(productCategoryIds);
    }

    @Override
    public Page<TSpecification> findPage(Pageable pageable){
        if(StringUtils.isBlank(pageable.getOrderProperty())){
            pageable.setOrderProperty("createDate");
            pageable.setOrderDirection(Order.Direction.desc);
        }
        Page<TSpecification> tSpecificationPage = this.tSpecificationDao.findPage(pageable);
        List<TSpecification> newContent = new ArrayList<>(tSpecificationPage.getContent().size());
        for(TSpecification tSpecification : tSpecificationPage.getContent()){
            tSpecification.setProductCategoryVO(this.tProductCategoryService.find(tSpecification.getProductCategory()));
            newContent.add(tSpecification);
        }
        return new Page<TSpecification>(newContent,tSpecificationPage.getTotal(),pageable);
    }

    @Override
    public TSpecification findDetailById(Long id){
        if(id == null ) return null;
        TSpecification tSpecification = super.find(id);
        tSpecification.setProductCategoryVO(this.tProductCategoryService.find(tSpecification.getProductCategory()));
        return tSpecification;
    }
}