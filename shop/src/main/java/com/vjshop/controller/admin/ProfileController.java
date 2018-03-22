
package com.vjshop.controller.admin;

import com.vjshop.entity.TAdmin;
import com.vjshop.service.TAdminService;
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
 * Controller - 个人资料
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminProfileController")
@RequestMapping("/admin/profile")
public class ProfileController extends BaseController {

	@Autowired
	private TAdminService adminService;

	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		TAdmin admin = adminService.getCurrent();
		return StringUtils.equals(DigestUtils.md5Hex(currentPassword), admin.getPassword());
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		model.addAttribute("admin", adminService.getCurrent());
		return "/admin/profile/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String currentPassword, String password, String email, RedirectAttributes redirectAttributes) {
		if (!isValid(TAdmin.class, "email", email)) {
			return ERROR_VIEW;
		}
		TAdmin pAdmin = adminService.getCurrent();
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(password)) {
			if (!isValid(TAdmin.class, "password", password)) {
				return ERROR_VIEW;
			}
			if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), pAdmin.getPassword())) {
				return ERROR_VIEW;
			}
			pAdmin.setPassword(DigestUtils.md5Hex(password));
		}
		pAdmin.setEmail(email);
		adminService.updateSelective(pAdmin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.jhtml";
	}

}