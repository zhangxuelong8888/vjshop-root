
package com.vjshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TReview;
import com.vjshop.service.TReviewService;
import com.vjshop.util.FreeMarkerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 评论
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("reviewListDirective")
public class ReviewListDirective extends BaseDirective {

	/** "会员ID"参数名称 */
	private static final String MEMBER_ID_PARAMETER_NAME = "memberId";

	/** "货品ID"参数名称 */
	private static final String GOODS_ID_PARAMETER_NAME = "goodsId";

	/** "类型"参数名称 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "reviews";

	@Autowired
	private TReviewService reviewService;

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
		Long memberId = FreeMarkerUtils.getParameter(MEMBER_ID_PARAMETER_NAME, Long.class, params);
		Long goodsId = FreeMarkerUtils.getParameter(GOODS_ID_PARAMETER_NAME, Long.class, params);
		TReview.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, TReview.Type.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, TReview.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<TReview> reviews = reviewService.findList(memberId, goodsId, type, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, reviews, env, body);
	}

}