
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TNavigation;
import com.vjshop.generated.db.tables.records.TNavigationRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

/**
 * Dao - 导航
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TNavigationDao extends JooqBaseDao<TNavigationRecord, com.vjshop.entity.TNavigation, Long> {

    public TNavigationDao() {
        super(TNavigation.T_NAVIGATION, com.vjshop.entity.TNavigation.class);
    }

    @Autowired
    public TNavigationDao(Configuration configuration) {
        super(TNavigation.T_NAVIGATION, com.vjshop.entity.TNavigation.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TNavigation object) {
        return object.getId();
    }

    /**
     * 查找导航
     *
     * @param position
     *            位置
     * @return 导航
     */
    public List<com.vjshop.entity.TNavigation> findList(com.vjshop.entity.TNavigation.Position position){
        ResultSet rs = getDslContext().selectFrom(TNavigation.T_NAVIGATION)
                .where(TNavigation.T_NAVIGATION.POSITION.eq(position.ordinal()))
                .orderBy(TNavigation.T_NAVIGATION.ORDERS.asc())
                .fetchResultSet();
        return resultSet2List(rs);
    }
}
