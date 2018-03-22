
package com.vjshop.controller.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TArticleCategory;
import com.vjshop.service.TArticleCategoryService;

import com.vjshop.service.TArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 文章分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminArticleCategoryController")
@RequestMapping("/admin/article_category")
public class ArticleCategoryController extends BaseController {

	@Autowired
	private TArticleCategoryService articleCategoryService;
	@Autowired
	private TArticleService articleService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		return "/admin/article_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TArticleCategory articleCategory, Long parentId, RedirectAttributes redirectAttributes) {
		articleCategory.setParent(parentId);
		if (!isValid(articleCategory)) {
			return ERROR_VIEW;
		}
		articleCategory.setTreePath(null);
		articleCategory.setGrade(null);
		articleCategoryService.save(articleCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TArticleCategory articleCategory = articleCategoryService.find(id);
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("articleCategory", articleCategory);
		model.addAttribute("children", articleCategoryService.findChildren(id, true, null));
		return "/admin/article_category/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TArticleCategory articleCategory, Long parentId, RedirectAttributes redirectAttributes) {
		articleCategory.setParent(parentId);
		if (!isValid(articleCategory)) {
			return ERROR_VIEW;
		}
		if (parentId != null) {
			if (articleCategory.getId() != null && parentId.equals(articleCategory.getId())) {
				return ERROR_VIEW;
			}
			TArticleCategory parent = articleCategoryService.find(parentId);
			List<TArticleCategory> children = articleCategoryService.findChildren(parentId, true, null);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		articleCategory.setModifyDate(new Timestamp(System.currentTimeMillis()));
		articleCategoryService.update(articleCategory, "treePath", "grade", "children", "articles");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		return "/admin/article_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		if (id == null) {
			return ERROR_MESSAGE;
		}
		List<TArticleCategory> children = articleCategoryService.findChildren(id, false, null);
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.articleCategory.deleteExistChildrenNotAllowed");
		}
		List<TArticle> articles = articleService.findByArticleCategory(id);
		if (articles != null && !articles.isEmpty()) {
			return Message.error("admin.articleCategory.deleteExistArticleNotAllowed");
		}
		articleCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}

}