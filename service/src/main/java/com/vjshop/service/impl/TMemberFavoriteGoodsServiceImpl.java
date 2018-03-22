
package com.vjshop.service.impl;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TGoodsDao;
import com.vjshop.dao.TMemberFavoriteGoodsDao;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMemberFavoriteGoods;
import com.vjshop.generated.db.tables.records.TMemberFavoriteGoodsRecord;
import com.vjshop.service.TMemberFavoriteGoodsService;
import org.jooq.Record2;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.vjshop.generated.db.tables.TGoods.T_GOODS;
import static com.vjshop.generated.db.tables.TMemberFavoriteGoods.T_MEMBER_FAVORITE_GOODS;

/**
 * Service - 友情链接
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TMemberFavoriteGoodsServiceImpl extends TBaseServiceImpl<TMemberFavoriteGoodsRecord, TMemberFavoriteGoods, Record2<Long, Long>>
		implements TMemberFavoriteGoodsService {

	@Autowired
	private TMemberFavoriteGoodsDao tMemberFavoriteGoodsDao;
    @Autowired
    private TGoodsDao tGoodsDao;

	@Transactional(readOnly = true)
	public Long count(Long memberId, Long goodsId) {
		return tMemberFavoriteGoodsDao.count(memberId, goodsId);
	}

	@Transactional(readOnly = true)
	public boolean exists(Long memberId, Long goodsId) {
		Long count = tMemberFavoriteGoodsDao.count(memberId, goodsId);
		return count != null && count.longValue() > 0;
	}

    @Transactional(readOnly = true)
	public Page<TGoods> findPage(Long memberId, Pageable pageable) {
        SelectQuery query = tGoodsDao.getDslContext().select(T_GOODS.fields())
                .from(T_GOODS).getQuery();

        query.addJoin(T_MEMBER_FAVORITE_GOODS, T_MEMBER_FAVORITE_GOODS.FAVORITE_GOODS.eq(T_GOODS.ID));
        query.addConditions(T_MEMBER_FAVORITE_GOODS.FAVORITE_MEMBERS.eq(memberId));

        return tGoodsDao.findPage(query, pageable);
	}

	@Override
	@Transactional
	public TMemberFavoriteGoods save(TMemberFavoriteGoods entity) {
		tMemberFavoriteGoodsDao.insert(entity);
		return entity;
	}

	@Transactional
	public TMemberFavoriteGoods save(Long memberId, Long goodsId) {
		TMemberFavoriteGoods mfg = new TMemberFavoriteGoods(memberId, goodsId);
		tMemberFavoriteGoodsDao.insert(mfg);
		return mfg;
	}

	@Transactional
	public void delete(Long memberId, Long goodsId) {
		tMemberFavoriteGoodsDao.delete(memberId, goodsId);
	}
}