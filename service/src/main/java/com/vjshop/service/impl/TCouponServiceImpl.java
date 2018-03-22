
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TCouponCodeDao;
import com.vjshop.dao.TCouponDao;
import com.vjshop.entity.TCoupon;
import com.vjshop.generated.db.tables.records.TCouponRecord;
import com.vjshop.service.TCouponService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 优惠券
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TCouponServiceImpl extends TBaseServiceImpl<TCouponRecord, TCoupon, Long> implements TCouponService {

	/** 价格表达式变量 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();

	@Autowired
	private TCouponDao tCouponDao;
	@Autowired
	private TCouponCodeDao tCouponCodeDao;

	static {
		Map<String, Object> variable0 = new HashMap<String, Object>();
		Map<String, Object> variable1 = new HashMap<String, Object>();
		Map<String, Object> variable2 = new HashMap<String, Object>();
		variable0.put("quantity", 99);
		variable0.put("price", new BigDecimal("99"));
		variable1.put("quantity", 99);
		variable1.put("price", new BigDecimal("9.9"));
		variable2.put("quantity", 99);
		variable2.put("price", new BigDecimal("0.99"));
		PRICE_EXPRESSION_VARIABLES.add(variable0);
		PRICE_EXPRESSION_VARIABLES.add(variable1);
		PRICE_EXPRESSION_VARIABLES.add(variable2);
	}

	@Transactional(readOnly = true)
	public boolean isValidPriceExpression(String priceExpression) {
		Assert.hasText(priceExpression);

		for (Map<String, Object> variable : PRICE_EXPRESSION_VARIABLES) {
			try {
				Binding binding = new Binding();
				for (Map.Entry<String, Object> entry : variable.entrySet()) {
					binding.setVariable(entry.getKey(), entry.getValue());
				}
				GroovyShell groovyShell = new GroovyShell(binding);
				Object result = groovyShell.evaluate(priceExpression);
				new BigDecimal(result.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	@Transactional(readOnly = true)
	public Page<TCoupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		return tCouponDao.findPage(isEnabled, isExchange, hasExpired, pageable);
	}

	@Transactional
	public void delete(Long id){
		//删除未使用的优惠码
		tCouponCodeDao.deleteByCouponId(id);
		//改为不可用
		TCoupon coupon = new TCoupon();
		coupon.setId(id);
		coupon.setIsEnabled(false);
		coupon.setModifyDate(new Timestamp(System.currentTimeMillis()));
		tCouponDao.updateSelective(coupon);
	}

	@Transactional
	public void delete(Long... ids){
		//删除未使用的优惠码
		tCouponCodeDao.deleteByCouponId(ids);
		//改为不可用
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tCouponDao.getDslContext().update(com.vjshop.generated.db.tables.TCoupon.T_COUPON)
				.set(com.vjshop.generated.db.tables.TCoupon.T_COUPON.IS_ENABLED, false)
				.set(com.vjshop.generated.db.tables.TCoupon.T_COUPON.MODIFY_DATE, now)
				.where(com.vjshop.generated.db.tables.TCoupon.T_COUPON.ID.in(ids))
				.execute();
	}

}