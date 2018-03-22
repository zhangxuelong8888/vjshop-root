
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.util.JooqUtils;
import com.vjshop.util.SpringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Dao - JOOQ 基类
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public abstract class JooqBaseDao<R extends UpdatableRecord<R>, T, I> extends DAOImpl<R, T, I> {

    /**
     * "ID"属性名称
     */
    public static final String ID_PROPERTY_NAME = "id";

    /**
     * 更新忽略属性
     */
    private static final String[] UPDATE_IGNORE_PROPERTIES = new String[]{"createDate", "version"};

    protected JooqBaseDao(Table<R> table, Class<T> type) {
        super(table, type);
    }

    protected JooqBaseDao(Table<R> table, Class<T> type, Configuration configuration) {
        super(table, type, configuration);
    }

    public boolean isNew(T entity) {
        return this.getId(entity) == null;
    }

    public SelectQuery getQuery() {
        return getDslContext().selectFrom(this.getTable()).getQuery();
    }

    public DSLContext getDslContext() {
        return DSL.using(this.configuration());
    }

    public JdbcMapper getJdbcMapper(String... keys) {
        return JdbcMapperFactory.newInstance().addKeys(keys).newMapper(this.getType());
    }

    /**
     * 获取属性名对应字段，驼峰转下划线
     *
     * @param property 属性名
     * @return Field
     * 字段
     */
    public Field getField(String property) {
        return JooqUtils.getField(property);
    }

    /**
     * 记录转pojo实体
     *
     * @param record 查询记录
     * @return pojo实体
     */
    public T record2Pojo(R record) {
        return record == null ? null : this.mapper().map(record);
    }

    /**
     * 记录转pojo实体集合
     *
     * @param rs 查询记录
     * @return pojo实体集合
     */
    public List<T> resultSet2List(ResultSet rs) {
        try {
            Stream<T> stream = getJdbcMapper(ID_PROPERTY_NAME).stream(rs);
            return stream.collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
            }
        }
    }

    /**
     * 记录转pojo实体集合
     *
     * @param rs
     *            查询记录
     * @param keys
     *            关键字段，默认"id"
     * @return pojo实体集合
     */
    public List<T> resultSet2List(ResultSet rs, String... keys){
        if (ArrayUtils.isEmpty(keys)){
            keys = new String[]{ID_PROPERTY_NAME};
        }
        try {
            Stream<T> stream = getJdbcMapper(keys).stream(rs);
            return stream.collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(SpringUtils.getMessage("shop.message.error"));
            }
        }
    }

    /**
     * 插入并返回实体对象（包括自增id）
     *
     * @param pojo
     * @return
     */
    public T insertAndFetch(T pojo) {
        R record = getDslContext().newRecord(getTable(), pojo);
        record.insert();
        return record2Pojo(record);
    }

    /**
     * 查找实体对象
     *
     * @param id ID
     * @return 实体对象，若不存在则返回null
     */
    public T find(I id) {
        return this.fetchOne(DSL.field(ID_PROPERTY_NAME), id);
    }

    /**
     * 查找实体对象
     *
     * @param id ID
     * @return 实体对象，若不存在则返回null
     */
    public List<T> find(I... id) {
        return this.fetch(DSL.field(ID_PROPERTY_NAME), id);
    }

    /**
     * 查找实体对象（加行锁）
     *
     * @param id ID
     * @return 实体对象，若不存在则返回null
     */
    public T findWithLock(I id) {
        return getDslContext().selectFrom(this.getTable())
                .where(DSL.field(ID_PROPERTY_NAME).eq(id))
                .forUpdate().fetchOneInto(this.getType());
    }

    /**
     * 更新实体对象
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    public void updateSelective(T entity) {
        Assert.notNull(entity);
        Assert.isTrue(this.getId(entity) != null);

        T persistant = find(this.getId(entity));
        if (persistant != null) {
            JooqUtils.copyProperties(entity, persistant);
        }
        super.update(persistant);
    }

    /**
     * 更新部分信息
     * <p>必须设置主键</p>
     *
     * @return 影响条数
     */
    public int updateSelective(Map<String, Object> params) {
        Assert.notNull(params);
        Assert.isTrue(!params.isEmpty() && params.containsKey(ID_PROPERTY_NAME));

        I id = (I) params.get(ID_PROPERTY_NAME);
        params.remove(ID_PROPERTY_NAME);
        UpdateSetStep setStep = getDslContext().update(getTable());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (null != entry.getValue()) {
                setStep = setStep.set(getField(entry.getKey()), entry.getValue());
            }
        }
        return ((UpdateSetMoreStep) setStep).where(DSL.field(ID_PROPERTY_NAME).eq(id))
                .execute();
    }

    /**
     * 更新实体对象
     *
     * @param entity           实体对象
     * @param ignoreProperties 忽略属性
     * @return 实体对象
     */
    public T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity);
        Assert.isTrue(this.getId(entity) != null);

        T persistant = find(this.getId(entity));
        if (persistant != null) {
            JooqUtils.copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
        }
        super.update(persistant);
        return persistant;
    }

    /**
     * 查找实体对象集合
     *
     * @param filters 筛选
     * @param orders  排序
     * @return 实体对象集合
     */
    public List<T> findList(List<Filter> filters, List<Order> orders) {
        return findList(null, null, null, filters, orders);
    }

    /**
     * 查找实体对象集合
     *
     * @param query   查询条件
     * @param first   起始记录
     * @param count   数量
     * @param filters 筛选
     * @param orders  排序
     * @return 实体对象集合
     */
    public List<T> findList(SelectQuery query, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        return findList(query, first, count, filters, orders, null);
    }

    /**
     * 查找实体对象集合
     *
     * @param query   查询条件
     * @param first   起始记录
     * @param count   数量
     * @param filters 筛选
     * @param orders  排序
     * @param keys    关键字段，默认"id"
     * @return 实体对象集合
     */
    public List<T> findList(SelectQuery query, Integer first, Integer count, List<Filter> filters, List<Order> orders, String... keys) {
        if (query == null) {
            query = getQuery();
        }
        //处理条件
        JooqUtils.toFilters(query, filters);
        JooqUtils.toOrders(query, orders);
        //查询
        if (count != null) {
            query.addLimit(count.intValue());
        }
        if (first != null) {
            query.addOffset(first.intValue());
        }
        ResultSet rs = query.fetchResultSet();
        return resultSet2List(rs, keys);
    }

    /**
     * 查找实体对象分页
     *
     * @param pageable 分页信息
     * @return 实体对象分页
     */
    public Page<T> findPage(Pageable pageable) {
        return findPage(getQuery(), pageable);
    }

    /**
     * 查找实体对象分页
     *
     * @param query    查询条件
     * @param pageable 分页信息
     * @return 实体对象分页
     */
    public Page<T> findPage(SelectQuery query, Pageable pageable) {
        Assert.notNull(query);
        if (pageable == null) {
            pageable = new Pageable();
        } else {
            //查询条件
            setFilterOrder(pageable);
            JooqUtils.toFilters(query, pageable.getFilters());
        }
        //统计
        int total = query.fetchCount();
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            pageable.setPageNumber(totalPages);
        }
        if (total <= 0) {
            return new Page(new ArrayList<T>(0), total, pageable);
        }
        //排序
        JooqUtils.toOrders(query, pageable.getOrders());
        //查询
        query.addLimit(pageable.getPageSize());
        query.addOffset((pageable.getPageNumber() - 1) * pageable.getPageSize());
        ResultSet rs = query.fetchResultSet();
        return new Page(resultSet2List(rs), total, pageable);
    }

    /**
     * 查询实体对象数量
     *
     * @param filters 筛选
     * @return 实体对象数量
     */
    public long count(Filter... filters) {
        SelectQuery query = getDslContext().selectCount().from(getTable()).getQuery();
        JooqUtils.toFilters(query, Arrays.asList(filters));
        return ((Long) query.fetchOne(0, Long.class)).longValue();
    }

    /**
     * 统计
     *
     * @param filters 筛选
     * @return 实体对象数量
     */
    public Object aggregateQuery(SelectQuery query, Filter... filters) {
        if (filters != null) {
            JooqUtils.toFilters(query, Arrays.asList(filters));
        }
        return query.fetchOne(0, Object.class);
    }

    /**
     * 设置查询及排序条件
     *
     * @param pageable
     */
    void setFilterOrder(Pageable pageable) {
        if (StringUtils.isNotEmpty(pageable.getSearchProperty()) && StringUtils.isNotEmpty(pageable.getSearchValue())) {
            List<Filter> filters = new ArrayList<Filter>(1);
            Filter filter = new Filter();
            filter.setProperty(pageable.getSearchProperty());
            filter.setValue("%" + pageable.getSearchValue() + "%");
            filter.setOperator(Filter.Operator.like);
            filters.add(filter);
            pageable.setFilters(filters);
        }
        if (StringUtils.isNotEmpty(pageable.getOrderProperty())) {
            List<Order> orders = new ArrayList<Order>(1);
            Order order = new Order();
            order.setProperty(pageable.getOrderProperty());
            order.setDirection(pageable.getOrderDirection());
            orders.add(order);
            pageable.setOrders(orders);
        }
    }

}
