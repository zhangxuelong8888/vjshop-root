
package com.vjshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vjshop.entity.TArticleCategory;
import com.vjshop.service.TArticleCategoryService;
import com.vjshop.util.FreeMarkerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 下级文章分类列表
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("articleCategoryChildrenListDirective")
public class ArticleCategoryChildrenListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

	/** "是否递归"参数名称 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "articleCategories";

	@Autowired
	private TArticleCategoryService articleCategoryService;

	/**
	 * 执行
	 * 
	 * @param env
	 *            环境变量
	 * @param params
	 *            参数
	 * @param loopVars
	 *            循环变量
	 * @param body
	 *            模板内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Boolean recursive = FreeMarkerUtils.getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, params);
		Integer count = getCount(params);
		List<TArticleCategory> articleCategories = articleCategoryService.findChildren(articleCategoryId, recursive != null ? recursive : true, count);
		setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
	}

}