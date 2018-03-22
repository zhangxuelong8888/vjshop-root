
package com.vjshop.controller.admin;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;

import com.vjshop.FileType;
import com.vjshop.Message;
import com.vjshop.Setting;
import com.vjshop.service.CacheService;
import com.vjshop.service.FileService;
import com.vjshop.service.MailService;
import com.vjshop.service.SmsService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.mail.smtp.SMTPSenderFailedException;

/**
 * Controller - 系统设置
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminstingController")
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;
	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	/**
	 * SMTP测试
	 */
	@RequestMapping(value = "/test_smtp", method = RequestMethod.POST)
	public @ResponseBody
	Message testSmtp(String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, Boolean smtpSSLEnabled, String smtpFromMail, String toMail) {
		if (StringUtils.isEmpty(toMail)) {
			return ERROR_MESSAGE;
		}

		Setting setting = SystemUtils.getSetting();
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("smtpHost", smtpHost);
			properties.put("smtpPort", smtpPort);
			properties.put("smtpUsername", smtpUsername);
			properties.put("smtpSSLEnabled", smtpSSLEnabled);
			properties.put("smtpFromMail", smtpFromMail);
			if (!isValid(Setting.class, properties)) {
				return ERROR_MESSAGE;
			}
			mailService.sendTestSmtpMail(smtpHost, smtpPort, smtpUsername, StringUtils.isNotEmpty(smtpPassword) ? smtpPassword : setting.getSmtpPassword(), smtpSSLEnabled, smtpFromMail, toMail);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			if (rootCause != null) {
				if (rootCause instanceof UnknownHostException) {
					return Message.error("admin.setting.testSmtpUnknownHost");
				} else if (rootCause instanceof ConnectException || rootCause instanceof SocketTimeoutException) {
					return Message.error("admin.setting.testSmtpConnectFailed");
				} else if (rootCause instanceof AuthenticationFailedException) {
					return Message.error("admin.setting.testSmtpAuthenticationFailed");
				} else if (rootCause instanceof SMTPSenderFailedException) {
					return Message.error("admin.setting.testSmtpSenderFailed");
				}
			}
			return Message.error("admin.setting.testSmtpFailed");
		}
		return Message.success("admin.setting.testSmtpSuccess");
	}

	/**
	 * 短信余额查询
	 */
	@RequestMapping(value = "/sms_balance", method = RequestMethod.GET)
	public @ResponseBody
	Message smsBalance() {
		long balance = smsService.getBalance();
		if (balance < 0) {
			return Message.warn("admin.setting.smsInvalid");
		}
		return Message.success("admin.setting.smsBalanceResult", balance);
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		model.addAttribute("setting", SystemUtils.getSetting());
		model.addAttribute("locales", Setting.Locale.values());
		model.addAttribute("watermarkPositions", Setting.WatermarkPosition.values());
		model.addAttribute("roundTypes", Setting.RoundType.values());
		model.addAttribute("captchaTypes", Setting.CaptchaType.values());
		model.addAttribute("accountLockTypes", Setting.AccountLockType.values());
		model.addAttribute("stockAllocationTimes", Setting.StockAllocationTime.values());
		model.addAttribute("reviewAuthorities", Setting.ReviewAuthority.values());
		model.addAttribute("consultationAuthorities", Setting.ConsultationAuthority.values());
		return "/admin/setting/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Setting setting, MultipartFile watermarkImageFile, RedirectAttributes redirectAttributes) {
		if (!isValid(setting)) {
			return ERROR_VIEW;
		}
		Setting srcSetting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getSmtpPassword())) {
			setting.setSmtpPassword(srcSetting.getSmtpPassword());
		}
		if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
			if (!fileService.isValid(FileType.image, watermarkImageFile)) {
				addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
				return "redirect:edit.jhtml";
			}
			String watermarkImage = fileService.uploadLocal(FileType.image, watermarkImageFile);
			setting.setWatermarkImage(watermarkImage);
		} else {
			setting.setWatermarkImage(srcSetting.getWatermarkImage());
		}
		if (StringUtils.isEmpty(setting.getSmsSn()) || StringUtils.isEmpty(setting.getSmsKey())) {
			setting.setSmsSn(null);
			setting.setSmsKey(null);
		}
		setting.setIsCnzzEnabled(srcSetting.getIsCnzzEnabled());
		setting.setCnzzSiteId(srcSetting.getCnzzSiteId());
		setting.setCnzzPassword(srcSetting.getCnzzPassword());
		setting.setTheme(srcSetting.getTheme());
		SystemUtils.setSetting(setting);
		cacheService.clear();
		/*staticService.generateIndex();
		staticService.generateOther();*/

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:edit.jhtml";
	}

}