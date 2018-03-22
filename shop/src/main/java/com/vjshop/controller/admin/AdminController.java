package com.vjshop.controller.admin;

import java.util.HashSet;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.*;

import com.vjshop.service.TAdminService;
import com.vjshop.service.TRoleService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 管理员
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

	@Autowired
	private TAdminService tAdminService;
	@Autowired
	private TRoleService tRoleService;


	/**
	 * 检查用户名是否存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !tAdminService.usernameExists(username);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("roles", tRoleService.findAll());
		return "/admin/admin/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TAdmin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
		admin.setRules(new HashSet<TRole>(tRoleService.findList(roleIds)));
		if (!isValid(admin, ValidGroup.Save.class)) {
			return ERROR_VIEW;
		}
		if (tAdminService.usernameExists(admin.getUsername())) {
			return ERROR_VIEW;
		}
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		admin.setLockKey(null);
		tAdminService.save(admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("roles", tRoleService.findAll());
		TAdmin tAdmin = tAdminService.find(id);
		model.addAttribute("admin", tAdmin);
		model.addAttribute("adminRole", new HashSet<TRole>(tRoleService.findRolesByAdminId(id)));
		return "/admin/admin/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TAdmin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
		admin.setRules(new HashSet<TRole>(tRoleService.findList(roleIds)));
		if (!isValid(admin)) {
			return ERROR_VIEW;
		}
		TAdmin pAdmin = tAdminService.find(admin.getId());
		if (pAdmin == null) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(admin.getPassword())) {
			admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		} else {
			admin.setPassword(pAdmin.getPassword());
		}
		if (pAdmin.getIsLocked() && !admin.getIsLocked()) {
			admin.setLoginFailureCount(0);
			admin.setLockedDate(null);
		} else {
			admin.setIsLocked(pAdmin.getIsLocked());
			admin.setLoginFailureCount(pAdmin.getLoginFailureCount());
			admin.setLockedDate(pAdmin.getLockedDate());
		}
		tAdminService.update(admin, "username", "loginDate", "loginIp", "lockKey");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", tAdminService.findPage(pageable));
		return "/admin/admin/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids.length >= tAdminService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		tAdminService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}