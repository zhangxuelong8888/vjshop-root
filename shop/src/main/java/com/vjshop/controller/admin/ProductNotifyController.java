
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TProductNotify;
import com.vjshop.service.TProductNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller - 到货通知
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminProductNotifyntroller")
@RequestMapping("/admin/product_notify")
public class ProductNotifyController extends BaseController {

	@Autowired
	private TProductNotifyService tProductNotifyService;

	/**
	 * 发送到货通知
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public @ResponseBody
	Message send(Long[] ids) {
		List<TProductNotify> productNotifies = this.tProductNotifyService.findList(ids);
		int count = this.tProductNotifyService.send(productNotifies);
		return Message.success("admin.productNotify.sentSuccess", count);
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable, ModelMap model) {
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("hasSent", hasSent);
		model.addAttribute("page", this.tProductNotifyService.findPage(null, isMarketable, isOutOfStock, hasSent, pageable));
		return "/admin/product_notify/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tProductNotifyService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}