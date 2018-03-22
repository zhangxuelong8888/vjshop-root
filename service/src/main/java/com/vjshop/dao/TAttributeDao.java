
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TGoods;
import com.vjshop.generated.db.tables.TAttribute;
import com.vjshop.generated.db.tables.records.TAttributeRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Dao - 属性
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TAttributeDao extends JooqBaseDao<TAttributeRecord, com.vjshop.entity.TAttribute, Long> {

    public TAttributeDao() {
        super(TAttribute.T_ATTRIBUTE, com.vjshop.entity.TAttribute.class);
    }

    @Autowired
    public TAttributeDao(Configuration configuration) {
        super(TAttribute.T_ATTRIBUTE, com.vjshop.entity.TAttribute.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TAttribute object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TAttribute> findByProductCategory(Long... values) {
        return fetch(TAttribute.T_ATTRIBUTE.PRODUCT_CATEGORY, values);
    }

    /**
     * 查找未使用的属性序号
     *
     * @param productCategoryId
     *            商品分类ID
     * @return 未使用的属性序号，若不存在则返回null
     */
    public Integer findUnusedPropertyIndex(Long productCategoryId) {
        Assert.notNull(productCategoryId);

        for (int i = 0; i < TGoods.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            Long count = this.getDslContext().selectCount().from(TAttribute.T_ATTRIBUTE).where(TAttribute.T_ATTRIBUTE.PRODUCT_CATEGORY.eq(productCategoryId).and(TAttribute.T_ATTRIBUTE.PROPERTY_INDEX.eq(i))).fetchOne(0,Long.class);
            if (count.equals(0L)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 查找属性
     *
     * @param productCategoryId
     *            商品分类ID
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 属性
     */
    public List<com.vjshop.entity.TAttribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders){
        SelectQuery query = getQuery();
        if (productCategoryId != null){
            query.addConditions(TAttribute.T_ATTRIBUTE.PRODUCT_CATEGORY.eq(productCategoryId));
        }
        return findList(query, null, count, filters, orders);
    }
}
