
package com.vjshop.controller.admin;

import java.util.HashSet;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.*;
import com.vjshop.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 配送方式
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminShippingMethodController")
@RequestMapping("/admin/shipping_method")
public class ShippingMethodController extends BaseController {

	@Autowired
	private TShippingMethodService tShippingMethodService;

	@Autowired
	private TPaymentMethodService tPaymentMethodService;

	@Autowired
	private TDeliveryCorpService tDeliveryCorpService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("deliveryCorps", tDeliveryCorpService.findAll());
		model.addAttribute("paymentMethods", tPaymentMethodService.findAll());
		return "/admin/shipping_method/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TShippingMethod shippingMethod, Long defaultDeliveryCorpId, Long[] paymentMethodIds, RedirectAttributes redirectAttributes) {
		shippingMethod.setDefaultDeliveryCorp(defaultDeliveryCorpId);
		shippingMethod.setPaymentMethods(new HashSet<TPaymentMethod>(tPaymentMethodService.findListByIds(paymentMethodIds)));
		if (!isValid(shippingMethod)) {
			return ERROR_VIEW;
		}
		shippingMethod.setFreightConfigs(null);
		tShippingMethodService.save(shippingMethod);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("deliveryCorps", tDeliveryCorpService.findAll());
		model.addAttribute("paymentMethods", tPaymentMethodService.findAll());
		TShippingMethod tShippingMethod = tShippingMethodService.find(id);
		tShippingMethod.setPaymentMethods(new HashSet<TPaymentMethod>(tPaymentMethodService.findListByShippingMethodId(id)));
		model.addAttribute("shippingMethod", tShippingMethod);
		return "/admin/shipping_method/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TShippingMethod shippingMethod, Long defaultDeliveryCorpId, Long[] paymentMethodIds, RedirectAttributes redirectAttributes) {
		shippingMethod.setDefaultDeliveryCorp(defaultDeliveryCorpId);
		shippingMethod.setPaymentMethods(new HashSet<TPaymentMethod>(tPaymentMethodService.findListByIds(paymentMethodIds)));
		if (!isValid(shippingMethod)) {
			return ERROR_VIEW;
		}
		shippingMethod.setFreightConfigs(null);
		tShippingMethodService.save(shippingMethod);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tShippingMethodService.findPage(pageable));
		return "/admin/shipping_method/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids.length >= tShippingMethodService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		tShippingMethodService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}