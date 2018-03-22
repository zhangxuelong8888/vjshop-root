
package com.vjshop.entity;

import com.fasterxml.jackson.databind.JavaType;
import com.vjshop.Setting;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Entity - 商品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public class TProduct implements Serializable {

    private final JavaType specificationValueJavaType = JsonUtils.getCollectionType(List.class,SpecificationValue.class);

    private static final long serialVersionUID = -1533033911;

    /**
     * 普通商品验证组
     */
    public interface General extends Default {

    }

    /**
     * 兑换商品验证组
     */
    public interface Exchange extends Default {

    }

    /**
     * 赠品验证组
     */
    public interface Gift extends Default {

    }

    private Long       id;
    private Timestamp  createDate;
    private Timestamp  modifyDate;
    private Long       version;
    private Integer    allocatedStock;
    private BigDecimal cost;
    private Long       exchangePoint;
    private Boolean    isDefault;
    private BigDecimal marketPrice;
    private BigDecimal price;
    private Long       rewardPoint;
    private String     sn;
    private String     specificationValues;
    private Integer    stock;
    private Long       goods;

    private TGoods     goodsVO;
    /** 规格值 */
    private List<SpecificationValue> specificationValuesList = new ArrayList<SpecificationValue>();

    public TProduct() {}

    public TProduct(TProduct value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.allocatedStock = value.allocatedStock;
        this.cost = value.cost;
        this.exchangePoint = value.exchangePoint;
        this.isDefault = value.isDefault;
        this.marketPrice = value.marketPrice;
        this.price = value.price;
        this.rewardPoint = value.rewardPoint;
        this.sn = value.sn;
        this.specificationValues = value.specificationValues;
        this.stock = value.stock;
        this.goods = value.goods;
    }

    public TProduct(
        Long       id,
        Timestamp  createDate,
        Timestamp  modifyDate,
        Long       version,
        Integer    allocatedStock,
        BigDecimal cost,
        Long       exchangePoint,
        Boolean    isDefault,
        BigDecimal marketPrice,
        BigDecimal price,
        Long       rewardPoint,
        String     sn,
        String     specificationValues,
        Integer    stock,
        Long       goods
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.allocatedStock = allocatedStock;
        this.cost = cost;
        this.exchangePoint = exchangePoint;
        this.isDefault = isDefault;
        this.marketPrice = marketPrice;
        this.price = price;
        this.rewardPoint = rewardPoint;
        this.sn = sn;
        this.specificationValues = specificationValues;
        this.stock = stock;
        this.goods = goods;

        if(StringUtils.isNotBlank(this.specificationValues)){
            this.specificationValuesList = JsonUtils.toObject(this.specificationValues,specificationValueJavaType);
        }
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

    public Integer getAllocatedStock() {
        return this.allocatedStock;
    }

    public void setAllocatedStock(Integer allocatedStock) {
        this.allocatedStock = allocatedStock;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @NotNull(groups = TProduct.Exchange.class)
    @Min(0)
    public Long getExchangePoint() {
        return this.exchangePoint;
    }

    public void setExchangePoint(Long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    @NotNull
    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getMarketPrice() {
        return this.marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @NotNull(groups = TProduct.General.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Min(0)
    public Long getRewardPoint() {
        return this.rewardPoint;
    }

    public void setRewardPoint(Long rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSpecificationValues() {
        return this.specificationValues;
    }

    public void setSpecificationValues(String specificationValues) {
        this.specificationValues = specificationValues;
    }

    @NotNull(groups = ValidGroup.Save.class)
    @Min(0)
    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getGoods() {
        return goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }


    public TGoods getGoodsVO() {
        return goodsVO;
    }

    public void setGoodsVO(TGoods goodsVO) {
        this.goodsVO = goodsVO;
    }

    public List<SpecificationValue> getSpecificationValuesList() {
        return specificationValuesList;
    }

    public void setSpecificationValuesList(List<SpecificationValue> specificationValuesList) {
        this.specificationValuesList = specificationValuesList;
    }

    public boolean isNew() {
        return getId() == null;
    }
    /**
     * 是否存在规格
     *
     * @return 是否存在规格
     */
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationValuesList());
    }

    /**
     * 获取规格值ID
     *
     * @return 规格值ID
     */
    public List<Integer> getSpecificationValueIds() {
        List<Integer> specificationValueIds = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(getSpecificationValuesList())) {
            for (SpecificationValue specificationValue : getSpecificationValuesList()) {
                specificationValueIds.add(specificationValue.getId());
            }
        }
        return specificationValueIds;
    }

    /**
     * 获取规格
     *
     * @return 规格
     */
    public List<String> getSpecifications() {
        List<String> specifications = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(getSpecificationValuesList())) {
            for (SpecificationValue specificationValue : getSpecificationValuesList()) {
                specifications.add(specificationValue.getValue());
            }
        }
        return specifications;
    }
    /**
     * 获取名称
     *
     * @return 名称
     */
    @Transient
    public String getName() {
        return getGoodsVO() != null ? getGoodsVO().getName() : null;
    }
    /**
     * 获取类型
     *
     * @return 类型
     */
    @Transient
    public Integer getType() {
        return getGoodsVO() != null ? getGoodsVO().getType() : null;
    }


    /**
     * 获取展示图片
     *
     * @return 展示图片
     */
    @Transient
    public String getImage() {
        return getGoodsVO() != null ? getGoodsVO().getImage() : null;
    }

    /**
     * 获取单位
     *
     * @return 单位
     */
    @Transient
    public String getUnit() {
        return getGoodsVO() != null ? getGoodsVO().getUnit() : null;
    }

    /**
     * 获取重量
     *
     * @return 重量
     */
    @Transient
    public Integer getWeight() {
        return getGoodsVO() != null ? getGoodsVO().getWeight() : null;
    }

    /**
     * 获取是否上架
     *
     * @return 是否上架
     */
    @Transient
    public boolean getIsMarketable() {
        return getGoodsVO() != null && BooleanUtils.isTrue(getGoodsVO().getIsMarketable());
    }

    /**
     * 获取是否列出
     *
     * @return 是否列出
     */
    @Transient
    public boolean getIsList() {
        return getGoodsVO() != null && BooleanUtils.isTrue(getGoodsVO().getIsList());
    }

    /**
     * 获取是否置顶
     *
     * @return 是否置顶
     */
    @Transient
    public boolean getIsTop() {
        return getGoodsVO() != null && BooleanUtils.isTrue(getGoodsVO().getIsTop());
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    @Transient
    public boolean getIsDelivery() {
        return getGoodsVO() != null && BooleanUtils.isTrue(getGoodsVO().getIsDelivery());
    }

    /**
     * 获取路径
     *
     * @return 路径
     */
    @Transient
    public String getPath() {
        return getGoodsVO() != null ? getGoodsVO().getPath() : null;
    }

    /**
     * 获取URL
     *
     * @return URL
     */
    @Transient
    public String getUrl() {
        return getGoodsVO() != null ? getGoodsVO().getUrl() : null;
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    @Transient
    public String getThumbnail() {
        return getGoodsVO() != null ? getGoodsVO().getThumbnail() : null;
    }

    /**
     * 获取可用库存
     *
     * @return 可用库存
     */
    @Transient
    public int getAvailableStock() {
        int availableStock = getStock() - getAllocatedStock();
        return availableStock >= 0 ? availableStock : 0;
    }

    /**
     * 获取是否库存警告
     *
     * @return 是否库存警告
     */
    @Transient
    public boolean getIsStockAlert() {
        Setting setting = SystemUtils.getSetting();
        return setting.getStockAlertCount() != null && getAvailableStock() <= setting.getStockAlertCount();
    }

    /**
     * 获取是否缺货
     *
     * @return 是否缺货
     */
    @Transient
    public boolean getIsOutOfStock() {
        return getAvailableStock() <= 0;
    }

    /**
     * 判断促销是否有效
     *
     * @param promotion
     *            促销
     * @return 促销是否有效
     */
    @Transient
    public boolean isValid(TPromotion promotion) {
        return getGoodsVO() != null ? getGoodsVO().isValid(promotion) : false;
    }

    /**
     * 获取有效促销
     *
     * @return 有效促销
     */
    @Transient
    public Set<TPromotion> getValidPromotions() {
        return getGoodsVO() != null ? getGoodsVO().getValidPromotions() : Collections.<TPromotion> emptySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TProduct (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(allocatedStock);
        sb.append(", ").append(cost);
        sb.append(", ").append(exchangePoint);
        sb.append(", ").append(isDefault);
        sb.append(", ").append(marketPrice);
        sb.append(", ").append(price);
        sb.append(", ").append(rewardPoint);
        sb.append(", ").append(sn);
        sb.append(", ").append(specificationValues);
        sb.append(", ").append(stock);
        sb.append(", ").append(goods);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TProduct that = (TProduct) o;

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
