
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TPointLogDao;
import com.vjshop.generated.db.tables.TMember;
import com.vjshop.generated.db.tables.TPointLog;
import com.vjshop.generated.db.tables.records.TPointLogRecord;
import com.vjshop.service.TPointLogService;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 积分记录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TPointLogServiceImpl extends TBaseServiceImpl<TPointLogRecord, com.vjshop.entity.TPointLog, Long>
		implements TPointLogService {

	@Resource
	private TPointLogDao tPointLogDao;

	@Transactional(readOnly = true)
	public Page<com.vjshop.entity.TPointLog> findPage(Long memberId, Pageable pageable) {
		SelectQuery query = tPointLogDao.getDslContext().select(TPointLog.T_POINT_LOG.fields())
				.from(TPointLog.T_POINT_LOG).getQuery();

		query.addSelect(TMember.T_MEMBER.as("memberInfo").ID.as("memberInfo.id"),
				TMember.T_MEMBER.as("memberInfo").USERNAME.as("memberInfo.username"));
		query.addJoin(TMember.T_MEMBER.as("memberInfo"), JoinType.LEFT_OUTER_JOIN,
				TMember.T_MEMBER.as("memberInfo").ID.eq(TPointLog.T_POINT_LOG.MEMBER));
		if (memberId != null){
			query.addConditions(TPointLog.T_POINT_LOG.MEMBER.eq(memberId));
		}
		return tPointLogDao.findPage(query, pageable);
	}

}