
package com.vjshop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import com.vjshop.CommonAttributes;
import com.vjshop.EnumConverter;
import com.vjshop.LogConfig;
import com.vjshop.Setting;
import com.vjshop.TemplateConfig;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

/**
 * Utils - 系统
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public final class SystemUtils {

	/** CacheManager */
	private static final CacheManager CACHE_MANAGER = CacheManager.create();

	/** BeanUtilsBean */
	private static final BeanUtilsBean BEAN_UTILS;

	static {
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(dateConverter, Date.class);
		BEAN_UTILS = new BeanUtilsBean(convertUtilsBean);
	}

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置
	 * 
	 * @return 系统设置
	 */
	@SuppressWarnings("unchecked")
	public static Setting getSetting() {
		Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
		String cacheKey = "setting";
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			Setting setting = new Setting();
			try {
				File vjshopXmlFile = new ClassPathResource(CommonAttributes.vjshop_XML_PATH).getFile();
				Document document = new SAXReader().read(vjshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/vjshop/setting");
				for (org.dom4j.Element element : elements) {
					try {
						String name = element.attributeValue("name");
						String value = element.attributeValue("value");
						BEAN_UTILS.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, setting));
			cacheElement = cache.get(cacheKey);
		}
		return (Setting) cacheElement.getObjectValue();
	}

	/**
	 * 设置系统设置
	 * 
	 * @param setting
	 *            系统设置
	 */
	@SuppressWarnings("unchecked")
	public static void setSetting(Setting setting) {
		Assert.notNull(setting);

		try {
			File vjshopXmlFile = new ClassPathResource(CommonAttributes.vjshop_XML_PATH).getFile();
			Document document = new SAXReader().read(vjshopXmlFile);
			List<org.dom4j.Element> elements = document.selectNodes("/vjshop/setting");
			for (org.dom4j.Element element : elements) {
				try {
					String name = element.attributeValue("name");
					String value = BEAN_UTILS.getProperty(setting, name);
					Attribute attribute = element.attribute("value");
					attribute.setValue(value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}

			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				xmlWriter = new XMLWriter(new FileOutputStream(vjshopXmlFile), outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					if (xmlWriter != null) {
						xmlWriter.close();
					}
				} catch (IOException e) {
				}
			}
			Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
			String cacheKey = "setting";
			cache.put(new Element(cacheKey, setting));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取模板配置
	 * 
	 * @param id
	 *            ID
	 * @return 模板配置
	 */
	public static TemplateConfig getTemplateConfig(String id) {
		Assert.hasText(id);

		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfig_" + id;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			TemplateConfig templateConfig = null;
			try {
				File vjshopXmlFile = new ClassPathResource(CommonAttributes.vjshop_XML_PATH).getFile();
				Document document = new SAXReader().read(vjshopXmlFile);
				org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/vjshop/templateConfig[@id='" + id + "']");
				if (element != null) {
					templateConfig = getTemplateConfig(element);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfig));
			cacheElement = cache.get(cacheKey);
		}
		return (TemplateConfig) cacheElement.getObjectValue();
	}

	/**
	 * 获取模板配置
	 * 
	 * @param type
	 *            类型
	 * @return 模板配置
	 */
	@SuppressWarnings("unchecked")
	public static List<TemplateConfig> getTemplateConfigs(TemplateConfig.Type type) {
		Ehcache cache = CACHE_MANAGER.getEhcache(TemplateConfig.CACHE_NAME);
		String cacheKey = "templateConfigs_" + type;
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			List<TemplateConfig> templateConfigs = new ArrayList<TemplateConfig>();
			try {
				File vjshopXmlFile = new ClassPathResource(CommonAttributes.vjshop_XML_PATH).getFile();
				Document document = new SAXReader().read(vjshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes(type != null ? "/vjshop/templateConfig[@type='" + type + "']" : "/vjshop/templateConfig");
				for (org.dom4j.Element element : elements) {
					templateConfigs.add(getTemplateConfig(element));
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, templateConfigs));
			cacheElement = cache.get(cacheKey);
		}
		return (List<TemplateConfig>) cacheElement.getObjectValue();
	}

	/**
	 * 获取所有模板配置
	 * 
	 * @return 所有模板配置
	 */
	public static List<TemplateConfig> getTemplateConfigs() {
		return getTemplateConfigs(null);
	}

	/**
	 * 获取所有日志配置
	 * 
	 * @return 所有日志配置
	 */
	@SuppressWarnings("unchecked")
	public static List<LogConfig> getLogConfigs() {
		Ehcache cache = CACHE_MANAGER.getEhcache(LogConfig.CACHE_NAME);
		String cacheKey = "logConfigs";
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			try {
				File vjshopXmlFile = new ClassPathResource(CommonAttributes.vjshop_XML_PATH).getFile();
				Document document = new SAXReader().read(vjshopXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/vjshop/logConfig");
				for (org.dom4j.Element element : elements) {
					String operation = element.attributeValue("operation");
					String urlPattern = element.attributeValue("urlPattern");
					LogConfig logConfig = new LogConfig();
					logConfig.setOperation(operation);
					logConfig.setUrlPattern(urlPattern);
					logConfigs.add(logConfig);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, logConfigs));
			cacheElement = cache.get(cacheKey);
		}
		return (List<LogConfig>) cacheElement.getObjectValue();
	}

	/**
	 * 获取模板配置
	 * 
	 * @param element
	 *            元素
	 * @return 模板配置
	 */
	private static TemplateConfig getTemplateConfig(org.dom4j.Element element) {
		Assert.notNull(element);

		String id = element.attributeValue("id");
		String type = element.attributeValue("type");
		String name = element.attributeValue("name");
		String templatePath = element.attributeValue("templatePath");
		String staticPath = element.attributeValue("staticPath");
		String description = element.attributeValue("description");

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setId(id);
		templateConfig.setType(TemplateConfig.Type.valueOf(type));
		templateConfig.setName(name);
		templateConfig.setTemplatePath(templatePath);
		templateConfig.setStaticPath(staticPath);
		templateConfig.setDescription(description);
		return templateConfig;
	}

}