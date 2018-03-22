
package com.vjshop.controller.admin;

import javax.servlet.http.HttpServletRequest;

import com.vjshop.Message;
import com.vjshop.Pageable;
import com.vjshop.entity.TConsultation;
import com.vjshop.service.TConsultationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 咨询
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("adminConsultationController")
@RequestMapping("/admin/consultation")
public class ConsultationController extends BaseController {

	@Autowired
	private TConsultationService consultationService;

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(Long id, ModelMap model) {
		model.addAttribute("consultation", consultationService.findDetails(id));
		return "/admin/consultation/reply";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(TConsultation.class, "content", content)) {
			return ERROR_VIEW;
		}
		TConsultation consultation = consultationService.find(id);
		if (consultation == null) {
			return ERROR_VIEW;
		}
		TConsultation replyConsultation = new TConsultation();
		replyConsultation.setContent(content);
		replyConsultation.setIp(request.getRemoteAddr());
		consultationService.reply(consultation, replyConsultation);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:reply.jhtml?id=" + id;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("consultation", consultationService.findDetails(id));
		return "/admin/consultation/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id, @RequestParam(defaultValue = "false") Boolean isShow, RedirectAttributes redirectAttributes) {
		TConsultation consultation = consultationService.find(id);
		if (consultation == null) {
			return ERROR_VIEW;
		}
		if (isShow != consultation.getIsShow()) {
			consultation.setIsShow(isShow);
			consultationService.update(consultation);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", consultationService.findPage(null, null, null, pageable));
		return "/admin/consultation/list";
	}

	/**
	 * 删除回复
	 */
	@RequestMapping(value = "/delete_reply", method = RequestMethod.POST)
	public @ResponseBody Message deleteReply(Long id) {
		TConsultation consultation = consultationService.find(id);
		if (consultation == null || consultation.getForConsultation() == null) {
			return ERROR_MESSAGE;
		}
		consultationService.delete(id);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if (ids != null) {
			consultationService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}