
package com.vjshop.job;

import java.sql.Timestamp;
import java.util.Calendar;

import com.vjshop.entity.TStatistic;

import com.vjshop.service.TStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 统计
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Lazy(false)
@Component("statisticJob")
public class StatisticJob {

	@Autowired
	private TStatisticService statisticService;

	/**
	 * 收集
	 */
	@Scheduled(cron = "${job.statistic_collect.cron}")
	public void collect() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (statisticService.exists(year, month, day)) {
			return;
		}
		TStatistic statistic = statisticService.collect(year, month, day);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		statistic.setCreateDate(now);
		statistic.setModifyDate(now);
		statistic.setVersion(0L);
		statisticService.save(statistic);
	}

}