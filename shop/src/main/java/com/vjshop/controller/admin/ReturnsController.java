
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.service.TReturnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 退货单
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminReturnsController")
@RequestMapping("/admin/returns")
public class ReturnsController extends BaseController {

	@Autowired
	private TReturnsService tReturnsService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("returns", this.tReturnsService.findDetailById(id));
		return "/admin/returns/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", this.tReturnsService.findPage(pageable));
		return "/admin/returns/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		this.tReturnsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}