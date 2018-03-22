package com.vjshop.dao;


import com.vjshop.Filter;
import com.vjshop.entity.TPluginConfig;
import com.vjshop.generated.db.tables.records.TPluginConfigRecord;
import java.sql.Timestamp;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.vjshop.generated.db.tables.TPluginConfig.T_PLUGIN_CONFIG;


/**
 * 插件配置DAO
 */
@Repository
public class TPluginConfigDao extends JooqBaseDao<TPluginConfigRecord, com.vjshop.entity.TPluginConfig, Long> {

    /**
     * 判断插件ID是否存在
     *
     * @param pluginId
     *            插件ID
     * @return 插件ID是否存在
     */
    public boolean pluginIdExists(String pluginId) {
        if (StringUtils.isEmpty(pluginId)) {
            return false;
        }

        Long count = count(new Filter("pluginId", Filter.Operator.eq, pluginId));
        return count > 0;
    }

    /**
     * 根据插件ID查找插件配置
     *
     * @param pluginId
     *            插件ID
     * @return 插件配置，若不存在则返回null
     */
    public TPluginConfig findByPluginId(String pluginId) {
        if (StringUtils.isEmpty(pluginId)) {
            return null;
        }
        return fetchOneByPluginId(pluginId);
    }

    /**
     * Create a new TPluginConfigDao without any configuration
     */
    public TPluginConfigDao() {
        super(T_PLUGIN_CONFIG, com.vjshop.entity.TPluginConfig.class);
    }

    /**
     * Create a new TPluginConfigDao with an attached configuration
     */
    @Autowired
    public TPluginConfigDao(Configuration configuration) {
        super(T_PLUGIN_CONFIG, com.vjshop.entity.TPluginConfig.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TPluginConfig object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchById(Long... values) {
        return fetch(T_PLUGIN_CONFIG.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.vjshop.entity.TPluginConfig fetchOneById(Long value) {
        return fetchOne(T_PLUGIN_CONFIG.ID, value);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByCreateDate(Timestamp... values) {
        return fetch(T_PLUGIN_CONFIG.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>modify_date IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByModifyDate(Timestamp... values) {
        return fetch(T_PLUGIN_CONFIG.MODIFY_DATE, values);
    }

    /**
     * Fetch records that have <code>version IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByVersion(Long... values) {
        return fetch(T_PLUGIN_CONFIG.VERSION, values);
    }

    /**
     * Fetch records that have <code>orders IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByOrders(Integer... values) {
        return fetch(T_PLUGIN_CONFIG.ORDERS, values);
    }

    /**
     * Fetch records that have <code>attributes IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByAttributes(String... values) {
        return fetch(T_PLUGIN_CONFIG.ATTRIBUTES, values);
    }

    /**
     * Fetch records that have <code>is_enabled IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByIsEnabled(Boolean... values) {
        return fetch(T_PLUGIN_CONFIG.IS_ENABLED, values);
    }

    /**
     * Fetch records that have <code>plugin_id IN (values)</code>
     */
    public List<com.vjshop.entity.TPluginConfig> fetchByPluginId(String... values) {
        return fetch(T_PLUGIN_CONFIG.PLUGIN_ID, values);
    }

    /**
     * Fetch a unique record that has <code>plugin_id = value</code>
     */
    public com.vjshop.entity.TPluginConfig fetchOneByPluginId(String value) {
        return fetchOne(T_PLUGIN_CONFIG.PLUGIN_ID, value);
    }
}
