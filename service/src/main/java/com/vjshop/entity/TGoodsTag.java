
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 商品-标签 关联
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TGoodsTag implements Serializable {

    private static final long serialVersionUID = -1347090203;

    private Long goods;
    private Long tags;

    public TGoodsTag() {}

    public TGoodsTag(TGoodsTag value) {
        this.goods = value.goods;
        this.tags = value.tags;
    }

    public TGoodsTag(
        Long goods,
        Long tags
    ) {
        this.goods = goods;
        this.tags = tags;
    }

    public Long getGoods() {
        return this.goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }

    public Long getTags() {
        return this.tags;
    }

    public void setTags(Long tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TGoodsTag (");

        sb.append(goods);
        sb.append(", ").append(tags);

        sb.append(")");
        return sb.toString();
    }
}
