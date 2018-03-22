
package com.vjshop.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity - 库存记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TStockLog implements Serializable {

    private static final long serialVersionUID = 446470377;

    /**
     * 设置操作员
     *
     * @param operator
     *            操作员
     */
    @Transient
    public void setOperator(TAdmin operator) {
        setOperator(operator != null ? operator.getUsername() : null);
    }

    private Long      id;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private Long      version;
    private Integer   inQuantity;
    private String    memo;
    private String    operator;
    private Integer   outQuantity;
    private Integer   stock;
    private Integer   type;
    private Long product;
    private TProduct      productVO;

    /**
     * 类型
     */
    public enum Type {

        /** 入库 */
        stockIn(0),

        /** 出库 */
        stockOut(1);

        private Integer typeValue;


        public Integer getTypeValue() {
            return typeValue;
        }

        public void setTypeValue(Integer typeValue) {
            this.typeValue = typeValue;
        }

        Type(Integer typeValue) {
            this.typeValue = typeValue;
        }
        public static TStockLog.Type valueOf(int ordinal){
            if (ordinal < 0 || ordinal >= values().length) {
                throw new IndexOutOfBoundsException("Invalid ordinal");
            }
            return values()[ordinal];
        }
    }

    public TStockLog() {}

    public TStockLog(TStockLog value) {
        this.id = value.id;
        this.createDate = value.createDate;
        this.modifyDate = value.modifyDate;
        this.version = value.version;
        this.inQuantity = value.inQuantity;
        this.memo = value.memo;
        this.operator = value.operator;
        this.outQuantity = value.outQuantity;
        this.stock = value.stock;
        this.type = value.type;
        this.product = value.product;
    }

    public TStockLog(
        Long      id,
        Timestamp createDate,
        Timestamp modifyDate,
        Long      version,
        Integer   inQuantity,
        String    memo,
        String    operator,
        Integer   outQuantity,
        Integer   stock,
        Integer   type,
        Long      product
    ) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.version = version;
        this.inQuantity = inQuantity;
        this.memo = memo;
        this.operator = operator;
        this.outQuantity = outQuantity;
        this.stock = stock;
        this.type = type;
        this.product = product;
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

    public Integer getInQuantity() {
        return this.inQuantity;
    }

    public void setInQuantity(Integer inQuantity) {
        this.inQuantity = inQuantity;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getOutQuantity() {
        return this.outQuantity;
    }

    public void setOutQuantity(Integer outQuantity) {
        this.outQuantity = outQuantity;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProduct() {
        return this.product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public TProduct getProductVO() {
        return productVO;
    }

    public void setProductVO(TProduct productVO) {
        this.productVO = productVO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TStockLog (");

        sb.append(id);
        sb.append(", ").append(createDate);
        sb.append(", ").append(modifyDate);
        sb.append(", ").append(version);
        sb.append(", ").append(inQuantity);
        sb.append(", ").append(memo);
        sb.append(", ").append(operator);
        sb.append(", ").append(outQuantity);
        sb.append(", ").append(stock);
        sb.append(", ").append(type);
        sb.append(", ").append(product);

        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TStockLog that = (TStockLog) o;

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
