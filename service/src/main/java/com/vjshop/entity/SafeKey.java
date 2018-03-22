
package com.vjshop.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Entity - 安全密钥
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Embeddable
public class SafeKey implements Serializable {

	private static final long serialVersionUID = -15486548528588455L;

	/** 密钥 */
	private String value;

	/** 过期时间 */
	private Timestamp expire;

	/**
	 * 获取密钥
	 * 
	 * @return 密钥
	 */
	@Column(name = "safe_key_value")
	public String getValue() {
		return value;
	}

	/**
	 * 设置密钥
	 * 
	 * @param value
	 *            密钥
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取过期时间
	 * 
	 * @return 过期时间
	 */
	@Column(name = "safe_key_expire")
	public Timestamp getExpire() {
		return expire;
	}

	/**
	 * 设置过期时间
	 * 
	 * @param expire
	 *            过期时间
	 */
	public void setExpire(Timestamp expire) {
		this.expire = expire;
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return getExpire() != null && !getExpire().after(new Timestamp(System.currentTimeMillis()));
	}

}
