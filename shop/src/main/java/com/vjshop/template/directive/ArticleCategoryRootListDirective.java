
package com.vjshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vjshop.entity.TArticleCategory;
import com.vjshop.service.TArticleCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 顶级文章分类列表
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("articleCategoryRootListDirective")
public class ArticleCategoryRootListDirective extends BaseDirective {

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
		Integer count = getCount(params);
		boolean useCache = useCache(env, params);
		List<TArticleCategory> articleCategories = articleCategoryService.findRoots(count, useCache);
		setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
	}

}