
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TPayment;
import com.vjshop.service.TPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 收款单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminPaymentController")
@RequestMapping("/admin/payment")
public class PaymentController extends BaseController {

	@Autowired
	private TPaymentService tPaymentService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("paymentMethods", TPayment.Method.values());
		model.addAttribute("payment", this.tPaymentService.findDetailById(id));
		return "/admin/payment/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("paymentMethods", TPayment.Method.values());
		model.addAttribute("page", this.tPaymentService.findPage(pageable));
		return "/admin/payment/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tPaymentService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}