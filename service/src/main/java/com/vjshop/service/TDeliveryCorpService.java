
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryCorp;

import java.util.List;

/**
 * Service - 物流公司
 * 
 * @author VJSHOP
 * @version 4.0
 */
public interface TDeliveryCorpService {

    /**
     * 保存
     * @param tDeliveryCorp
     */
    void save(TDeliveryCorp tDeliveryCorp);

    /**
     * 插入
     * @param tDeliveryCorp
     */
    void insert(TDeliveryCorp tDeliveryCorp);

    /**
     * 更新
     * @param tDeliveryCorp
     * @return
     */
    void update(TDeliveryCorp tDeliveryCorp, String... ignoreProperties);

    /**
     * 更新
     * @param tDeliveryCorp
     * @return
     */
    void updateSelective(TDeliveryCorp tDeliveryCorp);

    /**
     * 删除
     * @param ids
     * @return
     */
    void delete(Long[] ids);

    /**
     * 查询
     * @param pageable
     * @return
     */
    Page<TDeliveryCorp> findPage(Pageable pageable);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    TDeliveryCorp find(Long id);

    /**
     * 查询所有记录
     * @return
     */
    List<TDeliveryCorp> findAll();
}