
package com.vjshop.entity;

import java.io.Serializable;

/**
 * Entity - 会员-收藏商品
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TMemberFavoriteGoods implements Serializable {

    private static final long serialVersionUID = -992537947;

    private Long favoriteMembers;
    private Long favoriteGoods;

    public TMemberFavoriteGoods() {}

    public TMemberFavoriteGoods(TMemberFavoriteGoods value) {
        this.favoriteMembers = value.favoriteMembers;
        this.favoriteGoods = value.favoriteGoods;
    }

    public TMemberFavoriteGoods(
        Long favoriteMembers,
        Long favoriteGoods
    ) {
        this.favoriteMembers = favoriteMembers;
        this.favoriteGoods = favoriteGoods;
    }

    public Long getFavoriteMembers() {
        return this.favoriteMembers;
    }

    public void setFavoriteMembers(Long favoriteMembers) {
        this.favoriteMembers = favoriteMembers;
    }

    public Long getFavoriteGoods() {
        return this.favoriteGoods;
    }

    public void setFavoriteGoods(Long favoriteGoods) {
        this.favoriteGoods = favoriteGoods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TMemberFavoriteGoods (");

        sb.append(favoriteMembers);
        sb.append(", ").append(favoriteGoods);

        sb.append(")");
        return sb.toString();
    }
}
