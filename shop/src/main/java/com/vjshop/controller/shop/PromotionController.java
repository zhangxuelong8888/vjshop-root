
package com.vjshop.controller.shop;

import com.vjshop.entity.TPromotion;
import com.vjshop.exception.ResourceNotFoundException;
import com.vjshop.service.TPromotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 促销
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopPromotionController")
@RequestMapping("/promotion")
public class PromotionController extends BaseController {

	@Autowired
	private TPromotionService promotionService;

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		TPromotion promotion = promotionService.find(id);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("promotion", promotion);
		return "/shop/${theme}/promotion/content";
	}

}