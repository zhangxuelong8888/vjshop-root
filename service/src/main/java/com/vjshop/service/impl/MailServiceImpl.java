
package com.vjshop.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Setting;
import com.vjshop.TemplateConfig;
import com.vjshop.dao.TMemberDao;
import com.vjshop.entity.*;
import com.vjshop.service.MailService;
import com.vjshop.service.TMessageConfigService;
import com.vjshop.util.SpringUtils;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service - 邮件
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("mailServiceImpl")
public class MailServiceImpl implements MailService {

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	private TMessageConfigService messageConfigService;
	@Autowired
	private TMemberDao tMemberDao;

	/**
	 * 添加邮件发送任务
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	private void addSendTask(final String smtpHost, final int smtpPort, final String smtpUsername, final String smtpPassword, final boolean smtpSSLEnabled, final String smtpFromMail, final String[] toMails, final String subject, final String content) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
			}
		});
	}

	/**
	 * 发送邮件
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param toMails
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	private void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content) {
		Assert.hasText(smtpHost);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(smtpFromMail);
		Assert.notEmpty(toMails);
		Assert.hasText(subject);
		Assert.hasText(content);

		try {
			Setting setting = SystemUtils.getSetting();
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(smtpPort);
			email.setAuthentication(smtpUsername, smtpPassword);
			email.setSSLOnConnect(smtpSSLEnabled);
			email.setFrom(smtpFromMail, setting.getSiteName());
			email.addTo(toMails);
			email.setSubject(subject);
			email.setCharset("UTF-8");
			email.setHtmlMsg(content);
			email.send();
		} catch (EmailException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String content, boolean async) {
		Assert.hasText(smtpHost);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(smtpFromMail);
		Assert.notEmpty(toMails);
		Assert.hasText(subject);
		Assert.hasText(content);

		if (async) {
			addSendTask(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
		} else {
			send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content);
		}
	}

	public void send(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async) {
		Assert.hasText(smtpHost);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(smtpFromMail);
		Assert.notEmpty(toMails);
		Assert.hasText(subject);
		Assert.hasText(templatePath);

		try {
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate(templatePath);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content, async);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String[] toMails, String subject, String content, boolean async) {
		Assert.notEmpty(toMails);
		Assert.hasText(subject);
		Assert.hasText(content);

		Setting setting = SystemUtils.getSetting();
		String smtpHost = setting.getSmtpHost();
		int smtpPort = setting.getSmtpPort();
		String smtpUsername = setting.getSmtpUsername();
		String smtpPassword = setting.getSmtpPassword();
		boolean smtpSSLEnabled = setting.getSmtpSSLEnabled();
		String smtpFromMail = setting.getSmtpFromMail();
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, content, async);
	}

	public void send(String[] toMails, String subject, String templatePath, Map<String, Object> model, boolean async) {
		Assert.notEmpty(toMails);
		Assert.hasText(subject);
		Assert.hasText(templatePath);

		Setting setting = SystemUtils.getSetting();
		String smtpHost = setting.getSmtpHost();
		int smtpPort = setting.getSmtpPort();
		String smtpUsername = setting.getSmtpUsername();
		String smtpPassword = setting.getSmtpPassword();
		boolean smtpSSLEnabled = setting.getSmtpSSLEnabled();
		String smtpFromMail = setting.getSmtpFromMail();
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, toMails, subject, templatePath, model, async);
	}

	public void send(String toMail, String subject, String content) {
		Assert.hasText(toMail);
		Assert.hasText(subject);
		Assert.hasText(content);

		send(new String[] { toMail }, subject, content, true);
	}

	public void send(String toMail, String subject, String templatePath, Map<String, Object> model) {
		Assert.hasText(toMail);
		Assert.hasText(subject);
		Assert.hasText(templatePath);

		send(new String[] { toMail }, subject, templatePath, model, true);
	}

	public void sendTestSmtpMail(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword, boolean smtpSSLEnabled, String smtpFromMail, String toMail) {
		Assert.hasText(smtpHost);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(smtpFromMail);
		Assert.hasText(toMail);

		Setting setting = SystemUtils.getSetting();
		String subject = SpringUtils.getMessage("admin.mail.testSmtpSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("testSmtpMail");
		send(smtpHost, smtpPort, smtpUsername, smtpPassword, smtpSSLEnabled, smtpFromMail, new String[] { toMail }, subject, templateConfig.getRealTemplatePath(), null, false);
	}

	public void sendFindPasswordMail(String toMail, String username, SafeKey safeKey) {
		if (StringUtils.isEmpty(toMail) || StringUtils.isEmpty(username) || safeKey == null) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("username", username);
		model.put("safeKey", safeKey);
		String subject = SpringUtils.getMessage("shop.mail.findPasswordSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("findPasswordMail");
		send(toMail, subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendRegisterMemberMail(Long memberId) {
		TMember member = tMemberDao.find(memberId);
		if (member == null || StringUtils.isEmpty(member.getEmail())) {
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.registerMember);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("member", member);
		String subject = SpringUtils.getMessage("shop.mail.registerMemberSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerMemberMail");
		send(member.getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendCreateOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.createOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.createOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("createOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendUpdateOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.updateOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.updateOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("updateOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendCancelOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.cancelOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.cancelOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("cancelOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendReviewOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.reviewOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.reviewOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("reviewOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendPaymentOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.paymentOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.paymentOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("paymentOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendRefundsOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.refundsOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.refundsOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("refundsOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendShippingOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.shippingOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.shippingOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("shippingOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendReturnsOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.returnsOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.returnsOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("returnsOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendReceiveOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.receiveOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.receiveOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("receiveOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendCompleteOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.completeOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.completeOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("completeOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

	public void sendFailOrderMail(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.failOrder);
		if (messageConfig == null || !messageConfig.getIsMailEnabled()) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		String subject = SpringUtils.getMessage("shop.mail.failOrderSubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failOrderMail");
		send(order.getMemberVO().getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}
	
	/**
	 * 发送到货通知邮件(异步)
	 *
	 * @param productNotify
	 *            到货通知
	 */
	public void sendProductNotifyMail(TProductNotify productNotify){
		if (productNotify == null || StringUtils.isEmpty(productNotify.getEmail())) {
			return;
		}
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("productNotify", productNotify);
		String subject = SpringUtils.getMessage("shop.mail.productNotifySubject", setting.getSiteName());
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("productNotifyMail");
		send(productNotify.getEmail(), subject, templateConfig.getRealTemplatePath(), model);
	}

}