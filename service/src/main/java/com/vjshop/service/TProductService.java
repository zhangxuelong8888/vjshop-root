
package com.vjshop.service;

import com.vjshop.entity.TAdmin;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TStockLog;

import java.util.List;

/**
 * Service - 商品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TProductService extends TBaseService<TProduct, Long> {


    /**
     * 增加库存
     *
     * @param product  商品
     * @param amount   值
     * @param type     类型
     * @param operator 操作员
     * @param memo     备注
     */
    void addStock(TProduct product, int amount, TStockLog.Type type, TAdmin operator, String memo);

    /**
     * 增加已分配库存
     *
     * @param product 商品
     * @param amount  值
     */
    void addAllocatedStock(TProduct product, int amount);

    /**
     * 商品过滤
     *
     * @param products 商品
     */
    void filter(List<TProduct> products);


    /*********************************************************************************************************/

    TProduct findDetailById(Long productId);

    List<TProduct> search(String keyword, Integer count);

    /**
     * 编辑product
     */
    //void updates(TProduct product);

}