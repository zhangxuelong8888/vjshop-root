package com.vjshop.service.impl;

import com.vjshop.IndexQueryPage;
import com.vjshop.LuceneManager;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.dao.TArticleDao;
import com.vjshop.dao.TGoodsDao;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;
import com.vjshop.service.TSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service - 搜索
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Transactional
@Service
public class TSearchServiceImpl implements TSearchService {

	/** 模糊查询最小相似度 4.7.2中此数为int，取值为0,1,2 范围由小到大*/
//	private static final float FUZZY_QUERY_MINIMUM_SIMILARITY = 0.5F;
	private static final int FUZZY_QUERY_MINIMUM_SIMILARITY = 1;

	@Autowired
	private TArticleDao articleDao;
	@Autowired
	private TGoodsDao goodsDao;
	@Autowired
	private LuceneManager luceneManager;

	public void index() {
		index(TArticle.class);
		index(TGoods.class);
	}

	public void index(Class<?> type, Boolean isCreate) {
		if (type == TArticle.class) {
			for (int i = 0; i < articleDao.count(); i += 20) {
				List<TArticle> articles = articleDao.findList(null, i, 20, null, null);
				for (TArticle article : articles) {
					luceneManager.index(article, isCreate);
				}
			}
		} else if (type == TGoods.class) {
			for (int i = 0; i < goodsDao.count(); i += 20) {
				List<TGoods> goodsList = goodsDao.findList(null, i, 20, null, null);
				for (TGoods goods : goodsList) {
					luceneManager.index(goods, isCreate);
				}
			}
		}
		luceneManager.close();
	}

	public void index(Class<?> type) {
		index(type, true);
	}

	public void index(TArticle article, Boolean isCreate) {
		if (article != null) {
			luceneManager.index(article, isCreate);
			luceneManager.close();
		}
	}

	public void index(TGoods goods, Boolean isCreate) {
		if (goods != null) {
			luceneManager.index(goods, isCreate);
			luceneManager.close();
		}
	}

	public void purge() {
		purge(TArticle.class);
		purge(TGoods.class);
	}

	public void purge(Class<?> type) {
		if (type == TArticle.class) {
			luceneManager.delete(TArticle.class);
		} else if (type == TGoods.class) {
			luceneManager.delete(TGoods.class);
		}
		luceneManager.close();
	}

	public void purge(Class<?> type, String id) {
		luceneManager.delete(type, id);
		luceneManager.close();
	}

	public void purge(TArticle article) {
		if (article != null) {
			luceneManager.delete(TArticle.class, String.valueOf(article.getId()));
			luceneManager.close();
		}
	}

	public void purge(TGoods goods) {
		if (goods != null) {
			luceneManager.delete(TGoods.class, String.valueOf(goods.getId()));
			luceneManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<TArticle> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<TArticle>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}
		try {
			String text = QueryParser.escape(keyword);
			QueryParser titleParser = new QueryParser(Version.LUCENE_47, "title", new IKAnalyzer());
			titleParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query titleQuery = titleParser.parse(text);
			FuzzyQuery titleFuzzyQuery = new FuzzyQuery(new Term("title", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
			Query contentQuery = new TermQuery(new Term("content", text));
			Query isPublicationQuery = new TermQuery(new Term("isPublication", "true"));
			BooleanQuery textQuery = new BooleanQuery();
			BooleanQuery query = new BooleanQuery();
			textQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(titleFuzzyQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(contentQuery, BooleanClause.Occur.SHOULD);
			query.add(isPublicationQuery, BooleanClause.Occur.MUST);
			query.add(textQuery, BooleanClause.Occur.MUST);

			Sort sort = new Sort(new SortField[] { new SortField("isTop", SortField.Type.STRING, true), new SortField(null, SortField.Type.SCORE), new SortField("createDate", SortField.Type.LONG, true) });
			IndexQueryPage queryPage = luceneManager.queryPage(query, sort, pageable.getPageNumber(), pageable.getPageSize(), TArticle.class);

			return new Page<TArticle>(queryPage.getResultList(), queryPage.getResultSize(), pageable);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<TGoods> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, TGoods.OrderType orderType, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<TGoods>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}
		try {
			String text = QueryParser.escape(keyword);
			TermQuery snQuery = new TermQuery(new Term("sn", text));
			Query keywordQuery = new QueryParser(Version.LUCENE_47, "keyword", new IKAnalyzer()).parse(text);
			QueryParser nameParser = new QueryParser(Version.LUCENE_47, "name", new IKAnalyzer());
			nameParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query nameQuery = nameParser.parse(text);
			FuzzyQuery nameFuzzyQuery = new FuzzyQuery(new Term("name", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
			TermQuery introductionQuery = new TermQuery(new Term("introduction", text));
			TermQuery isMarketableQuery = new TermQuery(new Term("isMarketable", "true"));
			TermQuery isListQuery = new TermQuery(new Term("isList", "true"));
			BooleanQuery textQuery = new BooleanQuery();
			BooleanQuery query = new BooleanQuery();
			textQuery.add(snQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(keywordQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(nameQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(nameFuzzyQuery, BooleanClause.Occur.SHOULD);
			textQuery.add(introductionQuery, BooleanClause.Occur.SHOULD);
			query.add(isMarketableQuery, BooleanClause.Occur.MUST);
			query.add(isListQuery, BooleanClause.Occur.MUST);
			query.add(textQuery, BooleanClause.Occur.MUST);
			if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
				BigDecimal temp = startPrice;
				startPrice = endPrice;
				endPrice = temp;
			}
			if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0 && endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), endPrice.doubleValue(), true, true);
				query.add(numericRangeQuery, BooleanClause.Occur.MUST);
			} else if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), null, true, false);
				query.add(numericRangeQuery, BooleanClause.Occur.MUST);
			} else if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", null, endPrice.doubleValue(), false, true);
				query.add(numericRangeQuery, BooleanClause.Occur.MUST);
			}

			SortField[] sortFields = null;
			if (orderType != null) {
				switch (orderType) {
				case topDesc:
					sortFields = new SortField[] { new SortField("isTop", SortField.Type.STRING, true), new SortField(null, SortField.Type.SCORE), new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case priceAsc:
					sortFields = new SortField[] { new SortField("price", SortField.Type.DOUBLE, false), new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case priceDesc:
					sortFields = new SortField[] { new SortField("price", SortField.Type.DOUBLE, true), new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case salesDesc:
					sortFields = new SortField[] { new SortField("sales", SortField.Type.LONG, true), new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case scoreDesc:
					sortFields = new SortField[] { new SortField("score", SortField.Type.DOUBLE, true), new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case dateDesc:
					sortFields = new SortField[] { new SortField("createDate", SortField.Type.LONG, true) };
					break;
				case dateAsc:
					sortFields = new SortField[] { new SortField("createDate", SortField.Type.LONG, false) };
					break;
				}
			} else {
				sortFields = new SortField[] { new SortField("isTop", SortField.Type.STRING, true), new SortField(null, SortField.Type.SCORE), new SortField("createDate", SortField.Type.LONG, true) };
			}

			Sort sort = new Sort(sortFields);
			IndexQueryPage queryPage = luceneManager.queryPage(query, sort, pageable.getPageNumber(), pageable.getPageSize(), TGoods.class);
			return new Page<TGoods>(queryPage.getResultList(), queryPage.getResultSize(), pageable);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}