
package com.vjshop.dao;

import com.vjshop.generated.db.tables.TArticleTag;
import com.vjshop.generated.db.tables.records.TArticleTagRecord;
import org.jooq.Configuration;
import org.jooq.Record2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 属性
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TArticleTagDao extends JooqBaseDao<TArticleTagRecord, com.vjshop.entity.TArticleTag, Record2<Long, Long>> {

    public TArticleTagDao() {
        super(TArticleTag.T_ARTICLE_TAG, com.vjshop.entity.TArticleTag.class);
    }

    @Autowired
    public TArticleTagDao(Configuration configuration) {
        super(TArticleTag.T_ARTICLE_TAG, com.vjshop.entity.TArticleTag.class, configuration);
    }

    @Override
    protected Record2<Long, Long> getId(com.vjshop.entity.TArticleTag object) {
        return compositeKeyRecord(object.getArticles(), object.getTags());
    }

    public List<com.vjshop.entity.TArticleTag> findByArticles(Long... values) {
        return fetch(TArticleTag.T_ARTICLE_TAG.ARTICLES, values);
    }

    public List<com.vjshop.entity.TArticleTag> findByTags(Long... values) {
        return fetch(TArticleTag.T_ARTICLE_TAG.TAGS, values);
    }

    /**
     * 删除关联
     *
     * @param articleId
     *          文章ID
     */
    public int deleteByArticles(Long articleId){
        return getDslContext().deleteFrom(getTable()).where(TArticleTag.T_ARTICLE_TAG.ARTICLES.eq(articleId)).execute();
    }

    /**
     * 删除关联
     *
     * @param tagId
     *          标签ID
     */
    public int deleteByTags(Long tagId){
        return getDslContext().deleteFrom(getTable()).where(TArticleTag.T_ARTICLE_TAG.TAGS.eq(tagId)).execute();
    }

    /**
     * 删除关联
     *
     * @param articleIds
     *          文章ID
     */
    public int deleteByArticles(Long... articleIds){
        return getDslContext().deleteFrom(getTable()).where(TArticleTag.T_ARTICLE_TAG.ARTICLES.in(articleIds)).execute();
    }

    /**
     * 删除关联
     *
     * @param tagIds
     *          标签ID
     */
    public int deleteByTags(Long... tagIds){
        return getDslContext().deleteFrom(getTable()).where(TArticleTag.T_ARTICLE_TAG.TAGS.in(tagIds)).execute();
    }
}
