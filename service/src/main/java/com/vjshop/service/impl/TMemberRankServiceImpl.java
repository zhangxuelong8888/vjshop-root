
package com.vjshop.service.impl;

import com.vjshop.dao.TMemberRankDao;
import com.vjshop.entity.TMemberRank;
import com.vjshop.generated.db.tables.records.TMemberRankRecord;
import com.vjshop.service.TMemberRankService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Service - 会员等级
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TMemberRankServiceImpl extends TBaseServiceImpl<TMemberRankRecord, TMemberRank, Long>
		implements TMemberRankService {

	@Autowired
	private TMemberRankDao tMemberRankDao;


	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return tMemberRankDao.nameExists(name);
	}

	@Transactional(readOnly = true)
	public boolean nameUnique(String previousName, String currentName) {
		return StringUtils.equalsIgnoreCase(previousName, currentName) || !tMemberRankDao.nameExists(currentName);
	}

	@Transactional(readOnly = true)
	public boolean amountExists(BigDecimal amount) {
		return tMemberRankDao.amountExists(amount);
	}

	@Transactional(readOnly = true)
	public boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount) {
		return (previousAmount != null && previousAmount.compareTo(currentAmount) == 0) || !tMemberRankDao.amountExists(currentAmount);
	}

	@Transactional(readOnly = true)
	public TMemberRank findDefault() {
		return tMemberRankDao.findDefault();
	}

	@Transactional(readOnly = true)
	public TMemberRank findByAmount(BigDecimal amount) {
		return tMemberRankDao.findByAmount(amount);
	}

	@Override
	@Transactional
	public TMemberRank save(TMemberRank memberRank) {
		Assert.notNull(memberRank);

        Timestamp now = new Timestamp(System.currentTimeMillis());
		memberRank.setModifyDate(now);
		if (memberRank.getId() == null){
			memberRank.setCreateDate(now);
			memberRank.setVersion(0L);
		}
		return super.save(memberRank);
	}

	@Override
	@Transactional
	public TMemberRank update(TMemberRank memberRank) {
		Assert.notNull(memberRank);

        Timestamp now = new Timestamp(System.currentTimeMillis());
		memberRank.setModifyDate(now);
		return tMemberRankDao.update(memberRank, null);
	}

}