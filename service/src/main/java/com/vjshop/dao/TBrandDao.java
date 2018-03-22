
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.generated.db.tables.TBrand;
import com.vjshop.generated.db.tables.TGoods;
import com.vjshop.generated.db.tables.TProductCategory;
import com.vjshop.generated.db.tables.TProductCategoryBrand;
import com.vjshop.generated.db.tables.records.TBrandRecord;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.vjshop.generated.db.tables.TBrand.T_BRAND;
import static com.vjshop.generated.db.tables.TProductCategoryBrand.T_PRODUCT_CATEGORY_BRAND;

/**
 * Dao - 品牌
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TBrandDao extends JooqBaseDao<TBrandRecord, com.vjshop.entity.TBrand, Long> {

    public TBrandDao() {
        super(T_BRAND, com.vjshop.entity.TBrand.class);
    }

    @Autowired
    public TBrandDao(Configuration configuration) {
        super(T_BRAND, com.vjshop.entity.TBrand.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TBrand object) {
        return object.getId();
    }

    /** 关键字段 */
    private final String[] PROPERTY_KEYS = new String[]{ID_PROPERTY_NAME, "goods.id", "productCategories.id"};

    /**
     * 查找品牌
     *
     * @param brandId
     *            品牌ID
     * @return 品牌
     */
    public com.vjshop.entity.TBrand findDetails(Long brandId){
        SelectQuery query = getDslContext().select(T_BRAND.fields()).from(T_BRAND).getQuery();
        addJoin(query, brandId, null);
        List<com.vjshop.entity.TBrand> brandList = resultSet2List(query.fetchResultSet(), PROPERTY_KEYS);
        return CollectionUtils.isNotEmpty(brandList) ? brandList.get(0) : null;
    }

    /**
     * 查找品牌
     *
     * @param productCategoryId
     *            商品分类ID
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 品牌
     */
    public List<com.vjshop.entity.TBrand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders){
        SelectQuery query = getDslContext().select(TBrand.T_BRAND.fields()).from(TBrand.T_BRAND).getQuery();

        if (productCategoryId != null){
            query.addJoin(TProductCategoryBrand.T_PRODUCT_CATEGORY_BRAND, JoinType.LEFT_OUTER_JOIN,
                    TProductCategoryBrand.T_PRODUCT_CATEGORY_BRAND.BRANDS.eq(TBrand.T_BRAND.ID));
            query.addConditions(TProductCategoryBrand.T_PRODUCT_CATEGORY_BRAND.PRODUCT_CATEGORIES.eq(productCategoryId));
        }

        return findList(query, null, count, filters, orders, PROPERTY_KEYS);
    }

    /**
     * 查找品牌
     *
     * @return 品牌
     */
    @Override
    public List<com.vjshop.entity.TBrand> findAll(){
        SelectQuery query = getDslContext().select(T_BRAND.fields()).from(T_BRAND).getQuery();
        addJoin(query, null, null);
        return resultSet2List(query.fetchResultSet(), PROPERTY_KEYS);
    }

    private void addJoin(SelectQuery query, Long brandId, Long productCategoryId){
        query.addSelect(TGoods.T_GOODS.ID.as("goods.id"), TGoods.T_GOODS.NAME.as("goods.name"),
                TGoods.T_GOODS.PRODUCT_IMAGES.as("goodsInfo.productImages"));
        query.addJoin(TGoods.T_GOODS, JoinType.LEFT_OUTER_JOIN, TGoods.T_GOODS.BRAND.eq(T_BRAND.ID));

        query.addJoin(T_PRODUCT_CATEGORY_BRAND, JoinType.LEFT_OUTER_JOIN,
                T_PRODUCT_CATEGORY_BRAND.BRANDS.eq(T_BRAND.ID));

        query.addSelect(TProductCategory.T_PRODUCT_CATEGORY.ID.as("productCategories.id"),
                TProductCategory.T_PRODUCT_CATEGORY.NAME.as("productCategories.name"));
        query.addJoin(TProductCategory.T_PRODUCT_CATEGORY, JoinType.LEFT_OUTER_JOIN,
                TProductCategory.T_PRODUCT_CATEGORY.ID.eq(T_PRODUCT_CATEGORY_BRAND.PRODUCT_CATEGORIES));

        if (brandId != null){
            query.addConditions(T_BRAND.ID.eq(brandId));
        }
        if (productCategoryId != null){
            query.addConditions(T_PRODUCT_CATEGORY_BRAND.PRODUCT_CATEGORIES.eq(productCategoryId));
        }
    }

}
