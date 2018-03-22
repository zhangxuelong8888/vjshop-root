package com.vjshop.template.directive;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TGoodsService;
import com.vjshop.util.FreeMarkerUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 货品列表
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("goodsListDirective")
public class GoodsListDirective extends BaseDirective {

	/** "类型"参数名称 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/** "商品分类ID"参数名称 */
	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

	/** "品牌ID"参数名称 */
	private static final String BRAND_ID_PARAMETER_NAME = "brandId";

	/** "促销ID"参数名称 */
	private static final String PROMOTION_ID_PARAMETER_NAME = "promotionId";

	/** "标签ID"参数名称 */
	private static final String TAG_ID_PARAMETER_NAME = "tagId";

	/** "属性值"参数名称 */
	private static final String ATTRIBUTE_VALUE_PARAMETER_NAME = "attributeValue";

	/** "最低价格"参数名称 */
	private static final String START_PRICE_PARAMETER_NAME = "startPrice";

	/** "最高价格"参数名称 */
	private static final String END_PRICE_PARAMETER_NAME = "endPrice";

	/** "是否存在促销"参数名称 */
	private static final String HAS_PROMOTION_PARAMETER_NAME = "hasPromotion";

	/** "排序类型"参数名称 */
	private static final String ORDER_TYPE_PARAMETER_NAME = "orderType";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "goodsList";

	@Autowired
	private TGoodsService goodsService;

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
		TGoods.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, TGoods.Type.class, params);
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long brandId = FreeMarkerUtils.getParameter(BRAND_ID_PARAMETER_NAME, Long.class, params);
		Long promotionId = FreeMarkerUtils.getParameter(PROMOTION_ID_PARAMETER_NAME, Long.class, params);
		Long tagId = FreeMarkerUtils.getParameter(TAG_ID_PARAMETER_NAME, Long.class, params);
		Map<String, String> attributeValue = FreeMarkerUtils.getParameter(ATTRIBUTE_VALUE_PARAMETER_NAME, Map.class, params);
		BigDecimal startPrice = FreeMarkerUtils.getParameter(START_PRICE_PARAMETER_NAME, BigDecimal.class, params);
		BigDecimal endPrice = FreeMarkerUtils.getParameter(END_PRICE_PARAMETER_NAME, BigDecimal.class, params);
		Boolean hasPromotion = FreeMarkerUtils.getParameter(HAS_PROMOTION_PARAMETER_NAME, Boolean.class, params);
		TGoods.OrderType orderType = FreeMarkerUtils.getParameter(ORDER_TYPE_PARAMETER_NAME, TGoods.OrderType.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, TGoods.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		Map<Long, String> attributeValueMap = new HashMap<Long, String>();
		if (attributeValue != null) {
			for (Map.Entry<String, String> entry : attributeValue.entrySet()) {
				if (NumberUtils.isDigits(entry.getKey())) {
					Long attributeId = Long.valueOf(entry.getKey());
					attributeValueMap.put(attributeId, entry.getValue());
				}
			}
		}
		List<TGoods> goodsList = goodsService.findList(type, productCategoryId, brandId, promotionId, tagId, attributeValueMap, startPrice, endPrice, true, true, null, null, null, hasPromotion, orderType, count, filters, orders, useCache);

		setLocalVariable(VARIABLE_NAME, goodsList, env, body);
	}
}