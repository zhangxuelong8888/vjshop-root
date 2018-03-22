
package com.vjshop.service.impl;

import com.vjshop.dao.TReturnsItemDao;
import com.vjshop.entity.TReturnsItem;
import com.vjshop.generated.db.tables.records.TReturnsItemRecord;
import com.vjshop.service.TReturnsItemService;
import com.vjshop.service.TReturnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service - 退货单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tReturnsItemServiceImpl")
public class TReturnsItemServiceImpl extends TBaseServiceImpl<TReturnsItemRecord, TReturnsItem, Long> implements TReturnsItemService {

    @Autowired
    private TReturnsItemDao tReturnsItemDao;
    @Autowired
    private TReturnsService tReturnsService;

    @Override
    public List<TReturnsItem> findByReturnsIds(Long... ids) {
        List<TReturnsItem> tReturnsItems = this.tReturnsItemDao.fetchByReturns(ids);
        for(TReturnsItem tReturnsItem : tReturnsItems){
            tReturnsItem.setReturnsVO(this.tReturnsService.find(tReturnsItem.getReturns()));
        }
        return tReturnsItems;
    }

    public void insert(List<TReturnsItem> tReturnsItems){
        this.tReturnsItemDao.insert(tReturnsItems);
    }
}