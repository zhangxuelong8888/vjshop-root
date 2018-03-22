
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TBrand;

import com.vjshop.service.TBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 品牌
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminBrandController")
@RequestMapping("/admin/brand")
public class BrandController extends BaseController {

	@Autowired
	private TBrandService tBrandService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", TBrand.Type.values());
		return "/admin/brand/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TBrand brand, RedirectAttributes redirectAttributes) {
		if (!isValid(brand)) {
			return ERROR_VIEW;
		}
		if (Integer.valueOf(TBrand.Type.text.ordinal()).equals(brand.getType())) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			return ERROR_VIEW;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		brand.setCreateDate(now);
		brand.setModifyDate(now);
		brand.setVersion(0L);
		this.tBrandService.save(brand);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", TBrand.Type.values());
		model.addAttribute("brand", this.tBrandService.find(id));
		return "/admin/brand/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TBrand brand, RedirectAttributes redirectAttributes) {
		if (!isValid(brand)) {
			return ERROR_VIEW;
		}
		if (Integer.valueOf(TBrand.Type.text.ordinal()).equals(brand.getType())) {
			brand.setLogo(null);
		} else if (StringUtils.isEmpty(brand.getLogo())) {
			return ERROR_VIEW;
		}
		brand.setModifyDate(new Timestamp(System.currentTimeMillis()));
		this.tBrandService.update(brand);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
//		model.addAttribute("page", brandService.findPage(pageable));
		model.addAttribute("page", this.tBrandService.findPage(pageable));
		return "/admin/brand/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tBrandService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}