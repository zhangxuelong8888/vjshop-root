
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TPointLog;
import com.vjshop.generated.db.tables.records.TPointLogRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao 积分记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TPointLogDao extends JooqBaseDao<TPointLogRecord, com.vjshop.entity.TPointLog, Long> {

    public TPointLogDao() {
        super(TPointLog.T_POINT_LOG, com.vjshop.entity.TPointLog.class);
    }

    @Autowired
    public TPointLogDao(Configuration configuration) {
        super(TPointLog.T_POINT_LOG, com.vjshop.entity.TPointLog.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TPointLog object) {
        return object.getId();
    }

}
