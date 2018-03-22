
package com.vjshop.service;

import com.vjshop.entity.TReturnsItem;

import java.util.List;

/**
 * Service - 退货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TReturnsItemService extends TBaseService<TReturnsItem, Long> {

    List<TReturnsItem> findByReturnsIds(Long... ids);

    void insert(List<TReturnsItem> tReturnsItems);
}