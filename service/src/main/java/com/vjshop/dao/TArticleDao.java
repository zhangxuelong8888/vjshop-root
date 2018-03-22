
package com.vjshop.dao;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.TArticleCategory;
import com.vjshop.generated.db.tables.TArticle;
import com.vjshop.generated.db.tables.TArticleTag;
import com.vjshop.generated.db.tables.TTag;
import com.vjshop.generated.db.tables.records.TArticleRecord;
import com.vjshop.util.JooqUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import static com.vjshop.generated.db.tables.TArticleCategory.T_ARTICLE_CATEGORY;

/**
 * Dao - 文章
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TArticleDao extends JooqBaseDao<TArticleRecord, com.vjshop.entity.TArticle, Long> {

    public TArticleDao() {
        super(TArticle.T_ARTICLE, com.vjshop.entity.TArticle.class);
    }

    @Autowired
    public TArticleDao(Configuration configuration) {
        super(TArticle.T_ARTICLE, com.vjshop.entity.TArticle.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TArticle object) {
        return object.getId();
    }

    /**
     * 查找文章详细信息
     *
     * @return 文章详细信息
     */
    public com.vjshop.entity.TArticle findDetails(Long articleId) {
        SelectQuery query = getDslContext().select(TArticle.T_ARTICLE.fields()).from(TArticle.T_ARTICLE).getQuery();
        setQueryWithJoin(query);
        query.addConditions(TArticle.T_ARTICLE.ID.eq(articleId));
        ResultSet rs = query.fetchResultSet();
        List<com.vjshop.entity.TArticle> articleList = resultSet2List(rs);
        if (CollectionUtils.isNotEmpty(articleList)){
            return articleList.get(0);
        }
        return null;
    }

    /**
     * 查找文章
     *
     * @param join
     *            是否关联信息
     * @param articleCategoryId
     *            文章分类ID
     * @param tagId
     *            标签ID
     * @param isPublication
     *            是否发布
     * @param count
     *            数量
     * @param filters
     *            筛选
     * @param orders
     *            排序
     * @return 文章
     */
    public List<com.vjshop.entity.TArticle> findList(boolean join, Long articleCategoryId, Long tagId,
                                              Boolean isPublication, Integer count,
                                              List<Filter> filters, List<Order> orders){
        SelectQuery query = setQueryParam(join, articleCategoryId, tagId, isPublication, null, null, null);
        return super.findList(query, null, count, filters, orders);
    }

    /**
     * 查找文章
     *
     * @param join
     *            是否关联信息
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
     * @param first
     *            起始记录
     * @param count
     *            数量
     * @return 文章
     */
    public List<com.vjshop.entity.TArticle> findList(boolean join, Long articleCategoryId, Boolean isPublication,
                                              com.vjshop.entity.TArticle.GenerateMethod generateMethod,
                                              Timestamp beginDate, Timestamp endDate, Integer first, Integer count){
        SelectQuery query = setQueryParam(join, articleCategoryId, null, isPublication, generateMethod, beginDate, endDate);
        return super.findList(query, first, count, null, null);
    }

    /**
     * 查找文章分页
     *
     * @param join
     *            是否关联信息
     * @param articleCategoryId
     *            文章分类
     * @param tagId
     *            标签ID
     * @param isPublication
     *            是否发布
     * @param pageable
     *            分页信息
     * @return 文章分页
     */
    public Page<com.vjshop.entity.TArticle> findPage(boolean join, Long articleCategoryId, Long tagId,
                                              Boolean isPublication, Pageable pageable){
        SelectQuery query = setQueryParam(join, articleCategoryId, tagId, isPublication, null, null, null);
        if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null)
                && CollectionUtils.isEmpty(pageable.getOrders()))) {
            query.addOrderBy(TArticle.T_ARTICLE.IS_TOP.desc());
            query.addOrderBy(TArticle.T_ARTICLE.CREATE_DATE.desc());
        }
        return findPage(query, pageable);
    }
    /**
     * 设置相关查询条件
     *
     * @param join
     *            是否关联信息
     * @param articleCategoryId
     *            文章分类ID
     * @param tagId
     *            标签ID
     * @param isPublication
     *            是否发布
     * @param generateMethod
     *            静态生成方式
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return 查询条件
     */
    private SelectQuery setQueryParam(boolean join, Long articleCategoryId, Long tagId,
                                      Boolean isPublication, com.vjshop.entity.TArticle.GenerateMethod generateMethod,
                                      Timestamp beginDate, Timestamp endDate){
        SelectQuery query = getDslContext().select(TArticle.T_ARTICLE.fields()).from(TArticle.T_ARTICLE).getQuery();
        if (join){
            setQueryWithJoin(query);
        }
        if (articleCategoryId != null){
            String treePathLike = "%" + TArticleCategory.TREE_PATH_SEPARATOR + articleCategoryId + TArticleCategory.TREE_PATH_SEPARATOR + "%";
            SelectQuery subQuery = this.getDslContext().select(T_ARTICLE_CATEGORY.ID)
                    .from(T_ARTICLE_CATEGORY)
                    .where(T_ARTICLE_CATEGORY.ID.eq(articleCategoryId)
                            .or(T_ARTICLE_CATEGORY.TREE_PATH.like(treePathLike)))
                    .getQuery();
            query.addConditions(TArticle.T_ARTICLE.ARTICLE_CATEGORY.in(subQuery));
        }
        if (tagId != null){
            if (!join){
                query.addJoin(TArticleTag.T_ARTICLE_TAG, JoinType.LEFT_OUTER_JOIN, TArticleTag.T_ARTICLE_TAG.ARTICLES.eq(TArticle.T_ARTICLE.ID));
            }
            query.addConditions(TArticleTag.T_ARTICLE_TAG.TAGS.eq(tagId));
        }
        if (isPublication != null){
            query.addConditions(TArticle.T_ARTICLE.IS_PUBLICATION.eq(isPublication));
        }
        if (generateMethod != null){
            query.addConditions(TArticle.T_ARTICLE.GENERATE_METHOD.eq(generateMethod.ordinal()));
        }
        if (beginDate != null){
            query.addConditions(TArticle.T_ARTICLE.CREATE_DATE.ge(beginDate));
        }
        if (endDate != null){
            query.addConditions(TArticle.T_ARTICLE.CREATE_DATE.lt(endDate));
        }
        return query;
    }

    /**
     * 设置关联查询查询条件
     *
     * @param query 查询条件
     */
    public void setQueryWithJoin(SelectQuery query){
        query.addSelect(JooqUtils.getFields(T_ARTICLE_CATEGORY, "category"));
        query.addSelect(JooqUtils.getFields(TTag.T_TAG, "tags"));

        query.addJoin(T_ARTICLE_CATEGORY, JoinType.LEFT_OUTER_JOIN, T_ARTICLE_CATEGORY.ID.eq(TArticle.T_ARTICLE.ARTICLE_CATEGORY));
        query.addJoin(TArticleTag.T_ARTICLE_TAG, JoinType.LEFT_OUTER_JOIN, TArticleTag.T_ARTICLE_TAG.ARTICLES.eq(TArticle.T_ARTICLE.ID));
        query.addJoin(TTag.T_TAG, JoinType.LEFT_OUTER_JOIN, TTag.T_TAG.ID.eq(TArticleTag.T_ARTICLE_TAG.TAGS));
    }

    public List<com.vjshop.entity.TArticle> fetchByArticleCategory(Long articleCategoryId) {
        return fetch(TArticle.T_ARTICLE.ARTICLE_CATEGORY, articleCategoryId);
    }
}
