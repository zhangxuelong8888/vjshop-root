package com.vjshop.service;

import com.vjshop.entity.TStatistic;

import java.util.Date;
import java.util.List;

/**
 * Service - 统计
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TStatisticService extends TBaseService<TStatistic, Long> {

	/**
	 * 判断统计是否存在
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 统计是否存在
	 */
	boolean exists(int year, int month, int day);

	/**
	 * 收集
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 统计
	 */
	TStatistic collect(int year, int month, int day);

	/**
	 * 分析
	 * 
	 * @param period
	 *            周期
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 统计
	 */
	List<TStatistic> analyze(TStatistic.Period period, Date beginDate, Date endDate);

}