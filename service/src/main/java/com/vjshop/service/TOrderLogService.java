
package com.vjshop.service;

import com.vjshop.entity.TOrderLog;

import java.util.List;

/**
 * Service - 订单记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TOrderLogService extends TBaseService<TOrderLog, Long> {
    List<TOrderLog> findByOrderIds(Long... ids);

    TOrderLog insertAndFetch(TOrderLog tOrderLog);
}