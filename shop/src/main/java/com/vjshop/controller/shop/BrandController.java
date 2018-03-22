
package com.vjshop.controller.shop;

import com.vjshop.Pageable;
import com.vjshop.entity.TBrand;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.TBrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 品牌
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopBrandController")
@RequestMapping("/brand")
public class BrandController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 40;

	@Autowired
	private TBrandService brandService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
	public String list(@PathVariable Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", brandService.findPage(pageable));
		return "/shop/${theme}/brand/list";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		TBrand brand = brandService.findDetails(id);
		if (brand == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("brand", brand);
		return "/shop/${theme}/brand/content";
	}

}