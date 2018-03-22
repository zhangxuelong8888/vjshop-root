
package com.vjshop.entity;

import com.fasterxml.jackson.databind.JavaType;
import com.vjshop.Setting;
import com.vjshop.TemplateConfig;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 货品
 */
public class TGoods implements Serializable {

    private final JavaType specificationItemsListType = JsonUtils.getCollectionType(List.class,SpecificationItem.class);

    private final JavaType productImagesListType = JsonUtils.getCollectionType(List.class,ProductImage.class);

    private final JavaType parameterValuesListType = JsonUtils.getCollectionType(List.class,ParameterValue.class);

    private static final long serialVersionUID = 766206024;

    /** 点击数缓存名称 */
    public static final String HITS_CACHE_NAME = "goodsHits";

    /** 属性值属性个数 */
    public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

    /** 属性值属性名称前缀 */
    public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

    /**
     * 类型
     */
    public enum Type {

        /** 普通商品 */
        general,

        /** 兑换商品 */
        exchange,

        /** 赠品 */
        gift;

        public static Type valueOf(int ordinal){
            if (ordinal < 0 || ordinal >= values().length) {
                throw new IndexOutOfBoundsException("Invalid ordinal");
            }
            return values()[ordinal];
        }
    }

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

    /**
     * 排名类型
     */
    public enum RankingType {

        /** 评分 */
        score,

        /** 评分数 */
        scoreCount,

        /** 周点击数 */
        weekHits,

        /** 月点击数 */
        monthHits,

        /** 点击数 */
        hits,

        /** 周销量 */
        weekSales,

        /** 月销量 */
        monthSales,

        /** 销量 */
        sales
    }

    /**
     * 排序类型
     */
    public enum OrderType {

        /** 置顶降序 */
        topDesc,

        /** 价格升序 */
        priceAsc,

        /** 价格降序 */
        priceDesc,

        /** 销量降序 */
        salesDesc,

        /** 评分降序 */
        scoreDesc,

        /** 日期降序 */
        dateDesc,

        /** 日期升序 */
        dateAsc
    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private String     attributeValue0;
    private String     attributeValue1;
    private String     attributeValue10;
    private String     attributeValue11;
    private String     attributeValue12;
    private String     attributeValue13;
    private String     attributeValue14;
    private String     attributeValue15;
    private String     attributeValue16;
    private String     attributeValue17;
    private String     attributeValue18;
    private String     attributeValue19;
    private String     attributeValue2;
    private String     attributeValue3;
    private String     attributeValue4;
    private String     attributeValue5;
    private String     attributeValue6;
    private String     attributeValue7;
    private String     attributeValue8;
    private String     attributeValue9;
    private String     caption;
    private Integer    generateMethod;
    private Long       hits;
    private String     image;
    private String     introduction;
    private Boolean    isDelivery;
    private Boolean    isList;
    private Boolean    isMarketable;
    private Boolean    isTop;
    private String     keyword;
    private BigDecimal marketPrice;
    private String     memo;
    private Long       monthHits;
    private Timestamp  monthHitsDate;
    private Long       monthSales;
    private Timestamp  monthSalesDate;
    private String     name;
    private String     parameterValues;
    private BigDecimal price;
    private String     productImages;
    private Long       sales;
    private Double     score;
    private Long       scoreCount;
    private String     seoDescription;
    private String     seoKeywords;
    private String     seoTitle;
    private String     sn;
    private String     specificationItems;
    private Long       totalScore;
    private Integer    type;
    private String     unit;
    private Long       weekHits;
    private Timestamp  weekHitsDate;
    private Long       weekSales;
    private Timestamp  weekSalesDate;
    private Integer    weight;
    private Long       brand;
    private Long       productCategory;

    /** 商品分类 */
    private TProductCategory productCategoryVO;

    /** 品牌 */
    private TBrand brandVO;

    /** 商品图片 */
    private List<ProductImage> productImagesList = new ArrayList<ProductImage>();

    /** 参数值 */
    private List<ParameterValue> parameterValuesList = new ArrayList<ParameterValue>();

    /** 规格项 */
    private List<SpecificationItem> specificationItemsList = new ArrayList<SpecificationItem>();

    /** 促销 */
    private Set<TPromotion> promotions = new HashSet<TPromotion>();

    /** 标签 */
    private Set<TTag> tags = new HashSet<TTag>();

    /** 商品 */
    private Set<TProduct> products = new HashSet<TProduct>();

    public TGoods() {}

    public TGoods(TGoods value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.attributeValue0 = value.attributeValue0;
        this.attributeValue1 = value.attributeValue1;
        this.attributeValue10 = value.attributeValue10;
        this.attributeValue11 = value.attributeValue11;
        this.attributeValue12 = value.attributeValue12;
        this.attributeValue13 = value.attributeValue13;
        this.attributeValue14 = value.attributeValue14;
        this.attributeValue15 = value.attributeValue15;
        this.attributeValue16 = value.attributeValue16;
        this.attributeValue17 = value.attributeValue17;
        this.attributeValue18 = value.attributeValue18;
        this.attributeValue19 = value.attributeValue19;
        this.attributeValue2 = value.attributeValue2;
        this.attributeValue3 = value.attributeValue3;
        this.attributeValue4 = value.attributeValue4;
        this.attributeValue5 = value.attributeValue5;
        this.attributeValue6 = value.attributeValue6;
        this.attributeValue7 = value.attributeValue7;
        this.attributeValue8 = value.attributeValue8;
        this.attributeValue9 = value.attributeValue9;
        this.caption = value.caption;
        this.generateMethod = value.generateMethod;
        this.hits = value.hits;
        this.image = value.image;
        this.introduction = value.introduction;
        this.isDelivery = value.isDelivery;
        this.isList = value.isList;
        this.isMarketable = value.isMarketable;
        this.isTop = value.isTop;
        this.keyword = value.keyword;
        this.marketPrice = value.marketPrice;
        this.memo = value.memo;
        this.monthHits = value.monthHits;
        this.monthHitsDate = value.monthHitsDate;
        this.monthSales = value.monthSales;
        this.monthSalesDate = value.monthSalesDate;
        this.name = value.name;
        this.parameterValues = value.parameterValues;
        this.price = value.price;
        this.productImages = value.productImages;
        this.sales = value.sales;
        this.score = value.score;
        this.scoreCount = value.scoreCount;
        this.seoDescription = value.seoDescription;
        this.seoKeywords = value.seoKeywords;
        this.seoTitle = value.seoTitle;
        this.sn = value.sn;
        this.specificationItems = value.specificationItems;
        this.totalScore = value.totalScore;
        this.type = value.type;
        this.unit = value.unit;
        this.weekHits = value.weekHits;
        this.weekHitsDate = value.weekHitsDate;
        this.weekSales = value.weekSales;
        this.weekSalesDate = value.weekSalesDate;
        this.weight = value.weight;
        this.brand = value.brand;
        this.productCategory = value.productCategory;
    }

    public TGoods(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        String     attributeValue0,
        String     attributeValue1,
        String     attributeValue10,
        String     attributeValue11,
        String     attributeValue12,
        String     attributeValue13,
        String     attributeValue14,
        String     attributeValue15,
        String     attributeValue16,
        String     attributeValue17,
        String     attributeValue18,
        String     attributeValue19,
        String     attributeValue2,
        String     attributeValue3,
        String     attributeValue4,
        String     attributeValue5,
        String     attributeValue6,
        String     attributeValue7,
        String     attributeValue8,
        String     attributeValue9,
        String     caption,
        Integer    generateMethod,
        Long       hits,
        String     image,
        String     introduction,
        Boolean    isDelivery,
        Boolean    isList,
        Boolean    isMarketable,
        Boolean    isTop,
        String     keyword,
        BigDecimal marketPrice,
        String     memo,
        Long       monthHits,
        Timestamp  monthHitsDate,
        Long       monthSales,
        Timestamp  monthSalesDate,
        String     name,
        String     parameterValues,
        BigDecimal price,
        String     productImages,
        Long       sales,
        Double     score,
        Long       scoreCount,
        String     seoDescription,
        String     seoKeywords,
        String     seoTitle,
        String     sn,
        String     specificationItems,
        Long       totalScore,
        Integer    type,
        String     unit,
        Long       weekHits,
        Timestamp  weekHitsDate,
        Long       weekSales,
        Timestamp  weekSalesDate,
        Integer    weight,
        Long       brand,
        Long       productCategory
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.attributeValue0 = attributeValue0;
        this.attributeValue1 = attributeValue1;
        this.attributeValue10 = attributeValue10;
        this.attributeValue11 = attributeValue11;
        this.attributeValue12 = attributeValue12;
        this.attributeValue13 = attributeValue13;
        this.attributeValue14 = attributeValue14;
        this.attributeValue15 = attributeValue15;
        this.attributeValue16 = attributeValue16;
        this.attributeValue17 = attributeValue17;
        this.attributeValue18 = attributeValue18;
        this.attributeValue19 = attributeValue19;
        this.attributeValue2 = attributeValue2;
        this.attributeValue3 = attributeValue3;
        this.attributeValue4 = attributeValue4;
        this.attributeValue5 = attributeValue5;
        this.attributeValue6 = attributeValue6;
        this.attributeValue7 = attributeValue7;
        this.attributeValue8 = attributeValue8;
        this.attributeValue9 = attributeValue9;
        this.caption = caption;
        this.generateMethod = generateMethod;
        this.hits = hits;
        this.image = image;
        this.introduction = introduction;
        this.isDelivery = isDelivery;
        this.isList = isList;
        this.isMarketable = isMarketable;
        this.isTop = isTop;
        this.keyword = keyword;
        this.marketPrice = marketPrice;
        this.memo = memo;
        this.monthHits = monthHits;
        this.monthHitsDate = monthHitsDate;
        this.monthSales = monthSales;
        this.monthSalesDate = monthSalesDate;
        this.name = name;
        this.parameterValues = parameterValues;
        this.price = price;
        this.productImages = productImages;
        this.sales = sales;
        this.score = score;
        this.scoreCount = scoreCount;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.seoTitle = seoTitle;
        this.sn = sn;
        this.specificationItems = specificationItems;
        this.totalScore = totalScore;
        this.type = type;
        this.unit = unit;
        this.weekHits = weekHits;
        this.weekHitsDate = weekHitsDate;
        this.weekSales = weekSales;
        this.weekSalesDate = weekSalesDate;
        this.weight = weight;
        this.brand = brand;
        this.productCategory = productCategory;

        if(StringUtils.isNotBlank(this.parameterValues))
            this.parameterValuesList = JsonUtils.toObject(this.parameterValues,parameterValuesListType);
        if(StringUtils.isNotBlank(this.productImages))
            this.productImagesList = JsonUtils.toObject(this.productImages,productImagesListType);
        if(StringUtils.isNotBlank(this.specificationItems))
            this.specificationItemsList = JsonUtils.toObject(this.specificationItems,specificationItemsListType);
    }

    public String typeName() {
        return TGoods.Type.valueOf(this.type).name();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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
    public String getAttributeValue0() {
        return this.attributeValue0;
    }

    public void setAttributeValue0(String attributeValue0) {
        this.attributeValue0 = attributeValue0;
    }

    @Length(max = 200)
    public String getAttributeValue1() {
        return this.attributeValue1;
    }

    public void setAttributeValue1(String attributeValue1) {
        this.attributeValue1 = attributeValue1;
    }

    @Length(max = 200)
    public String getAttributeValue10() {
        return this.attributeValue10;
    }

    public void setAttributeValue10(String attributeValue10) {
        this.attributeValue10 = attributeValue10;
    }

    @Length(max = 200)
    public String getAttributeValue11() {
        return this.attributeValue11;
    }

    public void setAttributeValue11(String attributeValue11) {
        this.attributeValue11 = attributeValue11;
    }

    @Length(max = 200)
    public String getAttributeValue12() {
        return this.attributeValue12;
    }

    public void setAttributeValue12(String attributeValue12) {
        this.attributeValue12 = attributeValue12;
    }

    @Length(max = 200)
    public String getAttributeValue13() {
        return this.attributeValue13;
    }

    public void setAttributeValue13(String attributeValue13) {
        this.attributeValue13 = attributeValue13;
    }

    @Length(max = 200)
    public String getAttributeValue14() {
        return this.attributeValue14;
    }

    public void setAttributeValue14(String attributeValue14) {
        this.attributeValue14 = attributeValue14;
    }

    @Length(max = 200)
    public String getAttributeValue15() {
        return this.attributeValue15;
    }

    public void setAttributeValue15(String attributeValue15) {
        this.attributeValue15 = attributeValue15;
    }

    @Length(max = 200)
    public String getAttributeValue16() {
        return this.attributeValue16;
    }

    public void setAttributeValue16(String attributeValue16) {
        this.attributeValue16 = attributeValue16;
    }

    @Length(max = 200)
    public String getAttributeValue17() {
        return this.attributeValue17;
    }

    public void setAttributeValue17(String attributeValue17) {
        this.attributeValue17 = attributeValue17;
    }

    @Length(max = 200)
    public String getAttributeValue18() {
        return this.attributeValue18;
    }

    public void setAttributeValue18(String attributeValue18) {
        this.attributeValue18 = attributeValue18;
    }

    @Length(max = 200)
    public String getAttributeValue19() {
        return this.attributeValue19;
    }

    public void setAttributeValue19(String attributeValue19) {
        this.attributeValue19 = attributeValue19;
    }

    @Length(max = 200)
    public String getAttributeValue2() {
        return this.attributeValue2;
    }

    public void setAttributeValue2(String attributeValue2) {
        this.attributeValue2 = attributeValue2;
    }

    @Length(max = 200)
    public String getAttributeValue3() {
        return this.attributeValue3;
    }

    public void setAttributeValue3(String attributeValue3) {
        this.attributeValue3 = attributeValue3;
    }

    @Length(max = 200)
    public String getAttributeValue4() {
        return this.attributeValue4;
    }

    public void setAttributeValue4(String attributeValue4) {
        this.attributeValue4 = attributeValue4;
    }

    @Length(max = 200)
    public String getAttributeValue5() {
        return this.attributeValue5;
    }

    public void setAttributeValue5(String attributeValue5) {
        this.attributeValue5 = attributeValue5;
    }

    @Length(max = 200)
    public String getAttributeValue6() {
        return this.attributeValue6;
    }

    public void setAttributeValue6(String attributeValue6) {
        this.attributeValue6 = attributeValue6;
    }

    @Length(max = 200)
    public String getAttributeValue7() {
        return this.attributeValue7;
    }

    public void setAttributeValue7(String attributeValue7) {
        this.attributeValue7 = attributeValue7;
    }

    @Length(max = 200)
    public String getAttributeValue8() {
        return this.attributeValue8;
    }

    public void setAttributeValue8(String attributeValue8) {
        this.attributeValue8 = attributeValue8;
    }

    @Length(max = 200)
    public String getAttributeValue9() {
        return this.attributeValue9;
    }

    public void setAttributeValue9(String attributeValue9) {
        this.attributeValue9 = attributeValue9;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    @Length(max = 200)
    @Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @NotNull
    public Boolean getIsDelivery() {
        return this.isDelivery;
    }

    public void setIsDelivery(Boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    @NotNull
    public Boolean getIsList() {
        return this.isList;
    }

    public void setIsList(Boolean isList) {
        this.isList = isList;
    }

    @NotNull
    public Boolean getIsMarketable() {
        return this.isMarketable;
    }

    public void setIsMarketable(Boolean isMarketable) {
        this.isMarketable = isMarketable;
    }

    @NotNull
    public Boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    @Length(max = 200)
    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        if (keyword != null) {
            keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
        }
        this.keyword = keyword;
    }

    public BigDecimal getMarketPrice() {
        return this.marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Length(max = 200)
    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getMonthHits() {
        return this.monthHits;
    }

    public void setMonthHits(Long monthHits) {
        this.monthHits = monthHits;
    }

    public Timestamp getMonthHitsDate() {
        return this.monthHitsDate;
    }

    public void setMonthHitsDate(Timestamp monthHitsDate) {
        this.monthHitsDate = monthHitsDate;
    }

    public Long getMonthSales() {
        return this.monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public Timestamp getMonthSalesDate() {
        return this.monthSalesDate;
    }

    public void setMonthSalesDate(Timestamp monthSalesDate) {
        this.monthSalesDate = monthSalesDate;
    }

    @NotEmpty
    @Length(max = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSales() {
        return this.sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Double getScore() {
        return this.score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getScoreCount() {
        return this.scoreCount;
    }

    public void setScoreCount(Long scoreCount) {
        this.scoreCount = scoreCount;
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

    @Pattern(regexp = "^[0-9a-zA-Z_-]+$")
    @Length(max = 100)
    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Long getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @NotNull(groups = ValidGroup.Save.class)
    public Integer getType() {
        return type;
    }

    @Length(max = 200)
    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getWeekHits() {
        return this.weekHits;
    }

    public void setWeekHits(Long weekHits) {
        this.weekHits = weekHits;
    }

    public Timestamp getWeekHitsDate() {
        return this.weekHitsDate;
    }

    public void setWeekHitsDate(Timestamp weekHitsDate) {
        this.weekHitsDate = weekHitsDate;
    }

    public Long getWeekSales() {
        return this.weekSales;
    }

    public void setWeekSales(Long weekSales) {
        this.weekSales = weekSales;
    }

    public Timestamp getWeekSalesDate() {
        return this.weekSalesDate;
    }

    public void setWeekSalesDate(Timestamp weekSalesDate) {
        this.weekSalesDate = weekSalesDate;
    }

    @Min(0)
    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


    public String getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(String parameterValues) {
        this.parameterValues = parameterValues;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public String getSpecificationItems() {
        return specificationItems;
    }

    public void setSpecificationItems(String specificationItems) {
        this.specificationItems = specificationItems;
    }

    public Long getBrand() {
        return brand;
    }

    public void setBrand(Long brand) {
        this.brand = brand;
    }

    @NotNull
    public Long getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Long productCategory) {
        this.productCategory = productCategory;
    }

    public TProductCategory getProductCategoryVO() {
        return productCategoryVO;
    }

    public void setProductCategoryVO(TProductCategory productCategoryVO) {
        this.productCategoryVO = productCategoryVO;
    }

    public TBrand getBrandVO() {
        return brandVO;
    }

    public void setBrandVO(TBrand brandVO) {
        this.brandVO = brandVO;
    }

    public List<ProductImage> getProductImagesList() {
        if (CollectionUtils.isEmpty(productImagesList) && StringUtils.isNotEmpty(this.productImages)) {
            this.productImagesList = JsonUtils.toObject(this.productImages,productImagesListType);
        }
        return productImagesList;
    }

    public void setProductImagesList(List<ProductImage> productImagesList) {
        this.productImagesList = productImagesList;
        if (CollectionUtils.isNotEmpty(productImagesList))
            this.setProductImages(JsonUtils.toJson(productImagesList));
    }

    public List<ParameterValue> getParameterValuesList() {
        return parameterValuesList;
    }

    public void setParameterValuesList(List<ParameterValue> parameterValuesList) {
        this.parameterValuesList = parameterValuesList;
        if (CollectionUtils.isNotEmpty(parameterValuesList))
            this.setParameterValues(JsonUtils.toJson(parameterValuesList));
    }

    public List<SpecificationItem> getSpecificationItemsList() {
        return specificationItemsList;
    }

    public void setSpecificationItemsList(List<SpecificationItem> specificationItemsList) {
        this.specificationItemsList = specificationItemsList;
        if (CollectionUtils.isNotEmpty(specificationItemsList))
            this.setSpecificationItems(JsonUtils.toJson(specificationItemsList));
    }

    public Set<TPromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<TPromotion> promotions) {
        this.promotions = promotions;
    }

    public Set<TTag> getTags() {
        return tags;
    }

    public void setTags(Set<TTag> tags) {
        this.tags = tags;
    }

    public Set<TProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<TProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TGoods (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(attributeValue0);
        sb.append(", ").append(attributeValue1);
        sb.append(", ").append(attributeValue10);
        sb.append(", ").append(attributeValue11);
        sb.append(", ").append(attributeValue12);
        sb.append(", ").append(attributeValue13);
        sb.append(", ").append(attributeValue14);
        sb.append(", ").append(attributeValue15);
        sb.append(", ").append(attributeValue16);
        sb.append(", ").append(attributeValue17);
        sb.append(", ").append(attributeValue18);
        sb.append(", ").append(attributeValue19);
        sb.append(", ").append(attributeValue2);
        sb.append(", ").append(attributeValue3);
        sb.append(", ").append(attributeValue4);
        sb.append(", ").append(attributeValue5);
        sb.append(", ").append(attributeValue6);
        sb.append(", ").append(attributeValue7);
        sb.append(", ").append(attributeValue8);
        sb.append(", ").append(attributeValue9);
        sb.append(", ").append(caption);
        sb.append(", ").append(generateMethod);
        sb.append(", ").append(hits);
        sb.append(", ").append(image);
        sb.append(", ").append(introduction);
        sb.append(", ").append(isDelivery);
        sb.append(", ").append(isList);
        sb.append(", ").append(isMarketable);
        sb.append(", ").append(isTop);
        sb.append(", ").append(keyword);
        sb.append(", ").append(marketPrice);
        sb.append(", ").append(memo);
        sb.append(", ").append(monthHits);
        sb.append(", ").append(monthHitsDate);
        sb.append(", ").append(monthSales);
        sb.append(", ").append(monthSalesDate);
        sb.append(", ").append(name);
        sb.append(", ").append(parameterValues);
        sb.append(", ").append(price);
        sb.append(", ").append(productImages);
        sb.append(", ").append(sales);
        sb.append(", ").append(score);
        sb.append(", ").append(scoreCount);
        sb.append(", ").append(seoDescription);
        sb.append(", ").append(seoKeywords);
        sb.append(", ").append(seoTitle);
        sb.append(", ").append(sn);
        sb.append(", ").append(specificationItems);
        sb.append(", ").append(totalScore);
        sb.append(", ").append(type);
        sb.append(", ").append(unit);
        sb.append(", ").append(weekHits);
        sb.append(", ").append(weekHitsDate);
        sb.append(", ").append(weekSales);
        sb.append(", ").append(weekSalesDate);
        sb.append(", ").append(weight);
        sb.append(", ").append(brand);
        sb.append(", ").append(productCategory);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TGoods that = (TGoods) o;

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

    public boolean isNew() {
        return getId() == null;
    }

    /**
     * 获取路径
     *
     * @return 路径
     */
    @Transient
    public String getPath() {
        TemplateConfig templateConfig = SystemUtils.getTemplateConfig("goodsContent");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("goods", this);
        return templateConfig.getRealStaticPath(model);
    }

    /**
     * 获取URL
     *
     * @return URL
     */
    @Transient
    public String getUrl() {
        Setting setting = SystemUtils.getSetting();
        return setting.getSiteUrl() + getPath();
    }

    /**
     * 是否存在规格
     *
     * @return 是否存在规格
     */
    @Transient
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationItemsList());
    }

    /**
     * 设置属性值
     *
     * @param attribute
     *            属性
     * @param attributeValue
     *            属性值
     */
    @Transient
    public void setAttributeValue(TAttribute attribute, String attributeValue) {
        if (attribute == null || attribute.getPropertyIndex() == null) {
            return;
        }

        try {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
            PropertyUtils.setProperty(this, propertyName, attributeValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 移除所有属性值
     */
    @Transient
    public void removeAttributeValue() {
        for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
            try {
                PropertyUtils.setProperty(this, propertyName, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    @Transient
    public String getThumbnail() {
        if (CollectionUtils.isEmpty(getProductImagesList())) {
            return null;
        }
        return getProductImagesList().get(0).getThumbnail();
    }
    /**
     * 获取默认商品
     *
     * @return 默认商品
     */
    @Transient
    public TProduct getDefaultProduct() {
        return (TProduct) CollectionUtils.find(getProducts(), new Predicate() {
            public boolean evaluate(Object object) {
                TProduct product = (TProduct) object;
                return product != null && product.getIsDefault();
            }
        });
    }

    /**
     * 获取规格项条目ID
     *
     * @return 规格项条目ID
     */
    @Transient
    public List<Integer> getSpecificationItemEntryIds() {
        List<Integer> specificationItemEntryIds = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(getSpecificationItemsList())) {
            for (SpecificationItem specificationItem : getSpecificationItemsList()) {
                if (CollectionUtils.isNotEmpty(specificationItem.getEntries())) {
                    for (SpecificationItem.Entry entry : specificationItem.getEntries()) {
                        specificationItemEntryIds.add(entry.getId());
                    }
                }
            }
            Collections.sort(specificationItemEntryIds);
        }
        return specificationItemEntryIds;
    }

    /**
     * 获取属性值
     *
     * @param attribute
     *            属性
     * @return 属性值
     */
    public String getAttributeValue(TAttribute attribute) {
        if (attribute == null || attribute.getPropertyIndex() == null) {
            return null;
        }

        try {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
            return (String) PropertyUtils.getProperty(this, propertyName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 判断促销是否有效
     *
     * @param promotion
     *            促销
     * @return 促销是否有效
     */
    public boolean isValid(TPromotion promotion) {
        if (!TGoods.Type.general.equals(getType()) || promotion == null || !promotion.hasBegun() || promotion.hasEnded() || CollectionUtils.isEmpty(promotion.getMemberRanks())) {
            return false;
        }
        if (getValidPromotions().contains(promotion)) {
            return true;
        }
        return false;
    }

    /**
     * 获取有效促销
     *
     * @return 有效促销
     */
    @SuppressWarnings("unchecked")
    @Transient
    public Set<TPromotion> getValidPromotions() {
        if (!TGoods.Type.general.equals(getType()) || CollectionUtils.isEmpty(getPromotions())) {
            return Collections.emptySet();
        }

        return new HashSet<TPromotion>(CollectionUtils.select(getPromotions(), new Predicate() {
            public boolean evaluate(Object object) {
                TPromotion promotion = (TPromotion) object;
                return promotion != null && promotion.hasBegun() && !promotion.hasEnded() && CollectionUtils.isNotEmpty(promotion.getMemberRanks());
            }
        }));
    }
}
