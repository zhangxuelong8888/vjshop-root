
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TParameterDao;
import com.vjshop.entity.TParameter;
import com.vjshop.generated.db.tables.records.TParameterRecord;
import com.vjshop.service.TParameterService;
import com.vjshop.service.TProductCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 参数
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tParameterServiceImpl")
public class TParameterServiceImpl extends TBaseServiceImpl<TParameterRecord, TParameter, Long> implements TParameterService {
    @Autowired
    private TParameterDao tParameterDao;

    @Autowired
    private TProductCategoryService tProductCategoryService;


    public List<TParameter> findByProductCategoryIds(Long... productCategoryIds){
        return this.tParameterDao.fetchByProductCategory(productCategoryIds);
    }

    public TParameter findDetailById(Long id){
        TParameter tParameter = null;
        if(id != null) {
            tParameter = super.find(id);
            tParameter.setProductCategoryVO(this.tProductCategoryService.find(tParameter.getProductCategory()));
        }
        return tParameter;
    }

    public Page<TParameter> findPage(Pageable pageable){
        if(StringUtils.isBlank(pageable.getOrderProperty())) {
            pageable.setOrderProperty("create_Date");
            pageable.setOrderDirection(Order.Direction.desc);
        }

        Page<TParameter> tParameterPage = this.tParameterDao.findPage(pageable);
        List<TParameter> newContent = new ArrayList<>(tParameterPage.getContent().size());
        for(TParameter tParameter : tParameterPage.getContent()){
            tParameter.setProductCategoryVO(tParameter.getProductCategory()==null?null:this.tProductCategoryService.find(tParameter.getProductCategory()));
            newContent.add(tParameter);
        }
        Page<TParameter> newPage = new Page<>(newContent,tParameterPage.getTotal(),pageable);
        return newPage;
    }
}