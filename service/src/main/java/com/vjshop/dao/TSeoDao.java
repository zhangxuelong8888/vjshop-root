
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TSeo;
import com.vjshop.generated.db.tables.records.TSeoRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao - SEO设置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TSeoDao extends JooqBaseDao<TSeoRecord, com.vjshop.entity.TSeo, Long> {

    public TSeoDao() {
        super(TSeo.T_SEO, com.vjshop.entity.TSeo.class);
    }

    @Autowired
    public TSeoDao(Configuration configuration) {
        super(TSeo.T_SEO, com.vjshop.entity.TSeo.class, configuration);
    }

    @Override
    public Long getId(com.vjshop.entity.TSeo object) {
        return object.getId();
    }

    /**
     * 查找SEO设置
     *
     * @param type
     *            类型
     * @return SEO设置
     */
    public com.vjshop.entity.TSeo find(com.vjshop.entity.TSeo.Type type){
        return fetchOne(TSeo.T_SEO.TYPE, type.ordinal());
    }

}
