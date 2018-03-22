
package com.vjshop.service;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMemberFavoriteGoods;
import org.jooq.Record2;

/**
 * Service - 会员-收藏商品
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TMemberFavoriteGoodsService extends TBaseService<TMemberFavoriteGoods, Record2<Long, Long>> {

	/**
	 * 查找收藏数量
	 *
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            商品ID
	 * @return 收藏数量
	 */
	Long count(Long memberId, Long goodsId);

	/**
	 * 是否存在收藏
	 *
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            商品ID
	 * @return 是否存在
	 */
	boolean exists(Long memberId, Long goodsId);

	/**
	 * 分页获取收藏商品
	 *
	 * @param memberId
	 * 			  会员ID
	 * @param pageable
	 * 			  分页
	 * @return 分页商品
	 */
	Page<TGoods> findPage(Long memberId, Pageable pageable);

	/**
	 * 保存收藏
	 *
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            商品ID
	 * @return 收藏
	 */
	TMemberFavoriteGoods save(Long memberId, Long goodsId);

	/**
	 * 删除收藏
	 *
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            商品ID
	 * @return 影响数量
	 */
	void delete(Long memberId, Long goodsId);

}