
package com.vjshop.job;

import com.vjshop.service.TOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 订单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Lazy(false)
@Component("orderJob")
public class OrderJob {

	@Autowired
	private TOrderService orderService;

	/**
	 * 过期订单处理
	 */
	@Scheduled(cron = "${job.order_expired_processing.cron}")
	public void expiredProcessing() {
		orderService.undoExpiredUseCouponCode();
		orderService.undoExpiredExchangePoint();
		orderService.releaseExpiredAllocatedStock();
	}

}