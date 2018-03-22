
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TParameter;

import java.util.List;

/**
 * Service - 参数
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TParameterService extends TBaseService<TParameter, Long> {
    List<TParameter> findByProductCategoryIds(Long... productCategoryIds);

    TParameter findDetailById(Long id);

    Page<TParameter> findPage(Pageable pageable);
}