
package com.vjshop.controller.shop;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TReview;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TReviewService;
import com.vjshop.util.SystemUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 评论
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopReviewController")
@RequestMapping("/review")
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TReviewService reviewService;
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
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("goods", goods);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/${theme}/review/add";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{goodsId}", method = RequestMethod.GET)
	public String content(@PathVariable Long goodsId, Integer pageNumber, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("goods", goods);
		model.addAttribute("page", reviewService.findPage(null, goods.getId(), null, true, pageable));
		return "/shop/${theme}/review/content";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(String captchaId, String captcha, Long goodsId, Integer score, String content, HttpServletRequest request) {
		if (!captchaService.isValid(Setting.CaptchaType.review, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			return Message.error("shop.review.disabled");
		}
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			return ERROR_MESSAGE;
		}
		if (!isValid(TReview.class, "score", score) || !isValid(TReview.class, "content", content)) {
			return ERROR_MESSAGE;
		}
		TMember member = memberService.getCurrent();
		if (!Setting.ReviewAuthority.anyone.equals(setting.getReviewAuthority()) && member == null) {
			return Message.error("shop.review.accessDenied");
		}
		if (member != null && !reviewService.hasPermission(member.getId(), goodsId)) {
			return Message.error("shop.review.noPermission");
		}

		TReview review = new TReview();
		review.setScore(score);
		review.setContent(content);
		review.setIp(request.getRemoteAddr());
		review.setMember(member.getId());
		review.setGoods(goodsId);
		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
			reviewService.save(review);
			return Message.success("shop.review.check");
		} else {
			review.setIsShow(true);
			reviewService.save(review);
			return Message.success("shop.review.success");
		}
	}

}