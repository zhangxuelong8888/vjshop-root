
package com.vjshop.job;

import com.vjshop.service.TCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 购物车
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Lazy(false)
@Component("cartJob")
public class CartJob {

	@Autowired
	private TCartService cartService;

	/**
	 * 清除过期购物车
	 */
	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		cartService.evictExpired();
	}

}