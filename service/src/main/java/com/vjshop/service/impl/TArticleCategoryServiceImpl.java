
package com.vjshop.service.impl;

import com.vjshop.dao.TArticleCategoryDao;
import com.vjshop.entity.TArticleCategory;
import com.vjshop.generated.db.tables.records.TArticleCategoryRecord;
import com.vjshop.service.TArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Service - 文章分类
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TArticleCategoryServiceImpl extends TBaseServiceImpl<TArticleCategoryRecord, TArticleCategory, Long>
		implements TArticleCategoryService {

	@Autowired
	private TArticleCategoryDao tArticleCategoryDao;

	@Transactional(readOnly = true)
	public List<TArticleCategory> findRoots() {
		return tArticleCategoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<TArticleCategory> findRoots(Integer count) {
		return tArticleCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<TArticleCategory> findRoots(Integer count, boolean useCache) {
		return tArticleCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<TArticleCategory> findParents(TArticleCategory articleCategory, boolean recursive, Integer count) {
		return tArticleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "articleCategory", condition = "#useCache")
	public List<TArticleCategory> findParents(Long articleCategoryId, boolean recursive, Integer count, boolean useCache) {
		TArticleCategory articleCategory = tArticleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		return tArticleCategoryDao.findParents(articleCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<TArticleCategory> findTree() {
		return tArticleCategoryDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	public List<TArticleCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count) {
		return tArticleCategoryDao.findChildren(articleCategoryId, recursive, count);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public TArticleCategory save(TArticleCategory articleCategory) {
		Assert.notNull(articleCategory);

		setValue(articleCategory);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		articleCategory.setModifyDate(now);
		if (articleCategory.getId() == null){
			articleCategory.setCreateDate(now);
			articleCategory.setVersion(0L);
		}
		return super.save(articleCategory);
	}

	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public TArticleCategory update(TArticleCategory articleCategory) {
		Assert.notNull(articleCategory);

		setValue(articleCategory);
		List<TArticleCategory> categoryList = tArticleCategoryDao.findChildren(articleCategory.getId(), true, null);
		for (TArticleCategory children : categoryList) {
			setValue(children);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		articleCategory.setModifyDate(now);
		return tArticleCategoryDao.update(articleCategory, null);
	}

	/**
	 * 设置值
	 * 
	 * @param articleCategory
	 *            文章分类
	 */
	private void setValue(TArticleCategory articleCategory) {
		if (articleCategory == null) {
			return;
		}
		TArticleCategory parent = tArticleCategoryDao.find(articleCategory.getParent());
		if (parent != null) {
			articleCategory.setTreePath(parent.getTreePath() + parent.getId() + TArticleCategory.TREE_PATH_SEPARATOR);
		} else {
			articleCategory.setTreePath(TArticleCategory.TREE_PATH_SEPARATOR);
		}
		articleCategory.setGrade(articleCategory.getParentIds().length);
	}

}