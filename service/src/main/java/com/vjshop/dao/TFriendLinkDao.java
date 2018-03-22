
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TFriendLink;
import com.vjshop.generated.db.tables.records.TFriendLinkRecord;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 友情链接
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TFriendLinkDao extends JooqBaseDao<TFriendLinkRecord, com.vjshop.entity.TFriendLink, Long> {

    public TFriendLinkDao() {
        super(TFriendLink.T_FRIEND_LINK, com.vjshop.entity.TFriendLink.class);
    }

    @Autowired
    public TFriendLinkDao(Configuration configuration) {
        super(TFriendLink.T_FRIEND_LINK, com.vjshop.entity.TFriendLink.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TFriendLink object) {
        return object.getId();
    }

    /**
     * 查找友情链接
     *
     * @param type
     *            类型
     * @return 友情链接
     */
    public List<com.vjshop.entity.TFriendLink> findList(com.vjshop.entity.TFriendLink.Type type){
        SelectQuery query = getQuery();
        if (type != null){
            query.addConditions(TFriendLink.T_FRIEND_LINK.TYPE.eq(type.ordinal()));
        }
        query.addOrderBy(TFriendLink.T_FRIEND_LINK.ORDERS.asc());
        return resultSet2List(query.fetchResultSet());
    }

}
