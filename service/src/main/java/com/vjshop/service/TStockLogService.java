
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TStockLog;

/**
 * Service - 库存记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TStockLogService extends TBaseService<TStockLog,Long> {

    Page<TStockLog> findPage(Pageable pageable);
}