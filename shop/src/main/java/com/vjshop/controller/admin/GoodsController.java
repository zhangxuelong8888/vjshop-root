
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.*;
import com.vjshop.service.*;
import com.vjshop.util.JsonUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Controller - 货品
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminGoodsController")
@RequestMapping("/admin/goods")
public class GoodsController extends BaseController {

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "parameterValueServiceImpl")
	private ParameterValueService parameterValueService;
	@Resource(name = "specificationItemServiceImpl")
	private SpecificationItemService specificationItemService;
	@Autowired
	private TAdminService tAdminService;

	@Autowired
	private TGoodsService tGoodsService;
	@Autowired
	private TBrandService tBrandService;
	@Autowired
	private TProductCategoryService tProductCategoryService;
	@Autowired
	private TPromotionService tPromotionService;
	@Autowired
	private TTagService tTagService;
	@Autowired
	private TSpecificationService tSpecificationService;
	@Autowired
	private TAttributeService tAttributeService;
	@Autowired
	private TProductService tProductService;

	/**
	 * 检查编号是否存在
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		return !tGoodsService.snExists(sn);
	}

	/**
	 * 获取参数
	 */
	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> parameters(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		TProductCategory productCategory = this.tProductCategoryService.findDetailById(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
			return data;
		}
		for (TParameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("group", parameter.getParameterGroup());
			item.put("names", parameter.getNameList());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取属性
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> attributes(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		TProductCategory productCategory = this.tProductCategoryService.findDetailById(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
			return data;
		}
		for (TAttribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptionsList());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取规格
	 */
	@RequestMapping(value = "/specifications", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> specifications(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		TProductCategory productCategory = this.tProductCategoryService.findDetailById(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return data;
		}
		for (TSpecification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptionsList());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", TGoods.Type.values());
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("brands", this.tBrandService.findAll());
		model.addAttribute("promotions", this.tPromotionService.findAll());
		model.addAttribute("tags", this.tTagService.findByTypes(TTag.Type.goods.ordinal()));
		model.addAttribute("specifications", this.tSpecificationService.findAll());
		return "/admin/goods/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TGoods goods, ProductForm productForm, ProductListForm productListForm, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImagesList());
		parameterValueService.filter(goods.getParameterValuesList());
		specificationItemService.filter(goods.getSpecificationItemsList());
		tProductService.filter(productListForm.getProductList());

		goods.setProductCategoryVO(tProductCategoryService.findDetailById(productCategoryId));
		goods.setProductCategory(productCategoryId);
		goods.setBrandVO(this.tBrandService.find(brandId));
        goods.setBrand(brandId);
        goods.setPromotions(new HashSet<TPromotion>(this.tPromotionService.findList(promotionIds)));
        goods.setTags(new HashSet<TTag>(this.tTagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (TAttribute attribute : goods.getProductCategoryVO().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = this.tAttributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(goods.getSn()) && tGoodsService.snExists(goods.getSn())) {
			return ERROR_VIEW;
		}

		TAdmin admin = tAdminService.getCurrent();
		if (goods.hasSpecification()) {
			List<TProduct> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(TGoods.Type.valueOf(goods.getType())), ValidGroup.Save.class)) {
				return ERROR_VIEW;
			}
			this.tGoodsService.save(goods, products, admin);
		} else {
			TProduct product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(TGoods.Type.valueOf(goods.getType())), ValidGroup.Save.class)) {
				return ERROR_VIEW;
			}
			this.tGoodsService.save(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types",TGoods.Type.values());
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("brands",this.tPromotionService.findAll());
		model.addAttribute("promotions",this.tPromotionService.findAll());
		model.addAttribute("tags",this.tTagService.findByTypes(TTag.Type.goods.ordinal()));
		model.addAttribute("specifications",this.tSpecificationService.findAll());
		model.addAttribute("goods", this.tGoodsService.findDetailById(id));
		return "/admin/goods/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TGoods goods, ProductForm productForm, ProductListForm productListForm, Long id, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImagesList());
		parameterValueService.filter(goods.getParameterValuesList());
		specificationItemService.filter(goods.getSpecificationItemsList());
		tProductService.filter(productListForm.getProductList());
		if(CollectionUtils.isNotEmpty(goods.getParameterValuesList()))goods.setParameterValues(JsonUtils.toJson(goods.getParameterValuesList()));

		TGoods pGoods = this.tGoodsService.find(id);
		goods.setType(pGoods.getType());
		goods.setProductCategoryVO(tProductCategoryService.findDetailById(productCategoryId));
		goods.setProductCategory(productCategoryId);
		goods.setBrandVO(tBrandService.find(brandId));
		goods.setBrand(brandId);
		goods.setPromotions(new HashSet<TPromotion>(this.tPromotionService.findList(promotionIds)));
		goods.setTags(new HashSet<TTag>(this.tTagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (TAttribute attribute : goods.getProductCategoryVO().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = this.tAttributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, ValidGroup.Update.class)) {
			return ERROR_VIEW;
		}

		TAdmin admin = tAdminService.getCurrent();
		if (goods.hasSpecification()) {
			List<TProduct> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(TGoods.Type.valueOf(goods.getType())), ValidGroup.Update.class)) {
				return ERROR_VIEW;
			}
			this.tGoodsService.update(goods, products, admin);
		} else {
			TProduct product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(TGoods.Type.valueOf(goods.getType())), ValidGroup.Update.class)) {
				return ERROR_VIEW;
			}
			this.tGoodsService.update(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId,
					   Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
					   Boolean isStockAlert, Pageable pageable, ModelMap model) {
		model.addAttribute("types",TGoods.Type.values());
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("brands",this.tBrandService.findAll());
		model.addAttribute("promotions",this.tPromotionService.findAll());
		model.addAttribute("tags",this.tTagService.findByTypes(TTag.Type.goods.ordinal()));
		model.addAttribute("type",type);
		model.addAttribute("productCategoryId",productCategoryId);
		model.addAttribute("brandId",brandId);
		model.addAttribute("promotionId",promotionId);
		model.addAttribute("tagId",tagId);
		model.addAttribute("isMarketable",isMarketable);
		model.addAttribute("isList",isList);
		model.addAttribute("isTop",isTop);
		model.addAttribute("isOutOfStock",isOutOfStock);
		model.addAttribute("isStockAlert",isStockAlert);
		model.addAttribute("page",this.tGoodsService.findPage(type, productCategoryId, brandId, promotionId, tagId, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable));
		return "/admin/goods/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tGoodsService.deleteCascade(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 根据类型获取验证组
	 * 
	 * @param type
	 *            类型
	 * @return 验证组
	 */
	private Class<?> getValidationGroup(TGoods.Type type) {
		Assert.notNull(type);

		switch (type) {
		case general:
			return TProduct.General.class;
		case exchange:
			return TProduct.Exchange.class;
		case gift:
			return TProduct.Gift.class;
		}
		return null;
	}

	/**
	 * FormBean - 商品
	 * 
	 * @author VJSHOP Team
	 * @version 4.0
	 */
	public static class ProductForm {

		/** 商品 */
		private TProduct product;

		/**
		 * 获取商品
		 * 
		 * @return 商品
		 */
		public TProduct getProduct() {
			return product;
		}

		/**
		 * 设置商品
		 * 
		 * @param product
		 *            商品
		 */
		public void setProduct(TProduct product) {
			this.product = product;
		}

	}

	/**
	 * FormBean - 商品
	 * 
	 * @author VJSHOP Team
	 * @version 4.0
	 */
	public static class ProductListForm {

		/** 商品 */
		private List<TProduct> productList;

		/**
		 * 获取商品
		 * 
		 * @return 商品
		 */
		public List<TProduct> getProductList() {
			return productList;
		}

		/**
		 * 设置商品
		 * 
		 * @param productList
		 *            商品
		 */
		public void setProductList(List<TProduct> productList) {
			this.productList = productList;
		}

	}

}