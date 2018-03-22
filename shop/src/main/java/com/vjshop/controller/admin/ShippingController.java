
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;

import com.vjshop.service.TShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 发货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminShippingController")
@RequestMapping("/admin/shipping")
public class ShippingController extends BaseController {

	@Autowired
	private TShippingService tShippingService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("shipping", this.tShippingService.findDetailById(id));
		return "/admin/shipping/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.tShippingService.findPage(pageable));
		return "/admin/shipping/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tShippingService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}