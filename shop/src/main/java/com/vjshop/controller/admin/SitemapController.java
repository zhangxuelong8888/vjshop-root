
package com.vjshop.controller.admin;

import javax.annotation.Resource;

import com.vjshop.TemplateConfig;
import com.vjshop.service.StaticService;
import com.vjshop.util.SystemUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - Sitemap
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminSitemapController")
@RequestMapping("/admin/sitemap")
public class SitemapController extends BaseController {

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	/**
	 * 生成Sitemap
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String generate(ModelMap model) {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("sitemapIndex");
		model.addAttribute("sitemapIndexPath", templateConfig.getRealStaticPath());
		return "/admin/sitemap/generate";
	}

	/**
	 * 生成Sitemap
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public String generate(RedirectAttributes redirectAttributes) {
		staticService.generateSitemap();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:generate.jhtml";
	}

}