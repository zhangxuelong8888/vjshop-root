
package com.vjshop.service;

import com.vjshop.entity.TShippingItem;

import java.util.List;

/**
 * Service - 发货项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TShippingItemService extends TBaseService<TShippingItem, Long> {
    List<TShippingItem> findListByShippingIds(Long... ids);

    void insert(List<TShippingItem> tShippingItems);
}