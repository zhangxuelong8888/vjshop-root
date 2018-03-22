
package com.vjshop.controller.admin;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Pageable;
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
 * Controller - 消息
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminMessageController")
@RequestMapping("/admin/message")
public class MessageController extends BaseController {

	@Autowired
	private TMessageService tMessageService;
	@Autowired
	private TMemberService tMemberService;

	/**
	 * 检查用户名是否合法
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return tMemberService.usernameExists(username);
	}

	/**
	 * 发送
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(Long draftMessageId, Model model) {
		TMessage draftMessage = tMessageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && draftMessage.getSender() == null) {
			model.addAttribute("draftMessage", draftMessage);
			model.addAttribute("receiver", tMemberService.find(draftMessage.getReceiver()));
		}
		return "/admin/message/send";
	}

	/**
	 * 发送
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(Long draftMessageId, String username, String title, String content, @RequestParam(defaultValue = "false") Boolean isDraft, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TMessage.class, "content", content)) {
			return ERROR_VIEW;
		}
		TMessage draftMessage = tMessageService.find(draftMessageId);
		if (draftMessage != null && draftMessage.getIsDraft() && draftMessage.getSender() == null) {
			tMessageService.delete(draftMessage);
		}
		TMember receiver = null;
		if (StringUtils.isNotEmpty(username)) {
			receiver = tMemberService.findByUsername(username);
			if (receiver == null) {
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
		message.setSender(null);
		message.setReceiver(receiver.getId());
		message.setForMessage(null);
		message.setReplyMessages(null);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		message.setCreateDate(now);
		message.setModifyDate(now);
		message.setVersion(0L);
		tMessageService.save(message);
		if (isDraft) {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("admin.message.saveDraftSuccess"));
			return "redirect:draft.jhtml";
		} else {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("admin.message.sendSuccess"));
			return "redirect:list.jhtml";
		}
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, Model model) {
		TMessage message = tMessageService.find(id);
		if (message == null || message.getIsDraft() || message.getForMessage() != null) {
			return ERROR_VIEW;
		}
		if ((message.getSender() != null && message.getReceiver() != null) || (message.getReceiver() == null && message.getReceiverDelete()) || (message.getSender() == null && message.getSenderDelete())) {
			return ERROR_VIEW;
		}
		if (message.getReceiver() == null) {
			message.setReceiverRead(true);
		} else {
			message.setSenderRead(true);
		}
		tMessageService.updateSelective(message);
		message.setSenders(tMemberService.find(message.getSender()));
		message.setReplyMessages(new LinkedHashSet<TMessage>(tMessageService.findReplyMessages(message.getId())));
		model.addAttribute("adminMessage", message);
		return "/admin/message/view";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TMessage.class, "content", content)) {
			return ERROR_VIEW;
		}
		TMessage forMessage = tMessageService.find(id);
		if (forMessage == null || forMessage.getIsDraft() || forMessage.getForMessage() != null) {
			return ERROR_VIEW;
		}
		if ((forMessage.getSender() != null && forMessage.getReceiver() != null) || (forMessage.getReceiver() == null && forMessage.getReceiverDelete()) || (forMessage.getSender() == null && forMessage.getSenderDelete())) {
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
		message.setSender(null);
		message.setReceiver(forMessage.getReceiver() == null ? forMessage.getSender() : forMessage.getReceiver());
		if ((forMessage.getReceiver() == null && !forMessage.getSenderDelete()) || (forMessage.getSender() == null && !forMessage.getReceiverDelete())) {
			message.setForMessage(forMessage.getId());
		}
		message.setReplyMessages(null);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		message.setCreateDate(now);
		message.setModifyDate(now);
		message.setVersion(0L);
		tMessageService.save(message);

		if (forMessage.getSender() == null) {
			forMessage.setSenderRead(true);
			forMessage.setReceiverRead(false);
		} else {
			forMessage.setSenderRead(false);
			forMessage.setReceiverRead(true);
		}
		tMessageService.update(forMessage);

		if ((forMessage.getReceiver() == null && !forMessage.getSenderDelete()) || (forMessage.getSender() == null && !forMessage.getReceiverDelete())) {
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return "redirect:view.jhtml?id=" + forMessage.getId();
		} else {
			addFlashMessage(redirectAttributes, com.vjshop.Message.success("admin.message.replySuccess"));
			return "redirect:list.jhtml";
		}
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, Model model) {
		model.addAttribute("page", tMessageService.findPage(null, pageable));
		return "/admin/message/list";
	}

	/**
	 * 草稿箱
	 */
	@RequestMapping(value = "/draft", method = RequestMethod.GET)
	public String draft(Pageable pageable, Model model) {
		model.addAttribute("page", tMessageService.findDraftPage(null, pageable));
		return "/admin/message/draft";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody
	com.vjshop.Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				tMessageService.delete(id, null);
			}
		}
		return SUCCESS_MESSAGE;
	}

}