
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TCouponCodeDao;
import com.vjshop.dao.TCouponDao;
import com.vjshop.dao.TMemberDao;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TCouponCode;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TPointLog;
import com.vjshop.generated.db.tables.records.TCouponCodeRecord;
import com.vjshop.service.TCouponCodeService;
import com.vjshop.service.TMemberService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service - 优惠码
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TCouponCodeServiceImpl extends TBaseServiceImpl<TCouponCodeRecord, TCouponCode, Long> implements TCouponCodeService {

	@Autowired
	private TCouponCodeDao tCouponCodeDao;
	@Autowired
	private TCouponDao tCouponDao;
	@Autowired
	private TMemberService tMemberService;

	@Transactional(readOnly = true)
	public boolean codeExists(String code) {
		return tCouponCodeDao.codeExists(code);
	}

	@Transactional(readOnly = true)
	public TCouponCode findByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		TCouponCode tCouponCode = tCouponCodeDao.findByCode(code);
		tCouponCode.setCouponInfo(this.tCouponDao.find(tCouponCode.getCoupon()));

		return tCouponCode;
	}

	public TCouponCode generate(Long couponId, String prefix, Long memberId) {
		Assert.notNull(couponId);
		Assert.notNull(prefix);

		TCouponCode couponCode = new TCouponCode();
		couponCode.setCode(prefix + DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)).toUpperCase());
		couponCode.setIsUsed(false);
		couponCode.setCoupon(couponId);
		couponCode.setMember(memberId);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		couponCode.setModifyDate(now);
		couponCode.setCreateDate(now);
		couponCode.setVersion(0L);
		return tCouponCodeDao.insertAndFetch(couponCode);
	}

	public List<TCouponCode> generate(Long couponId, String prefix, Long memberId, Integer count) {
		Assert.notNull(couponId);
		Assert.notNull(prefix);
		Assert.notNull(count);

		List<TCouponCode> couponCodes = new ArrayList<TCouponCode>();
		for (int i = 0; i < count; i++) {
			TCouponCode couponCode = generate(couponId, prefix, memberId);
			couponCodes.add(couponCode);
		}
		return couponCodes;
	}

	public TCouponCode exchange(Long couponId, Long memberId, String operator) {
		Assert.notNull(couponId);
		Assert.notNull(memberId);

		TCoupon coupon = tCouponDao.find(couponId);
		Assert.notNull(coupon);
		Assert.notNull(coupon.getPoint());
		Assert.state(coupon.getIsEnabled() && coupon.getIsExchange() && !coupon.hasExpired());

		TMember member = tMemberService.find(memberId);
		Assert.notNull(member);
		Assert.notNull(member.getPoint());
		Assert.state(member.getPoint() >= coupon.getPoint());

		if (coupon.getPoint() > 0) {
			tMemberService.addPoint(memberId, - coupon.getPoint(), TPointLog.Type.exchange, operator, null);
		}
		return generate(couponId, coupon.getPrefix(), memberId);
	}

	@Transactional(readOnly = true)
	public Page<TCouponCode> findPage(Long memberId, Pageable pageable) {
		return tCouponCodeDao.findPage(memberId, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Long couponId, Long memberId, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		return tCouponCodeDao.count(couponId, memberId, hasBegun, hasExpired, isUsed);
	}

	public TCouponCode findDetailById(Long id){
		if(id == null) return null;
		TCouponCode tCouponCode = super.find(id);
		tCouponCode.setCouponInfo(this.tCouponDao.find(tCouponCode.getCoupon()));
		return tCouponCode;
	}

}