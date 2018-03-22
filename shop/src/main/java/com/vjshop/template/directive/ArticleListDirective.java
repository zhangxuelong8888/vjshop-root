
package com.vjshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TArticle;
import com.vjshop.service.TArticleService;
import com.vjshop.util.FreeMarkerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 文章列表
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("articleListDirective")
public class ArticleListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

	/** "标签ID"参数名称 */
	private static final String TAG_ID_PARAMETER_NAME = "tagId";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "articles";

	@Autowired
	private TArticleService articleService;

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
		Long tagId = FreeMarkerUtils.getParameter(TAG_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, TArticle.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<TArticle> articles = articleService.findList(false, articleCategoryId, tagId, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, articles, env, body);
	}

}