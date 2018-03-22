
package com.vjshop.dao;

import com.vjshop.generated.db.tables.records.TProductCategoryRecord;
import com.vjshop.util.SpringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.jooq.Configuration;
import org.jooq.SelectLimitStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vjshop.generated.db.tables.TProductCategory.T_PRODUCT_CATEGORY;

/**
 * Dao - 商品分类
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TProductCategoryDao extends JooqBaseDao<TProductCategoryRecord, com.vjshop.entity.TProductCategory, Long> {

    public TProductCategoryDao() {
        super(T_PRODUCT_CATEGORY, com.vjshop.entity.TProductCategory.class);
    }

    @Autowired
    public TProductCategoryDao(Configuration configuration) {
        super(T_PRODUCT_CATEGORY, com.vjshop.entity.TProductCategory.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TProductCategory object) {
        return object.getId();
    }

    public List<com.vjshop.entity.TProductCategory> fetchByParent(Long... values) {
        return fetch(T_PRODUCT_CATEGORY.PARENT, values);
    }

    public List<com.vjshop.entity.TProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count) {
        List<com.vjshop.entity.TProductCategory> list = null;
        ResultSet res = null;
        SelectLimitStep sql = null;

        if (recursive) {
            if (productCategoryId != null) {
                sql = super.getDslContext().select().from(T_PRODUCT_CATEGORY).where(T_PRODUCT_CATEGORY.TREE_PATH.like("%" + com.vjshop.entity.TProductCategory.TREE_PATH_SEPARATOR + productCategoryId + com.vjshop.entity.TProductCategory.TREE_PATH_SEPARATOR + "%")).orderBy(T_PRODUCT_CATEGORY.GRADE.asc(), T_PRODUCT_CATEGORY.ORDERS.asc());
            } else {
                sql = super.getDslContext().select().from(T_PRODUCT_CATEGORY).orderBy(T_PRODUCT_CATEGORY.GRADE.asc(), T_PRODUCT_CATEGORY.ORDERS.asc());
            }
            if (count != null) {
                sql.limit(count);
            }

        } else {
            sql = super.getDslContext().select().from(T_PRODUCT_CATEGORY).where(T_PRODUCT_CATEGORY.PARENT.eq(productCategoryId)).orderBy(T_PRODUCT_CATEGORY.ORDERS.asc());
            if (count != null) {
                sql.limit(count);
            }
        }
        res = sql.fetchResultSet();
        list = resultSet2List(res);
        this.sort(list);
        return list;
    }

    private List<com.vjshop.entity.TProductCategory> findProductCategoryAndChildren(Long parentId){
        List<com.vjshop.entity.TProductCategory> list = null;
        ResultSet res = null;
        if(parentId != null)
            res = super.getDslContext().select().from(T_PRODUCT_CATEGORY).where(T_PRODUCT_CATEGORY.PARENT.eq(parentId)).orderBy(T_PRODUCT_CATEGORY.GRADE.asc(), T_PRODUCT_CATEGORY.ORDERS.asc()).fetchResultSet();
        else
            res = super.getDslContext().select().from(T_PRODUCT_CATEGORY).where(T_PRODUCT_CATEGORY.PARENT.isNull()).orderBy(T_PRODUCT_CATEGORY.GRADE.asc(), T_PRODUCT_CATEGORY.ORDERS.asc()).fetchResultSet();

        list = resultSet2List(res);
        for(com.vjshop.entity.TProductCategory parentCategory : list){
            List<com.vjshop.entity.TProductCategory> subCategorys = this.findProductCategoryAndChildren(parentCategory.getId());
            parentCategory.setChildren(subCategorys);
        }

        return list;
    }

    /**
     * 排序商品分类
     *
     * @param productCategories
     *            商品分类
     */
    private void sort(List<com.vjshop.entity.TProductCategory> productCategories) {
        if (CollectionUtils.isEmpty(productCategories)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
        for (com.vjshop.entity.TProductCategory productCategory : productCategories) {
            orderMap.put(productCategory.getId(), productCategory.getOrders());
        }
        Collections.sort(productCategories, new Comparator<com.vjshop.entity.TProductCategory>() {
            @Override
            public int compare(com.vjshop.entity.TProductCategory productCategory1, com.vjshop.entity.TProductCategory productCategory2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
                Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
                Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
                CompareToBuilder compareToBuilder = new CompareToBuilder();
                while (iterator1.hasNext() && iterator2.hasNext()) {
                    Long id1 = iterator1.next();
                    Long id2 = iterator2.next();
                    Integer order1 = orderMap.get(id1);
                    Integer order2 = orderMap.get(id2);
                    compareToBuilder.append(order1, order2).append(id1, id2);
                    if (!iterator1.hasNext() || !iterator2.hasNext()) {
                        compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }

    public List<com.vjshop.entity.TProductCategory> findRoots(Integer count) {
        SelectQuery<TProductCategoryRecord> query = getDslContext().selectFrom(T_PRODUCT_CATEGORY)
                .where(T_PRODUCT_CATEGORY.PARENT.isNull())
                .orderBy(T_PRODUCT_CATEGORY.ORDERS.asc()).getQuery();
        if (null != count) {
            query.addLimit(count);
        }
        return super.findList(query, null, null, null, null);
    }

    public List<com.vjshop.entity.TProductCategory> findParents(com.vjshop.entity.TProductCategory productCategory, boolean recursive, Integer count) {
        if (productCategory == null || productCategory.getParent() == null) {
            return Collections.emptyList();
        }
        SelectQuery<TProductCategoryRecord> query = getDslContext().selectFrom(T_PRODUCT_CATEGORY).getQuery();
        if (recursive) {
            query.addConditions(T_PRODUCT_CATEGORY.ID.in(Arrays.asList(productCategory.getParentIds())));
            query.addOrderBy(T_PRODUCT_CATEGORY.GRADE.asc());
        } else {
            query.addConditions(T_PRODUCT_CATEGORY.ID.eq(productCategory.getParent()));
        }
        if (null != count) {
            query.addLimit(count);
        }
        return super.findList(query, null, null, null, null);
    }
}
