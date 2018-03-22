package com.vjshop.service.impl;

import com.vjshop.dao.*;
import com.vjshop.entity.TStatistic;
import com.vjshop.generated.db.tables.records.TStatisticRecord;
import com.vjshop.service.TStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service - 统计
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TStatisticServiceImpl extends TBaseServiceImpl<TStatisticRecord, TStatistic, Long> implements TStatisticService {

	@Autowired
	private TStatisticDao statisticDao;
	@Autowired
	private TMemberDao memberDao;
	@Autowired
	private TOrderDao orderDao;

	@Transactional(readOnly = true)
	public boolean exists(int year, int month, int day) {
		return statisticDao.exists(year, month, day);
	}

	@Transactional(readOnly = true)
	public TStatistic collect(int year, int month, int day) {
		Assert.state(month >= 0);
		Assert.state(day >= 0);

		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, day);
		beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
		beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
		Date beginDate = beginCalendar.getTime();

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, day);
		endCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, beginCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, beginCalendar.getActualMaximum(Calendar.SECOND));
		Date endDate = endCalendar.getTime();

		TStatistic statistics = new TStatistic();
		statistics.setYear(year);
		statistics.setMonth(month);
		statistics.setDay(day);
		statistics.setRegisterMemberCount(memberDao.registerMemberCount(beginDate, endDate));
		statistics.setCreateOrderCount(orderDao.createOrderCount(beginDate, endDate));
		statistics.setCompleteOrderCount(orderDao.completeOrderCount(beginDate, endDate));
		statistics.setCreateOrderAmount(orderDao.createOrderAmount(beginDate, endDate));
		statistics.setCompleteOrderAmount(orderDao.completeOrderAmount(beginDate, endDate));

		return statistics;
	}

	@Transactional(readOnly = true)
	public List<TStatistic> analyze(TStatistic.Period period, Date beginDate, Date endDate) {
		return statisticDao.analyze(period, beginDate, endDate);
	}

}