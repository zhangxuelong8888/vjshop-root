
package com.vjshop.controller.shop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vjshop.Message;
import com.vjshop.entity.TCart;
import com.vjshop.entity.TCartItem;
import com.vjshop.entity.TGoods;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TProduct;
import com.vjshop.service.TCartItemService;
import com.vjshop.service.TCartService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TProductService;
import com.vjshop.util.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 购物车
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopCartController")
@RequestMapping("/cart")
public class CartController extends BaseController {

	@Autowired
	private TMemberService memberService;
	@Autowired
	private TProductService productService;
	@Autowired
	private TCartService cartService;
	@Autowired
	private TCartItemService cartItemService;

	/**
	 * 数量
	 */
	@RequestMapping(value = "/quantity", method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> quantity() {
		Map<String, Integer> data = new HashMap<String, Integer>();
		TCart cart = cartService.getCurrent();
		data.put("quantity", cart != null ? cart.getProductQuantity() : 0);
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add")
	public @ResponseBody Message add(Long productId, Integer quantity, HttpServletRequest request,
									 HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		TProduct product = productService.findDetailById(productId);
		if (product == null) {
			return Message.warn("shop.cart.productNotExist");
		}
		if (product.getType() != null && TGoods.Type.general.ordinal() != product.getType().intValue()) {
			return Message.warn("shop.cart.productNotForSale");
		}
		if (!product.getIsMarketable()) {
			return Message.warn("shop.cart.productNotMarketable");
		}

		TCart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.containsProduct(productId)) {
				TCartItem cartItem = cart.getCartItemByProductId(productId);
				if (TCartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > TCartItem.MAX_QUANTITY) {
					return Message.warn("shop.cart.addQuantityNotAllowed", TCartItem.MAX_QUANTITY);
				}
				if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					return Message.warn("shop.cart.productLowStock");
				}
			} else {
				if (TCart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= TCart.MAX_CART_ITEM_COUNT) {
					return Message.warn("shop.cart.addCartItemCountNotAllowed", TCart.MAX_CART_ITEM_COUNT);
				}
				if (TCartItem.MAX_QUANTITY != null && quantity > TCartItem.MAX_QUANTITY) {
					return Message.warn("shop.cart.addQuantityNotAllowed", TCartItem.MAX_QUANTITY);
				}
				if (quantity > product.getAvailableStock()) {
					return Message.warn("shop.cart.productLowStock");
				}
			}
		} else {
			if (TCartItem.MAX_QUANTITY != null && quantity > TCartItem.MAX_QUANTITY) {
				return Message.warn("shop.cart.addQuantityNotAllowed", TCartItem.MAX_QUANTITY);
			}
			if (quantity > product.getAvailableStock()) {
				return Message.warn("shop.cart.productLowStock");
			}
		}
		cart = cartService.add(productId, quantity);

		TMember member = memberService.getCurrent();
		if (member == null) {
			WebUtils.addCookie(request, response, TCart.KEY_COOKIE_NAME, cart.getCartKey(), TCart.TIMEOUT);
		}
		return Message.success("shop.cart.addSuccess", cart.getProductQuantity(), currency(cart.getEffectivePrice(false), true, false));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("cart", cartService.getCurrent());
		return "/shop/${theme}/cart/list";
	}

	/**
	 * 选择购物车项
	 * @param cartItemId
	 * @param isSelected
	 * @return
	 */
	@RequestMapping(value = "/selected_cartitem", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectedCartItem(Long cartItemId, Boolean isSelected) {
		Map map = new HashMap();
		cartService.updateSelectStatus(cartItemId, isSelected);
		map.put("message", SUCCESS_MESSAGE);
		return map;
	}

	/**
	 * 选择购物车项
	 * @param cartItemIds
	 * @param isSelected
	 * @return
	 */
	@RequestMapping(value = "/selected_cartitem_all", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectedCartItemAll(String cartItemIds, Boolean isSelected) {
		Map map = new HashMap();
		cartService.updateSelectStatus(cartItemIds, isSelected);
		map.put("message", SUCCESS_MESSAGE);
		return map;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TCart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		if (!cart.contains(id)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		if (TCartItem.MAX_QUANTITY != null && quantity > TCartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.addQuantityNotAllowed", TCartItem.MAX_QUANTITY));
			return data;
		}
		TCartItem cartItem = cart.getCartItem(id);
		TProduct product = cartItem.getProductVO();
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			return data;
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice(true));
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		return data;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		TCart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		if (!cart.contains(id)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			return data;
		}
		cartItemService.delete(id);
		cart.remove(id);

		data.put("message", SUCCESS_MESSAGE);
		data.put("isLowStock", cart.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice(true));
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		return data;
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody Message clear() {
		TCart cart = cartService.findByMemberId(memberService.getCurrentUserId());
		cartService.deleteCascade(cart.getId());
		return SUCCESS_MESSAGE;
	}

}