package com.vjshop.controller.admin;

import com.vjshop.Pageable;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 商品排名
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminGoodsRankingController")
@RequestMapping("/admin/goods_ranking")
public class GoodsRankingController extends BaseController {

	@Autowired
	private TGoodsService goodsService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(TGoods.RankingType rankingType, Pageable pageable, Model model) {
		if (rankingType == null) {
			rankingType = TGoods.RankingType.sales;
		}
		model.addAttribute("rankingTypes", TGoods.RankingType.values());
		model.addAttribute("rankingType", rankingType);
		model.addAttribute("page", goodsService.findPage(rankingType, pageable));
		return "/admin/goods_ranking/list";
	}

}