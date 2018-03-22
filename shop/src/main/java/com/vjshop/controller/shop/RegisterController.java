
package com.vjshop.controller.shop;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vjshop.Message;
import com.vjshop.Principal;
import com.vjshop.Setting;
import com.vjshop.entity.*;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.TCartService;
import com.vjshop.service.TMemberAttributeService;
import com.vjshop.service.TMemberRankService;
import com.vjshop.service.TMemberService;
import com.vjshop.service.RSAService;
import com.vjshop.util.SystemUtils;
import com.vjshop.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员注册
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopRegisterController")
@RequestMapping("/register")
public class RegisterController extends BaseController {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Autowired
	private TMemberService memberService;
	@Autowired
	private TMemberRankService memberRankService;
	@Autowired
	private TMemberAttributeService memberAttributeService;
	@Autowired
	private TCartService cartService;

	/**
	 * 检查用户名是否被禁用或已存在
	 */
	@RequestMapping(value = "/check_username")
	public @ResponseBody boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !memberService.usernameDisabled(username) && !memberService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否存在
	 */
	@RequestMapping(value = "/check_email")
	public @ResponseBody boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return !memberService.emailExists(email);
	}

	/**
	 * 注册页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("genders", TMember.Gender.values());
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/${theme}/register/index";
	}

	/**
	 * 注册提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Message submit(String captchaId, String captcha, String username, String email, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (!captchaService.isValid(Setting.CaptchaType.memberRegister, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			return Message.error("shop.register.disabled");
		}
		String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);
		if (!isValid(TMember.class, "username", username, ValidGroup.Save.class) || !isValid(TMember.class, "password", password, ValidGroup.Save.class) || !isValid(TMember.class, "email", email, ValidGroup.Save.class)) {
			return Message.error("shop.common.invalid");
		}
		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			return Message.error("shop.common.invalid");
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return Message.error("shop.common.invalid");
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return Message.error("shop.register.disabledExist");
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(email)) {
			return Message.error("shop.register.emailExist");
		}

		TMember member = new TMember();
		member.removeAttributeValue();
		for (TMemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return Message.error("shop.common.invalid");
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute.getEnumType(), memberAttribute.getPropertyIndex(), memberAttributeValue);
		}
		member.setUsername(username);
		member.setPassword(DigestUtils.md5Hex(password));
		member.setEmail(email);
		member.setNickname(null);
		member.setPoint(0L);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Timestamp(System.currentTimeMillis()));
		member.setLoginPluginId(null);
		member.setOpenId(null);
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

		return Message.success("shop.register.success");
	}

}