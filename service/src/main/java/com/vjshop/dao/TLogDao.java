
package com.vjshop.dao;

import com.vjshop.generated.db.tables.records.TLogRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TLog.T_LOG;

/**
 * Dao - 日志
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TLogDao extends JooqBaseDao<TLogRecord, com.vjshop.entity.TLog, Long> {

    /**
     * 删除所有日志
     */
    public void removeAll(){
        getDslContext().deleteFrom(T_LOG).execute();
    }

    public TLogDao() {
        super(T_LOG, com.vjshop.entity.TLog.class);
    }

    @Autowired
    public TLogDao(Configuration configuration) {
        super(T_LOG, com.vjshop.entity.TLog.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TLog object) {
        return object.getId();
    }

}
