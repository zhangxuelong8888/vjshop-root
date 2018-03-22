
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TAd;
import com.vjshop.generated.db.tables.TAdPosition;
import com.vjshop.generated.db.tables.records.TAdRecord;
import com.vjshop.util.JooqUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao - 广告
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TAdDao extends JooqBaseDao<TAdRecord, com.vjshop.entity.TAd, Long> {

    public TAdDao() {
        super(TAd.T_AD, com.vjshop.entity.TAd.class);
    }

    @Autowired
    public TAdDao(Configuration configuration) {
        super(TAd.T_AD, com.vjshop.entity.TAd.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TAd object) {
        return object.getId();
    }

    @Override
    public Page<com.vjshop.entity.TAd> findPage(Pageable pageable) {
        SelectQuery query = getDslContext().select(TAd.T_AD.fields())
                .from(TAd.T_AD).getQuery();
        query.addSelect(JooqUtils.getFields(TAdPosition.T_AD_POSITION, "adPositionInfo"));
        query.addJoin(TAdPosition.T_AD_POSITION, JoinType.LEFT_OUTER_JOIN,
                TAdPosition.T_AD_POSITION.ID.eq(TAd.T_AD.AD_POSITION));
        return super.findPage(query, pageable);
    }
}
