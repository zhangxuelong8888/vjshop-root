
package com.vjshop.listener;

import java.io.File;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.vjshop.service.ConfigService;
import com.vjshop.service.TSearchService;
import com.vjshop.service.StaticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Listener - 初始化
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

	/** 安装初始化配置文件 */
	private static final String INSTALL_INIT_CONFIG_FILE_PATH = "/install_init.conf";

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(InitListener.class.getName());

	/** ServletContext */
	private ServletContext servletContext;

	@Value("${system.version}")
	private String systemVersion;
	@Resource(name = "configServiceImpl")
	private ConfigService configService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Autowired
	private TSearchService searchService;

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 事件执行
	 * 
	 * @param contextRefreshedEvent
	 *            ContextRefreshedEvent
	 */
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (servletContext != null && contextRefreshedEvent.getApplicationContext().getParent() == null) {
			String info = "I|n|i|t|i|a|l|i|z|i|n|g| |V|J|S|H|O|P| |" + systemVersion;
			LOGGER.info(info.replace("|", ""));
			configService.init();
			searchService.purge();
			searchService.index();
		}
	}

}