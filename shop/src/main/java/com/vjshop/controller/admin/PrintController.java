
package com.vjshop.controller.admin;

import com.vjshop.entity.*;
import com.vjshop.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 打印
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminPrintController")
@RequestMapping("/admin/print")
public class PrintController extends BaseController {

	@Autowired
	private TOrderService tOrderService;
	@Autowired
	private TDeliveryTemplateService tDeliveryTemplateService;
	@Autowired
	private TDeliveryCenterService tDeliveryCenterService;

	/**
	 * 订单打印
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String order(Long id, ModelMap model) {
		model.addAttribute("goodsTypes", TGoods.Type.values());
		model.addAttribute("order", this.tOrderService.findDetailById(id));
		return "/admin/print/order";
	}

	/**
	 * 购物单打印
	 */
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String product(Long id, ModelMap model) {
		model.addAttribute("goodsTypes", TGoods.Type.values());
		model.addAttribute("order", this.tOrderService.findDetailById(id));
		return "/admin/print/product";
	}

	/**
	 * 发货单打印
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.GET)
	public String shipping(Long id, ModelMap model) {
		model.addAttribute("goodsTypes", TGoods.Type.values());
		model.addAttribute("order", this.tOrderService.findDetailById(id));
		return "/admin/print/shipping";
	}

	/**
	 * 快递单打印
	 */
	@RequestMapping(value = "/delivery", method = RequestMethod.GET)
	public String delivery(Long orderId, Long deliveryTemplateId, Long deliveryCenterId, ModelMap model) {
		TDeliveryTemplate deliveryTemplate = this.tDeliveryTemplateService.find(deliveryTemplateId);
		TDeliveryCenter deliveryCenter = this.tDeliveryCenterService.find(deliveryCenterId);
		if (deliveryTemplate == null) {
			deliveryTemplate = this.tDeliveryTemplateService.findDefault();
		}
		if (deliveryCenter == null) {
			deliveryCenter = this.tDeliveryCenterService.findDefault();
		}
		model.addAttribute("goodsTypes", TGoods.Type.values());
		model.addAttribute("deliveryTemplates", this.tDeliveryTemplateService.findAll());
		model.addAttribute("deliveryCenters", this.tDeliveryCenterService.findAll());
		model.addAttribute("order", this.tOrderService.findDetailById(orderId));
		model.addAttribute("deliveryTemplate", deliveryTemplate);
		model.addAttribute("deliveryCenter", deliveryCenter);
		return "/admin/print/delivery";
	}

}