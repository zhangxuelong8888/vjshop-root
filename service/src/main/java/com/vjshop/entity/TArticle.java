
package com.vjshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vjshop.Setting;
import com.vjshop.TemplateConfig;
import com.vjshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entity - 文章
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TArticle implements Serializable {

    private static final long serialVersionUID = 1101404497;

    /** 点击数缓存名称 */
    public static final String HITS_CACHE_NAME = "articleHits";

    /** 内容分页长度 */
    private static final int PAGE_CONTENT_LENGTH = 2000;

    /** 内容分页标签 */
    private static final String PAGE_BREAK_TAG = "vjshop_page_break_tag";

    /** 段落配比 */
    private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("[^,;\\.!?，；。！？]*([,;\\.!?，；。！？]+|$)");

    /**
     * 静态生成方式
     */
    public enum GenerateMethod {

        /** 无 */
        none,

        /** 即时 */
        eager,

        /** 延时 */
        lazy
    }

    /** 文章分类 */
    private TArticleCategory category;

    /** 标签 */
    private Set<TTag> tags = new HashSet<TTag>();

    /**
     * 获取路径
     *
     * @return 路径
     */
    @Transient
    public String getPath() {
        return getPath(1);
    }

    /**
     * 获取路径
     *
     * @param pageNumber
     *            页码
     * @return 路径
     */
    @Transient
    public String getPath(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return null;
        }
        TemplateConfig templateConfig = SystemUtils.getTemplateConfig("articleContent");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("article", this);
        model.put("pageNumber", pageNumber);
        return templateConfig.getRealStaticPath(model);
    }

    /**
     * 获取URL
     *
     * @return URL
     */
    @Transient
    public String getUrl() {
        return getUrl(1);
    }

    /**
     * 获取URL
     *
     * @param pageNumber
     *            页码
     * @return URL
     */
    @Transient
    public String getUrl(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return null;
        }
        Setting setting = SystemUtils.getSetting();
        return setting.getSiteUrl() + getPath(pageNumber);
    }

    /**
     * 获取文本内容
     *
     * @return 文本内容
     */
    @Transient
    public String getText() {
        if (StringUtils.isEmpty(getContent())) {
            return StringUtils.EMPTY;
        }
        return StringUtils.remove(Jsoup.parse(getContent()).text(), PAGE_BREAK_TAG);
    }

    /**
     * 获取分页内容
     *
     * @return 分页内容
     */
    @Transient
    public String[] getPageContents() {
        if (StringUtils.isEmpty(getContent())) {
            return new String[] { StringUtils.EMPTY };
        }
        if (StringUtils.contains(getContent(), PAGE_BREAK_TAG)) {
            return StringUtils.splitByWholeSeparator(getContent(), PAGE_BREAK_TAG);
        }
        List<Node> childNodes = Jsoup.parse(getContent()).body().childNodes();
        if (CollectionUtils.isEmpty(childNodes)) {
            return new String[] { getContent() };
        }
        List<String> pageContents = new ArrayList<String>();
        int textLength = 0;
        StringBuilder paragraph = new StringBuilder();
        for (Node node : childNodes) {
            if (node instanceof Element) {
                Element element = (Element) node;
                paragraph.append(element.outerHtml());
                textLength += element.text().length();
                if (textLength >= PAGE_CONTENT_LENGTH) {
                    pageContents.add(paragraph.toString());
                    textLength = 0;
                    paragraph.setLength(0);
                }
            } else if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                Matcher matcher = PARAGRAPH_PATTERN.matcher(textNode.text());
                while (matcher.find()) {
                    String content = matcher.group();
                    paragraph.append(content);
                    textLength += content.length();
                    if (textLength >= PAGE_CONTENT_LENGTH) {
                        pageContents.add(paragraph.toString());
                        textLength = 0;
                        paragraph.setLength(0);
                    }
                }
            }
        }
        String pageContent = paragraph.toString();
        if (StringUtils.isNotEmpty(pageContent)) {
            pageContents.add(pageContent);
        }
        return pageContents.toArray(new String[pageContents.size()]);
    }

    /**
     * 获取分页内容
     *
     * @param pageNumber
     *            页码
     * @return 分页内容
     */
    @Transient
    public String getPageContent(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return null;
        }
        String[] pageContents = getPageContents();
        if (pageContents.length < pageNumber) {
            return null;
        }
        return pageContents[pageNumber - 1];
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    @Transient
    public int getTotalPages() {
        return getPageContents().length;
    }

    /**
     * 获取分类
     *
     * @return 分类
     */
    public TArticleCategory getCategory() {
        return category;
    }
    /**
     * 设置分类
     *
     * @param category
     *          分类
     */
    public void setCategory(TArticleCategory category) {
        this.category = category;
    }

    /**
     * 获取标签
     *
     * @return 文章
     */
    public Set<TTag> getTags() {
        return tags;
    }

    /**
     * 设置标签
     *
     * @param tags
     *          标签
     */
    public void setTags(Set<TTag> tags) {
        if (CollectionUtils.isEmpty(tags)){
            this.tags = null;
            return;
        }
        tags.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                if (o == null) return true;
                TTag that = (TTag) o;
                return null == that.getId();
            }
        });
        this.tags = tags;
    }

    private Long      id;
    @JsonIgnore
    //@javax.persistence.Transient
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private String    author;
    private String    content;
    private Integer   generateMethod;
    private Long      hits;
    private Boolean   isPublication;
    private Boolean   isTop;
    private String    seoDescription;
    private String    seoKeywords;
    private String    seoTitle;
    private String    title;
    private Long      articleCategory;

    public TArticle() {}

    public TArticle(TArticle value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.author = value.author;
        this.content = value.content;
        this.generateMethod = value.generateMethod;
        this.hits = value.hits;
        this.isPublication = value.isPublication;
        this.isTop = value.isTop;
        this.seoDescription = value.seoDescription;
        this.seoKeywords = value.seoKeywords;
        this.seoTitle = value.seoTitle;
        this.title = value.title;
        this.articleCategory = value.articleCategory;
    }

    public TArticle(
            Long id,
            Timestamp createDate,
            Timestamp modifyDate,
            Long version,
            String author,
            String content,
            Integer generateMethod,
            Long hits,
            Boolean isPublication,
            Boolean isTop,
            String seoDescription,
            String seoKeywords,
            String seoTitle,
            String title,
            Long articleCategory
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.author = author;
        this.content = content;
        this.generateMethod = generateMethod;
        this.hits = hits;
        this.isPublication = isPublication;
        this.isTop = isTop;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.seoTitle = seoTitle;
        this.title = title;
        this.articleCategory = articleCategory;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @JsonIgnore
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }


    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Length(max = 200)
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGenerateMethod() {
        return this.generateMethod;
    }

    public void setGenerateMethod(Integer generateMethod) {
        this.generateMethod = generateMethod;
    }

    public Long getHits() {
        return this.hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    @NotNull
    public Boolean getIsPublication() {
        return this.isPublication;
    }

    public void setIsPublication(Boolean isPublication) {
        this.isPublication = isPublication;
    }

    @NotNull
    public Boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    @Length(max = 200)
    public String getSeoDescription() {
        return this.seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    @Length(max = 200)
    public String getSeoKeywords() {
        return this.seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        if (seoKeywords != null) {
            seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
        }
        this.seoKeywords = seoKeywords;
    }

    @Length(max = 200)
    public String getSeoTitle() {
        return this.seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    @NotEmpty
    @Length(max = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public Long getArticleCategory() {
        return this.articleCategory;
    }

    public void setArticleCategory(Long articleCategory) {
        this.articleCategory = articleCategory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TArticle (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(author);
        sb.append(", ").append(content);
        sb.append(", ").append(generateMethod);
        sb.append(", ").append(hits);
        sb.append(", ").append(isPublication);
        sb.append(", ").append(isTop);
        sb.append(", ").append(seoDescription);
        sb.append(", ").append(seoKeywords);
        sb.append(", ").append(seoTitle);
        sb.append(", ").append(title);
        sb.append(", ").append(articleCategory);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TArticle that = (TArticle) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
