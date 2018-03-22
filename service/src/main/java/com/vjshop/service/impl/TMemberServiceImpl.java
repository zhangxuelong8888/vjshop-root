
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Principal;
import com.vjshop.Setting;
import com.vjshop.dao.*;
import com.vjshop.entity.TDepositLog;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberRank;
import com.vjshop.entity.TPointLog;
import com.vjshop.generated.db.tables.records.TMemberRecord;
import com.vjshop.service.MailService;
import com.vjshop.service.SmsService;
import com.vjshop.service.TMemberService;
import com.vjshop.util.SystemUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Service - 会员
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tMemberServiceImpl")
public class TMemberServiceImpl extends TBaseServiceImpl<TMemberRecord, TMember, Long>
		implements TMemberService {

	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;

	@Autowired
	private TMemberDao tMemberDao;
	@Autowired
	private TDepositLogDao tDepositLogDao;
	@Autowired
	private TPointLogDao tPointLogDao;
	@Autowired
	private TMemberRankDao tMemberRankDao;
	@Autowired
	private TReviewDao tReviewDao;
	@Autowired
	private TConsultationDao tConsultationDao;
	@Autowired
	private TMemberFavoriteGoodsDao tMemberFavoriteGoodsDao;

	@Override
	public TMember findDetails(Long memberId) {
		TMember member = tMemberDao.findDetails(memberId);
		if (member == null){
			return null;
		}
		Long reviewCount = tReviewDao.count(memberId, null, null, null);
		Long consultationCount = tConsultationDao.count(memberId, null, null);
		Long favoriteCount = tMemberFavoriteGoodsDao.count(memberId, null);
		member.setReviewCount(reviewCount);
		member.setConsultationCount(consultationCount);
		member.setFavoriteGoodsCount(favoriteCount);
		return member;
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return tMemberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);

		Setting setting = SystemUtils.getSetting();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return tMemberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		}
		return !tMemberDao.emailExists(currentEmail);
	}

	@Transactional(readOnly = true)
	public TMember find(String loginPluginId, String openId) {
		return tMemberDao.find(loginPluginId, openId);
	}

	@Transactional(readOnly = true)
	public TMember findByUsername(String username) {
		return tMemberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<TMember> findListByEmail(String email) {
		return tMemberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<TMember> findPage(com.vjshop.entity.TMember.RankingType rankingType, Pageable pageable) {
		return tMemberDao.findPage(rankingType, pageable);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		return requestAttributes != null && requestAttributes.getAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) != null;
	}

	@Transactional(readOnly = true)
	public TMember getCurrent() {
		return getCurrent(false);
	}

	@Transactional(readOnly = true)
	public TMember getCurrent(boolean lock) {
		Long id = getCurrentUserId();
		if (lock) {
			return tMemberDao.findWithLock(id);
		} else {
			return tMemberDao.find(id);
		}
	}

	@Override
	public Long getCurrentUserId() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		return principal != null ? principal.getId() : null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		return principal != null ? principal.getUsername() : null;
	}

	@Transactional
	public void addBalance(Long memberId, BigDecimal amount, TDepositLog.Type type, String operator, String memo) {
		Assert.notNull(memberId);
		Assert.notNull(amount);
		Assert.notNull(type);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		TMember member = tMemberDao.findWithLock(memberId);
		//预存款
		if (member.getBalance() == null){
			member.setBalance(amount);
		} else {
			BigDecimal total = member.getBalance().add(amount);
			if(total.compareTo(BigDecimal.ZERO) >= 0){
				member.setBalance(total);
			}
		}

		Timestamp now = new Timestamp(System.currentTimeMillis());
		member.setModifyDate(now);
		tMemberDao.updateSelective(member);
		//新增预存款记录
		TDepositLog depositLog = new TDepositLog();
		depositLog.setType(type.ordinal());
		depositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		depositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		depositLog.setBalance(member.getBalance());
		depositLog.setOperator(operator);
		depositLog.setMemo(memo);
		depositLog.setMember(memberId);
		depositLog.setCreateDate(now);
		depositLog.setModifyDate(now);
		depositLog.setVersion(0L);
		tDepositLogDao.insert(depositLog);
	}

	@Transactional
	public void addPoint(Long memberId, long amount, TPointLog.Type type, String operator, String memo) {
		Assert.notNull(memberId);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}
		TMember member = tMemberDao.findWithLock(memberId);
		//消费积分
		if (member.getPoint() == null){
			member.setPoint(amount);
		} else {
			long total = member.getPoint() + amount;
			if(total >= 0){
				member.setPoint(total);
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		member.setModifyDate(now);
		tMemberDao.updateSelective(member);
		//新增积分记录
		TPointLog pointLog = new TPointLog();
		pointLog.setType(type.ordinal());
		pointLog.setCredit(amount > 0 ? amount : 0L);
		pointLog.setDebit(amount < 0 ? Math.abs(amount) : 0L);
		pointLog.setBalance(member.getPoint());
		pointLog.setOperator(operator);
		pointLog.setMemo(memo);
		pointLog.setMember(memberId);
		pointLog.setCreateDate(now);
		pointLog.setModifyDate(now);
		pointLog.setVersion(0L);
		tPointLogDao.insert(pointLog);
	}

	@Transactional
	public void addAmount(Long memberId, BigDecimal amount) {
		Assert.notNull(memberId);
		Assert.notNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		TMember member = tMemberDao.findWithLock(memberId);
		//消费金额
		if (member.getAmount() == null){
			member.setAmount(amount);
		} else {
			BigDecimal total = member.getAmount().add(amount);
			if(total.compareTo(BigDecimal.ZERO) >= 0){
				member.setAmount(total);
			}
		}
		//更新会员等级
		TMemberRank memberRank = tMemberRankDao.find(member.getMemberRank());
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			TMemberRank newMemberRank = tMemberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank.getId());
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		member.setModifyDate(now);
		tMemberDao.updateSelective(member);
	}

	@Override
	@Transactional
	public TMember save(TMember member) {
		Assert.notNull(member);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		member.setModifyDate(now);
		if (member.getId() == null){
			member.setCreateDate(now);
			member.setVersion(0L);
			member.setUsername(StringUtils.lowerCase(member.getUsername()));
			member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		}
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member = super.save(member);
		//发送短信
		mailService.sendRegisterMemberMail(member.getId());
		smsService.sendRegisterMemberSms(member.getId());
		return member;
	}

}