
package com.vjshop.controller.shop;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vjshop.Message;
import com.vjshop.Principal;
import com.vjshop.Setting;
import com.vjshop.entity.TCart;
import com.vjshop.entity.TMember;
import com.vjshop.entity.TMemberRank;
import com.vjshop.entity.TPointLog;
import com.vjshop.plugin.LoginPlugin;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.TCartService;
import com.vjshop.service.TMemberRankService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.PluginService;
import com.vjshop.service.RSAService;
import com.vjshop.util.SystemUtils;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员登录
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopLoginController")
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TCartService cartService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Autowired
	private TMemberRankService memberRankService;

	/**
	 * 登录检测
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public @ResponseBody
	boolean check() {
		return memberService.isAuthenticated();
	}

	/**
	 * 登录页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.equalsIgnoreCase(redirectUrl, setting.getSiteUrl()) || StringUtils.startsWithIgnoreCase(redirectUrl, request.getContextPath() + "/") || StringUtils.startsWithIgnoreCase(redirectUrl, setting.getSiteUrl() + "/")) {
			model.addAttribute("redirectUrl", redirectUrl);
		}
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("loginPlugins", pluginService.getLoginPlugins(true));
		return "/shop/${theme}/login/index";
	}

	/**
	 * 登录提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Message submit(String captchaId, String captcha, String username, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);

		if (!captchaService.isValid(Setting.CaptchaType.memberLogin, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}
		TMember member;
		Setting setting = SystemUtils.getSetting();
		if (setting.getIsEmailLogin() && username.contains("@")) {
			List<TMember> members = memberService.findListByEmail(username);
			if (members.isEmpty()) {
				member = null;
			} else if (members.size() == 1) {
				member = members.get(0);
			} else {
				return Message.error("shop.login.unsupportedAccount");
			}
		} else {
			member = memberService.findByUsername(username);
		}
		if (member == null) {
			return Message.error("shop.login.unknownAccount");
		}
		if (!member.getIsEnabled()) {
			return Message.error("shop.login.disabledAccount");
		}
		if (member.getIsLocked()) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					return Message.error("shop.login.lockedAccount");
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(false);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					return Message.error("shop.login.lockedAccount");
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(false);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(true);
				member.setLockedDate(now);
			}
			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				return Message.error("shop.login.accountLockCount", setting.getAccountLockCount());
			} else {
				return Message.error("shop.login.incorrectCredentials");
			}
		}
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(now);
		member.setLoginFailureCount(0);
		memberService.update(member);

		TCart cart = cartService.getCurrent();
		if (cart != null && cart.getMember() == null) {
			cartService.merge(member.getId(), cart);
			WebUtils.removeCookie(request, response, TCart.KEY_COOKIE_NAME);
		}

		Map<String, Object> attributes = new HashMap<String, Object>();
		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		session.setAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
		WebUtils.addCookie(request, response, TMember.USERNAME_COOKIE_NAME, member.getUsername());
		if (StringUtils.isNotEmpty(member.getNickname())) {
			WebUtils.addCookie(request, response, TMember.NICKNAME_COOKIE_NAME, member.getNickname());
		}

		return SUCCESS_MESSAGE;
	}

	/**
	 * 插件提交
	 */
	@RequestMapping(value = "/plugin_submit", method = RequestMethod.GET)
	public String pluginSubmit(String pluginId, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		LoginPlugin loginPlugin = pluginService.getLoginPlugin(pluginId);
		if (loginPlugin == null || !loginPlugin.getIsEnabled()) {
			return ERROR_VIEW;
		}
		model.addAttribute("requestUrl", loginPlugin.getRequestUrl());
		model.addAttribute("requestMethod", loginPlugin.getRequestMethod());
		model.addAttribute("requestCharset", loginPlugin.getRequestCharset());
		model.addAttribute("parameterMap", loginPlugin.getParameterMap(request));
		if (StringUtils.isNotEmpty(loginPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + loginPlugin.getRequestCharset());
		}
		return "/shop/${theme}/login/plugin_submit";
	}

	/**
	 * 插件通知
	 */
	@RequestMapping(value = "/plugin_notify/{pluginId}", method = RequestMethod.GET)
	public String pluginNotify(@PathVariable String pluginId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
		LoginPlugin loginPlugin = pluginService.getLoginPlugin(pluginId);
		if (loginPlugin != null && loginPlugin.getIsEnabled() && loginPlugin.verifyNotify(request)) {
			Setting setting = SystemUtils.getSetting();
			String openId = loginPlugin.getOpenId(request);
			if (StringUtils.isEmpty(openId)) {
				model.addAttribute("errorMessage", message("shop.login.pluginError"));
				return ERROR_VIEW;
			}
			TMember member = memberService.find(pluginId, openId);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if (member != null) {
				if (!member.getIsEnabled()) {
					model.addAttribute("errorMessage", message("shop.login.disabledAccount"));
					return ERROR_VIEW;
				}
				if (member.getIsLocked()) {
					if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
						int loginFailureLockTime = setting.getAccountLockTime();
						if (loginFailureLockTime == 0) {
							model.addAttribute("errorMessage", message("shop.login.lockedAccount"));
							return ERROR_VIEW;
						}
						Date lockedDate = member.getLockedDate();
						Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
						if (new Date().after(unlockDate)) {
							member.setLoginFailureCount(0);
							member.setIsLocked(false);
							member.setLockedDate(null);
							memberService.update(member);
						} else {
							model.addAttribute("errorMessage", message("shop.login.lockedAccount"));
							return ERROR_VIEW;
						}
					} else {
						member.setLoginFailureCount(0);
						member.setIsLocked(false);
						member.setLockedDate(null);
						memberService.update(member);
					}
				}
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(now);
				member.setLoginFailureCount(0);
				memberService.update(member);
			} else {
				if (!setting.getIsRegisterEnabled()) {
					model.addAttribute("errorMessage", message("shop.login.registerDisabled"));
					return ERROR_VIEW;
				}
				String email = loginPlugin.getEmail(request);
				String nickname = loginPlugin.getNickname(request);
				member = new TMember();
				String username = openId;
				for (int i = 0; memberService.usernameExists(username); i++) {
					username = openId + i;
				}
				member.removeAttributeValue();
				member.setUsername(username);
				member.setPassword(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
				member.setEmail(email);
				member.setNickname(nickname);
				member.setPoint(0L);
				member.setBalance(BigDecimal.ZERO);
				member.setAmount(BigDecimal.ZERO);
				member.setIsEnabled(true);
				member.setIsLocked(false);
				member.setLoginFailureCount(0);
				member.setLockedDate(null);
				member.setRegisterIp(request.getRemoteAddr());
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(now);
				member.setLoginPluginId(pluginId);
				member.setOpenId(openId);
				member.setLockKey(null);
				member.setSafeKey(null);
				TMemberRank memberRank = memberRankService.findDefault();
				if (memberRank != null){
					member.setMemberRank(memberRank.getId());
				}
				member = memberService.save(member);

				if (setting.getRegisterPoint() > 0) {
					memberService.addPoint(member.getId(), setting.getRegisterPoint(), TPointLog.Type.reward, null, null);
				}
			}
			TCart cart = cartService.getCurrent();
			if (cart != null && cart.getMember() == null) {
				cartService.merge(member.getId(), cart);
				WebUtils.removeCookie(request, response, TCart.KEY_COOKIE_NAME);
			}

			Map<String, Object> attributes = new HashMap<String, Object>();
			Enumeration<?> keys = session.getAttributeNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				attributes.put(key, session.getAttribute(key));
			}
			session.invalidate();
			session = request.getSession();
			for (Map.Entry<String, Object> entry : attributes.entrySet()) {
				session.setAttribute(entry.getKey(), entry.getValue());
			}

			session.setAttribute(TMember.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
			WebUtils.addCookie(request, response, TMember.USERNAME_COOKIE_NAME, member.getUsername());
			if (StringUtils.isNotEmpty(member.getNickname())) {
				WebUtils.addCookie(request, response, TMember.NICKNAME_COOKIE_NAME, member.getNickname());
			}
		}
		return "redirect:/";
	}

}