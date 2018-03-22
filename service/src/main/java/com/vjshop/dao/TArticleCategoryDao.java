
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TArticleCategory;
import com.vjshop.generated.db.tables.records.TArticleCategoryRecord;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.jooq.Configuration;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;

/**
 * Dao - 文章分类
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TArticleCategoryDao extends JooqBaseDao<TArticleCategoryRecord, com.vjshop.entity.TArticleCategory, Long> {

    public TArticleCategoryDao() {
        super(TArticleCategory.T_ARTICLE_CATEGORY, com.vjshop.entity.TArticleCategory.class);
    }

    @Autowired
    public TArticleCategoryDao(Configuration configuration) {
        super(TArticleCategory.T_ARTICLE_CATEGORY, com.vjshop.entity.TArticleCategory.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TArticleCategory object) {
        return object.getId();
    }

    /**
     * 查找顶级文章分类
     *
     * @param count
     *            数量
     * @return 顶级文章分类
     */
    public List<com.vjshop.entity.TArticleCategory> findRoots(Integer count){
        SelectQuery query = getDslContext().selectFrom(TArticleCategory.T_ARTICLE_CATEGORY)
                .where(TArticleCategory.T_ARTICLE_CATEGORY.PARENT.isNull())
                .orderBy(TArticleCategory.T_ARTICLE_CATEGORY.ORDERS.asc()).getQuery();
        if (count != null){
            query.addLimit(count.intValue());
        }
        ResultSet rs = query.fetchResultSet();
        return resultSet2List(rs);
    }

    /**
     * 查找上级文章分类
     *
     * @param articleCategory
     *            文章分类
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 上级文章分类
     */
    public List<com.vjshop.entity.TArticleCategory> findParents(com.vjshop.entity.TArticleCategory articleCategory, boolean recursive, Integer count){
        if (articleCategory == null || articleCategory.getParent() == null) {
            return Collections.emptyList();
        }
        SelectQuery query = getQuery();
        if (recursive){
            query.addConditions(TArticleCategory.T_ARTICLE_CATEGORY.ID.in(articleCategory.getParentIds()));
            query.addOrderBy(TArticleCategory.T_ARTICLE_CATEGORY.GRADE.asc());
        } else {
            query.addConditions(TArticleCategory.T_ARTICLE_CATEGORY.ID.eq(articleCategory.getParent()));
        }
        if (count != null){
            query.addLimit(count.intValue());
        }
        ResultSet rs = query.fetchResultSet();
        return resultSet2List(rs);
    }

    /**
     * 查找下级文章分类
     *
     * @param articleCategoryId
     *            文章分类ID
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 下级文章分类
     */
    public List<com.vjshop.entity.TArticleCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count){
        SelectQuery query = getQuery();
        if (recursive) {
            if (articleCategoryId != null) {
                String treePathLike = "%" + com.vjshop.entity.TArticleCategory.TREE_PATH_SEPARATOR + articleCategoryId + com.vjshop.entity.TArticleCategory.TREE_PATH_SEPARATOR + "%";
                query.addConditions(TArticleCategory.T_ARTICLE_CATEGORY.TREE_PATH.like(treePathLike));
            }
            query.addOrderBy(TArticleCategory.T_ARTICLE_CATEGORY.GRADE.asc());
            query.addOrderBy(TArticleCategory.T_ARTICLE_CATEGORY.ORDERS.asc());
            if (count != null){
                query.addLimit(count.intValue());
            }
            ResultSet rs = query.fetchResultSet();
            List<com.vjshop.entity.TArticleCategory> categoryList = resultSet2List(rs);
            sort(categoryList);
            return categoryList;
        } else {
            query.addConditions(TArticleCategory.T_ARTICLE_CATEGORY.PARENT.eq(articleCategoryId));
            query.addOrderBy(TArticleCategory.T_ARTICLE_CATEGORY.ORDERS.asc());
            if (count != null){
                query.addLimit(count.intValue());
            }
            ResultSet rs = query.fetchResultSet();
            return resultSet2List(rs);
        }
    }

    /**
     * 排序文章分类
     *
     * @param articleCategories
     *            文章分类
     */
    private void sort(List<com.vjshop.entity.TArticleCategory> articleCategories) {
        if (CollectionUtils.isEmpty(articleCategories)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
        for (com.vjshop.entity.TArticleCategory articleCategory : articleCategories) {
            orderMap.put(articleCategory.getId(), articleCategory.getOrders());
        }
        Collections.sort(articleCategories, new Comparator<com.vjshop.entity.TArticleCategory>() {
            @Override
            public int compare(com.vjshop.entity.TArticleCategory articleCategory1, com.vjshop.entity.TArticleCategory articleCategory2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(articleCategory1.getParentIds(), articleCategory1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(articleCategory2.getParentIds(), articleCategory2.getId());
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
                        compareToBuilder.append(articleCategory1.getGrade(), articleCategory2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }

}
