
package com.vjshop.controller.admin;

import com.vjshop.Pageable;
import com.vjshop.entity.TSeo;
import com.vjshop.service.TSeoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - SEO设置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminSeoController")
@RequestMapping("/admin/seo")
public class SeoController extends BaseController {

	@Autowired
	private TSeoService seoService;

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("seo", seoService.find(id));
		return "/admin/seo/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TSeo seo, RedirectAttributes redirectAttributes) {
		if (!isValid(seo)) {
			return ERROR_VIEW;
		}
		seo.setModifyDate(new Timestamp(System.currentTimeMillis()));
		seoService.update(seo, "type");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", seoService.findPage(pageable));
		return "/admin/seo/list";
	}

}