package com.vjshop.controller.shop.member;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMessage;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Controller - 会员中心 - 消息
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberMessageController")
@RequestMapping("/member/message")
public class MessageController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMessageService messageService;
	@Autowired
	private TMemberService memberService;

	/**
	 * 检查用户名是否合法
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !StringUtils.equalsIgnoreCase(username, memberService.getCurrentUsername()) && memberService.usernameExists(username);
	}

	/**
	 * 发送
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(Long draftMessageId, Model model) {
		TMessage draftMessage = messageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && memberService.getCurrent().getId().equals(draftMessage.getSender())) {
			model.addAttribute("draftMessage", draftMessage);
			model.addAttribute("receiver", memberService.find(draftMessage.getReceiver()));
		}
		return "/shop/${theme}/member/message/send";
	}

	/**
	 * 发送
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(Long draftMessageId, String username, String title, String content, @RequestParam(defaultValue = "false") Boolean isDraft, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TMessage.class, "content", content)) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		TMessage draftMessage = messageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && member.getId().equals(draftMessage.getSender())) {
			messageService.delete(draftMessage);
		}
		TMember receiver = null;
		if (StringUtils.isNotEmpty(username)) {
			receiver = memberService.findByUsername(username);
			if (member.equals(receiver)) {
				return ERROR_VIEW;
			}
		}
		TMessage message = new TMessage();
		message.setTitle(title);
		message.setContent(content);
		message.setIp(request.getRemoteAddr());
		message.setIsDraft(isDraft);
		message.setSenderRead(true);
		message.setReceiverRead(false);
		message.setSenderDelete(false);
		message.setReceiverDelete(false);
		message.setSender(member.getId());
		message.setReceiver(receiver == null ? null : receiver.getId());
		message.setForMessage(null);
		message.setReplyMessages(null);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		message.setCreateDate(now);
		message.setModifyDate(now);
		message.setVersion(0L);
		messageService.save(message);
		if (isDraft) {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("shop.member.message.saveDraftSuccess"));
			return "redirect:draft.jhtml";
		} else {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("shop.member.message.sendSuccess"));
			return "redirect:list.jhtml";
		}
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, Model model) {
		TMessage message = messageService.find(id);
		if (message == null || message.getIsDraft() || message.getForMessage() != null) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if ((!member.getId().equals(message.getSender()) && !member.getId().equals(message.getReceiver())) || (member.getId().equals(message.getReceiver()) && message.getReceiverDelete()) || (member.getId().equals(message.getSender()) && message.getSenderDelete())) {
			return ERROR_VIEW;
		}
		if (member.getId().equals(message.getReceiver())) {
			message.setReceiverRead(true);
		} else {
			message.setSenderRead(true);
		}
		messageService.update(message);
		if (message.getSender() != null) {
			message.setSenders(memberService.find(message.getSender()));
		}
		if (message.getReceiver() != null) {
			message.setReceivers(memberService.find(message.getReceiver()));
		}
		message.setReplyMessages(new LinkedHashSet<TMessage>(messageService.findReplyMessages(message.getId())));
		model.addAttribute("memberMessage", message);
		return "/shop/${theme}/member/message/view";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TMessage.class, "content", content)) {
			return ERROR_VIEW;
		}
		TMessage forMessage = messageService.find(id);
		if (forMessage == null || forMessage.getIsDraft() || forMessage.getForMessage() != null) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if ((!member.getId().equals(forMessage.getSender()) && !member.getId().equals(forMessage.getReceiver())) || (member.getId().equals(forMessage.getReceiver()) && forMessage.getReceiverDelete()) || (member.getId().equals(forMessage.getSender()) && forMessage.getSenderDelete())) {
			return ERROR_VIEW;
		}
		TMessage message = new TMessage();
		message.setTitle("reply: " + forMessage.getTitle());
		message.setContent(content);
		message.setIp(request.getRemoteAddr());
		message.setIsDraft(false);
		message.setSenderRead(true);
		message.setReceiverRead(false);
		message.setSenderDelete(false);
		message.setReceiverDelete(false);
		message.setSender(member.getId());
		message.setReceiver(member.getId().equals(forMessage.getReceiver()) ? forMessage.getSender() : forMessage.getReceiver());
		message.setForMessage(null);
		message.setReplyMessages(null);
		if ((member.getId().equals(forMessage.getReceiver()) && !forMessage.getSenderDelete()) || (member.getId().equals(forMessage.getSender()) && !forMessage.getReceiverDelete())) {
			message.setForMessage(forMessage.getId());
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		message.setCreateDate(now);
		message.setModifyDate(now);
		message.setVersion(0L);
		messageService.save(message);

		if (member.getId().equals(forMessage.getSender())) {
			forMessage.setSenderRead(true);
			forMessage.setReceiverRead(false);
		} else {
			forMessage.setSenderRead(false);
			forMessage.setReceiverRead(true);
		}
		messageService.update(forMessage);

		if ((member.getId().equals(forMessage.getReceiver()) && !forMessage.getSenderDelete()) || (member.getId().equals(forMessage.getSender()) && !forMessage.getReceiverDelete())) {
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return "redirect:view.jhtml?id=" + forMessage.getId();
		} else {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("shop.member.message.replySuccess"));
			return "redirect:list.jhtml";
		}
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, Model model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Long memberId = memberService.getCurrentUserId();
		model.addAttribute("page", messageService.findPage(memberId, pageable));
		return "/shop/${theme}/member/message/list";
	}

	/**
	 * 草稿箱
	 */
	@RequestMapping(value = "/draft", method = RequestMethod.GET)
	public String draft(Integer pageNumber, Model model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Long memberId = memberService.getCurrentUserId();
		model.addAttribute("page", messageService.findDraftPage(memberId, pageable));
		return "/shop/${theme}/member/message/draft";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody
	com.vjshop.Message delete(Long id) {
		Long memberId = memberService.getCurrentUserId();
		messageService.delete(id, memberId);
		return SUCCESS_MESSAGE;
	}

}