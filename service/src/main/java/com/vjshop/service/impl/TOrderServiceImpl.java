package com.vjshop.service.impl;

import com.vjshop.Filter;
import com.vjshop.Page;
import com.vjshop.Pageable;
import com.vjshop.Setting;
import com.vjshop.dao.*;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TOrderRecord;
import com.vjshop.service.*;
import com.vjshop.util.JsonUtils;
import com.vjshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service - 订单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("tOrderServiceImpl")
public class TOrderServiceImpl extends TBaseServiceImpl<TOrderRecord,TOrder, Long> implements TOrderService {

	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;

	@Autowired
	private TOrderDao tOrderDao;
	@Autowired
	private TMemberService tMemberService;
	@Autowired
	private TShippingMethodService tShippingMethodService;
	@Autowired
	private TAreaService tAreaService;
	@Autowired
	private TPaymentMethodService tPaymentMethodService;
	@Autowired
	private TCouponCodeService tCouponCodeService;
	@Autowired
	private TOrderItemService tOrderItemService;
	@Autowired
	private TOrderCouponService tOrderCouponService;
	@Autowired
	private TCouponService tCouponService;
	@Autowired
	private TPaymentLogService tPaymentLogService;
	@Autowired
	private TPaymentService tPaymentService;
	@Autowired
	private TRefundsService tRefundsService;
	@Autowired
	private TShippingService tShippingService;
	@Autowired
	private TReturnsService tReturnsService;
	@Autowired
	private TOrderLogService tOrderLogService;
	@Autowired
	private TProductService tProductService;
	@Autowired
	private TGoodsService tGoodsService;
	@Autowired
	private TSnService tSnService;
	@Autowired
	private TCartService tCartService;

	@Transactional(readOnly = true)
	public TOrder findBySn(String sn) {
		return tOrderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<TOrder> findList(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<com.vjshop.Order> orders) {
		List<TOrder> list = tOrderDao.findList(type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, count, filters, orders);
		for (TOrder order : list) {
			order.setOrderItemsList(tOrderItemService.findListByOrderId(order.getId()));
		}
		return list;
	}

	@Transactional(readOnly = true)
	public Page<TOrder> findPage(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		Page<TOrder> tOrderPage = this.tOrderDao.findPage(type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, pageable);
		List<TOrder> newContent = new ArrayList<>(tOrderPage.getContent().size());
		for(TOrder tOrder : tOrderPage.getContent()){
			tOrder.setMemberVO(this.tMemberService.find(tOrder.getMember()));
			tOrder.setOrderItemsList(tOrderItemService.findListByOrderId(tOrder.getId()));
			newContent.add(tOrder);
		}

		return new Page<TOrder>(newContent,tOrderPage.getTotal(),pageable);
	}

	@Transactional(readOnly = true)
	public Long count(TOrder.Type type, TOrder.Status status, Long memberId, Long goodsId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		return tOrderDao.count(type, status, memberId, goodsId, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price);
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsTaxPriceEnabled()) {
			return BigDecimal.ZERO;
		}
		BigDecimal amount = price;
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		BigDecimal tax = amount.multiply(new BigDecimal(String.valueOf(setting.getTaxRate())));
		return tax.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(tax) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(TOrder order) {
		Assert.notNull(order);

		if (StringUtils.isBlank(order.getInvoiceContent()) && StringUtils.isBlank(order.getInvoiceTitle())) {
			return BigDecimal.ZERO;
		}
		return calculateTax(order.getPrice(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price);
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(fee == null || fee.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(freight == null || freight.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(tax == null || tax.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

		Setting setting = SystemUtils.getSetting();
		BigDecimal amount = price;
		if (fee != null) {
			amount = amount.add(fee);
		}
		if (freight != null) {
			amount = amount.add(freight);
		}
		if (tax != null) {
			amount = amount.add(tax);
		}
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		return amount.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(amount) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(TOrder order) {
		Assert.notNull(order);

		return calculateAmount(order.getPrice(), order.getFee(), order.getFreight(), order.getTax(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	public boolean isLocked(TOrder order, TAdmin admin, boolean autoLock) {
		Assert.notNull(order);
		Assert.notNull(admin);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
		if (autoLock && !isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
			order.setLockKey(admin.getLockKey());
			Date lockExpire = DateUtils.addSeconds(new Date(), TOrder.LOCK_EXPIRE);
			order.setLockExpire(new Timestamp(lockExpire.getTime()));
			order.setModifyDate(new Timestamp(System.currentTimeMillis()));
			this.updateOrder(order);
		}
		return isLocked;
	}

	public boolean isLocked(TOrder order, TMember member, boolean autoLock) {
		Assert.notNull(order);
		Assert.notNull(member);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
		if (autoLock && !isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
			order.setLockKey(member.getLockKey());
			Date lockExpire = DateUtils.addSeconds(new Date(), TOrder.LOCK_EXPIRE);
			order.setLockExpire(new Timestamp(lockExpire.getTime()));
			order.setModifyDate(new Timestamp(System.currentTimeMillis()));
			this.updateOrder(order);
		}
		return isLocked;
	}

	public void lock(TOrder order, TAdmin admin) {
		Assert.notNull(order);
		Assert.notNull(admin);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
		if (!isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
			order.setLockKey(admin.getLockKey());
			Date lockExpire = DateUtils.addSeconds(new Date(), TOrder.LOCK_EXPIRE);
			order.setLockExpire(new Timestamp(lockExpire.getTime()));
			order.setModifyDate(new Timestamp(System.currentTimeMillis()));
			this.updateOrder(order);
		}
	}

	public void lock(TOrder order, TMember member) {
		Assert.notNull(order);
		Assert.notNull(member);

		boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
		if (!isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
			order.setLockKey(member.getLockKey());
			Date lockExpire = DateUtils.addSeconds(new Date(), TOrder.LOCK_EXPIRE);
			order.setLockExpire(new Timestamp(lockExpire.getTime()));
			order.setModifyDate(new Timestamp(System.currentTimeMillis()));
			this.updateOrder(order);
		}
	}


    @Transactional
	public void undoExpiredUseCouponCode() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		while (true) {
			List<TOrder> orders = this.tOrderDao.findList(null, null, null, null, null, null, true, null, null, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (TOrder order : orders) {
					undoUseCouponCode(order);
					order.setModifyDate(now);
				}
				this.updateOrder(orders);
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

    @Transactional
	public void undoExpiredExchangePoint() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		while (true) {
			List<TOrder> orders = this.tOrderDao.findList(null, null, null, null, null, null, null, true, null, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (TOrder order : orders) {
					undoExchangePoint(order);
					order.setModifyDate(now);
				}
				this.updateOrder(orders);
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

    @Transactional
	public void releaseExpiredAllocatedStock() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		while (true) {
			List<TOrder> orders = this.tOrderDao.findList(null, null, null, null, null, null, null, null, true, true, 100, null, null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (TOrder order : orders) {
					releaseAllocatedStock(order);
					order.setModifyDate(now);
				}
				this.updateOrder(orders);
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Transactional
	public TOrder generate(TOrder.Type type, TCart cart, TReceiver receiver, TPaymentMethod paymentMethod, TShippingMethod shippingMethod, TCouponCode couponCode, String title, String content, BigDecimal balance, String memo) {
		Assert.notNull(type);
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.state(!cart.isEmpty());

		Setting setting = SystemUtils.getSetting();
		TMember member = this.tMemberService.find(cart.getMember());

		TOrder order = new TOrder();
		order.setType(type.ordinal());
		order.setPrice(cart.getPrice(true));
		order.setFee(BigDecimal.ZERO);
		order.setPromotionDiscount(cart.getDiscount(true));
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(cart.getEffectiveRewardPoint());
		order.setExchangePoint(cart.getExchangePoint());
		order.setWeight(cart.getWeight());
		order.setQuantity(cart.getQuantity());
		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);
		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoiceTitle(setting.getIsInvoiceEnabled() ? title:null);
		order.setInvoiceContent(setting.getIsInvoiceEnabled() ? content:null);
		order.setPaymentMethod(paymentMethod != null ? paymentMethod.getId() : null);
		order.setMember(member.getId());
		order.setPromotionNames(JsonUtils.toJson(cart.getPromotionNames()));
		order.setCouponsList(new ArrayList<TCoupon>(cart.getCoupons()));

		if (shippingMethod != null && paymentMethod != null && shippingMethod.isSupported(paymentMethod.getId()) && cart.getIsDelivery()) {
			order.setFreight(!cart.isFreeShipping() ? this.tShippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
			order.setShippingMethod(shippingMethod.getId());
		} else {
			order.setFreight(BigDecimal.ZERO);
			order.setShippingMethod(null);
		}

		if (couponCode != null && cart.isCouponAllowed() && cart.isValid(couponCode)) {
			BigDecimal couponDiscount = cart.getEffectivePrice(true).subtract(couponCode.getCouponInfo().calculatePrice(cart.getEffectivePrice(true), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode.getId());
		} else {
			order.setCouponDiscount(BigDecimal.ZERO);
			order.setCouponCode(null);
		}

		order.setTax(calculateTax(order));
		order.setAmount(calculateAmount(order));

		if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(member.getBalance()) <= 0 && balance.compareTo(order.getAmount()) <= 0) {
			order.setAmountPaid(balance);
		} else {
			order.setAmountPaid(BigDecimal.ZERO);
		}

		if (cart.getIsDelivery() && receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}

		List<TOrderItem> orderItems = order.getOrderItemsList();
		for (TCartItem cartItem : cart.getCartItems()) {
			TProduct product = cartItem.getProductVO();
			if (product != null) {
				TOrderItem orderItem = new TOrderItem();
				orderItem.setSn(product.getSn());
				orderItem.setName(product.getName());
				orderItem.setType(product.getType());
				orderItem.setPrice(cartItem.getPrice());
				orderItem.setWeight(product.getWeight());
				orderItem.setIsDelivery(product.getIsDelivery());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setProduct(cartItem.getProduct());
				orderItem.setOrders(order.getId());
				orderItem.setSpecifications(JsonUtils.toJson(product.getSpecifications()));
				orderItems.add(orderItem);
			}
		}

		for (TProduct gift : cart.getGifts()) {
			TOrderItem orderItem = new TOrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift.getId());
			orderItem.setOrders(order.getId());
			orderItem.setSpecifications(JsonUtils.toJson(gift.getSpecifications()));
			orderItems.add(orderItem);
		}

		return order;
	}

    @Transactional
	public TOrder create(TOrder.Type type, TCart cart, TReceiver receiver, TPaymentMethod paymentMethod, TShippingMethod shippingMethod, TCouponCode couponCode, String title,String content, BigDecimal balance, String memo) {
		Assert.notNull(type);
		Assert.notNull(cart);
		Assert.notNull(cart.getMemberVO());
		Assert.state(!cart.isEmpty());
		if (cart.getIsDelivery()) {
			Assert.notNull(receiver);
			Assert.notNull(shippingMethod);
			Assert.state(paymentMethod == null || shippingMethod.isSupported(paymentMethod.getId()));
		} else {
			Assert.isNull(receiver);
			Assert.isNull(shippingMethod);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());


		for (TCartItem cartItem : cart.getCartItems()) {
			TProduct product = cartItem.getProductVO();
			if (product == null || !product.getIsMarketable() || cartItem.getQuantity() > product.getAvailableStock()) {
				throw new IllegalArgumentException();
			}
		}

		for (TProduct gift : cart.getGifts()) {
			if (!gift.getIsMarketable() || gift.getIsOutOfStock()) {
				throw new IllegalArgumentException();
			}
		}

		Setting setting = SystemUtils.getSetting();
		TMember member = this.tMemberService.find(cart.getMember());

		TOrder order = new TOrder();
		order.setSn(this.tSnService.generate(TSn.Type.order));
		order.setType(type.ordinal());
		order.setPrice(cart.getPrice(true));
		order.setFee(BigDecimal.ZERO);
		order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ? this.tShippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
		order.setPromotionDiscount(cart.getDiscount(true));
		order.setOffsetAmount(BigDecimal.ZERO);
		order.setAmountPaid(BigDecimal.ZERO);
		order.setRefundAmount(BigDecimal.ZERO);
		order.setRewardPoint(cart.getEffectiveRewardPoint());
		order.setExchangePoint(cart.getExchangePoint());
		order.setWeight(cart.getWeight());
		order.setQuantity(cart.getQuantity());
		order.setShippedQuantity(0);
		order.setReturnedQuantity(0);
		if (cart.getIsDelivery()) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}
		order.setMemo(memo);
		order.setIsUseCouponCode(false);
		order.setIsExchangePoint(false);
		order.setIsAllocatedStock(false);
		order.setInvoiceTitle(setting.getIsInvoiceEnabled() ? title : null);
		order.setInvoiceContent(setting.getIsInvoiceEnabled() ? content : null);
		order.setShippingMethod(shippingMethod.getId());
		order.setShippingMethodVO(shippingMethod);
		order.setMember(member.getId());
		order.setPromotionNames(JsonUtils.toJson(cart.getPromotionNames()));
		order.setCouponsList(new ArrayList<TCoupon>(cart.getCoupons()));

		if (couponCode != null) {
			if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
				throw new IllegalArgumentException();
			}
			BigDecimal couponDiscount = cart.getEffectivePrice(true).subtract(couponCode.getCouponInfo().calculatePrice(cart.getEffectivePrice(true), cart.getProductQuantity()));
			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
			order.setCouponCode(couponCode.getId());
			order.setCouponCodeVO(this.tCouponCodeService.findDetailById(couponCode.getId()));
			useCouponCode(order);
		} else {
			order.setCouponDiscount(BigDecimal.ZERO);
		}

		order.setTax(calculateTax(order));
		order.setAmount(calculateAmount(order));

		if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0 || balance.compareTo(order.getAmount()) > 0)) {
			throw new IllegalArgumentException();
		}
		BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
			if (paymentMethod == null) {
				throw new IllegalArgumentException();
			}
			order.setStatus(Integer.valueOf(TPaymentMethod.Type.deliveryAgainstPayment.ordinal()).equals(paymentMethod.getType()) ? TOrder.Status.pendingPayment.ordinal() : TOrder.Status.pendingReview.ordinal());
			order.setPaymentMethod(paymentMethod.getId());
			order.setPaymentMethodVO(paymentMethod);
			if (paymentMethod.getTimeout() != null && Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus())) {
				order.setExpire(new Timestamp(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()).getTime()));
			}
			if (Integer.valueOf(TPaymentMethod.Method.online.ordinal()).equals(paymentMethod.getMethod())) {
				lock(order, member);
			}
		} else {
			order.setStatus(TOrder.Status.pendingReview.ordinal());
			order.setPaymentMethod(null);
		}

		List<TOrderItem> orderItems = order.getOrderItemsList();
		for (TCartItem cartItem : cart.getCartItems()) {
			TProduct product = cartItem.getProductVO();
			TOrderItem orderItem = new TOrderItem();
			orderItem.setSn(product.getSn());
			orderItem.setName(product.getName());
			orderItem.setType(product.getType());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setWeight(product.getWeight());
			orderItem.setIsDelivery(product.getIsDelivery());
			orderItem.setThumbnail(product.getThumbnail());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrders(order.getId());
			orderItem.setSpecifications(JsonUtils.toJson(product.getSpecifications()));
			orderItems.add(orderItem);
		}

		for (TProduct gift : cart.getGifts()) {
			TOrderItem orderItem = new TOrderItem();
			orderItem.setSn(gift.getSn());
			orderItem.setName(gift.getName());
			orderItem.setType(gift.getType());
			orderItem.setPrice(BigDecimal.ZERO);
			orderItem.setWeight(gift.getWeight());
			orderItem.setIsDelivery(gift.getIsDelivery());
			orderItem.setThumbnail(gift.getThumbnail());
			orderItem.setQuantity(1);
			orderItem.setShippedQuantity(0);
			orderItem.setReturnedQuantity(0);
			orderItem.setProduct(gift.getId());
			orderItem.setOrders(order.getId());
			orderItem.setSpecifications(JsonUtils.toJson(gift.getSpecifications()));
			orderItems.add(orderItem);
		}

		this.save(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.create.ordinal());
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		exchangePoint(order);
		if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
				|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
			allocateStock(order);
		}

		if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
			TPayment payment = new TPayment();
			payment.setMethod(TPayment.Method.deposit.ordinal());
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(balance);
			payment.setOrders(order.getId());
			payment(order, payment, null);
		}

		mailService.sendCreateOrderMail(order);
		smsService.sendCreateOrderSms(order);

		if (cart != null && cart.getId()!=null) {
			this.tCartService.deleteSelected(cart.getId());
		}

		return order;
	}

    @Transactional
	public TOrder save(TOrder order){
		Timestamp now = new Timestamp(System.currentTimeMillis());
		order.setModifyDate(now);
		if (order.getAreaVO() != null) {
			order.setAreaName(order.getAreaVO().getFullName());
		}
		if (order.getPaymentMethodVO() != null) {
			order.setPaymentMethodName(order.getPaymentMethodVO().getName());
			order.setPaymentMethodType(order.getPaymentMethodVO().getType());
		}
		if (order.getShippingMethodVO() != null) {
			order.setShippingMethodName(order.getShippingMethodVO().getName());
		}
		if (order.getId() == null){
			order.setCreateDate(now);
			order.setVersion(0L);
		}
		TOrder nOrder = super.save(order);
		order.setId(nOrder != null ? nOrder.getId(): null);
		//其他内容
		for (TOrderItem orderItem : order.getOrderItemsList()){
			orderItem.setOrders(order.getId());
			TOrderItem nOrderItem = tOrderItemService.save(orderItem);
			orderItem.setId(nOrderItem != null ? nOrderItem.getId() : null);
		}
		return order;
	}

    @Transactional
	public void update(TOrder order, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && (Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus()) || Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(order.getStatus())));

		Timestamp now = new Timestamp(System.currentTimeMillis());

		order.setAmount(calculateAmount(order));
		if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(TOrder.Status.pendingReview.ordinal());
			order.setExpire(null);
		} else {
			if (order.getPaymentMethod() != null && Integer.valueOf(TPaymentMethod.Type.deliveryAgainstPayment.ordinal()).equals(order.getPaymentMethodVO().getType())) {
				order.setStatus(TOrder.Status.pendingPayment.ordinal());
			} else {
				order.setStatus(TOrder.Status.pendingReview.ordinal());
				order.setExpire(null);
			}
		}
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.update.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendUpdateOrderMail(order);
		smsService.sendUpdateOrderSms(order);
	}

    @Transactional
	public void cancel(TOrder order) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && (Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus()) || Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(order.getStatus())));

		order.setStatus(TOrder.Status.canceled.ordinal());
		order.setExpire(null);

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);

		Timestamp now = new Timestamp(System.currentTimeMillis());
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.cancel.ordinal());
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendCancelOrderMail(order);
		smsService.sendCancelOrderSms(order);
	}

    @Transactional
	public void review(TOrder tOrder, boolean passed, TAdmin operator) {
		Assert.notNull(tOrder);
		Assert.isTrue(!tOrder.isNew());
		Assert.state(!tOrder.hasExpired() && Integer.valueOf(TOrder.Status.pendingReview.ordinal()).equals(tOrder.getStatus()));

		if (passed) {
			tOrder.setStatus(TOrder.Status.pendingShipment.ordinal());
		} else {
			tOrder.setStatus(TOrder.Status.denied.ordinal());

			undoUseCouponCode(tOrder);
			undoExchangePoint(tOrder);
			releaseAllocatedStock(tOrder);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tOrder.setModifyDate(now);
		this.updateOrder(tOrder);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.review.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(tOrder.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		this.mailService.sendReviewOrderMail(tOrder);
		this.smsService.sendReviewOrderSms(tOrder);
	}

    @Transactional
	public void payment(TOrder order, TPayment payment, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.notNull(payment);
		Assert.isTrue(payment.isNew());
		Assert.notNull(payment.getAmount());
		Assert.state(payment.getAmount().compareTo(BigDecimal.ZERO) > 0);

		Timestamp now = new Timestamp(System.currentTimeMillis());

		payment.setSn(this.tSnService.generate(TSn.Type.payment));
		payment.setOrders(order.getId());
		payment.setCreateDate(now);
		payment.setModifyDate(now);
		payment.setVersion(0L);
		this.tPaymentService.insert(payment);

		if (order.getMember() != null && payment.getMethod() != null && TPayment.Method.deposit.ordinal() == payment.getMethod().intValue()) {
			this.tMemberService.addBalance(order.getMember(), payment.getEffectiveAmount().negate(), TDepositLog.Type.payment,
					operator != null ? operator.getUsername() : null, null);
		}

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		order.setAmountPaid(order.getAmountPaid().add(payment.getEffectiveAmount()));
		order.setFee(order.getFee().add(payment.getFee()));
		order.setModifyDate(now);
		if (!order.hasExpired() && Integer.valueOf(TOrder.Status.pendingPayment.ordinal()).equals(order.getStatus()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(TOrder.Status.pendingReview.ordinal());
			order.setExpire(null);
		}
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.payment.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);


		mailService.sendPaymentOrderMail(order);
		smsService.sendPaymentOrderSms(order);
	}

    @Transactional
	public void refunds(TOrder order, TRefunds refunds, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0);
		Assert.notNull(refunds);
		Assert.isTrue(refunds.isNew());
		Assert.notNull(refunds.getAmount());
		Assert.state(refunds.getAmount().compareTo(BigDecimal.ZERO) > 0 && refunds.getAmount().compareTo(order.getRefundableAmount()) <= 0);

		Timestamp now = new Timestamp(System.currentTimeMillis());

		refunds.setSn(this.tSnService.generate(TSn.Type.refunds));
		refunds.setOrders(order.getId());
		refunds.setCreateDate(now);
		refunds.setModifyDate(now);
		refunds.setVersion(0L);
		this.tRefundsService.insertAndFetch(refunds);

		if (Integer.valueOf(TRefunds.Method.deposit.ordinal()).equals(refunds.getMethod())) {
			this.tMemberService.addBalance(order.getMember(), refunds.getAmount(), TDepositLog.Type.refunds, operator.getUsername(), null);
		}

		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setRefundAmount(order.getRefundAmount().add(refunds.getAmount()));
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setOrders(order.getId());
		orderLog.setType(TOrderLog.Type.refunds.ordinal());
		orderLog.setOperator(operator);
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendRefundsOrderMail(order);
		smsService.sendRefundsOrderSms(order);
	}

    @Transactional
	public void shipping(TOrder order, TShipping shipping, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getShippableQuantity() > 0);
		Assert.notNull(shipping);
		Assert.isTrue(shipping.isNew());
		Assert.notEmpty(shipping.getShippingItems());

		Timestamp now = new Timestamp(System.currentTimeMillis());

		shipping.setSn(this.tSnService.generate(TSn.Type.shipping));
		shipping.setOrders(order.getId());
		shipping.setCreateDate(now);
		shipping.setModifyDate(now);
		shipping.setVersion(0L);
		this.tShippingService.insertAndFetch(shipping);

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.ship.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		for (TShippingItem shippingItem : shipping.getShippingItems()) {
			TOrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
			orderItem.setModifyDate(now);
			this.tOrderItemService.update(orderItem);
			TProduct product = shippingItem.getProductVO();
			if (product != null) {
				if (shippingItem.getQuantity() > product.getStock()) {
					throw new IllegalArgumentException();
				}
				this.tProductService.addStock(product, -shippingItem.getQuantity(), TStockLog.Type.stockOut, operator, null);
				if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
					this.tProductService.addAllocatedStock(product, -shippingItem.getQuantity());
				}
			}
		}

		order.setShippedQuantity(order.getShippedQuantity() + shipping.getQuantity());
		order.setModifyDate(now);
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setStatus(TOrder.Status.shipped.ordinal());
			order.setIsAllocatedStock(false);
		}
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.shipping.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendShippingOrderMail(order);
		smsService.sendShippingOrderSms(order);
	}

    @Transactional
	public void returns(TOrder order, TReturns returns, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(order.getReturnableQuantity() > 0);
		Assert.notNull(returns);
		Assert.isTrue(returns.isNew());
		Assert.notEmpty(returns.getReturnsItems());

		Timestamp now = new Timestamp(System.currentTimeMillis());

		returns.setSn(this.tSnService.generate(TSn.Type.returns));
		returns.setOrders(order.getId());
		returns.setCreateDate(now);
		returns.setModifyDate(now);
		returns.setVersion(0L);
		this.tReturnsService.insertAndFetch(returns);

		for (TReturnsItem returnsItem : returns.getReturnsItems()) {
			TOrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + returnsItem.getQuantity());
			orderItem.setModifyDate(now);
			this.tOrderItemService.update(orderItem);
		}

		order.setReturnedQuantity(order.getReturnedQuantity() + returns.getQuantity());
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.returns.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendReturnsOrderMail(order);
		smsService.sendReturnsOrderSms(order);
	}

    @Transactional
	public void receive(TOrder order, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Integer.valueOf(TOrder.Status.shipped.ordinal()).equals(order.getStatus()));
		Timestamp now = new Timestamp(System.currentTimeMillis());
		order.setStatus(TOrder.Status.received.ordinal());
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.receive.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendReceiveOrderMail(order);
		smsService.sendReceiveOrderSms(order);
	}

    @Transactional
	public void complete(TOrder order, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && Integer.valueOf(TOrder.Status.received.ordinal()).equals(order.getStatus()));

		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (order.getRewardPoint() > 0) {
			this.tMemberService.addPoint(order.getMember(), order.getRewardPoint(), TPointLog.Type.reward, operator.getUsername(), null);
		}
		if (CollectionUtils.isNotEmpty(order.getCouponsList())) {
			for (TCoupon coupon : order.getCouponsList()) {
				this.tCouponCodeService.generate(coupon.getId(), coupon.getPrefix(),order.getMember());
			}
		}
		if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
			this.tMemberService.addAmount(order.getMember(),order.getAmountPaid());
		}
		for (TOrderItem orderItem : order.getOrderItemsList()) {
			TProduct product = orderItem.getProductVO();
			if (product != null && product.getGoods() != null) {
				this.tGoodsService.addSales(product.getGoodsVO().getId(), orderItem.getQuantity());
			}
		}

		order.setStatus(TOrder.Status.completed.ordinal());
		order.setCompleteDate(now);
		order.setModifyDate(now);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.complete.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendCompleteOrderMail(order);
		smsService.sendCompleteOrderSms(order);
	}

    @Transactional
	public void fail(TOrder order, TAdmin operator) {
		Assert.notNull(order);
		Assert.isTrue(!order.isNew());
		Assert.state(!order.hasExpired() && (Integer.valueOf(TOrder.Status.pendingShipment.ordinal()).equals(order.getStatus()) || Integer.valueOf(TOrder.Status.shipped.ordinal()).equals(order.getStatus()) || Integer.valueOf(TOrder.Status.received.ordinal()).equals(order.getStatus())));

		Timestamp now = new Timestamp(System.currentTimeMillis());
		order.setModifyDate(now);
		order.setStatus(TOrder.Status.failed.ordinal());

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);
		this.updateOrder(order);

		TOrderLog orderLog = new TOrderLog();
		orderLog.setType(TOrderLog.Type.fail.ordinal());
		orderLog.setOperator(operator);
		orderLog.setOrders(order.getId());
		orderLog.setCreateDate(now);
		orderLog.setModifyDate(now);
		orderLog.setVersion(0L);
		this.tOrderLogService.insertAndFetch(orderLog);

		mailService.sendFailOrderMail(order);
		smsService.sendFailOrderSms(order);
	}

	@Override
	@Transactional
	public void delete(TOrder order) {
		if (order != null && !Integer.valueOf(TOrder.Status.completed.ordinal()).equals(order.getStatus())) {

			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
			//			TODO
		}

		super.delete(order);
	}

	/**
	 * 优惠码使用
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void useCouponCode(TOrder order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		TCouponCode couponCode = order.getCouponCodeVO();
		couponCode.setIsUsed(true);
		couponCode.setUsedDate(new Timestamp(System.currentTimeMillis()));
		couponCode.setModifyDate(new Timestamp(System.currentTimeMillis()));
		this.tCouponCodeService.update(couponCode);
		order.setIsUseCouponCode(true);
	}

	/**
	 * 优惠码使用撤销
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void undoUseCouponCode(TOrder order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		TCouponCode couponCode = this.tCouponCodeService.findDetailById(order.getCouponCode());
		couponCode.setIsUsed(false);
		couponCode.setUsedDate(null);
		couponCode.setModifyDate(now);
		this.tCouponCodeService.update(couponCode);

		TOrderCoupon tOrderCoupon = new TOrderCoupon();
		tOrderCoupon.setOrders(order.getId());
		tOrderCoupon.setOrders(order.getCouponCodeVO().getCoupon());
		this.tOrderCouponService.delete(tOrderCoupon);

		order.setIsUseCouponCode(false);
		order.setCouponCode(null);
	}

	/**
	 * 积分兑换
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void exchangePoint(TOrder order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		this.tMemberService.addPoint(order.getMember(), -order.getExchangePoint(), TPointLog.Type.exchange, null, null);
		order.setIsExchangePoint(true);
	}

	/**
	 * 积分兑换撤销
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void undoExchangePoint(TOrder order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		TMember tMember = this.tMemberService.find(order.getMember());
		this.tMemberService.addPoint(order.getMember(),order.getExchangePoint(),TPointLog.Type.undoExchange,null,null);
		order.setIsExchangePoint(false);
	}

	/**
	 * 分配库存
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void allocateStock(TOrder order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItemsList() != null) {
			for (TOrderItem orderItem : order.getOrderItemsList()) {
				TProduct product = orderItem.getProductVO();
				if (product != null) {
					this.tProductService.addAllocatedStock(product, orderItem.getQuantity() - orderItem.getShippedQuantity());
				}
			}
		}
		order.setIsAllocatedStock(true);
	}

	/**
	 * 释放已分配库存
	 * 
	 * @param order
	 *            订单
	 */
    @Transactional
	private void releaseAllocatedStock(TOrder order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsAllocatedStock())) {
			return;
		}
		TOrderItem tOrderItemParam = new TOrderItem();
		tOrderItemParam.setOrders(order.getId());
		List<TOrderItem> tOrderItemList = this.tOrderItemService.findDetailList(tOrderItemParam);
		if (tOrderItemList != null) {
			for (TOrderItem orderItem : tOrderItemList) {
				TProduct product = orderItem.getProductVO();
				if (product != null) {
					this.tProductService.addAllocatedStock(product, -(orderItem.getQuantity() - orderItem.getShippedQuantity()));
				}
			}
		}
		order.setIsAllocatedStock(false);
	}

    @Transactional(readOnly = true)
	public TOrder findDetailById(Long id){
		if(id == null) return null;
		TOrder tOrder = this.find(id);
		tOrder.setAreaVO(this.tAreaService.find(tOrder.getArea()));
		tOrder.setPaymentMethodVO(this.tPaymentMethodService.find(tOrder.getPaymentMethod()));
		tOrder.setMemberVO(this.tMemberService.findDetails(tOrder.getMember()));
		tOrder.setCouponCodeVO(this.tCouponCodeService.findDetailById(tOrder.getCouponCode()));
		tOrder.setShippingMethodVO(this.tShippingMethodService.find(tOrder.getShippingMethod()));

		List<TOrderCoupon> tOrderCoupons = this.tOrderCouponService.findByOrders(id);
		Long[] couponIds = new Long[tOrderCoupons.size()];
		for(int i = 0 ; i < tOrderCoupons.size() ; i++){
			couponIds[i] = tOrderCoupons.get(i).getCoupons();
		}
		tOrder.setCouponsList(this.tCouponService.findList(couponIds));

		TOrderItem tOrderItemParam = new TOrderItem();
		tOrderItemParam.setOrders(id);
		tOrder.setOrderItemsList(this.tOrderItemService.findDetailList(tOrderItemParam));

		tOrder.setPaymentLogsList(this.tPaymentLogService.findByOrderId(id));

		tOrder.setPaymentsList(this.tPaymentService.findByOrderIds(id));

		tOrder.setRefundsList(this.tRefundsService.findByOrderIds(id));

		tOrder.setShippingsList(this.tShippingService.findByOrderIds(id));

		tOrder.setReturnsList(this.tReturnsService.findByOrderIds(id));

		tOrder.setOrderLogsList(this.tOrderLogService.findByOrderIds(id));

		return tOrder;
	}

	private void updateOrder(List<TOrder> tOrders) {
		for(TOrder tOrder : tOrders) {
			if (tOrder.getAreaVO() != null) {
				tOrder.setAreaName(tOrder.getAreaVO().getFullName());
			}
			if (tOrder.getPaymentMethodVO() != null) {
				tOrder.setPaymentMethodName(tOrder.getPaymentMethodVO().getName());
				tOrder.setPaymentMethodType(tOrder.getPaymentMethodVO().getType());
			}
			if (tOrder.getShippingMethodVO() != null) {
				tOrder.setShippingMethodName(tOrder.getShippingMethodVO().getName());
			}
		}
		this.tOrderDao.update(tOrders);
	}
	private void updateOrder(TOrder tOrder) {
		if (tOrder.getAreaVO() != null) {
			tOrder.setAreaName(tOrder.getAreaVO().getFullName());
		}
		if (tOrder.getPaymentMethodVO() != null) {
			tOrder.setPaymentMethodName(tOrder.getPaymentMethodVO().getName());
			tOrder.setPaymentMethodType(tOrder.getPaymentMethodVO().getType());
		}
		if (tOrder.getShippingMethodVO() != null) {
			tOrder.setShippingMethodName(tOrder.getShippingMethodVO().getName());
		}
		this.tOrderDao.update(tOrder);
	}
}