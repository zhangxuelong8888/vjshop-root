package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TFreightConfig;
import com.vjshop.generated.db.tables.records.TFreightConfigRecord;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vjshop.generated.db.tables.TArea.T_AREA;
import static com.vjshop.generated.db.tables.TFreightConfig.T_FREIGHT_CONFIG;

/**
 * Dao - 运费配置
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TFreightConfigDao extends JooqBaseDao<TFreightConfigRecord, TFreightConfig, Long> {

    /**
     * Create a new TFreightConfigDao without any configuration
     */
    public TFreightConfigDao() {
        super(T_FREIGHT_CONFIG, TFreightConfig.class);
    }

    /**
     * Create a new TFreightConfigDao with an attached configuration
     */
    @Autowired
    public TFreightConfigDao(Configuration configuration) {
        super(T_FREIGHT_CONFIG, TFreightConfig.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(TFreightConfig object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<TFreightConfig> fetchById(Long... values) {
        return fetch(T_FREIGHT_CONFIG.ID, values);
    }

    /**
     * 判断运费配置是否存在
     *
     * @param shippingMethodId 配送方式
     * @param areaId           地区
     * @return 运费配置是否存在
     */
    public boolean exists(Long shippingMethodId, Long areaId) {
        if (shippingMethodId == null || areaId == null) {
            return false;
        }
        Long count = getDslContext()
                .selectCount()
                .from(T_FREIGHT_CONFIG)
                .where(T_FREIGHT_CONFIG.SHIPPING_METHOD.eq(shippingMethodId))
                .and(T_FREIGHT_CONFIG.AREA.eq(areaId))
                .fetchOne(0, Long.class).longValue();
        return count > 0;
    }

    /**
     * 查找运费配置分页
     *
     * @param shippingMethodId 配送方式
     * @param pageable         分页信息
     * @return 运费配置分页
     */
    public Page<TFreightConfig> findPage(Long shippingMethodId, Pageable pageable) {
        List<Field<?>> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(T_FREIGHT_CONFIG.fields()));
        fields.add(T_AREA.ID.as("shippingMethodArea.id"));
        fields.add(T_AREA.FULL_NAME.as("shippingMethodArea.fullName"));
        SelectQuery query = getDslContext().select(fields).from(T_FREIGHT_CONFIG).getQuery();
        query.addJoin(T_AREA, T_FREIGHT_CONFIG.AREA.eq(T_AREA.ID));
        query.addConditions(T_FREIGHT_CONFIG.SHIPPING_METHOD.eq(shippingMethodId));
        return findPage(query, pageable);
    }

}