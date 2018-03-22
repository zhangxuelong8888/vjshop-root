
package com.vjshop.controller.admin;


import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.service.TLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 日志
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminLogController")
@RequestMapping("/admin/log")
public class LogController extends BaseController {

	@Autowired
	private TLogService tLogService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tLogService.findPage(pageable));
		return "/admin/log/list";
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("log", tLogService.find(id));
		return "/admin/log/view";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		tLogService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		tLogService.clear();
		return SUCCESS_MESSAGE;
	}

}