
package com.vjshop.controller.admin;

import java.util.List;

import com.vjshop.Message;
import com.vjshop.entity.*;
import com.vjshop.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

	@Autowired
	private TProductCategoryService tProductCategoryService;
	@Autowired
	private TBrandService tBrandService;
	@Autowired
	private TPromotionService tPromotionService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("brands", this.tBrandService.findAll());
		model.addAttribute("promotions", this.tPromotionService.findAll());
		return "/admin/product_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TProductCategory productCategory, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		productCategory.setBrands(this.tBrandService.findList(brandIds));
		productCategory.setPromotions(this.tPromotionService.findList(promotionIds));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setGoods(null);
		productCategory.setParameters(null);
		productCategory.setAttributes(null);
		productCategory.setSpecifications(null);
		this.tProductCategoryService.save(productCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TProductCategory productCategory = this.tProductCategoryService.findDetailById(id);
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		model.addAttribute("brands", this.tBrandService.findAll());
		model.addAttribute("promotions", this.tPromotionService.findAll());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("children", this.tProductCategoryService.findChildren(id, true, null));
		return "/admin/product_category/edit";
//		model.addAttribute("children2", productCategoryService.findChildren(productCategory, true, null));
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TProductCategory productCategory, Long[] brandIds, Long[] promotionIds, RedirectAttributes redirectAttributes) {
		productCategory.setBrands(this.tBrandService.findList(brandIds));
		productCategory.setPromotions(this.tPromotionService.findList(promotionIds));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		if (productCategory.getParent() != null) {
			TProductCategory parent = this.tProductCategoryService.find(productCategory.getId());
			if (parent.equals(productCategory)) {
				return ERROR_VIEW;
			}
			List<TProductCategory> children = tProductCategoryService.findChildren(parent.getId(), true, null);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		this.tProductCategoryService.update(productCategory,new String[]{});
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("productCategoryTree", this.tProductCategoryService.findTree());
		return "/admin/product_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		TProductCategory productCategory = this.tProductCategoryService.findDetailById(id);
		if (productCategory == null) {
			return ERROR_MESSAGE;
		}
		List<TProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		List<TGoods> goods = productCategory.getGoods();
		if (goods != null && !goods.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistProductNotAllowed");
		}
		this.tProductCategoryService.deleteCascade(id);
		return SUCCESS_MESSAGE;
	}

}