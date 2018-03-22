
package com.vjshop.template.directive;

import java.io.IOException;
import java.util.Map;

import com.vjshop.entity.TSeo;
import com.vjshop.service.TSeoService;
import com.vjshop.util.FreeMarkerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - SEO设置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("seoDirective")
public class SeoDirective extends BaseDirective {

	/** "类型"参数名称 */
	private static final String TYPE_PARAMETER_NAME = "type";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "seo";

	@Autowired
	private TSeoService seoService;

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
		TSeo.Type type = FreeMarkerUtils.getParameter(TYPE_PARAMETER_NAME, TSeo.Type.class, params);
		boolean useCache = useCache(env, params);
		TSeo seo = seoService.find(type, useCache);
		setLocalVariable(VARIABLE_NAME, seo, env, body);
	}

}