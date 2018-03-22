
package com.vjshop.controller.shop;

import com.vjshop.entity.TProductCategory;
import com.vjshop.service.TProductCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controller - 商品分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopProductCategoryController")
@RequestMapping("/product_category")
public class ProductCategoryController extends BaseController {

	@Autowired
	private TProductCategoryService productCategoryService;

	/**
	 * 首页
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		List<TProductCategory> productCategoryList = productCategoryService.findRoots();
		for (TProductCategory category : productCategoryList) {
			category.setChildren(productCategoryService.findChildren(category.getId(), false, null));
		}
		model.addAttribute("rootProductCategories", productCategoryList);
		return "/shop/${theme}/product_category/index";
	}

}