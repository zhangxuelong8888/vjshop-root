
package com.vjshop.service.impl;

import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TGoodsDao;
import com.vjshop.dao.TProductDao;
import com.vjshop.dao.TStockLogDao;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TStockLog;
import com.vjshop.generated.db.tables.records.TStockLogRecord;
import com.vjshop.service.TStockLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 库存记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tStockLogServiceImpl")
public class TStockLogServiceImpl extends TBaseServiceImpl<TStockLogRecord,TStockLog, Long> implements TStockLogService {
    @Autowired
    private TStockLogDao tStockLogDao;
    @Autowired
    private TProductDao tProductDao;
    @Autowired
    private TGoodsDao tGoodsDao;
    public Page<TStockLog> findPage(Pageable pageable){
        Pageable newPageable = new Pageable();
        BeanUtils.copyProperties(pageable,newPageable);

        Page<TStockLog> stockLogPage = this.tStockLogDao.findPage(pageable);
        List<TStockLog> tStockLogs = new ArrayList<>(stockLogPage.getContent().size());
        for(TStockLog tStockLog : stockLogPage.getContent()){
            TProduct tProduct = this.tProductDao.find(tStockLog.getProduct());
            tProduct.setGoodsVO(this.tGoodsDao.find(tProduct.getGoods()));
            System.out.println(tProduct.getSpecifications());
            tStockLog.setProductVO(tProduct);
            tStockLogs.add(tStockLog);
        }
        Page<TStockLog> newPage = new Page<>(tStockLogs,stockLogPage.getTotal(),newPageable);
        return newPage;
    }
}