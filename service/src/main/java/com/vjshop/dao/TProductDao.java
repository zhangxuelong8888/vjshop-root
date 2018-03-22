package com.vjshop.dao;

import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.records.TProductRecord;
import com.vjshop.util.SpringUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.Record1;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Dao - 商品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TProductDao extends JooqBaseDao<TProductRecord, com.vjshop.entity.TProduct, Long> {

    public TProductDao() {
        super(TProduct.T_PRODUCT, com.vjshop.entity.TProduct.class);
    }
    
    @Autowired
    public TProductDao(Configuration configuration) {
        super(TProduct.T_PRODUCT, com.vjshop.entity.TProduct.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TProduct object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TProduct> fetchByGoods(Long... values) {
        return fetch(TProduct.T_PRODUCT.GOODS, values);
    }

    public boolean snExists(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return false;
        }
        SelectQuery<Record1<Integer>> query = this.getDslContext().selectCount().from(TProduct.T_PRODUCT).where(TProduct.T_PRODUCT.SN.lower().eq(sn)).getQuery();
        return query.fetchOne(0, Integer.class) > 0;
    }

    /**
     * 下拉框模糊搜索product
     * @param keyword
     * @param count
     * @return
     */
    public List<com.vjshop.entity.TProduct> search(String keyword , Integer count){
        SelectQuery query = this.getDslContext().select(TProduct.T_PRODUCT.fields()).from(TProduct.T_PRODUCT).where(TProduct.T_PRODUCT.SN.like("%"+keyword+"%").or(TGoods.T_GOODS.NAME.like("%"+keyword+"%"))).getQuery();
        query.addJoin(TGoods.T_GOODS, JoinType.JOIN, TGoods.T_GOODS.ID.eq(TProduct.T_PRODUCT.GOODS));
        return super.findList(query,0,count,null,null);
    }
}
