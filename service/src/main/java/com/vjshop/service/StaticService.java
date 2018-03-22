
package com.vjshop.service;

import java.sql.Timestamp;
import java.util.Map;

import com.vjshop.entity.TArticle;
import com.vjshop.entity.TArticleCategory;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TProductCategory;

/**
 * Service - 静态化
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface StaticService {

	/**
	 * 生成静态
	 * 
	 * @param templatePath
	 *            模板文件路径
	 * @param staticPath
	 *            静态文件路径
	 * @param model
	 *            数据
	 * @return 生成数量
	 */
	int generate(String templatePath, String staticPath, Map<String, Object> model);

	/**
	 * 生成静态
	 * 
	 * @param article
	 *            文章
	 * @return 生成数量
	 */
	int generate(TArticle article);

	/**
	 * 生成静态
	 * 
	 * @param goods
	 *            货品
	 * @return 生成数量
	 */
	int generate(TGoods goods);

	/**
	 * 生成文章静态
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param isPublication
	 *            是否发布
	 * @param generateMethod
	 *            静态生成方式
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 生成数量
	 */
	int generateArticle(Long articleCategoryId, Boolean isPublication, TArticle.GenerateMethod generateMethod, Timestamp beginDate, Timestamp endDate);

	/**
	 * 生成货品静态
	 * 
	 * @param productCategoryId
	 *            商品分类
	 * @param isMarketable
	 *            是否上架
	 * @param generateMethod
	 *            静态生成方式
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 生成数量
	 */
	int generateGoods(Long productCategoryId, Boolean isMarketable, TGoods.GenerateMethod generateMethod, Timestamp beginDate, Timestamp endDate);

	/**
	 * 生成首页静态
	 * 
	 * @return 生成数量
	 */
	int generateIndex();

	/**
	 * 生成Sitemap
	 * 
	 * @return 生成数量
	 */
	int generateSitemap();

	/**
	 * 生成其它静态
	 * 
	 * @return 生成数量
	 */
	int generateOther();

	/**
	 * 生成所有静态
	 * 
	 * @return 生成数量
	 */
	int generateAll();

	/**
	 * 删除静态
	 * 
	 * @param staticPath
	 *            静态文件路径
	 * @return 删除数量
	 */
	int delete(String staticPath);

	/**
	 * 删除静态
	 * 
	 * @param article
	 *            文章
	 * @return 删除数量
	 */
	int delete(TArticle article);

	/**
	 * 删除静态
	 * 
	 * @param goods
	 *            货品
	 * @return 删除数量
	 */
	int delete(TGoods goods);

	/**
	 * 删除首页静态
	 * 
	 * @return 删除数量
	 */
	int deleteIndex();

	/**
	 * 删除其它静态
	 * 
	 * @return 删除数量
	 */
	int deleteOther();

}