
package com.vjshop;

/**
 * 主题
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class Theme {

	/** ID */
	private String id;

	/** 名称 */
	private String name;

	/** 版本 */
	private String version;

	/** 作者 */
	private String author;

	/** 网址 */
	private String siteUrl;

	/** 预览 */
	private String preview;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置版本
	 * 
	 * @param version
	 *            版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置作者
	 * 
	 * @param author
	 *            作者
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * 设置网址
	 * 
	 * @param siteUrl
	 *            网址
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	/**
	 * 获取预览
	 * 
	 * @return 预览
	 */
	public String getPreview() {
		return preview;
	}

	/**
	 * 设置预览
	 * 
	 * @param preview
	 *            预览
	 */
	public void setPreview(String preview) {
		this.preview = preview;
	}

}