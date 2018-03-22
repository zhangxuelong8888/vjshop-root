package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TDeliveryCorpDao;
import com.vjshop.entity.TDeliveryCorp;
import com.vjshop.generated.db.tables.records.TDeliveryCorpRecord;
import com.vjshop.service.TDeliveryCorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 物流公司
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TDeliveryCorpServiceImpl implements TDeliveryCorpService {

    @Autowired
    private TDeliveryCorpDao tDeliveryCorpDao;

    /**
     * 保存
     *
     * @param tDeliveryCorp
     */
    @Override
    public void save(TDeliveryCorp tDeliveryCorp) {
        Assert.notNull(tDeliveryCorp);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        tDeliveryCorp.setModifyDate(now);
        tDeliveryCorp.setVersion(0L);
        if (null == tDeliveryCorp.getId()) {
            tDeliveryCorp.setCreateDate(now);
            this.insert(tDeliveryCorp);
        } else {
            this.updateSelective(tDeliveryCorp);
        }
    }

    /**
     * 插入
     *
     * @param tDeliveryCorp
     */
    @Override
    public void insert(TDeliveryCorp tDeliveryCorp) {
        Assert.notNull(tDeliveryCorp);

        tDeliveryCorpDao.insert(tDeliveryCorp);
    }

    /**
     * 更新
     *
     * @param tDeliveryCorp
     * @return
     */
    @Override
    public void updateSelective(TDeliveryCorp tDeliveryCorp) {
        Assert.notNull(tDeliveryCorp);

        tDeliveryCorpDao.update(tDeliveryCorp);
    }

    /**
     * 更新
     *
     * @param tDeliveryCorp
     * @return
     */
    @Override
    public void update(TDeliveryCorp tDeliveryCorp, String... ignoreProperties) {
        Assert.notNull(tDeliveryCorp);

        tDeliveryCorpDao.update(tDeliveryCorp, ignoreProperties);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    public void delete(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        tDeliveryCorpDao.deleteById(ids);
    }


    @Override
    public Page<TDeliveryCorp> findPage(Pageable pageable) {
        return tDeliveryCorpDao.findPage(pageable);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public TDeliveryCorp find(Long id) {
        return tDeliveryCorpDao.fetchOneById(id);
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<TDeliveryCorp> findAll() {
        return tDeliveryCorpDao.findAll();
    }
}