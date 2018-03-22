package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 货品
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TGoodsService extends TBaseService<TGoods,Long>{

	/**
	 * 判断编号是否存在
	 *
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	boolean snExists(String sn);

	/**
	 * 根据编号查找货品
	 *
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 货品，若不存在则返回null
	 */
	TGoods findBySn(String sn);

	/**
	 * 查找货品
	 *
	 * @param type
	 *            类型
	 * @param productCategoryId
	 *            商品分类
	 * @param brandId
	 *            品牌
	 * @param promotionId
	 *            促销
	 * @param tagId
	 *            标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 货品
	 */
	List<TGoods> findList(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
						 Boolean isStockAlert, Boolean hasPromotion, TGoods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找货品
	 *
	 * @param type
	 *            类型
	 * @param productCategoryId
	 *            商品分类ID
	 * @param brandId
	 *            品牌ID
	 * @param promotionId
	 *            促销ID
	 * @param tagId
	 *            标签ID
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 货品
	 */
	List<TGoods> findList(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
						 Boolean hasPromotion, TGoods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找货品
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
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 货品
	 */
	List<TGoods> findList(Long productCategoryId, Boolean isMarketable, TGoods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count);

	/**
	 * 查找货品分页
	 *
	 * @param type
	 *            类型
	 * @param productCategoryId
	 *            商品分类
	 * @param brandId
	 *            品牌
	 * @param promotionId
	 *            促销
	 * @param tagId
	 *            标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	Page<TGoods> findPage(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                         Boolean isStockAlert, Boolean hasPromotion, TGoods.OrderType orderType, Pageable pageable);

	/**
	 * 查找货品分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	Page<TGoods> findPage(TGoods.RankingType rankingType, Pageable pageable);

	/**
	 * 查找收藏货品分页
	 * 
	 * @param memberId
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 收藏货品分页
	 */
	Page<TGoods> findPage(Long memberId, Pageable pageable);

	/**
	 * 查询货品数量
	 * 
	 * @param type
	 *            类型
	 * @param favoriteMemberId
	 *            收藏会员
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @return 货品数量
	 */
	Long count(TGoods.Type type, Long favoriteMemberId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert);

	/**
	 * 查看点击数
	 * 
	 * @param id
	 *            ID
	 * @return 点击数
	 */
	long viewHits(Long id);

	/**
	 * 增加点击数
	 * 
	 * @param goods
	 *            货品
	 * @param amount
	 *            值
	 */
	void addHits(Long goods, long amount);

	/**
	 * 增加销量
	 * 
	 * @param goods
	 *            货品
	 * @param amount
	 *            值
	 */
	void addSales(Long goods, long amount);

	/**
	 * 保存
	 * 
	 * @param goods
	 *            货品
	 * @param product
	 *            商品
	 * @param operator
	 *            操作员
	 * @return 货品
	 */
	TGoods save(TGoods goods, TProduct product, TAdmin operator);

	/**
	 * 保存
	 * 
	 * @param goods
	 *            货品
	 * @param products
	 *            商品
	 * @param operator
	 *            操作员
	 * @return 货品
	 */
	TGoods save(TGoods goods, List<TProduct> products, TAdmin operator);

	/**
	 * 更新
	 * 
	 * @param goods
	 *            货品
	 * @param product
	 *            商品
	 * @param operator
	 *            操作员
	 * @return 货品
	 */
	TGoods update(TGoods goods, TProduct product, TAdmin operator);

	/**
	 * 更新
	 * 
	 * @param goods
	 *            货品
	 * @param products
	 *            商品
	 * @param operator
	 *            操作员
	 * @return 货品
	 */
	TGoods update(TGoods goods, List<TProduct> products, TAdmin operator);



	TGoods findDetailById(Long id);

	List<TGoods> findByProductcategory(Long... productcategory);

	void deleteCascade(Long... ids);

}