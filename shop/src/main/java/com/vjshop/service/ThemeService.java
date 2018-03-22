
package com.vjshop.service;

import java.util.List;

import com.vjshop.Theme;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service - 主题
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface ThemeService {

	/**
	 * 获取所有主题
	 * 
	 * @return 所有主题
	 */
	List<Theme> getAll();

	/**
	 * 获取主题
	 * 
	 * @param id
	 *            ID
	 * @return 主题
	 */
	Theme get(String id);

	/**
	 * 上传主题
	 * 
	 * @param multipartFile
	 *            上传文件
	 * @return 是否上传成功
	 */
	boolean upload(MultipartFile multipartFile);

}