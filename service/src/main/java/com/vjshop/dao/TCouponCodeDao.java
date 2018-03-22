
package com.vjshop.dao;

import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.generated.db.tables.TCoupon;
import com.vjshop.generated.db.tables.TCouponCode;
import com.vjshop.generated.db.tables.records.TCouponCodeRecord;
import com.vjshop.util.JooqUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Configuration;
import org.jooq.JoinType;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Dao - 优惠码
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Repository
public class TCouponCodeDao extends JooqBaseDao<TCouponCodeRecord, com.vjshop.entity.TCouponCode, Long> {

    public TCouponCodeDao() {
        super(TCouponCode.T_COUPON_CODE, com.vjshop.entity.TCouponCode.class);
    }

    @Autowired
    public TCouponCodeDao(Configuration configuration) {
        super(TCouponCode.T_COUPON_CODE, com.vjshop.entity.TCouponCode.class, configuration);
    }

    @Override
    protected Long getId(com.vjshop.entity.TCouponCode object) {
        return object.getId();
    }

    /**
     * 判断优惠码是否存在
     *
     * @param code
     *            号码(忽略大小写)
     * @return 优惠码是否存在
     */
    public boolean codeExists(String code){
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        return getDslContext().fetchExists(TCouponCode.T_COUPON_CODE, TCouponCode.T_COUPON_CODE.CODE.eq(code));
    }

    /**
     * 根据号码查找优惠码
     *
     * @param code
     *            号码(忽略大小写)
     * @return 优惠码，若不存在则返回null
     */
    public com.vjshop.entity.TCouponCode findByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return fetchOne(TCouponCode.T_COUPON_CODE.CODE, code);
    }

    /**
     * 查找优惠码分页
     *
     * @param memberId
     *            会员ID
     * @param pageable
     *            分页信息
     * @return 优惠码分页
     */
    public Page<com.vjshop.entity.TCouponCode> findPage(Long memberId, Pageable pageable){
        SelectQuery query = getDslContext().select(TCouponCode.T_COUPON_CODE.fields()).from(TCouponCode.T_COUPON_CODE).getQuery();

        query.addSelect(JooqUtils.getFields(TCoupon.T_COUPON, "couponInfo"));
        query.addJoin(TCoupon.T_COUPON, JoinType.LEFT_OUTER_JOIN, TCoupon.T_COUPON.ID.eq(TCouponCode.T_COUPON_CODE.COUPON));

        if (memberId != null){
            query.addConditions(TCouponCode.T_COUPON_CODE.MEMBER.eq(memberId));
        }
        return findPage(query, pageable);
    }

    /**
     * 删除未使用的优惠码
     *
     * @param couponId
     *          优惠券ID
     * @return 删除条数
     */
    public int deleteByCouponId(Long couponId){
        return getDslContext().deleteFrom(TCouponCode.T_COUPON_CODE)
                .where(TCouponCode.T_COUPON_CODE.COUPON.eq(couponId)
                        .and(TCouponCode.T_COUPON_CODE.IS_USED.eq(false)))
                .execute();
    }

    /**
     * 删除未使用的优惠码
     *
     * @param couponIds
     *          优惠券ID
     * @return 删除条数
     */
    public int deleteByCouponId(Long... couponIds){
        return getDslContext().deleteFrom(TCouponCode.T_COUPON_CODE)
                .where(TCouponCode.T_COUPON_CODE.COUPON.in(couponIds)
                        .and(TCouponCode.T_COUPON_CODE.IS_USED.eq(false)))
                .execute();
    }

    /**
     * 查找优惠码数量
     *
     * @param couponId
     *            优惠券ID
     * @param memberId
     *            会员ID
     * @param hasBegun
     *            是否已开始
     * @param hasExpired
     *            是否已过期
     * @param isUsed
     *            是否已使用
     * @return 优惠码数量
     */
    public Long count(Long couponId, Long memberId, Boolean hasBegun, Boolean hasExpired, Boolean isUsed){
        SelectQuery query = getDslContext().selectCount().from(TCouponCode.T_COUPON_CODE).getQuery();
        if (couponId != null){
            query.addConditions(TCouponCode.T_COUPON_CODE.COUPON.eq(couponId));
        }
        if (memberId != null){
            query.addConditions(TCouponCode.T_COUPON_CODE.MEMBER.eq(memberId));
        }
        if (hasBegun != null || hasExpired != null){
            query.addJoin(TCoupon.T_COUPON, JoinType.LEFT_OUTER_JOIN, TCoupon.T_COUPON.ID.eq(TCouponCode.T_COUPON_CODE.COUPON));
        }
        if (hasBegun != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (hasBegun){
                query.addConditions(TCoupon.T_COUPON.BEGIN_DATE.isNull().or(TCoupon.T_COUPON.BEGIN_DATE.le(now)));
            } else {
                query.addConditions(TCoupon.T_COUPON.BEGIN_DATE.isNotNull().and(TCoupon.T_COUPON.BEGIN_DATE.gt(now)));
            }
        }
        if (hasExpired != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (hasExpired){
                query.addConditions(TCoupon.T_COUPON.END_DATE.isNotNull().and(TCoupon.T_COUPON.END_DATE.le(now)));
            } else {
                query.addConditions(TCoupon.T_COUPON.END_DATE.isNull().or(TCoupon.T_COUPON.END_DATE.gt(now)));
            }
        }
        if (isUsed != null){
            query.addConditions(TCouponCode.T_COUPON_CODE.IS_USED.eq(isUsed));
        }
        return (Long) query.fetchOne(0, Long.class);
    }

}
