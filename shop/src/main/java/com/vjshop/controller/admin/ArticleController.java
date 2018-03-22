
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TTag;
import com.vjshop.service.TArticleCategoryService;
import com.vjshop.service.TArticleService;
import com.vjshop.service.TTagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.HashSet;

/**
 * Controller - 文章
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Autowired
	private TArticleService articleService;
	@Autowired
	private TArticleCategoryService articleCategoryService;
	@Autowired
	private TTagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(TTag.Type.article));
		return "/admin/article/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TArticle article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryId);
		article.setTags(new HashSet<TTag>(tagService.findList(tagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setHits(0L);
		article.setGenerateMethod(TArticle.GenerateMethod.eager.ordinal());
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(TTag.Type.article));
		model.addAttribute("article", articleService.findDetails(id));
		return "/admin/article/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TArticle article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
		article.setArticleCategory(articleCategoryId);
		article.setTags(new HashSet<TTag>(tagService.findList(tagIds)));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setModifyDate(new Timestamp(System.currentTimeMillis()));
		articleService.update(article, "hits", "generateMethod");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleService.findPage(true, null, null, null, pageable));
		return "/admin/article/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}