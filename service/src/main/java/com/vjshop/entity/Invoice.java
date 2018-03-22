
package com.vjshop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 发票
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Embeddable
public class Invoice implements Serializable {

	private static final long serialVersionUID = 7259298832480479018L;

	/** 抬头 */
	private String title;

	/** 内容 */
	private String content;

	/**
	 * 构造方法
	 */
	public Invoice() {
	}

	/**
	 * 构造方法
	 * 
	 * @param title
	 *            抬头
	 * @param content
	 *            内容
	 */
	public Invoice(String title, String content) {
		this.title = title;
		this.content = content;
	}

	/**
	 * 获取抬头
	 * 
	 * @return 抬头
	 */
	@Length(max = 200)
	@Column(name = "invoice_title")
	public String getTitle() {
		return title;
	}

	/**
	 * 设置抬头
	 * 
	 * @param title
	 *            抬头
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Length(max = 200)
	@Column(name = "invoice_content")
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
