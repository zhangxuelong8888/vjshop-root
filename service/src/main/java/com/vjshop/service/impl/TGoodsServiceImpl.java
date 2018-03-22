package com.vjshop.service.impl;

import com.vjshop.*;
import com.vjshop.Order;
import com.vjshop.dao.*;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TGoodsRecord;
import com.vjshop.service.*;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Service - 货品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tGoodsServiceImpl")
public class TGoodsServiceImpl extends TBaseServiceImpl<TGoodsRecord, TGoods, Long> implements TGoodsService {

    @Resource(name = "ehCacheManager")
    private CacheManager cacheManager;
    @Resource(name = "specificationValueServiceImpl")
    private SpecificationValueService specificationValueService;
    @Autowired
    private TGoodsDao tGoodsDao;
    @Autowired
    private TProductDao tProductDao;
    @Autowired
    private TBrandDao tBrandDao;
    @Autowired
    private TStockLogDao tStockLogDao;
    @Autowired
    private TProductCategoryService tProductCategoryService;
    @Autowired
    private TProductCategoryDao tProductCategoryDao;
    @Autowired
    private TPromotionDao tPromotionDao;
    @Autowired
    private TGoodsPromotionDao tGoodsPromotionDao;
    @Autowired
    private TTagDao tTagDao;
    @Autowired
    private TGoodsTagDao tGoodsTagDao;
    @Autowired
    private TMemberFavoriteGoodsDao tMemberFavoriteGoodsDao;
    @Autowired
    private ProductImageService tProductImageService;
    @Autowired
    private TSnDao tSnDao;
    @Autowired
    private TAttributeDao tAttributeDao;
    @Autowired
    private StaticService staticService;
    @Autowired
    private TSearchService tSearchService;

    @Transactional(readOnly = true)
    public boolean snExists(String sn) {
        return tGoodsDao.snExists(sn);
    }

    @Transactional(readOnly = true)
    public TGoods findBySn(String sn) {
        return tGoodsDao.findBySn(sn);
    }

    @Transactional(readOnly = true)
    public List<TGoods> findList(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                Boolean isStockAlert, Boolean hasPromotion, TGoods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
        return tGoodsDao.findList(type, productCategoryId, brandId, promotionId, tagId, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "goods", condition = "#useCache")
    public List<TGoods> findList(TGoods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
                                Boolean hasPromotion, TGoods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
        TProductCategory productCategory = tProductCategoryDao.find(productCategoryId);
        if (productCategoryId != null && productCategory == null) {
            return Collections.emptyList();
        }
        TBrand brand = tBrandDao.find(brandId);
        if (brandId != null && brand == null) {
            return Collections.emptyList();
        }
        TPromotion promotion = tPromotionDao.find(promotionId);
        if (promotionId != null && promotion == null) {
            return Collections.emptyList();
        }
        TTag tag = tTagDao.find(tagId);
        if (tagId != null && tag == null) {
            return Collections.emptyList();
        }
        Map<TAttribute, String> map = new HashMap<TAttribute, String>();
        if (attributeValueMap != null) {
            for (Map.Entry<Long, String> entry : attributeValueMap.entrySet()) {
                TAttribute attribute = tAttributeDao.find(entry.getKey());
                if (attribute != null) {
                    map.put(attribute, entry.getValue());
                }
            }
        }
        if (MapUtils.isNotEmpty(attributeValueMap) && MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        return tGoodsDao.findList(type, productCategoryId, brandId, promotionId, tagId, map, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<TGoods> findList(Long productCategory, Boolean isMarketable, TGoods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count) {
        return tGoodsDao.findList(productCategory, isMarketable, generateMethod, beginDate, endDate, first, count);
    }

    @Transactional(readOnly = true)
    public Page<TGoods> findPage(TGoods.Type type, Long productCategory, Long brand, Long promotion, Long tag, Map<TAttribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                 Boolean isStockAlert, Boolean hasPromotion, TGoods.OrderType orderType, Pageable pageable) {

        Page<TGoods> tGoodsPage = this.tGoodsDao.findPage(type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, pageable);
        List<TGoods> newGoodsList = new ArrayList<TGoods>();
        for (TGoods goods : tGoodsPage.getContent()) {
            goods = this.findDetailById(goods.getId());
            newGoodsList.add(goods);
        }
        Page<TGoods> newPage = new Page<>(newGoodsList, tGoodsPage.getTotal(), pageable);
        return newPage;
    }

    @Transactional(readOnly = true)
    public Page<TGoods> findPage(TGoods.RankingType rankingType, Pageable pageable) {
        return tGoodsDao.findPage(rankingType, pageable);
    }

    /**
     * 查找收藏货品分页
     *
     * @param memberId 会员
     * @param pageable 分页信息
     * @return 收藏货品分页
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TGoods> findPage(Long memberId, Pageable pageable) {
        return tGoodsDao.findPage(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(TGoods.Type type, Long favoriteMemberId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
        return tGoodsDao.count(type, favoriteMemberId, isMarketable, isList, isTop, isOutOfStock, isStockAlert);
    }

    public long viewHits(Long id) {
        Assert.notNull(id);

        Ehcache cache = cacheManager.getEhcache(TGoods.HITS_CACHE_NAME);
        Element element = cache.get(id);
        Long hits;
        if (element != null) {
            hits = (Long) element.getObjectValue() + 1;
        } else {
            TGoods goods = tGoodsDao.find(id);
            if (goods == null) {
                return 0L;
            }
            hits = goods.getHits() + 1;
        }
        cache.put(new Element(id, hits));
        return hits;
    }

    public void addHits(Long goodsId, long amount) {
        Assert.notNull(goodsId);
        Assert.state(amount >= 0);

        if (amount == 0) {
            return;
        }

        TGoods goods = tGoodsDao.findWithLock(goodsId);

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekHitsCalendar = DateUtils.toCalendar(goods.getWeekHitsDate());
        Calendar monthHitsCalendar = DateUtils.toCalendar(goods.getMonthHitsDate());
        if (nowCalendar.get(Calendar.YEAR) > weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
            goods.setWeekHits(amount);
        } else {
            goods.setWeekHits(goods.getWeekHits() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
            goods.setMonthHits(amount);
        } else {
            goods.setMonthHits(goods.getMonthHits() + amount);
        }
        goods.setHits(goods.getHits() + amount);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        goods.setWeekHitsDate(now);
        goods.setMonthHitsDate(now);
        tGoodsDao.insert(goods);
    }

    public void addSales(Long goodsId, long amount) {
        Assert.notNull(goodsId);
        Assert.state(amount >= 0);

        if (amount == 0) {
            return;
        }

        TGoods goods = tGoodsDao.findWithLock(goodsId);

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekSalesCalendar = DateUtils.toCalendar(goods.getWeekSalesDate());
        Calendar monthSalesCalendar = DateUtils.toCalendar(goods.getMonthSalesDate());
        if (nowCalendar.get(Calendar.YEAR) > weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
            goods.setWeekSales(amount);
        } else {
            goods.setWeekSales(goods.getWeekSales() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
            goods.setMonthSales(amount);
        } else {
            goods.setMonthSales(goods.getMonthSales() + amount);
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        goods.setSales(goods.getSales() + amount);
        goods.setWeekSalesDate(now);
        goods.setMonthSalesDate(now);
        goods.setModifyDate(now);
        goods.setGenerateMethod(TGoods.GenerateMethod.lazy.ordinal());
        this.tGoodsDao.update(goods);
    }

    @Transactional
    public TGoods save(TGoods goods, TProduct product, TAdmin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

        switch (TGoods.Type.valueOf(goods.getType())) {
            case general:
                product.setExchangePoint(0L);
                break;
            case exchange:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                goods.setPromotions(null);
                break;
            case gift:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                product.setExchangePoint(0L);
                goods.setPromotions(null);
                break;
        }
        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
        }
        if (product.getRewardPoint() == null) {
            product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
        }
        product.setAllocatedStock(0);
        product.setIsDefault(true);
        product.setGoodsVO(goods);
        product.setGoods(goods.getId());
        product.setSpecificationValues(null);
        product.setCreateDate(new Timestamp(System.currentTimeMillis()));
        product.setModifyDate(new Timestamp(System.currentTimeMillis()));
        product.setVersion(0L);

        goods.setPrice(product.getPrice());
        goods.setMarketPrice(product.getMarketPrice());
        goods.setScore(0D);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Timestamp(System.currentTimeMillis()));
        goods.setMonthHitsDate(new Timestamp(System.currentTimeMillis()));
        goods.setWeekSalesDate(new Timestamp(System.currentTimeMillis()));
        goods.setMonthSalesDate(new Timestamp(System.currentTimeMillis()));
        goods.setGenerateMethod(TGoods.GenerateMethod.none.ordinal());
        goods.setSpecificationItems(null);
        goods.setProducts(null);
        if (CollectionUtils.isNotEmpty(goods.getSpecificationItemsList()))
            goods.setSpecificationItems(JsonUtils.toJson(goods.getSpecificationItemsList()));
        if (CollectionUtils.isNotEmpty(goods.getParameterValuesList()))
            goods.setParameterValues(JsonUtils.toJson(goods.getParameterValuesList()));
        if (CollectionUtils.isNotEmpty(goods.getProductImagesList()))
            goods.setProductImages(JsonUtils.toJson(goods.getProductImagesList()));
        setValue(goods);
//		goodsDao.persist(goods);
        Set<TTag> tTags = goods.getTags();
        Set<TPromotion> tPromotions = goods.getPromotions();
        goods = this.save(goods);
        this.updateGoodsTags(goods.getId(), tTags);
        this.updateGoodsPromotions(goods.getId(), tPromotions);

        setValue(product);
//		productDao.persist(product);
        product.setGoods(goods.getId());
        product = this.tProductDao.insertAndFetch(product);
        stockIn(product, operator);

        return goods;
    }

    @Transactional
    public TGoods save(TGoods goods, List<TProduct> products, TAdmin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(goods.hasSpecification());
        Assert.notEmpty(products);
        goods.setSpecificationItems(JsonUtils.toJson(goods.getSpecificationItemsList()));
        final List<SpecificationItem> specificationItems = goods.getSpecificationItemsList();
        if (CollectionUtils.exists(products, new Predicate() {
            private Set<List<Integer>> set = new HashSet<List<Integer>>();

            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product == null || !product.isNew() || !product.hasSpecification() || !set.add(product.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, product.getSpecificationValuesList());
            }
        })) {
            throw new IllegalArgumentException();
        }

        TProduct defaultProduct = (TProduct) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product != null && product.getIsDefault();
            }
        });
        if (defaultProduct == null) {
            defaultProduct = products.get(0);
            defaultProduct.setIsDefault(true);
        }

        for (TProduct product : products) {
            switch (TGoods.Type.valueOf(goods.getType())) {
                case general:
                    product.setExchangePoint(0L);
                    break;
                case exchange:
                    product.setPrice(BigDecimal.ZERO);
                    product.setRewardPoint(0L);
                    goods.setPromotions(null);
                    break;
                case gift:
                    product.setPrice(BigDecimal.ZERO);
                    product.setRewardPoint(0L);
                    product.setExchangePoint(0L);
                    goods.setPromotions(null);
                    break;
            }
            if (product.getMarketPrice() == null) {
                product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
            }
            if (product.getRewardPoint() == null) {
                product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
            }
            if (product != defaultProduct) {
                product.setIsDefault(false);
            }
            product.setAllocatedStock(0);
            product.setGoodsVO(goods);
            product.setGoods(goods.getId());
            product.setCreateDate(new Timestamp(System.currentTimeMillis()));
            product.setModifyDate(new Timestamp(System.currentTimeMillis()));
            product.setVersion(0L);
        }

        goods.setPrice(defaultProduct.getPrice());
        goods.setMarketPrice(defaultProduct.getMarketPrice());
        goods.setScore(0D);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Timestamp(System.currentTimeMillis()));
        goods.setMonthHitsDate(new Timestamp(System.currentTimeMillis()));
        goods.setWeekSalesDate(new Timestamp(System.currentTimeMillis()));
        goods.setMonthSalesDate(new Timestamp(System.currentTimeMillis()));
        goods.setGenerateMethod(TGoods.GenerateMethod.none.ordinal());
        goods.setProducts(null);
        if (CollectionUtils.isNotEmpty(goods.getSpecificationItemsList())) {
            goods.setSpecificationItems(JsonUtils.toJson(goods.getSpecificationItemsList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getParameterValuesList())) {
            goods.setParameterValues(JsonUtils.toJson(goods.getParameterValuesList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getProductImagesList())) {
            goods.setProductImages(JsonUtils.toJson(goods.getProductImagesList()));
        }
        setValue(goods);
        Set<TTag> tTags = goods.getTags();
        Set<TPromotion> tPromotions = goods.getPromotions();
        goods = this.save(goods);
        this.updateGoodsPromotions(goods.getId(), tPromotions);
        this.updateGoodsTags(goods.getId(), tTags);
//		goodsDao.persist(goods);

        for (TProduct product : products) {
            setValue(product);
            product.setSpecificationValues(JsonUtils.toJson(product.getSpecificationValuesList()));
            product.setGoods(goods.getId());
            product = this.tProductDao.insertAndFetch(product);
//			productDao.persist(product);
            stockIn(product, operator);
        }

        return goods;
    }

    @Transactional
    public TGoods update(TGoods goods, TProduct product, TAdmin operator) {
        Assert.notNull(goods);
        Assert.isTrue(!goods.isNew());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

//		TGoods pGoods = goodsDao.find(goods.getId());
        TGoods pGoods = this.findDetailById(goods.getId());
        switch (TGoods.Type.valueOf(pGoods.getType())) {
            case general:
                product.setExchangePoint(0L);
                break;
            case exchange:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                goods.setPromotions(null);
                break;
            case gift:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                product.setExchangePoint(0L);
                goods.setPromotions(null);
                break;
        }
        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
        }
        if (product.getRewardPoint() == null) {
            product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
        }
        product.setAllocatedStock(0);
        product.setIsDefault(true);
        product.setGoodsVO(pGoods);
        product.setGoods(pGoods.getId());
        product.setSpecificationValues(JsonUtils.toJson(product.getSpecificationValuesList()));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        product.setCreateDate(now);
        product.setModifyDate(now);
        product.setVersion(0L);

        if (pGoods.hasSpecification()) {
            for (TProduct pProduct : pGoods.getProducts()) {
//				productDao.remove(pProduct);
                this.tStockLogDao.delete(this.tStockLogDao.fetchByProduct(pProduct.getId()));
                this.tProductDao.delete(pProduct);
            }
            if (product.getStock() == null) {
                throw new IllegalArgumentException();
            }
            setValue(product);
//			productDao.persist(product);
            product = tProductDao.insertAndFetch(product);
            stockIn(product, operator);
        } else {
            List<TProduct> goodsProduct = this.tProductDao.fetchByGoods(pGoods.getId());
            pGoods.setProducts(new HashSet<>(goodsProduct));
            TProduct defaultProduct = pGoods.getDefaultProduct();
            defaultProduct.setPrice(product.getPrice());
            defaultProduct.setCost(product.getCost());
            defaultProduct.setMarketPrice(product.getMarketPrice());
            defaultProduct.setRewardPoint(product.getRewardPoint());
            defaultProduct.setExchangePoint(product.getExchangePoint());
        }

        goods.setPrice(product.getPrice());
        goods.setMarketPrice(product.getMarketPrice());
        setValue(goods);
        BeanUtils.copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
                "products");
//		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
//				"products");
        if (CollectionUtils.isNotEmpty(goods.getSpecificationItemsList())) {
            goods.setSpecificationItems(JsonUtils.toJson(goods.getSpecificationItemsList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getParameterValuesList())) {
            goods.setParameterValues(JsonUtils.toJson(goods.getParameterValuesList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getProductImagesList())) {
            goods.setProductImages(JsonUtils.toJson(goods.getProductImagesList()));
        }
        pGoods.setGenerateMethod(TGoods.GenerateMethod.eager.ordinal());
        this.update(pGoods);
        this.updateGoodsPromotions(pGoods.getId(), pGoods.getPromotions());
        this.updateGoodsTags(pGoods.getId(), pGoods.getTags());
        return pGoods;
    }

    @Transactional
    public TGoods update(TGoods goods, List<TProduct> products, TAdmin operator) {
        Assert.notNull(goods);
        Assert.isTrue(!goods.isNew());
        Assert.isTrue(goods.hasSpecification());
        Assert.notEmpty(products);
        final List<SpecificationItem> specificationItems = goods.getSpecificationItemsList();
        if (CollectionUtils.exists(products, new Predicate() {
            private Set<List<Integer>> set = new HashSet<List<Integer>>();

            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product == null || !product.isNew() || !product.hasSpecification() || !set.add(product.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, product.getSpecificationValuesList());
            }
        })) {
            throw new IllegalArgumentException();
        }

        TProduct defaultProduct = (TProduct) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product != null && product.getIsDefault();
            }
        });
        if (defaultProduct == null) {
            defaultProduct = products.get(0);
            defaultProduct.setIsDefault(true);
        }

//		TGoods pGoods = goodsDao.find(goods.getId());
        TGoods pGoods = this.findDetailById(goods.getId());
        for (TProduct product : products) {
            switch (TGoods.Type.valueOf(pGoods.getType())) {
                case general:
                    product.setExchangePoint(0L);
                    break;
                case exchange:
                    product.setPrice(BigDecimal.ZERO);
                    product.setRewardPoint(0L);
                    goods.setPromotions(null);
                    break;
                case gift:
                    product.setPrice(BigDecimal.ZERO);
                    product.setRewardPoint(0L);
                    product.setExchangePoint(0L);
                    goods.setPromotions(null);
                    break;
            }
            if (product.getMarketPrice() == null) {
                product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
            }
            if (product.getRewardPoint() == null) {
                product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
            }
            if (product != defaultProduct) {
                product.setIsDefault(false);
            }
            product.setAllocatedStock(0);
            product.setGoodsVO(pGoods);
            product.setGoods(pGoods.getId());
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (pGoods.hasSpecification()) {
            for (TProduct pProduct : pGoods.getProducts()) {
                if (!exists(products, pProduct.getSpecificationValueIds())) {
//					productDao.remove(pProduct);
                    this.tStockLogDao.delete(this.tStockLogDao.fetchByProduct(pProduct.getId()));
                    this.tProductDao.delete(pProduct);
                }
            }
            for (TProduct product : products) {
                TProduct pProduct = find(pGoods.getProducts(), product.getSpecificationValueIds());
                if (pProduct != null) {
                    pProduct.setPrice(product.getPrice());
                    pProduct.setCost(product.getCost());
                    pProduct.setMarketPrice(product.getMarketPrice());
                    pProduct.setRewardPoint(product.getRewardPoint());
                    pProduct.setExchangePoint(product.getExchangePoint());
                    pProduct.setIsDefault(product.getIsDefault());
                    pProduct.setModifyDate(new Timestamp(System.currentTimeMillis()));
                    this.tProductDao.update(pProduct);
                } else {
                    if (product.getStock() == null) {
                        throw new IllegalArgumentException();
                    }
                    setValue(product);
                    product.setCreateDate(now);
                    product.setModifyDate(now);
                    product.setVersion(0L);

                    product.setSpecificationValues(JsonUtils.toJson(product.getSpecificationValuesList()));
//					productDao.persist(product);
                    product = this.tProductDao.insertAndFetch(product);
                    stockIn(product, operator);
                }
            }
        } else {
//			productDao.remove(pGoods.getDefaultProduct());
            this.tStockLogDao.delete(this.tStockLogDao.fetchByProduct(pGoods.getDefaultProduct().getId()));
            this.tProductDao.delete(pGoods.getDefaultProduct());
            for (TProduct product : products) {
                if (product.getStock() == null) {
                    throw new IllegalArgumentException();
                }
                setValue(product);
                product.setSpecificationValues(JsonUtils.toJson(product.getSpecificationValuesList()));
                product.setCreateDate(now);
                product.setModifyDate(now);
                product.setVersion(0L);
//				productDao.persist(product);
                product = this.tProductDao.insertAndFetch(product);
                stockIn(product, operator);
            }
        }

        goods.setPrice(defaultProduct.getPrice());
        goods.setMarketPrice(defaultProduct.getMarketPrice());
        if (CollectionUtils.isNotEmpty(goods.getSpecificationItemsList())) {
            goods.setSpecificationItems(JsonUtils.toJson(goods.getSpecificationItemsList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getParameterValuesList())) {
            goods.setParameterValues(JsonUtils.toJson(goods.getParameterValuesList()));
        }
        if (CollectionUtils.isNotEmpty(goods.getProductImagesList())) {
            goods.setProductImages(JsonUtils.toJson(goods.getProductImagesList()));
        }
        setValue(goods);
        BeanUtils.copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
                "products");
//		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
//				"products");
        this.update(pGoods);
        pGoods.setGenerateMethod(TGoods.GenerateMethod.eager.ordinal());
        this.updateGoodsPromotions(pGoods.getId(), pGoods.getPromotions());
        this.updateGoodsTags(pGoods.getId(), pGoods.getTags());
        return pGoods;
    }

    //	@Override
    @Transactional
    @CacheEvict(value = {"goods", "productCategory"}, allEntries = true)
    public TGoods save(TGoods goods) {
        Assert.notNull(goods);

        goods = super.save(goods);
        tSearchService.index(goods, true);
        return goods;
    }

    //	@Override
    @Transactional
    @CacheEvict(value = {"goods", "productCategory"}, allEntries = true)
    public TGoods update(TGoods goods) {
        Assert.notNull(goods);

        goods = this.tGoodsDao.update(goods, null);
        tSearchService.index(goods, false);
        return goods;
    }

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Long goodsId) {
		staticService.delete(tGoodsDao.find(goodsId));
		tGoodsDao.deleteById(goodsId);
		tSearchService.purge(TGoods.class, String.valueOf(goodsId));
	}

    /**
     * 设置货品值
     *
     * @param goods 货品
     */
    private void setValue(TGoods goods) {
        if (goods == null) {
            return;
        }

        this.tProductImageService.generate(goods.getProductImagesList());
        if (StringUtils.isEmpty(goods.getImage()) && StringUtils.isNotEmpty(goods.getThumbnail())) {
            goods.setImage(goods.getThumbnail());
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (goods.isNew()) {
            goods.setCreateDate(now);
            goods.setModifyDate(now);
            goods.setVersion(0L);
            goods.setGenerateMethod(TGoods.GenerateMethod.eager.ordinal());

            if (StringUtils.isEmpty(goods.getSn())) {
                String sn;
                do {
                    sn = tSnDao.generate(TSn.Type.goods);
                } while (snExists(sn));
                goods.setSn(sn);
            }
        } else {
            goods.setModifyDate(now);
        }
    }

    /**
     * 设置商品值
     *
     * @param product 商品
     */
    private void setValue(TProduct product) {
        if (product == null) {
            return;
        }

        if (product.isNew()) {
            TGoods goods = product.getGoodsVO();
            if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
                String sn;
                int i = product.hasSpecification() ? 1 : 0;
                do {
                    sn = goods.getSn() + (i == 0 ? "" : "_" + i);
                    i++;
                } while (this.tProductDao.snExists(sn));
                product.setSn(sn);
            }
        }
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     * @return 默认市场价
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Assert.notNull(price);

        Setting setting = SystemUtils.getSetting();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return defaultMarketPriceScale != null ? setting.setScale(price.multiply(new BigDecimal(String.valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
    }

    /**
     * 计算默认赠送积分
     *
     * @param price 价格
     * @return 默认赠送积分
     */
    private long calculateDefaultRewardPoint(BigDecimal price) {
        Assert.notNull(price);

        Setting setting = SystemUtils.getSetting();
        Double defaultPointScale = setting.getDefaultPointScale();
        return defaultPointScale != null ? price.multiply(new BigDecimal(String.valueOf(defaultPointScale))).longValue() : 0L;
    }

    /**
     * 根据规格值ID查找商品
     *
     * @param products              商品
     * @param specificationValueIds 规格值ID
     * @return 商品
     */
    private TProduct find(Collection<TProduct> products, final List<Integer> specificationValueIds) {
        if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(specificationValueIds)) {
            return null;
        }

        return (TProduct) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product != null && product.getSpecificationValueIds() != null && product.getSpecificationValueIds().equals(specificationValueIds);
            }
        });
    }

    /**
     * 根据规格值ID判断商品是否存在
     *
     * @param products              商品
     * @param specificationValueIds 规格值ID
     * @return 商品是否存在
     */
    private boolean exists(Collection<TProduct> products, final List<Integer> specificationValueIds) {
        return find(products, specificationValueIds) != null;
    }

    /**
     * 入库
     *
     * @param product  商品
     * @param operator 操作员
     */
    private void stockIn(TProduct product, TAdmin operator) {
        if (product == null || product.getStock() == null || product.getStock() <= 0) {
            return;
        }

        TStockLog stockLog = new TStockLog();
        stockLog.setType(TStockLog.Type.stockIn.ordinal());
        stockLog.setInQuantity(product.getStock());
        stockLog.setOutQuantity(0);
        stockLog.setStock(product.getStock());
        stockLog.setOperator(operator.getUsername());
        stockLog.setMemo(null);
        stockLog.setProductVO(product);
        stockLog.setProduct(product.getId());
        stockLog.setCreateDate(new Timestamp(System.currentTimeMillis()));
        stockLog.setModifyDate(new Timestamp(System.currentTimeMillis()));
        stockLog.setVersion(0L);
//		stockLogDao.persist(stockLog);
        this.tStockLogDao.insert(stockLog);
    }


    public TGoods findDetailById(Long id) {
        Assert.notNull(id);

        TGoods goods = this.tGoodsDao.find(id);
        goods.setBrandVO(this.tBrandDao.find(goods.getBrand()));
        goods.setProductCategoryVO(this.tProductCategoryService.findDetailById(goods.getProductCategory()));
        goods.setProducts(new HashSet<>(this.tProductDao.fetchByGoods(goods.getId())));

        List<TGoodsPromotion> tGoodsPromotions = this.tGoodsPromotionDao.fetchByGoods(goods.getId());
        Long[] goodsPromotionIds = new Long[tGoodsPromotions.size()];
        for (int i = 0; i < tGoodsPromotions.size(); i++) {
            goodsPromotionIds[i] = tGoodsPromotions.get(i).getPromotions();
        }
        goods.setPromotions(new HashSet<>(this.tPromotionDao.fetchById(goodsPromotionIds)));

        List<TGoodsTag> tGoodsTags = this.tGoodsTagDao.fetchByGoods(goods.getId());
        Long[] goodsTagIds = new Long[tGoodsTags.size()];
        for (int i = 0; i < tGoodsTags.size(); i++) {
            goodsTagIds[i] = tGoodsTags.get(i).getTags();
        }
        goods.setTags(new HashSet<>(this.tTagDao.fetchById(goodsTagIds)));

        List<TMemberFavoriteGoods> tMemberFavoriteGoods = this.tMemberFavoriteGoodsDao.fetchByFavoriteGoods(goods.getId());
        Long[] memberIds = new Long[tMemberFavoriteGoods.size()];
        for (int i = 0; i < tMemberFavoriteGoods.size(); i++) {
            memberIds[i] = tMemberFavoriteGoods.get(i).getFavoriteMembers();
        }
        return goods;
    }

    public List<TGoods> findByProductcategory(Long... productcategory) {
        Assert.notNull(productcategory);
        return this.tGoodsDao.fetchByProductCategory(productcategory);
    }

    @Transactional
    @CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
    public void deleteCascade(Long... ids){
        List<TProduct> tProducts = this.tProductDao.fetchByGoods(ids);
        for(TProduct tProduct : tProducts){
            this.tStockLogDao.delete(this.tStockLogDao.fetchByProduct(tProduct.getId()));
        }
        this.tProductDao.delete(tProducts);
        for (Long id : ids) {
            this.tGoodsPromotionDao.deleteByGoods(id);
            this.tGoodsTagDao.deleteByGoods(id);
            tSearchService.purge(TGoods.class, String.valueOf(id));
        }
        super.delete(ids);

    }

    private void updateGoodsPromotions(Long goodsId, Set<TPromotion> tPromotions) {
        if (CollectionUtils.isNotEmpty(tPromotions)) {
            List<TGoodsPromotion> oldGoodsPromotions = this.tGoodsPromotionDao.fetchByGoods(goodsId);
            this.tGoodsPromotionDao.delete(oldGoodsPromotions);
            List<TGoodsPromotion> newGoodsPromotions = new ArrayList<>();
            for (TPromotion tPromotion : tPromotions) {
                TGoodsPromotion tempGoodsPromotion = new TGoodsPromotion(goodsId, tPromotion.getId());
                newGoodsPromotions.add(tempGoodsPromotion);
            }
            this.tGoodsPromotionDao.insert(newGoodsPromotions);
        }
    }

    private void updateGoodsTags(Long goodsId, Set<TTag> tTags) {
        if (CollectionUtils.isNotEmpty(tTags)) {
            List<TGoodsTag> oldGoodsTags = this.tGoodsTagDao.fetchByGoods(goodsId);
            this.tGoodsTagDao.delete(oldGoodsTags);
            List<TGoodsTag> newGoodsTags = new ArrayList<>();
            for (TTag tTag : tTags) {
                TGoodsTag tempGoodsTag = new TGoodsTag(goodsId, tTag.getId());
                newGoodsTags.add(tempGoodsTag);
            }
            this.tGoodsTagDao.insert(newGoodsTags);
        }
    }

}