
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TAdPosition;
import com.vjshop.generated.db.tables.records.TAdPositionRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao - 广告位
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TAdPositionDao extends JooqBaseDao<TAdPositionRecord, com.vjshop.entity.TAdPosition, Long> {

    public TAdPositionDao() {
        super(TAdPosition.T_AD_POSITION, com.vjshop.entity.TAdPosition.class);
    }

    @Autowired
    public TAdPositionDao(Configuration configuration) {
        super(TAdPosition.T_AD_POSITION, com.vjshop.entity.TAdPosition.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TAdPosition object) {
        return object.getId();
    }


}
