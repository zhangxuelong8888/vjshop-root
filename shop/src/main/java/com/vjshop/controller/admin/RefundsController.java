
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TRefunds;
import com.vjshop.service.TRefundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 退款单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminRefundsController")
@RequestMapping("/admin/refunds")
public class RefundsController extends BaseController {

	@Autowired
	private TRefundsService tRefundsService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("refundsMethods", TRefunds.Method.values());
		model.addAttribute("refunds", this.tRefundsService.findDetailById(id));
		return "/admin/refunds/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("refundsMethods", TRefunds.Method.values());
		model.addAttribute("page", this.tRefundsService.findPage(pageable));
		return "/admin/refunds/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tRefundsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}