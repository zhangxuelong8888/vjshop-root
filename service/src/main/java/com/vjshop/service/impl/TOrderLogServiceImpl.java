
package com.vjshop.service.impl;

import com.vjshop.dao.TOrderLogDao;
import com.vjshop.entity.TOrderLog;
import com.vjshop.generated.db.tables.records.TOrderLogRecord;
import com.vjshop.service.TOrderLogService;
import com.vjshop.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service - 订单记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tOrderLogServiceImpl")
public class TOrderLogServiceImpl extends TBaseServiceImpl<TOrderLogRecord,TOrderLog, Long> implements TOrderLogService {
    @Autowired
    private TOrderLogDao tOrderLogDao;
    @Autowired
    private TOrderService tOrderService;

    @Override
    public List<TOrderLog> findByOrderIds(Long... ids){
        List<TOrderLog> tOrderLogs = this.tOrderLogDao.fetchByOrders(ids);
        for(TOrderLog tOrderLog : tOrderLogs){
            tOrderLog.setOrderVO(this.tOrderService.find(tOrderLog.getOrders()));
        }
        return tOrderLogs;
    }
    @Override
    public TOrderLog insertAndFetch(TOrderLog tOrderLog){
        return this.tOrderLogDao.insertAndFetch(tOrderLog);
    }
}