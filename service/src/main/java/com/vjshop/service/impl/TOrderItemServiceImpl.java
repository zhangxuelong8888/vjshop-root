
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.dao.TOrderItemDao;
import com.vjshop.entity.TOrderItem;
import com.vjshop.generated.db.tables.records.TOrderItemRecord;
import com.vjshop.service.TOrderItemService;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.vjshop.generated.db.tables.TOrderItem.T_ORDER_ITEM;

/**
 * Service - 订单项
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tOrderItemServiceImpl")
public class TOrderItemServiceImpl extends TBaseServiceImpl<TOrderItemRecord,TOrderItem, Long> implements TOrderItemService {
    @Autowired
    private TOrderItemDao tOrderItemDao;

    @Autowired
    private TProductService tProductService;
    @Autowired
    private TOrderService tOrderService;

    @Override
    public List<TOrderItem> findDetailList(TOrderItem param){
        List<TOrderItem> tOrderItems = this.tOrderItemDao.findList(param);
        for(TOrderItem tOrderItem : tOrderItems){
            tOrderItem.setOrderVO(this.tOrderService.find(tOrderItem.getOrders()));
            tOrderItem.setProductVO(this.tProductService.findDetailById(tOrderItem.getProduct()));
        }

        return tOrderItems;
    }

    @Override
    public List<TOrderItem> findListByOrderId(Long orderId) {
        return tOrderItemDao.findListByOrderId(orderId);
    }

    public TOrderItem save(TOrderItem orderItem){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        orderItem.setModifyDate(now);
        if (orderItem.getId() == null){
            orderItem.setCreateDate(now);
            orderItem.setVersion(0L);
        }
        TOrderItem nOrderItem = super.save(orderItem);
        orderItem.setId(nOrderItem != null ? nOrderItem.getId(): null);
        return orderItem;
    }
}