package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TMessageDao;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMessage;
import com.vjshop.generated.db.tables.records.TMessageRecord;
import com.vjshop.service.TMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service - 消息
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TMessageServiceImpl extends TBaseServiceImpl<TMessageRecord, TMessage, Long> implements TMessageService {

	@Autowired
	private TMessageDao messageDao;

	@Transactional(readOnly = true)
	public Page<TMessage> findPage(Long memberId, Pageable pageable) {
		return messageDao.findPage(memberId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<TMessage> findDraftPage(Long senderId, Pageable pageable) {
		return messageDao.findDraftPage(senderId, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Long memberId, Boolean read) {
		return messageDao.count(memberId, read);
	}

	@Override
	public List<TMessage> findReplyMessages(Long messageId) {
		return messageDao.fetchByForMessage(messageId);
	}

	public void delete(Long id, Long memberId) {
		messageDao.remove(id, memberId);
	}

}