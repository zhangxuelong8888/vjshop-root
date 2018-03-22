package com.vjshop;

import com.vjshop.dao.TProductDao;
import com.vjshop.entity.TArticle;
import com.vjshop.entity.TGoods;
import com.vjshop.exception.LucenceIndexException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ck
 * @date 2017/6/12 0012
 */
@Component("luceneManager")
public class LuceneManager {

    @Value("${lucene.disk.directory}")
    private String directory;

    private Analyzer analyzer = new IKAnalyzer();

    @Autowired
    private ServletContext servletContext;

    private IndexWriter indexWriter;

    @Autowired
    private TProductDao tProductDao;

    private synchronized IndexWriter getIndexWriter(Class clazz) throws IOException{
        String dir = getDirectoryPath(clazz);
        if (indexWriter == null) {
            FSDirectory fsDirectory = FSDirectory.open(new File(dir));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        }
        return indexWriter;
    }

    public void close() {
        if (indexWriter != null) {
            try {
                indexWriter.close();
                indexWriter = null;
            } catch (IOException e) {
                throw new LucenceIndexException("关闭索引出错",e);
            }
        }
    }

    public void index(TGoods tGoods, Boolean isCreate) {
        try {
            IndexWriter writer = getIndexWriter(tGoods.getClass());
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(tGoods.getId()), Field.Store.YES));
            doc.add(new StringField("sn", tGoods.getSn(), Field.Store.YES));
            doc.add(new TextField("name", tGoods.getName(), Field.Store.YES));
            doc.add(new StringField("caption", StringUtils.defaultIfEmpty(tGoods.getCaption(), ""), Field.Store.YES));
            doc.add(new StringField("type", String.valueOf(tGoods.getType()), Field.Store.YES));
            doc.add(new StringField("image", StringUtils.defaultIfEmpty(tGoods.getImage(), ""), Field.Store.YES));
            doc.add(new StringField("unit", StringUtils.defaultIfEmpty(tGoods.getUnit(), ""), Field.Store.YES));
            doc.add(new StringField("weight", tGoods.getWeight() == null ? "" : tGoods.getWeight().toString(), Field.Store.YES));
            doc.add(new StringField("isMarketable", String.valueOf(tGoods.getIsMarketable()), Field.Store.YES));
            doc.add(new StringField("isList", String.valueOf(tGoods.getIsList()), Field.Store.YES));
            doc.add(new StringField("isTop", String.valueOf(tGoods.getIsTop()), Field.Store.YES));
            doc.add(new TextField("introduction", StringUtils.defaultIfEmpty(tGoods.getIntroduction(), ""), Field.Store.YES));
            doc.add(new TextField("keyword", StringUtils.defaultIfEmpty(tGoods.getKeyword(), ""), Field.Store.YES));
            doc.add(new StringField("productImages", StringUtils.defaultIfEmpty(tGoods.getProductImages(), ""), Field.Store.YES));

            DoubleField priceField = new DoubleField("price", convert2Double(tGoods.getPrice()) ,Field.Store.YES);
            DoubleField marketPriceField = new DoubleField("marketPrice",convert2Double(tGoods.getMarketPrice()), Field.Store.YES);
            DoubleField scoreField = new DoubleField("score",tGoods.getScore(), Field.Store.YES);
            LongField scoreCountField = new LongField("scoreCount",tGoods.getScoreCount(), Field.Store.YES);
            LongField weekSalesField = new LongField("weekSales",tGoods.getWeekSales(), Field.Store.YES);
            LongField monthSalesField = new LongField("monthSales",tGoods.getMonthSales(), Field.Store.YES);
            LongField salesField = new LongField("sales",tGoods.getSales(), Field.Store.YES);
            LongField createDateField = new LongField("createDate", tGoods.getCreateDate().getTime(), Field.Store.YES);
            LongField modifyDateField = new LongField("modifyDate", tGoods.getModifyDate().getTime(), Field.Store.YES);

            doc.add(priceField);
            doc.add(marketPriceField);
            doc.add(scoreField);
            doc.add(scoreCountField);
            doc.add(weekSalesField);
            doc.add(monthSalesField);
            doc.add(salesField);
            doc.add(createDateField);
            doc.add(modifyDateField);

            if (isCreate) {
                writer.addDocument(doc);
            } else {
                writer.updateDocument(new Term("id", String.valueOf(tGoods.getId())), doc);
            }
            writer.commit();
        } catch (IOException e) {
            throw new LucenceIndexException(e);
        }
    }

    public void index(TArticle tArticle, Boolean isCreate) {
        try {
            IndexWriter writer = getIndexWriter(tArticle.getClass());
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(tArticle.getId()), Field.Store.YES));
            doc.add(new TextField("title", tArticle.getTitle(), Field.Store.YES));
            doc.add(new StringField("author", StringUtils.defaultIfEmpty(tArticle.getAuthor(), ""), Field.Store.YES));
            doc.add(new TextField("content", StringUtils.defaultIfEmpty(tArticle.getContent(),""), Field.Store.YES));
            doc.add(new StringField("isPublication", String.valueOf(tArticle.getIsPublication()), Field.Store.YES));
            doc.add(new StringField("isTop", String.valueOf(tArticle.getIsTop()), Field.Store.YES));
            doc.add(new LongField("createDate", tArticle.getCreateDate().getTime(), Field.Store.YES));
            doc.add(new LongField("modifyDate", tArticle.getModifyDate().getTime(), Field.Store.YES));
            if (isCreate) {
                writer.addDocument(doc);
            } else {
                writer.updateDocument(new Term("id", String.valueOf(tArticle.getId())), doc);
            }
            writer.commit();
        } catch (IOException e) {
            throw new LucenceIndexException(e);
        }
    }

    private Double convert2Double(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return Double.NaN;
        }
        return Double.parseDouble(bigDecimal.toString());
    }

    public void delete(Class<?> type) {
        try {
            getIndexWriter(type).deleteAll();
        } catch (IOException e) {
            throw new LucenceIndexException("删除索引出错",e);
        }
    }

    public void delete(Class<?> type, String id) {
        try {
            getIndexWriter(type).deleteDocuments(new Term("id", id));
        } catch (IOException e) {
            throw new LucenceIndexException("删除索引出错",e);
        }
    }

    public IndexSearcher getSearch(Class<?> type) throws IOException{
        String dir = getDirectoryPath(type);
        IndexSearcher searcher = null;
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File(dir)));
        searcher = new IndexSearcher(indexReader);
        return searcher;
    }

    private String getDirectoryPath(Class<?> type) {
        return servletContext.getRealPath("/") + directory + "/" + type.getName();
    }

    /**
     * 分页查询
     * @param query 查询对象
     * @param sort 排序对象
     * @param pageNum 当前页 
     * @param pageSize 每页记录数
     * @param type 实体类
     * @return 分页对象
     */
    public IndexQueryPage queryPage(Query query, Sort sort, int pageNum, int pageSize, Class<?> type) {
        IndexQueryPage page = new IndexQueryPage();
        try {
            IndexSearcher indexSearcher = getSearch(type);
            TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE, sort);
            //总记录数
            if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
                page.setResultSize(0);
            } else {
                page.setResultSize(topDocs.totalHits);
            }

            int start = (pageNum - 1) * pageSize;
            int end = pageNum * pageSize;
            end = Math.min(page.getResultSize(), end);

            //转换成entity
            page.setResultList(transformer(topDocs, start, end, indexSearcher, type));
//            indexSearcher.close();
        } catch (IOException e) {
            throw new LucenceIndexException("查询索引出错",e);
        }
        return page;
    }

    private List transformer(TopDocs topDocs, int start, int end, IndexSearcher indexSearcher, Class<?> type) throws IOException {
        List list = new ArrayList(topDocs.totalHits);
        ScoreDoc[] docs = topDocs.scoreDocs;
        if (type == TArticle.class) {
            for (int i = start; i < end; i++) {
                TArticle article = new TArticle();
                Document doc = indexSearcher.doc(docs[i].doc);
                article.setId(Long.valueOf(doc.get("id")));
                article.setTitle(doc.get("title"));
                article.setAuthor(doc.get("author"));
                article.setContent(doc.get("content"));
                article.setIsPublication(Boolean.valueOf(doc.get("isPublication")));
                article.setIsTop(Boolean.valueOf(doc.get("isTop")));
                article.setCreateDate(new Timestamp(Long.parseLong(doc.get("createDate"))));
                article.setModifyDate(new Timestamp(Long.parseLong(doc.get("modifyDate"))));
                list.add(article);
            }
        } else if (type == TGoods.class) {
            for (int i = start; i < end; i++) {
                TGoods goods = new TGoods();
                Document doc = indexSearcher.doc(docs[i].doc);
                goods.setId(Long.valueOf(doc.get("id")));
                goods.setSn(doc.get("sn"));
                goods.setName(doc.get("name"));
                goods.setCaption(doc.get("caption"));
                goods.setType(Integer.valueOf(doc.get("type")));
                goods.setIsTop(Boolean.valueOf(doc.get("isTop")));
                goods.setPrice(new BigDecimal(doc.get("price")));
                goods.setMarketPrice(new BigDecimal(doc.get("marketPrice")));
                goods.setUnit(doc.get("unit"));
                goods.setWeight(StringUtils.isEmpty(doc.get("weight")) ? null : Integer.valueOf(doc.get("weight")));
                goods.setIsMarketable(Boolean.valueOf(doc.get("isMarketable")));
                goods.setIsList(Boolean.valueOf(doc.get("isList")));
                goods.setIsTop(Boolean.valueOf(doc.get("isTop")));
                goods.setIntroduction(doc.get("introduction"));
                goods.setKeyword(doc.get("keyword"));
                goods.setScore(Double.valueOf(doc.get("score")));
                goods.setScoreCount(Long.valueOf(doc.get("scoreCount")));
                goods.setWeekSales(Long.valueOf(doc.get("weekSales")));
                goods.setMonthSales(Long.valueOf(doc.get("monthSales")));
                goods.setSales(Long.valueOf(doc.get("sales")));
                goods.setCreateDate(new Timestamp(Long.parseLong(doc.get("createDate"))));
                goods.setModifyDate(new Timestamp(Long.parseLong(doc.get("modifyDate"))));
                goods.setProductImages(doc.get("productImages"));
                goods.setProducts(new HashSet<>(this.tProductDao.fetchByGoods(goods.getId())));
                list.add(goods);
            }
        }

        return list;
    }

    /*public static void main(String[] args) {
        try {
            String dir = "E:\\ideaProject\\vjshop_new\\shop\\target\\vjshop\\WEB-INF\\index\\com.vjshop.entity.TGoods\\";
            IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(dir)));
            IndexSearcher searcher = new IndexSearcher(indexReader);

            String text = QueryParser.escape("iphone");
            Query keywordQuery = null;
            keywordQuery = new QueryParser(Version.LUCENE_36, "keyword", new IKAnalyzer()).parse(text);
            BooleanQuery textQuery = new BooleanQuery();
            BooleanQuery query = new BooleanQuery();
            textQuery.add(keywordQuery, BooleanClause.Occur.SHOULD);
            query.add(textQuery, BooleanClause.Occur.MUST);
            NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", 1000.00, 50000.00, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
            TopDocs topDocs = searcher.search(query, 100);
            for (ScoreDoc doc : topDocs.scoreDocs) {
                Document doc1 = searcher.doc(doc.doc);
                System.out.println("-->" + doc1.get("title"));
            }
            indexReader.close();

        } catch (IOException e) {
            throw new LucenceIndexException("关闭索引出错",e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/
}
