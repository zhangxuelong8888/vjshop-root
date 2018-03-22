
package com.vjshop.controller.shop;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import com.vjshop.Message;
import com.vjshop.Setting;
import com.vjshop.entity.TMember;
import com.vjshop.entity.SafeKey;
import com.vjshop.entity.ValidGroup;
import com.vjshop.service.CaptchaService;
import com.vjshop.service.MailService;
import com.vjshop.service.TMemberService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 密码
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("shopPasswordController")
@RequestMapping("/password")
public class PasswordController extends BaseController {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Autowired
	private TMemberService memberService;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String find(Model model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/${theme}/password/find";
	}

	/**
	 * 找回密码提交
	 */
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Message find(String captchaId, String captcha, String username, String email) {
		if (!captchaService.isValid(Setting.CaptchaType.findPassword, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(email)) {
			return Message.error("shop.common.invalid");
		}
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("shop.password.memberNotExist");
		}
		if (StringUtils.isEmpty(member.getEmail())) {
			return Message.error("shop.password.emailEmpty");
		}
		if (!StringUtils.equalsIgnoreCase(member.getEmail(), email)) {
			return Message.error("shop.password.invalidEmail");
		}
		Setting setting = SystemUtils.getSetting();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? new Timestamp(DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()).getTime()) : null);
		member.setSafeKey(safeKey);
		memberService.update(member);
		mailService.sendFindPasswordMail(member.getEmail(), member.getUsername(), safeKey);
		return Message.success("shop.password.mailSuccess");
	}

	/**
	 * 重置密码
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset(String username, String key, Model model) {
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(key)) {
			return ERROR_VIEW;
		}
		if (safeKey.hasExpired()) {
			model.addAttribute("errorMessage", Message.warn("shop.password.hasExpired"));
			return ERROR_VIEW;
		}
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("member", member);
		model.addAttribute("key", key);
		return "/shop/${theme}/password/reset";
	}

	/**
	 * 重置密码提交
	 */
	@RequestMapping(value = "reset", method = RequestMethod.POST)
	public @ResponseBody Message reset(String captchaId, String captcha, String username, String newPassword, String key) {
		if (!captchaService.isValid(Setting.CaptchaType.resetPassword, captchaId, captcha)) {
			return Message.error("shop.captcha.invalid");
		}
		TMember member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_MESSAGE;
		}
		if (!isValid(TMember.class, "password", newPassword, ValidGroup.Save.class)) {
			return Message.warn("shop.password.invalidPassword");
		}
		Setting setting = SystemUtils.getSetting();
		if (newPassword.length() < setting.getPasswordMinLength() || newPassword.length() > setting.getPasswordMaxLength()) {
			return Message.warn("shop.password.invalidPassword");
		}
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(key)) {
			return ERROR_MESSAGE;
		}
		if (safeKey.hasExpired()) {
			return Message.error("shop.password.hasExpired");
		}
		member.setPassword(DigestUtils.md5Hex(newPassword));
		member.setSafeKey(null);
		memberService.update(member);
		return Message.success("shop.password.resetSuccess");
	}

}