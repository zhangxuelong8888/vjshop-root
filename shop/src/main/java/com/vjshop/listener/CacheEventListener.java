
package com.vjshop.listener;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TArticleService;
import com.vjshop.service.TGoodsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

/**
 * Listener - 缓存
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Component("cacheEventListener")
public class CacheEventListener implements net.sf.ehcache.event.CacheEventListener {

	@Autowired
	private TArticleService articleService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private EhCacheCacheManager cacheManager;

	/**
	 * 元素回收调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementEvicted(Ehcache ehcache, Element element) {
		updateVersion(ehcache.getName());
	}

	/**
	 * 元素过期调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementExpired(Ehcache ehcache, Element element) {
		String cacheName = ehcache.getName();
		if (StringUtils.equals(cacheName, TArticle.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			TArticle article = articleService.find(id);
			if (article != null && hits != null && hits > 0) {
				article.setHits(hits);
				articleService.update(article);
			}
		} else if (StringUtils.equals(cacheName, TGoods.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			TGoods goods = goodsService.find(id);
			if (goods != null && hits != null && hits > 0) {
				goods.setHits(hits);
				goodsService.update(goods);
			}
		}
		updateVersion(ehcache.getName());
	}

	/**
	 * 元素添加调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
		updateVersion(ehcache.getName());
	}

	/**
	 * 元素移除调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
		updateVersion(ehcache.getName());
	}

	/**
	 * 元素更新调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
		updateVersion(ehcache.getName());
	}

	/**
	 * 删除调用
	 * 
	 * @param ehcache
	 *            缓存
	 */
	public void notifyRemoveAll(Ehcache ehcache) {
		updateVersion(ehcache.getName());
	}

	/**
	 * 释放
	 */
	public void dispose() {
	}

	/**
	 * 克隆
	 * 
	 * @return 对象
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	private void updateVersion(String cacheName){
		Cache cache = cacheManager.getCache("version");
		if( !"version".equals(cacheName)){
			Cache.ValueWrapper value = cache.get(cacheName);
			if(value != null){
				Integer ver = (Integer) value.get();
				cache.put(cacheName,ver+1);
			}else{
				cache.put(cacheName,0);
			}
		}
	}

}