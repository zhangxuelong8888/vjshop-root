package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TMessage;

import java.util.List;

/**
 * Service - 消息
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TMessageService extends TBaseService<TMessage, Long> {

	/**
	 * 查找消息分页
	 * 
	 * @param memberId
	 *            会员ID,null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 消息分页
	 */
	Page<TMessage> findPage(Long memberId, Pageable pageable);

	/**
	 * 查找草稿分页
	 * 
	 * @param senderId
	 *            发件人ID,null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 草稿分页
	 */
	Page<TMessage> findDraftPage(Long senderId, Pageable pageable);

	/**
	 * 查找消息数量
	 * 
	 * @param memberId
	 *            会员ID，null表示管理员
	 * @param read
	 *            是否已读
	 * @return 消息数量，不包含草稿
	 */
	Long count(Long memberId, Boolean read);

	/**
	 * 查找回复的消息
	 * @param messageId 原消息ID
	 * @return
	 */
    List<TMessage> findReplyMessages(Long messageId);

    /**
	 * 删除消息
	 * 
	 * @param id
	 *            ID
	 * @param memberId
	 *            执行人,null表示管理员
	 */
	void delete(Long id, Long memberId);

}