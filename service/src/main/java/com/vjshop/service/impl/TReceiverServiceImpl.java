
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TAreaDao;
import com.vjshop.dao.TReceiverDao;
import com.vjshop.entity.TArea;
import com.vjshop.entity.TReceiver;
import com.vjshop.generated.db.tables.records.TReceiverRecord;
import com.vjshop.service.TReceiverService;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service - 收货地址
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TReceiverServiceImpl extends TBaseServiceImpl<TReceiverRecord, TReceiver, Long> implements TReceiverService {

	@Autowired
	private TReceiverDao tReceiverDao;
	@Autowired
	private TAreaDao tAreaDao;

	@Transactional(readOnly = true)
	public TReceiver findDefault(Long memberId) {
		return tReceiverDao.findDefault(memberId);
	}

	@Override
	public Long count(Long memberId) {
		return tReceiverDao.count(memberId);
	}

	@Override
	public List<TReceiver> findList(Long memberId) {
		return tReceiverDao.findList(memberId);
	}

	@Transactional(readOnly = true)
	public Page<TReceiver> findPage(Long memberId, Pageable pageable) {
		return tReceiverDao.findPage(memberId, pageable);
	}

	@Override
	@Transactional
	public TReceiver save(TReceiver receiver) {
		Assert.notNull(receiver);
		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			tReceiverDao.setDefault(receiver);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		receiver.setModifyDate(now);
		if (receiver.getId() == null) {
			receiver.setCreateDate(now);
			receiver.setVersion(0L);
		}
		if (receiver.getArea() != null){
			TArea area = tAreaDao.find(receiver.getArea());
			receiver.setAreaName(area != null ? area.getFullName() : null);
		}
		return super.save(receiver);
	}

	@Override
	@Transactional
	public TReceiver update(TReceiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			tReceiverDao.setDefault(receiver);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		receiver.setModifyDate(now);
		return super.update(receiver, "areaName", "member");
	}

}