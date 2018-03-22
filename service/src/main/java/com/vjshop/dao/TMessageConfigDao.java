
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TMessageConfig;
import com.vjshop.generated.db.tables.records.TMessageConfigRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao - 消息配置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TMessageConfigDao extends JooqBaseDao<TMessageConfigRecord, com.vjshop.entity.TMessageConfig, Long> {

    /**
     * Create a new TMessageConfigDao without any configuration
     */
    public TMessageConfigDao() {
        super(TMessageConfig.T_MESSAGE_CONFIG, com.vjshop.entity.TMessageConfig.class);
    }

    @Autowired
    public TMessageConfigDao(Configuration configuration) {
        super(TMessageConfig.T_MESSAGE_CONFIG, com.vjshop.entity.TMessageConfig.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TMessageConfig object) {
        return object.getId();
    }

    /**
     * 查找消息配置
     *
     * @param type
     *            类型
     * @return 消息配置
     */
    public com.vjshop.entity.TMessageConfig findByType(com.vjshop.entity.TMessageConfig.Type type){
        return super.fetchOne(TMessageConfig.T_MESSAGE_CONFIG.TYPE, type.ordinal());
    }
}
