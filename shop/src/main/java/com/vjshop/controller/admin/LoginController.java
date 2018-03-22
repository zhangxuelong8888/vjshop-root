package com.vjshop.controller.admin;

import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vjshop.Message;
import com.vjshop.Setting;
import com.vjshop.service.RSAService;
import com.vjshop.service.TAdminService;
import com.vjshop.util.SystemUtils;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 管理员登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminLoginController")
@RequestMapping("/admin/login")
public class LoginController extends BaseController {

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Autowired
	private TAdminService adminService;

	/**
	 * 登录
	 */
	@RequestMapping
	public String index(HttpServletRequest request, ModelMap model) {
		//String loginToken = WebUtils.getCookie(request, TAdmin.LOGIN_TOKEN_COOKIE_NAME);
		//if (!StringUtils.equalsIgnoreCase(loginToken, adminService.getLoginToken())) {
		//	return "redirect:/";
		//}
		if (adminService.isAuthenticated()) {
			return "redirect:common/main.jhtml";
		}
		Message failureMessage = null;
		String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (StringUtils.isNotEmpty(loginFailure)) {
			if (loginFailure.equals("com.vjshop.exception.IncorrectCaptchaException")) {
				failureMessage = Message.error("admin.captcha.invalid");
			} else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
				failureMessage = Message.error("admin.login.unknownAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
				failureMessage = Message.error("admin.login.disabledAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
				failureMessage = Message.error("admin.login.lockedAccount");
			} else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
				Setting setting = SystemUtils.getSetting();
				if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.admin)) {
					failureMessage = Message.error("admin.login.accountLockCount", setting.getAccountLockCount());
				} else {
					failureMessage = Message.error("admin.login.incorrectCredentials");
				}
			} else if (loginFailure.equals("com.vjshop.exception.IncorrectLicenseException")) {
				failureMessage = Message.error("admin.login.incorrectLicense");
			} else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
				failureMessage = Message.error("admin.login.authentication");
			}
		}
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("failureMessage", failureMessage);
		return "/admin/login/index";
	}

}