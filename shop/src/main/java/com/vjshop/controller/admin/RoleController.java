
package com.vjshop.controller.admin;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TRole;

import com.vjshop.service.TRoleService;
import com.vjshop.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller - 角色
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminRoleController")
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Autowired
	private TRoleService tRoleService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "/admin/role/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TRole role, RedirectAttributes redirectAttributes) {
		if (!isValid(role)) {
			return ERROR_VIEW;
		}
		role.setIsSystem(false);
		tRoleService.save(role);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TRole role = tRoleService.find(id);
		role.setAuthoritiesList(JsonUtils.toObject(role.getAuthorities(), List.class));
		model.addAttribute("role", role);
		return "/admin/role/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TRole role, RedirectAttributes redirectAttributes) {
		if (!isValid(role)) {
			return ERROR_VIEW;
		}
		TRole pRole = tRoleService.find(role.getId());
		if (pRole == null || pRole.getIsSystem()) {
			return ERROR_VIEW;
		}
		tRoleService.update(role, "isSystem");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tRoleService.findPage(pageable));
		return "/admin/role/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				TRole role = tRoleService.find(id);
				boolean hasUser = tRoleService.hasUser(id);
				if (role != null && (role.getIsSystem() || hasUser)) {
					return Message.error("admin.role.deleteExistNotAllowed", role.getName());
				}
			}
			tRoleService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}