
package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Order;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TArticleCategoryDao;
import com.vjshop.dao.TArticleDao;
import com.vjshop.dao.TArticleTagDao;
import com.vjshop.dao.TTagDao;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TArticleTag;
import com.vjshop.entity.TTag;
import com.vjshop.generated.db.tables.records.TArticleRecord;
import com.vjshop.service.StaticService;
import com.vjshop.service.TArticleService;
import com.vjshop.service.TSearchService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service - 文章
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TArticleServiceImpl extends TBaseServiceImpl<TArticleRecord, TArticle, Long>
		implements TArticleService {

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;
	@Autowired
	private TArticleDao tArticleDao;
	@Autowired
	private TArticleCategoryDao tArticleCategoryDao;
	@Autowired
	private TTagDao tTagDao;
	@Autowired
	private TArticleTagDao tArticleTagDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Autowired
	private TSearchService tSearchService;

	@Transactional(readOnly = true)
	public TArticle findDetails(Long articleId) {
		return tArticleDao.findDetails(articleId);
	}

	@Transactional(readOnly = true)
	public List<TArticle> findList(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders) {
		return tArticleDao.findList(join, articleCategoryId, tagId, isPublication, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "article", condition = "#useCache")
	public List<TArticle> findList(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		if (articleCategoryId != null && !tArticleCategoryDao.existsById(articleCategoryId)) {
			return Collections.emptyList();
		}
		if (tagId != null && !tTagDao.existsById(tagId)) {
			return Collections.emptyList();
		}
		return tArticleDao.findList(join, articleCategoryId, tagId, isPublication, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<TArticle> findList(boolean join, Long articleCategoryId, Boolean isPublication,
								   TArticle.GenerateMethod generateMethod, Timestamp beginDate,
								   Timestamp endDate, Integer first, Integer count) {
		return tArticleDao.findList(join, articleCategoryId, isPublication, generateMethod, beginDate, endDate, first, count);
	}

	@Transactional(readOnly = true)
	public Page<TArticle> findPage(boolean join, Long articleCategoryId, Long tagId, Boolean isPublication, Pageable pageable) {
		return tArticleDao.findPage(join, articleCategoryId, tagId, isPublication, pageable);
	}

	public long viewHits(Long id) {
		Assert.notNull(id);

		Ehcache cache = cacheManager.getEhcache(TArticle.HITS_CACHE_NAME);
		Element element = cache.get(id);
		Long hits;
		if (element != null) {
			hits = (Long) element.getObjectValue() + 1;
		} else {
			TArticle article = tArticleDao.find(id);
			if (article == null) {
				return 0L;
			}
			hits = article.getHits() + 1;
		}
		cache.put(new Element(id, hits));
		return hits;
	}

    @Override
    public List<TArticle> findByArticleCategory(Long articleCategoryId) {
        return tArticleDao.fetchByArticleCategory(articleCategoryId);
    }

    @Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public TArticle save(TArticle article) {
		Assert.notNull(article);
		Set<TTag> tags =  article.getTags();
		article.setGenerateMethod(TArticle.GenerateMethod.eager.ordinal());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		article.setModifyDate(now);
		if (article.getId() == null){
			article.setCreateDate(now);
			article.setVersion(0L);
		}
		article = super.save(article);
		tSearchService.index(article, true);
		updateArticleTags(article.getId(), tags);
		return article;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public TArticle update(TArticle article) {
		Assert.notNull(article);
		Assert.isTrue(article.getId() != null);

		Set<TTag> tags =  article.getTags();
		article.setGenerateMethod(TArticle.GenerateMethod.eager.ordinal());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		article.setModifyDate(now);
		article = tArticleDao.update(article, null);
		updateArticleTags(article.getId(), tags);
		return article;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public TArticle update(TArticle article, String... ignoreProperties){
		Assert.notNull(article);
		Assert.isTrue(article.getId() != null);

		Set<TTag> tags =  article.getTags();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		article.setModifyDate(now);
		article = tArticleDao.update(article, ignoreProperties);
		tSearchService.index(article, false);
		updateArticleTags(article.getId(), tags);
		return article;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long[] articleIds) {
		for (Long articleId : articleIds) {
			TArticle article = tArticleDao.find(articleId);
			staticService.delete(article);
			tArticleDao.deleteById(articleId);
			tSearchService.purge(TArticle.class, String.valueOf(articleId));
		}
	}

	private void updateArticleTags(Long articleId, Set<TTag> tags){
		if (articleId == null){
			return;
		}
		tArticleTagDao.deleteByArticles(articleId);
		if (CollectionUtils.isNotEmpty(tags)){
			List<TArticleTag> articleTagList = new ArrayList<TArticleTag>();
			for (TTag tag : tags){
				TArticleTag articleTag = new TArticleTag(articleId, tag.getId());
				articleTagList.add(articleTag);
			}
			tArticleTagDao.insert(articleTagList);
		}
	}

}