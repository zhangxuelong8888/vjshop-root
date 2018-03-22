
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.entity.TArea;

import com.vjshop.service.TAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

/**
 * Controller - 地区
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminAreaController")
@RequestMapping("/admin/area")
public class AreaController extends BaseController {

	@Autowired
	private TAreaService areaService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long parentId, ModelMap model) {
		model.addAttribute("parent", areaService.find(parentId));
		return "/admin/area/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TArea area, Long parentId, RedirectAttributes redirectAttributes) {
		area.setParent(parentId);
		if (!isValid(area)) {
			return ERROR_VIEW;
		}
		area.setFullName(null);
		area.setTreePath(null);
		area.setGrade(null);
		area.setOrders(null);
		areaService.save(area);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("area", areaService.find(id));
		return "/admin/area/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TArea area, RedirectAttributes redirectAttributes) {
		if (!isValid(area)) {
			return ERROR_VIEW;
		}
		area.setModifyDate(new Timestamp(System.currentTimeMillis()));
		areaService.update(area, "fullName", "treePath", "grade", "parent", "children", "members", "receivers", "orders", "deliveryCenters", "freightConfigs");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long parentId, ModelMap model) {
		TArea parent = areaService.find(parentId);
		if (parent != null) {
			model.addAttribute("parent", parent);
			model.addAttribute("areas", areaService.findChildren(parent, false, null));
		} else {
			model.addAttribute("areas", areaService.findRoots());
		}
		return "/admin/area/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		areaService.delete(id);
		return SUCCESS_MESSAGE;
	}

}