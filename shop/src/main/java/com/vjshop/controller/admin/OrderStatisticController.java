
package com.vjshop.controller.admin;

import java.util.Date;

import com.vjshop.entity.TStatistic;

import com.vjshop.service.TStatisticService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 订单统计
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminOrderStatisticController")
@RequestMapping("/admin/order_statistic")
public class OrderStatisticController extends BaseController {

	@Autowired
	private TStatisticService statisticService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(TStatistic.Period period, Date beginDate, Date endDate, Model model) {
		if (period == null) {
			period = TStatistic.Period.day;
		}
		if (beginDate == null) {
			switch (period) {
			case year:
				beginDate = DateUtils.addYears(new Date(), -10);
				break;
			case month:
				beginDate = DateUtils.addYears(new Date(), -1);
				break;
			case day:
				beginDate = DateUtils.addMonths(new Date(), -1);
				break;
			}
		}
		if (endDate == null) {
			endDate = new Date();
		}
		model.addAttribute("periods", TStatistic.Period.values());
		model.addAttribute("period", period);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statistics", statisticService.analyze(period, beginDate, endDate));
		return "/admin/order_statistic/list";
	}

}