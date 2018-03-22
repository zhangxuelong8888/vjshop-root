
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TCartItem;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TProduct;
import com.vjshop.generated.db.tables.records.TCartItemRecord;
import com.vjshop.util.JooqUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 购物车项
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TCartItemDao extends JooqBaseDao<TCartItemRecord, com.vjshop.entity.TCartItem, Long> {

    public TCartItemDao() {
        super(TCartItem.T_CART_ITEM, com.vjshop.entity.TCartItem.class);
    }

    @Autowired
    public TCartItemDao(Configuration configuration) {
        super(TCartItem.T_CART_ITEM, com.vjshop.entity.TCartItem.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TCartItem object) {
        return object.getId();
    }

    /**
     * 获取购物车项
     *
     * @param cartItemId
     *            购物车项ID
     * @return 购物车项
     */
    public com.vjshop.entity.TCartItem findDetails(Long cartItemId) {
        SelectQuery query = getDslContext().select(TCartItem.T_CART_ITEM.fields())
                .from(TCartItem.T_CART_ITEM)
                .where(TCartItem.T_CART_ITEM.ID.eq(cartItemId))
                .getQuery();

        addQuery(query);

        List<com.vjshop.entity.TCartItem> cartItemList = resultSet2List(query.fetchResultSet(), "id", "productVO.id", "productVO.goodsVO.id");
        if (CollectionUtils.isNotEmpty(cartItemList)){
            return cartItemList.get(0);
        }
        return null;
    }

    /**
     * 获取购物车项
     *
     * @param cartId
     *            购物车ID
     * @return 购物车项
     */
    public List<com.vjshop.entity.TCartItem> findList(Long cartId) {
        SelectQuery query = getDslContext().select(TCartItem.T_CART_ITEM.fields())
                .from(TCartItem.T_CART_ITEM)
                .where(TCartItem.T_CART_ITEM.CART.eq(cartId))
                .getQuery();

        addQuery(query);

        return resultSet2List(query.fetchResultSet(), "id", "productVO.id", "productVO.goodsVO.id");
    }

    /**
     * 获取已选中购物车项
     *
     * @param cartId
     *            购物车ID
     * @return 购物车项
     */
    public List<com.vjshop.entity.TCartItem> findListBySelected(Long cartId) {
        SelectQuery query = getDslContext().select(TCartItem.T_CART_ITEM.fields())
                .from(TCartItem.T_CART_ITEM)
                .where(TCartItem.T_CART_ITEM.CART.eq(cartId))
                .and(TCartItem.T_CART_ITEM.IS_SELECTED.eq(true))
                .getQuery();

        addQuery(query);

        return resultSet2List(query.fetchResultSet(), "id", "productVO.id", "productVO.goodsVO.id");
    }

    private void addQuery(SelectQuery query) {
        query.addSelect(JooqUtils.getFields(TProduct.T_PRODUCT, "productVO"));
        query.addSelect(JooqUtils.getFields(TGoods.T_GOODS, "productVO.goodsVO"));

        query.addJoin(TProduct.T_PRODUCT, JoinType.LEFT_OUTER_JOIN, TProduct.T_PRODUCT.ID.eq(TCartItem.T_CART_ITEM.PRODUCT));
        query.addJoin(TGoods.T_GOODS, JoinType.LEFT_OUTER_JOIN, TGoods.T_GOODS.ID.eq(TProduct.T_PRODUCT.GOODS));
    }

    /**
     * 删除购物车项
     *
     * @param cartId
     *            购物车ID
     * @return 影响条数
     */
    public int deleteByCartId(Long cartId){
        return getDslContext().deleteFrom(TCartItem.T_CART_ITEM)
                .where(TCartItem.T_CART_ITEM.CART.eq(cartId))
                .execute();
    }

    /**
     * 删除已选中的购物车项
     * @param cartId
     * @return
     */
    public int deleteSelected(Long cartId) {
        return getDslContext().deleteFrom(TCartItem.T_CART_ITEM)
                .where(TCartItem.T_CART_ITEM.CART.eq(cartId))
                .and(TCartItem.T_CART_ITEM.IS_SELECTED.isTrue())
                .execute();
    }

    /**
     * 更新购物车项选中状态
     * @param cartItemIds 购物车项id
     * @param cartId 购物车id
     * @param isSelected 选中状态
     */
    public void updateSelectedStatus(Long[] cartItemIds, Long cartId, Boolean isSelected) {
        getDslContext().update(TCartItem.T_CART_ITEM)
                .set(TCartItem.T_CART_ITEM.IS_SELECTED, isSelected)
                .where(TCartItem.T_CART_ITEM.CART.eq(cartId))
                .and(TCartItem.T_CART_ITEM.ID.in(cartItemIds))
                .execute();
    }

}
