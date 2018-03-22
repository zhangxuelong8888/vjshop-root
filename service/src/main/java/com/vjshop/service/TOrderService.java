
package com.vjshop.service;

import com.vjshop.Filter;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.entity.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service - 订单
 *
 * @author VJSHOP Team
 * @version 4.0
 */
public interface TOrderService extends TBaseService<TOrder, Long> {

    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    TOrder findBySn(String sn);

    /**
     * 查找订单
     *
     * @param type             类型
     * @param status           状态
     * @param memberId         会员ID
     * @param goodsId          货品ID
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @param count            数量
     * @param filters          筛选
     * @param orders           排序
     * @return 订单
     */
    List<TOrder> findList(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                         List<com.vjshop.Order> orders);

    /**
     * 查找订单分页
     *
     * @param type             类型
     * @param status           状态
     * @param memberId         会员ID
     * @param goodsId          货品ID
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @param pageable         分页信息
     * @return 订单分页
     */
    Page<TOrder> findPage(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable);

    /**
     * 查询订单数量
     *
     * @param type             类型
     * @param status           状态
     * @param memberId         会员ID
     * @param goodsId          货品ID
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @return 订单数量
     */
    Long count(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

    /**
     * 计算税金
     *
     * @param price             商品价格
     * @param promotionDiscount 促销折扣
     * @param couponDiscount    优惠券折扣
     * @param offsetAmount      调整金额
     * @return 税金
     */
    BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

    /**
     * 计算税金
     *
     * @param order 订单
     * @return 税金
     */
    BigDecimal calculateTax(TOrder order);

    /**
     * 计算订单金额
     *
     * @param price             商品价格
     * @param fee               支付手续费
     * @param freight           运费
     * @param tax               税金
     * @param promotionDiscount 促销折扣
     * @param couponDiscount    优惠券折扣
     * @param offsetAmount      调整金额
     * @return 订单金额
     */
    BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

    /**
     * 计算订单金额
     *
     * @param order 订单
     * @return 订单金额
     */
    BigDecimal calculateAmount(TOrder order);

    /**
     * 判断订单是否锁定
     *
     * @param order    订单
     * @param admin    管理员
     * @param autoLock 是否自动加锁
     * @return 订单是否锁定
     */
    boolean isLocked(TOrder order, TAdmin admin, boolean autoLock);

    /**
     * 判断订单是否锁定
     *
     * @param order    订单
     * @param member   会员
     * @param autoLock 是否自动加锁
     * @return 订单是否锁定
     */
    boolean isLocked(TOrder order, TMember member, boolean autoLock);

    /**
     * 订单锁定
     *
     * @param order 订单
     * @param admin 管理员
     */
    void lock(TOrder order, TAdmin admin);

    /**
     * 订单锁定
     *
     * @param order  订单
     * @param member 会员
     */
    void lock(TOrder order, TMember member);

    /**
     * 过期订单优惠码使用撤销
     */
    void undoExpiredUseCouponCode();

    /**
     * 过期订单积分兑换撤销
     */
    void undoExpiredExchangePoint();

    /**
     * 释放过期订单已分配库存
     */
    void releaseExpiredAllocatedStock();

    /**
     * 订单生成
     *
     * @param type           类型
     * @param cart           购物车
     * @param receiver       收货地址
     * @param paymentMethod  支付方式
     * @param shippingMethod 配送方式
     * @param couponCode     优惠码
     * @param title        发票
     * @param content        发票
     * @param balance        使用余额
     * @param memo           附言
     * @return 订单
     */
    TOrder generate(TOrder.Type type, TCart cart, TReceiver receiver, TPaymentMethod paymentMethod, TShippingMethod shippingMethod, TCouponCode couponCode, String title, String content, BigDecimal balance, String memo);

    /**
     * 订单创建
     *
     * @param type           类型
     * @param cart           购物车
     * @param receiver       收货地址
     * @param paymentMethod  支付方式
     * @param shippingMethod 配送方式
     * @param couponCode     优惠码
     * @param title        发票
     * @param content        发票
     * @param balance        使用余额
     * @param memo           附言
     * @return 订单
     */
    TOrder create(TOrder.Type type, TCart cart, TReceiver receiver, TPaymentMethod paymentMethod, TShippingMethod shippingMethod, TCouponCode couponCode, String title,String content, BigDecimal balance, String memo);

    /**
     * 订单更新
     *
     * @param order    订单
     * @param operator 操作员
     */
    void update(TOrder order, TAdmin operator);

    /**
     * 订单取消
     *
     * @param order 订单
     */
    void cancel(TOrder order);

    /**
     * 订单审核
     *
     * @param order    订单
     * @param passed   是否审核通过
     * @param operator 操作员
     */
    void review(TOrder order, boolean passed, TAdmin operator);

    /**
     * 订单收款
     *
     * @param order    订单
     * @param payment  收款单
     * @param operator 操作员
     */
    void payment(TOrder order, TPayment payment, TAdmin operator);

    /**
     * 订单退款
     *
     * @param order    订单
     * @param refunds  退款单
     * @param operator 操作员
     */
    void refunds(TOrder order, TRefunds refunds, TAdmin operator);

    /**
     * 订单发货
     *
     * @param order    订单
     * @param shipping 发货单
     * @param operator 操作员
     */
    void shipping(TOrder order, TShipping shipping, TAdmin operator);

    /**
     * 订单退货
     *
     * @param order    订单
     * @param returns  退货单
     * @param operator 操作员
     */
    void returns(TOrder order, TReturns returns, TAdmin operator);

    /**
     * 订单收货
     *
     * @param order    订单
     * @param operator 操作员
     */
    void receive(TOrder order, TAdmin operator);

    /**
     * 订单完成
     *
     * @param order    订单
     * @param operator 操作员
     */
    void complete(TOrder order, TAdmin operator);

    /**
     * 订单失败
     *
     * @param order    订单
     * @param operator 操作员
     */
    void fail(TOrder order, TAdmin operator);

    /**********************************************************************************************************************************************************************************************************/
    TOrder findDetailById(Long id);

}