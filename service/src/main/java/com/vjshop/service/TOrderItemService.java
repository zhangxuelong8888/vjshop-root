
package com.vjshop.service;

import com.vjshop.entity.TOrderItem;

import java.util.List;

/**
 * Service - 订单项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TOrderItemService extends TBaseService<TOrderItem, Long> {

    List<TOrderItem> findDetailList(TOrderItem param);

    List<TOrderItem> findListByOrderId(Long orderId);

}