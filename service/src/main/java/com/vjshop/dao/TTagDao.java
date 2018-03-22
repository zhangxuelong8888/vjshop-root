
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TTag;
import com.vjshop.generated.db.tables.records.TTagRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 标签
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TTagDao extends JooqBaseDao<TTagRecord, com.vjshop.entity.TTag, Long> {

    public TTagDao() {
        super(TTag.T_TAG, com.vjshop.entity.TTag.class);
    }

    @Autowired
    public TTagDao(Configuration configuration) {
        super(TTag.T_TAG, com.vjshop.entity.TTag.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TTag object) {
        return object.getId();
    }

    /**
     * 查找实体对象
     *
     * @param ids
     *            ID
     * @return 实体对象
     */
    public List<com.vjshop.entity.TTag> fetchById(Long... ids) {
        return fetch(TTag.T_TAG.ID, ids);
    }

    /**
     * 查找标签
     *
     * @param type
     *            类型
     * @return 标签
     */
    public List<com.vjshop.entity.TTag> findList(com.vjshop.entity.TTag.Type type){
        SelectQuery query = getQuery();
        if (type != null){
            query.addConditions(TTag.T_TAG.TYPE.eq(type.ordinal()));
        }
        query.addOrderBy(TTag.T_TAG.ORDERS.asc());
        return resultSet2List(query.fetchResultSet());
    }

}
