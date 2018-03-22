
package com.vjshop.template.directive;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.entity.TAdPosition;
import com.vjshop.service.TAdPositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 广告位
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("adPositionDirective")
public class AdPositionDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "adPosition";

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private TAdPositionService adPositionService;

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
		Long id = getId(params);
		boolean useCache = useCache(env, params);
		TAdPosition adPosition = adPositionService.find(id, useCache);
		if (body != null) {
			setLocalVariable(VARIABLE_NAME, adPosition, env, body);
		} else {
			if (adPosition != null && adPosition.getTemplate() != null) {
				try {
					Map<String, Object> model = new HashMap<String, Object>();
					model.put(VARIABLE_NAME, adPosition);
					Writer out = env.getOut();
					new Template("adTemplate", new StringReader(adPosition.getTemplate()), freeMarkerConfigurer.getConfiguration()).process(model, out);
				} catch (TemplateException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}

}