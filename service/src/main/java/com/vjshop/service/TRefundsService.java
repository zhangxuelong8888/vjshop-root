
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TRefunds;

import java.util.List;

/**
 * Service - 退款单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TRefundsService extends TBaseService<TRefunds, Long> {

    List<TRefunds> findByOrderIds(Long... ids);

    TRefunds insertAndFetch(TRefunds tRefunds);

    Page<TRefunds> findPage(Pageable pageable);

    TRefunds findDetailById(Long id);
}