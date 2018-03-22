
package com.vjshop.plugin.localStorage;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.vjshop.Setting;
import com.vjshop.plugin.StoragePlugin;
import com.vjshop.util.SystemUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Plugin - 本地文件存储
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("localStoragePlugin")
public class LocalStoragePlugin extends StoragePlugin implements ServletContextAware {

	/** ServletContext */
	private ServletContext servletContext;

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "VJSHOP";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.vjshop.com";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "local_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		File destFile = new File(servletContext.getRealPath(path));
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
		Setting setting = SystemUtils.getSetting();
		return setting.getSiteUrl() + path;
	}

}