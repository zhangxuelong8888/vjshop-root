
package com.vjshop;

import com.vjshop.util.FreeMarkerUtils;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * FreeMarker视图解析器
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class FreeMarkerViewResolver extends AbstractTemplateViewResolver {

	/**
	 * 构造方法
	 */
	public FreeMarkerViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * 视图类型
	 * 
	 * @return 视图类型
	 */
	@Override
	protected Class<FreeMarkerView> requiredViewClass() {
		return FreeMarkerView.class;
	}

	/**
	 * 创建视图
	 * 
	 * @param viewName
	 *            视图名称
	 * @return 视图
	 */
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		return super.buildView(FreeMarkerUtils.process(viewName));
	}

}