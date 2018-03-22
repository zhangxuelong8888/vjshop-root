
package com.vjshop.service.impl;

import com.vjshop.dao.TCartItemDao;
import com.vjshop.entity.TCartItem;
import com.vjshop.generated.db.tables.records.TCartItemRecord;
import com.vjshop.service.TCartItemService;
import com.vjshop.service.TCartService;
import com.vjshop.service.TProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service - 购物车项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tCartItemServiceImpl")
public class TCartItemServiceImpl extends TBaseServiceImpl<TCartItemRecord,TCartItem, Long> implements TCartItemService {

    @Autowired
    private TCartItemDao tCartItemDao;
    @Autowired
    private TCartService tCartService;
    @Autowired
    private TProductService tProductService;

    public List<TCartItem> findListByCartId(Long cartId){
        List<TCartItem> tCartItems = this.tCartItemDao.findList(cartId);
        for(TCartItem tCartItem : tCartItems){
            tCartItem.setProductVO(this.tProductService.findDetailById(tCartItem.getProduct()));
            tCartItem.setCartVO(this.tCartService.find(tCartItem.getCart()));
        }

        return tCartItems;
    }
}