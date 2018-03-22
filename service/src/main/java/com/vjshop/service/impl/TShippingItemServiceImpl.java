
package com.vjshop.service.impl;

import com.vjshop.dao.TShippingItemDao;
import com.vjshop.entity.TShippingItem;
import com.vjshop.generated.db.tables.records.TShippingItemRecord;
import com.vjshop.service.TProductService;
import com.vjshop.service.TShippingItemService;
import com.vjshop.service.TShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service - 发货项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tShippingItemServiceImpl")
public class TShippingItemServiceImpl extends TBaseServiceImpl<TShippingItemRecord,TShippingItem, Long> implements TShippingItemService {
    @Autowired
    private TShippingItemDao tShippingItemDao;
    @Autowired
    private TProductService tProductService;
    @Autowired
    private TShippingService tShippingService;

    @Override
    public List<TShippingItem> findListByShippingIds(Long... ids){
        List<TShippingItem> tShippingItems = this.tShippingItemDao.fetchByShipping(ids);
        for(TShippingItem tShippingItem : tShippingItems){
            tShippingItem.setShippingVO(this.tShippingService.find(tShippingItem.getShipping()));
            tShippingItem.setProductVO(this.tProductService.find(tShippingItem.getProduct()));
        }
        return tShippingItems;
    }

    @Override
    @Transactional
    public void insert(List<TShippingItem> tShippingItems){
        this.tShippingItemDao.insert(tShippingItems);
    }
}