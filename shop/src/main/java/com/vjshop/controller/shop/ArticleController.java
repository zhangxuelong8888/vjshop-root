
package com.vjshop.controller.shop;

import com.vjshop.Pageable;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TArticleCategory;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.TArticleCategoryService;
import com.vjshop.service.TArticleService;
import com.vjshop.service.TSearchService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 文章
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopArticleController")
@RequestMapping("/article")
public class ArticleController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 20;

	@Autowired
	private TArticleService articleService;
	@Autowired
	private TArticleCategoryService articleCategoryService;
	@Autowired
	private TSearchService searchService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String list(@PathVariable Long id, Integer pageNumber, ModelMap model) {
		TArticleCategory articleCategory = articleCategoryService.find(id);
		if (articleCategory == null) {
			throw new ResourceNotFoundException();
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("articleCategory", articleCategory);
		model.addAttribute("page", articleService.findPage(false, id, null, true, pageable));
		return "/shop/${theme}/article/list";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{id}/{pageNumber}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, @PathVariable Integer pageNumber, ModelMap model) {
		TArticle article = articleService.findDetails(id);
		if (!article.getIsPublication()) {
			return null;
		}
		//article.setContent(article.getPageContent(pageNumber));
		model.addAttribute("article", article);
		return "/shop/${theme}/article/content";
	}

	/**
	 * 搜索
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(String keyword, Integer pageNumber, ModelMap model) {
		if (StringUtils.isEmpty(keyword)) {
			return ERROR_VIEW;
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("articleKeyword", keyword);
		model.addAttribute("page", searchService.search(keyword, pageable));
		return "/shop/${theme}/article/search";
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody Long hits(@PathVariable Long id) {
		return articleService.viewHits(id);
	}

}