
package com.vjshop.controller.shop;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.entity.TConsultation;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.TConsultationService;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TMemberService;
import com.vjshop.util.SystemUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 咨询
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopConsultationController")
@RequestMapping("/consultation")
public class ConsultationController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TConsultationService consultationService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TMemberService memberService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	/**
	 * 发表
	 */
	@RequestMapping(value = "/add/{goodsId}", method = RequestMethod.GET)
	public String add(@PathVariable Long goodsId, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			throw new ResourceNotFoundException();
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("goods", goods);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/${theme}/consultation/add";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{goodsId}", method = RequestMethod.GET)
	public String content(@PathVariable Long goodsId, Integer pageNumber, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			throw new ResourceNotFoundException();
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("goods", goods);
		model.addAttribute("page", consultationService.findPage(null, goods.getId(), true, pageable));
		return "/shop/${theme}/consultation/content";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	Message save(String captchaId, String captcha, Long goodsId, String content, HttpServletRequest request) {
		if (!captchaService.isValid(Setting.CaptchaType.consultation, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsConsultationEnabled()) {
			return Message.error("shop.consultation.disabled");
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			return ERROR_MESSAGE;
		}
		if (!isValid(TConsultation.class, "content", content)) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (!Setting.ConsultationAuthority.anyone.equals(setting.getConsultationAuthority()) && member == null) {
			return Message.error("shop.consultation.accessDenied");
		}

		TConsultation consultation = new TConsultation();
		consultation.setContent(content);
		consultation.setIp(request.getRemoteAddr());
		consultation.setMember(member.getId());
		consultation.setGoods(goods.getId());
		consultation.setForConsultation(null);
		consultation.setReplyConsultations(null);
		if (setting.getIsConsultationCheck()) {
			consultation.setIsShow(false);
			consultationService.save(consultation);
			return Message.success("shop.consultation.check");
		} else {
			consultation.setIsShow(true);
			consultationService.save(consultation);
			return Message.success("shop.consultation.success");
		}
	}

}