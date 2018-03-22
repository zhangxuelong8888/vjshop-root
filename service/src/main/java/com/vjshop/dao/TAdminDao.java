
package com.vjshop.dao;

import com.vjshop.entity.TAdmin;
import com.vjshop.generated.db.tables.records.TAdminRecord;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TAdmin.T_ADMIN;

/**
 * Dao - 管理员
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TAdminDao extends JooqBaseDao<TAdminRecord, com.vjshop.entity.TAdmin, Long> {

    /**
     * 判断用户名是否存在
     *
     * @param username
     *            用户名(忽略大小写)
     * @return 用户名是否存在
     */
    public boolean usernameExists(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        List<TAdmin> tAdmins = fetch(T_ADMIN.USERNAME, username);
        return tAdmins.size() > 0;
    }

    /**
     * 根据用户名查找管理员
     *
     * @param username
     *            用户名(忽略大小写)
     * @return 管理员，若不存在则返回null
     */
    public TAdmin findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return fetchOne(T_ADMIN.USERNAME, username);
    }

    public TAdminDao() {
        super(T_ADMIN, com.vjshop.entity.TAdmin.class);
    }

    @Autowired
    public TAdminDao(Configuration configuration) {
        super(T_ADMIN, com.vjshop.entity.TAdmin.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TAdmin object) {
        return object.getId();
    }

}
