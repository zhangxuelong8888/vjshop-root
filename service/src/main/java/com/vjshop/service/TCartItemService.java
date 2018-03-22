
package com.vjshop.service;

import com.vjshop.entity.TCartItem;

import java.util.List;

/**
 * Service - 购物车项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TCartItemService extends TBaseService<TCartItem, Long> {

    List<TCartItem> findListByCartId(Long cartId);

}