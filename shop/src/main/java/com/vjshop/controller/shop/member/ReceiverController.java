
package com.vjshop.controller.shop.member;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TReceiver;
import com.vjshop.entity.TMember;
import com.vjshop.service.TAreaService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TReceiverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 收货地址
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberReceiverController")
@RequestMapping("/member/receiver")
public class ReceiverController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TReceiverService receiverService;
	@Autowired
	private TAreaService areaService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Long memberId = memberService.getCurrentUserId();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", receiverService.findPage(memberId, pageable));
		return "/shop/${theme}/member/receiver/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(RedirectAttributes redirectAttributes) {
		TMember member = memberService.getCurrent();
		Long count = receiverService.count(member.getId());
		if (TReceiver.MAX_RECEIVER_COUNT != null && count != null && count.longValue() >= TReceiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, Message.warn("shop.member.receiver.addCountNotAllowed", TReceiver.MAX_RECEIVER_COUNT));
			return "redirect:list.jhtml";
		}
		return "/shop/${theme}/member/receiver/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TReceiver receiver, Long areaId, RedirectAttributes redirectAttributes) {
		receiver.setArea(areaId);
		if (!isValid(receiver)) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		Long count = receiverService.count(member.getId());
		if (TReceiver.MAX_RECEIVER_COUNT != null && count != null && count >= TReceiver.MAX_RECEIVER_COUNT) {
			return ERROR_VIEW;
		}
		receiver.setAreaName(null);
		receiver.setMember(member.getId());
		receiverService.save(receiver);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
		TReceiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_VIEW;
		}
		Long memberId = memberService.getCurrentUserId();
		if (!memberId.equals(receiver.getMember())) {
			return ERROR_VIEW;
		}
		if (receiver.getArea() != null){
			receiver.setAreaVO(areaService.find(receiver.getArea()));
		}
		model.addAttribute("receiver", receiver);
		return "/shop/${theme}/member/receiver/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TReceiver receiver, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		receiver.setArea(areaId);
		if (!isValid(receiver)) {
			return ERROR_VIEW;
		}
		TReceiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			return ERROR_VIEW;
		}
		Long memberId = memberService.getCurrentUserId();
		if (!memberId.equals(pReceiver.getMember())) {
			return ERROR_VIEW;
		}
		receiver.setMember(memberId);
		receiverService.update(receiver);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		TReceiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_MESSAGE;
		}
		Long memberId = memberService.getCurrentUserId();
		if (!memberId.equals(receiver.getMember())) {
			return ERROR_MESSAGE;
		}
		receiverService.delete(id);
		return SUCCESS_MESSAGE;
	}

}