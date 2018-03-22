
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TReturns;

import java.util.List;

/**
 * Service - 退货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TReturnsService extends TBaseService<TReturns, Long> {

    List<TReturns> findByOrderIds(Long... ids);

    TReturns insertAndFetch(TReturns tReturns);

    TReturns findDetailById(Long id);

    Page<TReturns> findPage(Pageable pageable);
}