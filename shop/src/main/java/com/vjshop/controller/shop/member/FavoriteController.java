
package com.vjshop.controller.shop.member;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.controller.shop.BaseController;
import com.vjshop.entity.TMember;
import com.vjshop.service.TGoodsService;
import com.vjshop.service.TMemberFavoriteGoodsService;
import com.vjshop.service.TMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 商品收藏
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopMemberFavoriteController")
@RequestMapping("/member/favorite")
public class FavoriteController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TMemberFavoriteGoodsService memberFavoriteGoodsService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody Message add(Long goodsId) {
		if (!goodsService.exists(goodsId)) {
			return ERROR_MESSAGE;
		}

		Long memberId = memberService.getCurrentUserId();
		if (memberFavoriteGoodsService.exists(memberId, goodsId)) {
			return Message.warn("shop.member.favorite.exist");
		}
		Long count = memberFavoriteGoodsService.count(memberId, null);
		if (TMember.MAX_FAVORITE_COUNT != null && count != null && count >= TMember.MAX_FAVORITE_COUNT) {
			return Message.warn("shop.member.favorite.addCountNotAllowed", TMember.MAX_FAVORITE_COUNT);
		}
		memberFavoriteGoodsService.save(memberId, goodsId);
		return Message.success("shop.member.favorite.success");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Long memberId = memberService.getCurrentUserId();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", memberFavoriteGoodsService.findPage(memberId, pageable));
		return "/shop/${theme}/member/favorite/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		memberFavoriteGoodsService.delete(memberService.getCurrentUserId(), id);
		return SUCCESS_MESSAGE;
	}

}