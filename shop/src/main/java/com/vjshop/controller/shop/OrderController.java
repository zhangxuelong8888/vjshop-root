
package com.vjshop.controller.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.entity.TCart;
import com.vjshop.entity.TCartItem;
import com.vjshop.entity.TCoupon;
import com.vjshop.entity.TCouponCode;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TOrder;
import com.vjshop.entity.TPaymentMethod;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TReceiver;
import com.vjshop.entity.TShippingMethod;
import com.vjshop.plugin.PaymentPlugin;
import com.vjshop.service.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 订单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopOrderController")
@RequestMapping("/order")
public class OrderController extends BaseController {

	@Autowired
	private TProductService productService;
	@Autowired
	private TGoodsService goodsService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TReceiverService receiverService;
	@Autowired
	private TCartService cartService;
	@Autowired
	private TPaymentMethodService paymentMethodService;
	@Autowired
	private TShippingMethodService shippingMethodService;
	@Autowired
	private TCouponCodeService couponCodeService;
	@Autowired
	private TCouponService couponService;
	@Autowired
	private TOrderService orderService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 检查积分兑换
	 */
	@RequestMapping(value = "/check_exchange", method = RequestMethod.GET)
	public @ResponseBody Message checkExchange(Long productId, Integer quantity) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		TProduct product = productService.find(productId);
		if (product == null) {
			return ERROR_MESSAGE;
		}
		TGoods goods = goodsService.find(product.getGoods());
		product.setGoodsVO(goods);
		if (product.getType() == null || TGoods.Type.exchange.ordinal() != product.getType().intValue()) {
			return ERROR_MESSAGE;
		}
		if (!product.getIsMarketable()) {
			return Message.warn("shop.order.productNotMarketable");
		}
		if (quantity > product.getAvailableStock()) {
			return Message.warn("shop.order.productLowStock");
		}
		TMember member = memberService.getCurrent();
		if (member == null || member.getPoint() < product.getExchangePoint() * quantity) {
			return Message.warn("shop.order.lowPoint");
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 保存收货地址
	 */
	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveReceiver(TReceiver receiver, Long areaId) {
		Map<String, Object> data = new HashMap<String, Object>();
		receiver.setArea(areaId);
		if (!isValid(receiver)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Long memberId = memberService.getCurrentUserId();
		Long receiverCount = receiverService.count(memberId);
		if (TReceiver.MAX_RECEIVER_COUNT != null && receiverCount != null && receiverCount.longValue() >= TReceiver.MAX_RECEIVER_COUNT) {
			data.put("message", Message.error("shop.order.addReceiverCountNotAllowed", TReceiver.MAX_RECEIVER_COUNT));
			return data;
		}
		receiver.setAreaName(null);
		receiver.setMember(memberId);
		receiverService.save(receiver);
		data.put("message", SUCCESS_MESSAGE);
		data.put("id", receiver.getId());
		data.put("consignee", receiver.getConsignee());
		data.put("areaName", receiver.getAreaName());
		data.put("address", receiver.getAddress());
		data.put("zipCode", receiver.getZipCode());
		data.put("phone", receiver.getPhone());
		return data;
	}

	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody void lock(String sn) {
		TOrder order = orderService.findBySn(sn);
		TMember member = memberService.getCurrent();
		TPaymentMethod paymentMethod = paymentMethodService.find(order.getPaymentMethod());
		if (order != null && member != null && member.getId() != null && member.getId().equals(order.getMember()) && order.getPaymentMethod() != null
				&& paymentMethod.getMethod() != null && TPaymentMethod.Method.online.ordinal() == paymentMethod.getMethod().intValue()
				&& order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0) {
			orderService.lock(order, member);
		}
	}

	/**
	 * 检查等待付款
	 */
	@RequestMapping(value = "/check_pending_payment", method = RequestMethod.GET)
	public @ResponseBody boolean checkPendingPayment(String sn) {
		TOrder order = orderService.findBySn(sn);
		TMember member = memberService.getCurrent();
		TPaymentMethod paymentMethod = paymentMethodService.find(order.getPaymentMethod());
		return order != null && member != null && member.getId() != null && member.getId().equals(order.getMember()) && order.getPaymentMethod() != null
				&& paymentMethod.getMethod() != null && TPaymentMethod.Method.online.ordinal()  == paymentMethod.getMethod().intValue()
				&& order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 检查优惠券
	 */
	@RequestMapping(value = "/check_coupon", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> checkCoupon(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		TCart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!cart.isCouponAllowed()) {
			data.put("message", Message.warn("shop.order.couponNotAllowed"));
			return data;
		}
		TCouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			TCoupon coupon = couponService.find(couponCode.getCoupon());
			if (couponCode.getIsUsed()) {
				data.put("message", Message.warn("shop.order.couponCodeUsed"));
				return data;
			}
			if (!coupon.getIsEnabled()) {
				data.put("message", Message.warn("shop.order.couponDisabled"));
				return data;
			}
			if (!coupon.hasBegun()) {
				data.put("message", Message.warn("shop.order.couponNotBegin"));
				return data;
			}
			if (coupon.hasExpired()) {
				data.put("message", Message.warn("shop.order.couponHasExpired"));
				return data;
			}
			if (!cart.isValid(coupon)) {
				data.put("message", Message.warn("shop.order.couponInvalid"));
				return data;
			}
			data.put("message", SUCCESS_MESSAGE);
			data.put("couponName", coupon.getName());
			return data;
		} else {
			data.put("message", Message.warn("shop.order.couponCodeNotExist"));
			return data;
		}
	}

	/**
	 * 结算
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(ModelMap model) {
		TCart cart = cartService.getCurrentSelected();
		if (cart == null || cart.isEmpty()) {
			return "redirect:/cart/list.jhtml";
		}

		Long memberId = memberService.getCurrentUserId();
		TReceiver defaultReceiver = receiverService.findDefault(memberId);
		TOrder order = orderService.generate(TOrder.Type.general, cart, defaultReceiver, null, null, null, null, null, null, null);
		model.addAttribute("order", order);
		model.addAttribute("defaultReceiver", defaultReceiver);
		model.addAttribute("cartToken", cart.getToken());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		return "/shop/${theme}/order/checkout";
	}

	/**
	 * 结算
	 */
	@RequestMapping(value = "/checkout", params = "type=exchange", method = RequestMethod.GET)
	public String checkout(Long productId, Integer quantity, ModelMap model) {
		if (quantity == null || quantity < 1) {
			return ERROR_VIEW;
		}
		TProduct product = productService.findDetailById(productId);
		if (product == null) {
			return ERROR_VIEW;
		}
		if (product.getType() == null || TGoods.Type.exchange.ordinal() != product.getType().intValue()) {
			return ERROR_VIEW;
		}
		if (!product.getIsMarketable()) {
			return ERROR_VIEW;
		}
		if (quantity > product.getAvailableStock()) {
			return ERROR_VIEW;
		}
		TMember member = memberService.getCurrent();
		if (member == null || member.getPoint() < product.getExchangePoint() * quantity) {
			return ERROR_VIEW;
		}
		Set<TCartItem> cartItems = new HashSet<TCartItem>();
		TCartItem cartItem = new TCartItem();
		cartItem.setProduct(productId);
		cartItem.setQuantity(quantity);
		cartItem.setProductVO(product);
		cartItems.add(cartItem);
		TCart cart = new TCart();
		cart.setMember(member.getId());
		cart.setCartItems(cartItems);
		TReceiver defaultReceiver = receiverService.findDefault(member.getId());
		TOrder order = orderService.generate(TOrder.Type.exchange, cart, defaultReceiver, null, null, null, null, null, null, null);
		model.addAttribute("productId", productId);
		model.addAttribute("quantity", quantity);
		model.addAttribute("order", order);
		model.addAttribute("defaultReceiver", defaultReceiver);
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		return "/shop/${theme}/order/checkout";
	}


	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> calculate(Long receiverId, Long paymentMethodId, Long shippingMethodId, String code,
													   String invoiceTitle, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		TCart cart = cartService.getCurrentSelected();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TMember member = memberService.getCurrent();
		TReceiver receiver = receiverService.find(receiverId);
		if (receiver != null && member != null && member.getId() != null && !member.getId().equals(receiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}

		TPaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		TShippingMethod shippingMethod = shippingMethodService.findDetails(shippingMethodId);
		TCouponCode couponCode = couponCodeService.findByCode(code);
		TOrder order = orderService.generate(TOrder.Type.general, cart, receiver, paymentMethod, shippingMethod, couponCode, invoiceTitle, null, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("price", order.getPrice());
		data.put("fee", order.getFee());
		data.put("freight", order.getFreight());
		data.put("tax", order.getTax());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("amount", order.getAmount());
		data.put("amountPayable", order.getAmountPayable());
		return data;
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", params = "type=exchange", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> calculate(Long productId, Integer quantity, Long receiverId, Long paymentMethodId,
													   Long shippingMethodId, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TProduct product = productService.find(productId);
		if (product == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TMember member = memberService.getCurrent();
		TReceiver receiver = receiverService.find(receiverId);
		if (receiver != null && member != null && member.getId() != null && !member.getId().equals(receiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		TPaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		TShippingMethod shippingMethod = shippingMethodService.findDetails(shippingMethodId);
		Set<TCartItem> cartItems = new HashSet<TCartItem>();
		TCartItem cartItem = new TCartItem();
		cartItem.setProduct(productId);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		TCart cart = new TCart();
		cart.setMember(member.getId());
		cart.setCartItems(cartItems);
		TOrder order = orderService.generate(TOrder.Type.general, cart, receiver, paymentMethod, shippingMethod, null, null, null, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("price", order.getPrice());
		data.put("fee", order.getFee());
		data.put("freight", order.getFreight());
		data.put("tax", order.getTax());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("amount", order.getAmount());
		data.put("amountPayable", order.getAmountPayable());
		return data;
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> create(String cartToken, Long receiverId, Long paymentMethodId, Long shippingMethodId,
													String code, String invoiceTitle, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		TCart cart = cartService.getCurrentSelected();
		if (cart == null || cart.isEmpty()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!StringUtils.equals(cart.getToken(), cartToken)) {
			data.put("message", Message.warn("shop.order.cartHasChanged"));
			return data;
		}
		if (cart.hasNotMarketable()) {
			data.put("message", Message.warn("shop.order.hasNotMarketable"));
			return data;
		}
		if (cart.getIsLowStock()) {
			data.put("message", Message.warn("shop.order.cartLowStock"));
			return data;
		}
		TMember member = memberService.getCurrent();
		TReceiver receiver = null;
		TShippingMethod shippingMethod = null;
		TPaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (cart.getIsDelivery()) {
			receiver = receiverService.find(receiverId);
			if (receiver == null || member == null || member.getId() == null || !member.getId().equals(receiver.getMember())) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
			shippingMethod = shippingMethodService.findDetails(shippingMethodId);
			if (shippingMethod == null) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
		}
		TCouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && !cart.isValid(couponCode)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		TOrder order = orderService.create(TOrder.Type.general, cart, receiver, paymentMethod, shippingMethod, couponCode, invoiceTitle, null, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("sn", order.getSn());
		return data;
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", params = "type=exchange", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> create(Long productId, Integer quantity, Long receiverId, Long paymentMethodId, Long shippingMethodId, BigDecimal balance, String memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TProduct product = productService.find(productId);
		if (product == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (product.getType() == null || TGoods.Type.exchange.ordinal() != product.getType().intValue()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("shop.order.productNotMarketable"));
			return data;
		}
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.order.productLowStock"));
			return data;
		}
		TMember member = memberService.getCurrent();
		TReceiver receiver = null;
		TShippingMethod shippingMethod = null;
		TPaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (product.getIsDelivery()) {
			receiver = receiverService.find(receiverId);
			if (receiver == null || member == null || member.getId() == null || !member.getId().equals(receiver.getMember())) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
			shippingMethod = shippingMethodService.findDetails(shippingMethodId);
			if (shippingMethod == null) {
				data.put("message", ERROR_MESSAGE);
				return data;
			}
		}
		if (member == null || member.getPoint() < product.getExchangePoint() * quantity) {
			data.put("message", Message.warn("shop.order.lowPoint"));
			return data;
		}
		if (balance != null && balance.compareTo(member.getBalance()) > 0) {
			data.put("message", Message.warn("shop.order.insufficientBalance"));
			return data;
		}
		Set<TCartItem> cartItems = new HashSet<TCartItem>();
		TCartItem cartItem = new TCartItem();
		cartItem.setProduct(productId);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		TCart cart = new TCart();
		cart.setMember(member.getId());
		cart.setCartItems(cartItems);

		TOrder order = orderService.create(TOrder.Type.exchange, cart, receiver, paymentMethod, shippingMethod, null, null, null, balance, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("sn", order.getSn());
		return data;
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(String sn, ModelMap model, RedirectAttributes redirectAttributes) {
		TOrder order = orderService.findBySn(sn);
		TMember member = memberService.getCurrent();
		if (order == null || member == null || member.getId() == null || !member.getId().equals(order.getMember()) ||
				order.getPaymentMethod() == null || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			return ERROR_VIEW;
		}
		TPaymentMethod paymentMethod = paymentMethodService.find(order.getPaymentMethod());
		if (paymentMethod.getMethod() != null && TPaymentMethod.Method.online.ordinal() == paymentMethod.getMethod().intValue()) {
			if (orderService.isLocked(order, member, true)) {
				addFlashMessage(redirectAttributes, Message.warn("shop.order.locked"));
				return "redirect:/member/order/view.jhtml?sn=" + sn + ".jhtml";
			}
			List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
			if (CollectionUtils.isNotEmpty(paymentPlugins)) {
				PaymentPlugin defaultPaymentPlugin = paymentPlugins.get(0);
				model.addAttribute("fee", defaultPaymentPlugin.calculateFee(order.getAmountPayable()));
				model.addAttribute("amount", defaultPaymentPlugin.calculateAmount(order.getAmountPayable()));
				model.addAttribute("defaultPaymentPlugin", defaultPaymentPlugin);
				model.addAttribute("paymentPlugins", paymentPlugins);
			}
		}
		TShippingMethod shippingMethod = shippingMethodService.find(order.getShippingMethod());
		order.setPaymentMethodName(paymentMethod.getName());
		order.setShippingMethodName(shippingMethod.getName());
		order.setPaymentMethodVO(paymentMethod);

		model.addAttribute("order", order);
		return "/shop/${theme}/order/payment";
	}

	/**
	 * 计算支付金额
	 */
	@RequestMapping(value = "/calculate_amount", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		TOrder order = orderService.findBySn(sn);
		TMember member = memberService.getCurrent();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (order == null || member == null || member.getId() == null || !member.getId().equals(order.getMember()) ||
				paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(order.getAmountPayable()));
		data.put("amount", paymentPlugin.calculateAmount(order.getAmountPayable()));
		return data;
	}

}