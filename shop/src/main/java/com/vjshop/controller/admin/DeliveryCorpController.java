
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TDeliveryCorp;

import com.vjshop.service.TDeliveryCorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 物流公司
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminDeliveryCorpController")
@RequestMapping("/admin/delivery_corp")
public class DeliveryCorpController extends BaseController {

	@Autowired
	private TDeliveryCorpService tDeliveryCorpService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "/admin/delivery_corp/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TDeliveryCorp deliveryCorp, RedirectAttributes redirectAttributes) {
		if (!isValid(deliveryCorp)) {
			return ERROR_VIEW;
		}
		tDeliveryCorpService.save(deliveryCorp);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("deliveryCorp", tDeliveryCorpService.find(id));
		return "/admin/delivery_corp/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TDeliveryCorp deliveryCorp, RedirectAttributes redirectAttributes) {
		if (!isValid(deliveryCorp)) {
			return ERROR_VIEW;
		}
		tDeliveryCorpService.save(deliveryCorp);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tDeliveryCorpService.findPage(pageable));
		return "/admin/delivery_corp/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		tDeliveryCorpService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}