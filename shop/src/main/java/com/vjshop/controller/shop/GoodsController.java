
package com.vjshop.controller.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Pageable;
import com.vjshop.entity.*;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.*;

import com.vjshop.util.SpringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 货品
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopGoodsController")
@RequestMapping("/goods")
public class GoodsController extends BaseController {

	/** 最大对比货品数 */
	public static final Integer MAX_COMPARE_GOODS_COUNT = 4;

	/** 最大浏览记录货品数 */
	public static final Integer MAX_HISTORY_GOODS_COUNT = 10;

	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TProductCategoryService productCategoryService;
	@Autowired
	private TBrandService brandService;
	@Autowired
	private TPromotionService promotionService;
	@Autowired
	private TTagService tagService;
	@Autowired
	private TAttributeService attributeService;
	@Autowired
	private TSearchService searchService;

	/**
	 * 对比栏
	 */
	@RequestMapping(value = "/compare_bar", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> compareBar(Long[] goodsIds) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_COMPARE_GOODS_COUNT) {
			return data;
		}

		List<TGoods> goodsList = goodsService.findList(goodsIds);
		for (TGoods goods : goodsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", goods.getId());
			item.put("name", goods.getName());
			item.put("price", goods.getPrice());
			item.put("marketPrice", goods.getMarketPrice());
			item.put("thumbnail", goods.getThumbnail());
			item.put("url", goods.getUrl());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加对比
	 */
	@RequestMapping(value = "/add_compare", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> addCompare(Long goodsId) {
		Map<String, Object> data = new HashMap<String, Object>();
		TGoods goods = goodsService.find(goodsId);
		if (goods == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}

		data.put("message", SUCCESS_MESSAGE);
		data.put("id", goods.getId());
		data.put("name", goods.getName());
		data.put("price", goods.getPrice());
		data.put("marketPrice", goods.getMarketPrice());
		data.put("thumbnail", goods.getThumbnail());
		data.put("url", goods.getUrl());
		return data;
	}

	/**
	 * 浏览记录
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> history(Long[] goodsIds) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_HISTORY_GOODS_COUNT) {
			return data;
		}

		List<TGoods> goodsList = goodsService.findList(goodsIds);
		for (TGoods goods : goodsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", goods.getName());
			item.put("price", goods.getPrice());
			item.put("thumbnail", goods.getThumbnail());
			item.put("url", goods.getUrl());
			data.add(item);
		}
		return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
	public String list(@PathVariable Long productCategoryId, TGoods.Type type, Long brandId, Long promotionId,
					   Long tagId, BigDecimal startPrice, BigDecimal endPrice, TGoods.OrderType orderType,
					   Integer pageNumber, Integer pageSize, HttpServletRequest request, ModelMap model) {
		TProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			throw new ResourceNotFoundException();
		}
		TBrand brand = brandService.find(brandId);
		TPromotion promotion = promotionService.find(promotionId);
		TTag tag = tagService.find(tagId);
		Map<TAttribute, String> attributeValueMap = new HashMap<TAttribute, String>();
		List<TAttribute> attributes = attributeService.findByProductCategoryId(productCategoryId);
		if (CollectionUtils.isNotEmpty(attributes)) {
			for (TAttribute attribute : attributes) {
				String value = request.getParameter("attribute_" + attribute.getId());
				String attributeValue = attributeService.toAttributeValue(attribute, value);
				if (attributeValue != null) {
					attributeValueMap.put(attribute, attributeValue);
				}
			}
		}

		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", TGoods.OrderType.values());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("type", type);
		model.addAttribute("brand", brand);
		model.addAttribute("promotion", promotion);
		model.addAttribute("tag", tag);
		model.addAttribute("attributeValueMap", attributeValueMap);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("page", goodsService.findPage(type, productCategoryId, brandId, promotionId,
				tagId, attributeValueMap, startPrice, endPrice, true, true, null,
				null, null, null, orderType, pageable));
		return "/shop/${theme}/goods/list";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(TGoods.Type type, Long brandId, Long promotionId, Long tagId, BigDecimal startPrice,
					   BigDecimal endPrice, TGoods.OrderType orderType, Integer pageNumber, Integer pageSize,
					   ModelMap model) {
		TBrand brand = brandService.find(brandId);
		TPromotion promotion = promotionService.find(promotionId);
		TTag tag = tagService.find(tagId);

		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", TGoods.OrderType.values());
		model.addAttribute("type", type);
		model.addAttribute("brand", brand);
		model.addAttribute("promotion", promotion);
		model.addAttribute("tag", tag);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("page", goodsService.findPage(type, null, brandId, promotionId,
				tagId, null, startPrice, endPrice, true, true, null,
				null, null, null, orderType, pageable));
		return "/shop/${theme}/goods/list";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model){
		TGoods goods =  goodsService.findDetailById(id);
		if (goods == null){
			throw new ResourceNotFoundException();
		}
		if (!goods.getIsMarketable()) {
			throw new RuntimeException(SpringUtils.getMessage("shop.goods.isMarketable"));
		}
		model.addAttribute("goods", goods);
		return "/shop/${theme}/goods/content";
	}

	/**
	 * 搜索
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(String keyword, BigDecimal startPrice, BigDecimal endPrice, TGoods.OrderType orderType,
						 Integer pageNumber, Integer pageSize, ModelMap model) {
		if (StringUtils.isEmpty(keyword)) {
			return ERROR_VIEW;
		}

		Pageable pageable = new Pageable(pageNumber, pageSize);
		model.addAttribute("orderTypes", TGoods.OrderType.values());
		model.addAttribute("goodsKeyword", keyword);
		model.addAttribute("startPrice", startPrice);
		model.addAttribute("endPrice", endPrice);
		model.addAttribute("orderType", orderType);
		model.addAttribute("page", searchService.search(keyword, startPrice, endPrice, orderType, pageable));
		return "/shop/${theme}/goods/search";
	}

	/**
	 * 对比
	 */
	@RequestMapping(value = "/compare", method = RequestMethod.GET)
	public String compare(Long[] goodsIds, ModelMap model) {
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_COMPARE_GOODS_COUNT) {
			return ERROR_VIEW;
		}

		List<TGoods> goodsList = goodsService.findList(goodsIds);
		if (CollectionUtils.isEmpty(goodsList)) {
			return ERROR_VIEW;
		}

		model.addAttribute("goodsList", goodsList);
		return "/shop/${theme}/goods/compare";
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{goodsId}", method = RequestMethod.GET)
	public @ResponseBody
	Long hits(@PathVariable Long goodsId) {
		if (goodsId == null) {
			return 0L;
		}

		return goodsService.viewHits(goodsId);
	}

}