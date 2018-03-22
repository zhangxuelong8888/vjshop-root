
package com.vjshop.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.vjshop.Setting;
import com.vjshop.TemplateConfig;
import com.vjshop.dao.TMemberDao;
import com.vjshop.entity.*;
import com.vjshop.service.TMessageConfigService;
import com.vjshop.service.SmsService;
import com.vjshop.util.SystemUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

//import cn.emay.sdk.client.api.Client;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service - 短信
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("smsServiceImpl")
public class SmsServiceImpl implements SmsService {

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Autowired
	private TMessageConfigService messageConfigService;
	@Autowired
	private TMemberDao tMemberDao;

	/**
	 * 添加短信发送任务
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 */
	private void addSendTask(final String[] mobiles, final String content, final Date sendTime) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				send(mobiles, content, sendTime);
			}
		});
	}

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号码
	 * @param content
	 *            内容
	 * @param sendTime
	 *            发送时间
	 */
	private void send(String[] mobiles, String content, Date sendTime) {
		Assert.notEmpty(mobiles);
		Assert.hasText(content);

		Setting setting = SystemUtils.getSetting();
		String smsSn = setting.getSmsSn();
		String smsKey = setting.getSmsKey();
		if (StringUtils.isEmpty(smsSn) || StringUtils.isEmpty(smsKey)) {
			return;
		}
		try {
//			Client client = new Client(smsSn, smsKey);
			if (sendTime != null) {
//				client.sendScheduledSMS(mobiles, content, DateFormatUtils.format(sendTime, "yyyyMMddhhmmss"));
			} else {
//				client.sendSMS(mobiles, content, 5);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String[] mobiles, String content, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles);
		Assert.hasText(content);

		if (async) {
			addSendTask(mobiles, content, sendTime);
		} else {
			send(mobiles, content, sendTime);
		}
	}

	public void send(String[] mobiles, String templatePath, Map<String, Object> model, Date sendTime, boolean async) {
		Assert.notEmpty(mobiles);
		Assert.hasText(templatePath);

		try {
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate(templatePath);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			send(mobiles, content, sendTime, async);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void send(String mobile, String content) {
		Assert.hasText(mobile);
		Assert.hasText(content);

		send(new String[] { mobile }, content, null, true);
	}

	public void send(String mobile, String templatePath, Map<String, Object> model) {
		Assert.hasText(mobile);
		Assert.hasText(templatePath);

		send(new String[] { mobile }, templatePath, model, null, true);
	}

	public void sendRegisterMemberSms(Long memberId) {
		TMember member = tMemberDao.find(memberId);
		if (member == null || StringUtils.isEmpty(member.getMobile())) {
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.registerMember);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("member", member);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("registerMemberSms");
		send(member.getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendCreateOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.createOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("createOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendUpdateOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.updateOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("updateOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	private String findMobile(TOrder order) {
		String mobile = "";
		TMember member = tMemberDao.find(order.getMember());
		if (member != null) {
			mobile = member.getMobile();
		}
		return mobile;
	}

	public void sendCancelOrderSms(TOrder order) {
		if (order == null || order.getMember() == null || StringUtils.isEmpty(findMobile(order))) {
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.cancelOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("cancelOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReviewOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.reviewOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("reviewOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendPaymentOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.paymentOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("paymentOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendRefundsOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.refundsOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("refundsOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendShippingOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.shippingOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("shippingOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReturnsOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.returnsOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("returnsOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendReceiveOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.receiveOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("receiveOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendCompleteOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.completeOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("completeOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public void sendFailOrderSms(TOrder order) {
		if (order == null || order.getMember() == null) {
			return;
		}
		TMember member = tMemberDao.find(order.getMember());
		if (member == null || member.getEmail() == null){
			return;
		}
		TMessageConfig messageConfig = messageConfigService.find(TMessageConfig.Type.failOrder);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", order);
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("failOrderSms");
		send(order.getMemberVO().getMobile(), templateConfig.getRealTemplatePath(), model);
	}

	public long getBalance() {
		Setting setting = SystemUtils.getSetting();
		String smsSn = setting.getSmsSn();
		String smsKey = setting.getSmsKey();
		if (StringUtils.isEmpty(smsSn) || StringUtils.isEmpty(smsKey)) {
			return -1L;
		}
		try {
//			Client client = new Client(smsSn, smsKey);
//			double result = client.getBalance();
//			if (result >= 0) {
//				return (long) (result * 10);
//			}
		} catch (Exception e) {
		}
		return -1L;
	}

}