package com.vjshop.dao;


import com.vjshop.Filter;
import com.vjshop.entity.TStatistic;
import com.vjshop.generated.db.tables.records.TStatisticRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import static com.vjshop.generated.db.tables.TStatistic.T_STATISTIC;


/**
 * 统计DAO
 */
@Repository
public class TStatisticDao extends JooqBaseDao<TStatisticRecord, com.vjshop.entity.TStatistic, Long> {

    public boolean exists(int year, int month, int day) {
        Filter yearFilter = new Filter(T_STATISTIC.YEAR.getName(), Filter.Operator.eq, year);
        Filter monthFilter = new Filter(T_STATISTIC.MONTH.getName(), Filter.Operator.eq, month);
        Filter dayFilter = new Filter(T_STATISTIC.DAY.getName(), Filter.Operator.eq, day);
        return count(yearFilter, monthFilter, dayFilter) > 0;
    }

    public List<TStatistic> analyze(TStatistic.Period period, Date beginDate, Date endDate) {
        Assert.notNull(period);
        SelectQuery query = null;
        switch (period) {
            case year:
                query = getDslContext().select(T_STATISTIC.YEAR, DSL.sum(T_STATISTIC.REGISTER_MEMBER_COUNT).as("register_member_count"), DSL.sum(T_STATISTIC.CREATE_ORDER_COUNT).as("create_order_count"),
                        DSL.sum(T_STATISTIC.COMPLETE_ORDER_COUNT).as("complete_order_count"), DSL.sum(T_STATISTIC.CREATE_ORDER_AMOUNT).as("create_order_amount"),
                        DSL.sum(T_STATISTIC.COMPLETE_ORDER_AMOUNT).as("complete_order_amount")).from(T_STATISTIC).groupBy(T_STATISTIC.YEAR).getQuery();
                break;
            case month:
                query = getDslContext().select(T_STATISTIC.YEAR, T_STATISTIC.MONTH, DSL.sum(T_STATISTIC.REGISTER_MEMBER_COUNT).as("register_member_count"),
                        DSL.sum(T_STATISTIC.CREATE_ORDER_COUNT).as("create_order_count"), DSL.sum(T_STATISTIC.COMPLETE_ORDER_COUNT).as("complete_order_count"),
                        DSL.sum(T_STATISTIC.CREATE_ORDER_AMOUNT).as("create_order_amount"), DSL.sum(T_STATISTIC.COMPLETE_ORDER_AMOUNT).as("complete_order_amount"))
                        .from(T_STATISTIC).groupBy(T_STATISTIC.YEAR, T_STATISTIC.MONTH).getQuery();
                break;
            case day:
                query = getDslContext().select(T_STATISTIC.YEAR, T_STATISTIC.MONTH, T_STATISTIC.DAY, T_STATISTIC.REGISTER_MEMBER_COUNT, T_STATISTIC.CREATE_ORDER_COUNT, T_STATISTIC.COMPLETE_ORDER_COUNT,
                        T_STATISTIC.CREATE_ORDER_AMOUNT, T_STATISTIC.COMPLETE_ORDER_AMOUNT).from(T_STATISTIC).getQuery();
                break;
        }

        if (beginDate != null) {
            Calendar calendar = DateUtils.toCalendar(beginDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            query.addConditions(T_STATISTIC.YEAR.gt(year).or(T_STATISTIC.YEAR.eq(year).and(T_STATISTIC.MONTH.gt(month)).or(T_STATISTIC.YEAR.eq(year)
                    .and(T_STATISTIC.MONTH.eq(month)).and(T_STATISTIC.DAY.greaterOrEqual(day)))));
        }
        if (endDate != null) {
            Calendar calendar = DateUtils.toCalendar(endDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            query.addConditions(T_STATISTIC.YEAR.lt(year).or(T_STATISTIC.YEAR.eq(year).and(T_STATISTIC.MONTH.lt(month)).or(T_STATISTIC.YEAR.eq(year)
                    .and(T_STATISTIC.MONTH.eq(month)).and(T_STATISTIC.DAY.le(day)))));
        }

        return findList(query, null, null, null, null);
    }

    /**
     * Create a new TStatisticDao without any configuration
     */
    public TStatisticDao() {
        super(T_STATISTIC, com.vjshop.entity.TStatistic.class);
    }

    /**
     * Create a new TStatisticDao with an attached configuration
     */
    @Autowired
    public TStatisticDao(Configuration configuration) {
        super(T_STATISTIC, com.vjshop.entity.TStatistic.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(com.vjshop.entity.TStatistic object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.vjshop.entity.TStatistic> fetchById(Long... values) {
        return fetch(T_STATISTIC.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.vjshop.entity.TStatistic fetchOneById(Long value) {
        return fetchOne(T_STATISTIC.ID, value);
    }
}
