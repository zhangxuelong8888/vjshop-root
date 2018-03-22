
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TPaymentMethod;
import com.vjshop.service.TPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 支付方式
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminPaymentMethodController")
@RequestMapping("/admin/payment_method")
public class PaymentMethodController extends BaseController {

	@Autowired
	private TPaymentMethodService tPaymentMethodService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", TPaymentMethod.Type.values());
		model.addAttribute("methods", TPaymentMethod.Method.values());
		return "/admin/payment_method/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TPaymentMethod paymentMethod, RedirectAttributes redirectAttributes) {
		if (!isValid(paymentMethod)) {
			return ERROR_VIEW;
		}
		/*paymentMethod.setShippingMethods(null);
		paymentMethod.setOrder(null);*/
		tPaymentMethodService.save(paymentMethod);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", TPaymentMethod.Type.values());
		model.addAttribute("methods", TPaymentMethod.Method.values());
		model.addAttribute("paymentMethod", tPaymentMethodService.find(id));
		return "/admin/payment_method/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TPaymentMethod paymentMethod, RedirectAttributes redirectAttributes) {
		if (!isValid(paymentMethod)) {
			return ERROR_VIEW;
		}
		tPaymentMethodService.save(paymentMethod);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tPaymentMethodService.findPage(pageable));
		return "/admin/payment_method/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids.length >= tPaymentMethodService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		tPaymentMethodService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}