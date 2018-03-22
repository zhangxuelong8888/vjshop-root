
package com.vjshop.controller.shop;

import java.util.HashMap;
import java.util.Map;

import com.vjshop.Message;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TProduct;
import com.vjshop.entity.TProductNotify;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TProductNotifyService;
import com.vjshop.service.TProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 到货通知
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopProductNotifyController")
@RequestMapping("/product_notify")
public class ProductNotifyController extends BaseController {

	@Autowired
	private TProductNotifyService productNotifyService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TProductService productService;

	/**
	 * 获取当前会员E-mail
	 */
	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> email() {
		TMember member = memberService.getCurrent();
		String email = member != null ? member.getEmail() : null;
		Map<String, String> data = new HashMap<String, String>();
		data.put("email", email);
		return data;
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> save(String email, Long productId) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!isValid(TProductNotify.class, "email", email)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		TProduct product = productService.findDetailById(productId);
		if (product == null) {
			data.put("message", Message.warn("shop.productNotify.productNotExist"));
			return data;
		}
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("shop.productNotify.productNotMarketable"));
			return data;
		}
		if (!product.getIsOutOfStock()) {
			data.put("message", Message.warn("shop.productNotify.productInStock"));
			return data;
		}
		if (productNotifyService.exists(productId, email)) {
			data.put("message", Message.warn("shop.productNotify.exist"));
			return data;
		}
		TProductNotify productNotify = new TProductNotify();
		productNotify.setEmail(email);
		productNotify.setHasSent(false);
		productNotify.setMember(memberService.getCurrentUserId());
		productNotify.setProduct(productId);
		productNotifyService.save(productNotify);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}

}