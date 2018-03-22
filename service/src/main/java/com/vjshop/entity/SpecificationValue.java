
package com.vjshop.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 规格值
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public class SpecificationValue implements Serializable {

	private static final long serialVersionUID = 2727026870667528070L;

	/** ID */
	private Integer id;

	/** 值 */
	private String value;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	@NotNull
	public Integer getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	@NotEmpty
	@Length(max = 200)
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
