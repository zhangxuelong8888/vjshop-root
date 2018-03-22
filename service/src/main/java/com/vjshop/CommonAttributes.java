
package com.vjshop;

/**
 * 公共参数
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** vjshop.xml文件路径 */
	public static final String vjshop_XML_PATH = "/vjshop.xml";

	/** vjshop.properties文件路径 */
	public static final String vjshop_PROPERTIES_PATH = "/vjshop.properties";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}