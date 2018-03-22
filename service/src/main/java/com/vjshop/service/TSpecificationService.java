
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TParameter;
import com.vjshop.entity.TSpecification;

import java.util.List;

/**
 * Service - 规格
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TSpecificationService extends TBaseService<TSpecification,Long>{

    List<TSpecification> findAll();

    List<TSpecification> findByProductCategoryIds(Long... productCategoryIds);

    Page<TSpecification> findPage(Pageable pageable);

    TSpecification findDetailById(Long id);
}