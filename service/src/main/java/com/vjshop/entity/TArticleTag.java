
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 文章标签关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TArticleTag implements Serializable {

    private static final long serialVersionUID = -812837136;

    private Long articles;
    private Long tags;

    public TArticleTag() {}

    public TArticleTag(TArticleTag value) {
        this.articles = value.articles;
        this.tags = value.tags;
    }

    public TArticleTag(
            Long articles,
            Long tags
    ) {
        this.articles = articles;
        this.tags = tags;
    }

    public Long getArticles() {
        return this.articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }

    public Long getTags() {
        return this.tags;
    }

    public void setTags(Long tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TArticleTag (");

        sb.append(articles);
        sb.append(", ").append(tags);

        sb.append(")");
        return sb.toString();
    }
}
