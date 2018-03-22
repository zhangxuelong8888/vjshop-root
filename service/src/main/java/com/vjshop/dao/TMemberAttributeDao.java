
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TMember;
import com.vjshop.generated.db.tables.TMemberAttribute;
import com.vjshop.generated.db.tables.records.TMemberAttributeRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao 会员注册项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TMemberAttributeDao extends JooqBaseDao<TMemberAttributeRecord, com.vjshop.entity.TMemberAttribute, Long> {

    public TMemberAttributeDao() {
        super(TMemberAttribute.T_MEMBER_ATTRIBUTE, com.vjshop.entity.TMemberAttribute.class);
    }

    @Autowired
    public TMemberAttributeDao(Configuration configuration) {
        super(TMemberAttribute.T_MEMBER_ATTRIBUTE, com.vjshop.entity.TMemberAttribute.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TMemberAttribute object) {
        return object.getId();
    }

    /**
     * 查找未使用的属性序号
     *
     * @return 未使用的属性序号，若不存在则返回null
     */
    public Integer findUnusedPropertyIndex(){
        for (int i = 0; i < TMember.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            boolean isExists = getDslContext().fetchExists(TMemberAttribute.T_MEMBER_ATTRIBUTE, TMemberAttribute.T_MEMBER_ATTRIBUTE.PROPERTY_INDEX.eq(i));
            if (!isExists) {
                return i;
            }
        }
        return null;
    }

    /**
     * 查找会员注册项
     *
     * @param isEnabled
     *            是否启用
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 会员注册项
     */
    public List<com.vjshop.entity.TMemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders){
        SelectQuery query = getDslContext().selectFrom(TMemberAttribute.T_MEMBER_ATTRIBUTE)
                .where(TMemberAttribute.T_MEMBER_ATTRIBUTE.IS_ENABLED.eq(isEnabled))
                .getQuery();
        return super.findList(query, 0, count, filters, orders);
    }

}
