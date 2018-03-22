package com.vjshop.dao;


import com.vjshop.generated.db.tables.TAdminRole;
import com.vjshop.generated.db.tables.records.TAdminRoleRecord;
import java.util.List;
import org.jooq.Configuration;
import org.jooq.Record2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TAdminRole.T_ADMIN_ROLE;


/**
 * 系统用户角色关系DAO
 */
@Repository
public class TAdminRoleDao extends JooqBaseDao<TAdminRoleRecord, com.vjshop.entity.TAdminRole, Record2<Long, Long>> {

    /**
     * Create a new TAdminRoleDao without any configuration
     */
    public TAdminRoleDao() {
        super(T_ADMIN_ROLE, com.vjshop.entity.TAdminRole.class);
    }

    /**
     * Create a new TAdminRoleDao with an attached configuration
     */
    @Autowired
    public TAdminRoleDao(Configuration configuration) {
        super(T_ADMIN_ROLE, com.vjshop.entity.TAdminRole.class, configuration);
    }

    public void deleteByAdminId(Long... adminId) {
        getDslContext().deleteFrom(T_ADMIN_ROLE).where(T_ADMIN_ROLE.ADMINS.in(adminId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Record2<Long, Long> getId(com.vjshop.entity.TAdminRole object) {
        return compositeKeyRecord(object.getAdmins(), object.getRoles());
    }

    /**
     * Fetch records that have <code>admins IN (values)</code>
     */
    public List<com.vjshop.entity.TAdminRole> fetchByAdmins(Long... values) {
        return fetch(T_ADMIN_ROLE.ADMINS, values);
    }

    /**
     * Fetch records that have <code>roles IN (values)</code>
     */
    public List<com.vjshop.entity.TAdminRole> fetchByRoles(Long... values) {
        return fetch(T_ADMIN_ROLE.ROLES, values);
    }
}
