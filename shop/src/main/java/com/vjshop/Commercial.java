
package com.vjshop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 商业
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Commercial {

	/**
	 * 序列号
	 */
	String sn();

	/**
	 * 密钥
	 */
	String key();

}