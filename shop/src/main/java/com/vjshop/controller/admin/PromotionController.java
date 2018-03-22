
package com.vjshop.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMemberRank;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TPromotion;
import com.vjshop.service.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 促销
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminPromotionController")
@RequestMapping("/admin/promotion")
public class PromotionController extends BaseController {

	@Autowired
	private TPromotionService promotionService;
	@Autowired
	private TMemberRankService memberRankService;
	@Autowired
	private TProductService productService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TCouponService couponService;

	/**
	 * 检查价格运算表达式是否正确
	 */
	@RequestMapping(value = "/check_price_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPriceExpression(String priceExpression) {
		if (StringUtils.isEmpty(priceExpression)) {
			return false;
		}
		return promotionService.isValidPriceExpression(priceExpression);
	}

	/**
	 * 检查积分运算表达式是否正确
	 */
	@RequestMapping(value = "/check_point_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPointExpression(String pointExpression) {
		if (StringUtils.isEmpty(pointExpression)) {
			return false;
		}
		return promotionService.isValidPointExpression(pointExpression);
	}

	/**
	 * 赠品选择
	 */
	@RequestMapping(value = "/gift_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> giftSelect(@RequestParam("q") String keyword, Long[] excludeIds, @RequestParam("limit") Integer count) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}
		//Set<TProduct> excludes = new HashSet<TProduct>(productService.findList(excludeIds));
		//List<TProduct> products = productService.search(TGoods.Type.gift, keyword, excludes, count);
		List<TProduct> products = productService.search(keyword, count);
		for (TProduct product : products) {
			if (ArrayUtils.contains(excludeIds, product.getId())) {
				continue;
			}
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("specifications", product.getSpecifications());
			item.put("url", product.getUrl());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("coupons", couponService.findAll());
		return "/admin/promotion/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TPromotion promotion, Long[] memberRankIds, Long[] couponIds, Long[] giftIds, RedirectAttributes redirectAttributes) {
		setPromotion(promotion, memberRankIds, couponIds, giftIds);
		if (!isValid(promotion)) {
			return ERROR_VIEW;
		}
		if (!validPromotion(promotion)){
			return ERROR_VIEW;
		}
		promotionService.save(promotion);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("promotion", promotionService.findDetails(id));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("coupons", couponService.findAll());
		return "/admin/promotion/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TPromotion promotion, Long[] memberRankIds, Long[] couponIds, Long[] giftIds, RedirectAttributes redirectAttributes) {
		setPromotion(promotion, memberRankIds, couponIds, giftIds);
		if (!validPromotion(promotion)){
			return ERROR_VIEW;
		}
		promotionService.update(promotion);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", promotionService.findPage(pageable));
		return "/admin/promotion/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		promotionService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置促销内容
	 * @param promotion 促销
	 * @param memberRankIds 可参与会员等级ID
	 * @param couponIds 优惠券ID
	 * @param giftIds 礼品ID
	 */
	private void setPromotion(TPromotion promotion, Long[] memberRankIds, Long[] couponIds, Long[] giftIds){
		promotion.setMemberRanks(new HashSet<TMemberRank>(memberRankService.findList(memberRankIds)));
		promotion.setCoupons(new HashSet<TCoupon>(couponService.findList(couponIds)));
		if (ArrayUtils.isNotEmpty(giftIds)) {
			List<TProduct> gifts = productService.findList(giftIds);
			CollectionUtils.filter(gifts, new Predicate() {
				public boolean evaluate(Object object) {
					TProduct gift = (TProduct) object;
					if (gift == null || gift.getGoods() == null){
						return false;
					}
					gift.setGoodsVO(goodsService.find(gift.getGoods()));
					return gift != null && gift.getType() != null && TGoods.Type.gift.ordinal() == gift.getType();
				}
			});
			promotion.setGifts(new HashSet<TProduct>(gifts));
		} else {
			promotion.setGifts(null);
		}
		promotion.setGoods(null);
		promotion.setProductCategories(null);
	}

	private boolean validPromotion(TPromotion promotion){
		if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
			return false;
		}
		if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
			return false;
		}
		if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
			return false;
		}
		if (StringUtils.isNotEmpty(promotion.getPriceExpression()) && !promotionService.isValidPriceExpression(promotion.getPriceExpression())) {
			return false;
		}
		if (StringUtils.isNotEmpty(promotion.getPointExpression()) && !promotionService.isValidPointExpression(promotion.getPointExpression())) {
			return false;
		}
		return true;
	}

}